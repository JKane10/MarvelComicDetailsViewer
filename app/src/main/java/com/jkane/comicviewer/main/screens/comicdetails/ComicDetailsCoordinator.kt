package com.jkane.comicviewer.main.screens.comicdetails

import android.content.Context
import com.jkane.comicviewer.R
import com.jkane.comicviewer.application.database.databases.ComicDatabase
import com.jkane.comicviewer.application.database.entities.ComicEntity
import com.jkane.comicviewer.application.database.entities.InvalidComicEntity
import com.jkane.comicviewer.application.database.observers.ComicEntityObserver
import com.jkane.comicviewer.application.database.observers.InvalidComicEntityObserver
import com.jkane.comicviewer.application.network.models.Comic
import com.jkane.comicviewer.application.network.observers.NetworkObserver
import com.jkane.comicviewer.application.network.services.MarvelService
import com.jkane.comicviewer.application.utils.httpToHttps
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Contains the business logic of the application.
 *
 * Contains a multi-step Observer chain. The logic is as follows.
 * **************************************************************
 * 1) A comicId will be passed to #[getComicDetails]
 *
 *
 * 2) #[getComicDetails] will kick off an observer to check if that comicId exists in the sqlite db.
 * 2a) If it does exist in the db, those comic details will be loaded from the db.
 * 2b) If it does not exist in the db, that comicId will be passed to #[checkInvalidTableForComicId]
 *     to check if it is known as an invalid comic id to avoid a network call.
 * 2c) If it is an invalid comic id, a new random Id is queried in #[getComicDetails]
 *
 *
 * 3) If it is not an invalid comic id, a network call is made in #[requestComicFromApi]
 * 3a) If the request is valid, the model is generated and the viewmodel is updated.
 * 3b) If the request is invalid, the error is examined, if it is an invalid ID, log it to the DB
 */
class ComicDetailsCoordinator(
    private val comicDatabase: ComicDatabase,
    private val marvelService: MarvelService,
    private val viewModel: ComicDetailsViewModel,
    private val context: Context
) {

    /**
     * Entry point for request chain
     *
     * @param comicId - Id for comic to query
     */
    fun getComicDetails(comicId: String) {
        loadComicDataToViewModel(comicId)
    }

    /**
     * Checks if the comic exists in the local sqlite DB.
     *
     * If it does, update the viewModel with the sqlite data
     * If it does not, check if it is a known invalid Id
     */
    private fun loadComicDataToViewModel(comicId: String) {
        // check db
        viewModel.setLoading(true)
        comicDatabase.comicDao().getComicById(comicId.toInt())
            .subscribeOn(Schedulers.io())
            .doOnSuccess {
                viewModel.updateStateFromDB(it)
            }.doOnError {
                checkInvalidTableForComicId(comicId)
            }.subscribe(ComicEntityObserver())
    }

    /**
     * Checks if the comic exists in the invalid sqlite DB.
     *
     * If it does, re-request with another random Id as the original Id is already known as invalid
     * If it does not, request the comicId from the API as we do not know if it is valid or invalid
     */
    private fun checkInvalidTableForComicId(comicId: String) {
        comicDatabase.comicDao().getInvalidComicById(comicId.toInt())
            .subscribeOn(Schedulers.io())
            .doOnSuccess {
                // Comic Id is invalid; check another
                loadComicDataToViewModel((1..20000).random().toString())
            }.doOnError {
                // Comic Id is not marked as invalid locally, attempt to get from API
                requestComicFromApi(comicId)
            }.subscribe(InvalidComicEntityObserver())
    }

    /**
     * Requests the comicId from the Marvel API.
     *
     * If it is valid and a successful response comes back, updates the viewmodel with the results
     * and updates the database with the data.
     *
     * If it is not valid, check if it is not found, if it is not found, add to the invalid id DB
     * If it is not valid for another reason, display a generic error message.
     */
    private fun requestComicFromApi(comicId: String) {
        marvelService.getComicDetails(comicId)
            .subscribeOn(Schedulers.io())
            .doOnNext {
                it.data.results.first().let { comic ->
                    viewModel.updateStateFromNetwork(generateModel(comic))
                }
            }.doAfterNext {
                insertComicIntoDB(generateModel(it.data.results.first()))
            }.doOnError { error ->
                when (error) {
                    is HttpException -> {
                        if (error.code() == 404) {
                            viewModel.errorMessage.postValue(
                                context.getString(R.string.comic_not_found).format(comicId)
                            )
                            comicDatabase
                                .comicDao()
                                .addInvalidComic(InvalidComicEntity(comicId.toInt()))
                        } else {
                            viewModel.errorMessage.postValue(context.getString(R.string.super_generic_error_message))
                        }
                    }
                    else -> viewModel.errorMessage.postValue(context.getString(R.string.super_generic_error_message))
                }
                viewModel.isLoading.postValue(false)
            }.subscribe(NetworkObserver())
    }

    /**
     * Generates a domain model for passing to a DB entity or viewModel.
     *
     * Manipulates the data as needed to get it to a displayable form.
     */
    // TODO - Move some of this functionality into utils to clean up logic.
    fun generateModel(comic: Comic) =
        ComicDetailsModel(
            comicId = comic.id.toString(),
            isLoading = false,
            title = comic.title,
            description = comic.description,
            imageUrl = comic.thumbnail?.run { "${path?.httpToHttps()}.${extension}" },
            // Bad logic, but based on API results, this seems to be the only differentiator
            coverCreators = comic.creators?.items
                ?.filter { it.role?.contains("cover") ?: false }
                ?.toList()?.joinToString { "\n${it.name}" }?.removePrefix("\n"),
            interiorCreators = comic.creators?.items
                ?.filter { !(it.role?.contains("cover") ?: true) }
                ?.toList()?.joinToString { "\n${it.name}" }?.removePrefix("\n"),
            publishDate = comic.dates?.filter { it.type == "onsaleDate" }.run {
                if (!this.isNullOrEmpty()) {
                    this.first().date.run {
                        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                        val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                        formatter.format(parser.parse(this))
                    }
                } else {
                    null
                }
            }
        )

    private fun insertComicIntoDB(comicModel: ComicDetailsModel) {
        comicDatabase.comicDao().addComic(
            ComicEntity(
                id = comicModel.comicId.toInt(),
                title = comicModel.title,
                description = comicModel.description,
                coverImageUrl = comicModel.imageUrl,
                coverCreators = comicModel.coverCreators,
                interiorCreators = comicModel.interiorCreators,
                publishDate = comicModel.publishDate
            )
        )
    }
}
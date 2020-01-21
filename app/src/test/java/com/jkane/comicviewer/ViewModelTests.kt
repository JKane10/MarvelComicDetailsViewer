package com.jkane.comicviewer

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jkane.comicviewer.application.database.databases.ComicDatabase
import com.jkane.comicviewer.application.network.models.*
import com.jkane.comicviewer.application.network.services.MarvelService
import com.jkane.comicviewer.main.screens.comicdetails.ComicDetailsCoordinator
import com.jkane.comicviewer.main.screens.comicdetails.ComicDetailsViewModel
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock

/**
 * Tests various viewModel states
 */
class ViewModelTests {
    private val titleValue = "Title"
    private val descriptionValue = "description"
    private val coverImageUrlValue = "http://imageUrl.com"
    private val extensionValue = "jpg"
    private val coverCreatorsValue = "test cover creators"
    private val interiorCreatorsValue = "interior creators"
    private val publishValue = "Jul 10, 1984"

    lateinit var viewModel: ComicDetailsViewModel
    lateinit var coordinator: ComicDetailsCoordinator
    lateinit var comic: Comic

    var comicDatabase: ComicDatabase = mock(ComicDatabase::class.java)
    var marvelService: MarvelService = mock(MarvelService::class.java)
    var context: Context = mock(Context::class.java)


    // Used for testing LiveData
    // This ensures all posted values are immediately reflected when accessed.
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        viewModel = ComicDetailsViewModel()
        coordinator = ComicDetailsCoordinator(
            comicDatabase,
            marvelService,
            viewModel,
            context
        )

        //Create test data
        comic = Comic(
            id = 1,
            digitalId = 2,
            title = titleValue,
            issueNumber = 3,
            description = descriptionValue,
            pageCount = 20,
            thumbnail = MarvelImage(
                path = coverImageUrlValue,
                extension = extensionValue
            ),
            dates = listOf(MarvelDates(
                type = "onsaleDate",
                date = "1984-07-10T00:00:00-0400"
            )),
            creators = MarvelCreatorData(
                items = listOf(
                    MarvelCreator(
                        name = coverCreatorsValue,
                        resourceURI = "",
                        role = "cover"
                    ),
                    MarvelCreator(
                        name = interiorCreatorsValue,
                        resourceURI = "",
                        role = "interior"
                    )
                )
            )
        )
    }

    @Test
    fun `test ViewModel initial state`() {
        viewModel.apply {
            setInitialState()
            Assert.assertTrue(comicId.value == null)
            Assert.assertTrue(isLoading.value == true)
            Assert.assertTrue(title.value == null)
            Assert.assertTrue(description.value == null)
            Assert.assertTrue(coverImageUrl.value == null)
            Assert.assertTrue(coverCreators.value == null)
            Assert.assertTrue(interiorCreators.value == null)
        }
    }

    @Test
    fun `test model generation from comic network object`() {
        coordinator.generateModel(comic)
    }

    @Test
    fun `test ViewModel update state`() {
        viewModel.apply {
            updateStateFromNetwork(coordinator.generateModel(comic))
            Assert.assertTrue(isLoading.value == false)
            Assert.assertTrue(title.value == titleValue)
            Assert.assertTrue(description.value == descriptionValue)
            Assert.assertTrue(interiorCreators.value == interiorCreatorsValue)
            Assert.assertTrue(coverCreators.value == coverCreatorsValue)
            Assert.assertTrue(publishDate.value == publishValue)
            Assert.assertTrue(
                coverImageUrl.value ==
                        coverImageUrlValue
                            .replace("http", "https")
                            .plus(".$extensionValue")
            )
        }
    }
}
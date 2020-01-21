package com.jkane.comicviewer.main.screens.comicdetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jkane.comicviewer.application.database.entities.ComicEntity

/**
 * ViewModel to maintain view state.
 *
 * Contains values for UI components
 * These values are bound in to UI elements in #[ComicDetailsFragment]
 * These values are updated by the #[ComicDetailsCoordinator]
 */
class ComicDetailsViewModel : ViewModel() {
    val comicId: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val isLoading: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val title: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val description: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val coverImageUrl: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val coverCreators: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val interiorCreators: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val errorMessage: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val publishDate: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    fun setInitialState() {
        comicId.value = null
        isLoading.value = true
        title.value = null
        description.value = null
        coverImageUrl.value = null
        coverCreators.value = null
        interiorCreators.value = null
        publishDate.value = null
    }

    fun setLoading(bool: Boolean) {
        isLoading.postValue(bool)
    }

    fun updateStateFromNetwork(comic: ComicDetailsModel) {
        setLoading(false)
        title.postValue(comic.title)
        description.postValue(comic.description)
        coverImageUrl.postValue(comic.imageUrl)
        coverCreators.postValue(comic.coverCreators)
        interiorCreators.postValue(comic.interiorCreators)
        publishDate.postValue(comic.publishDate)
    }

    fun updateStateFromDB(comic: ComicEntity) {
        setLoading(false)
        title.postValue(comic.title)
        description.postValue(comic.description)
        coverImageUrl.postValue(comic.coverImageUrl)
        coverCreators.postValue(comic.coverCreators)
        interiorCreators.postValue(comic.interiorCreators)
        publishDate.postValue(comic.publishDate)
    }
}

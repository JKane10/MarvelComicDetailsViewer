package com.jkane.comicviewer.main.screens.comicdetails

/**
 * Domain Model for the Comic Details Feature
 *
 * Used to bridge data between #[ComicDetailsCoordinator] and #[ComicDetailsViewModel]
 * Coordinator will generate these models and the ViewModel will update data with them.
 */
data class ComicDetailsModel (
    val comicId: String,
    val isLoading: Boolean = false,
    val title: String? = null,
    val description: String? = null,
    val imageUrl: String? = null,
    val coverCreators: String? = null,
    val interiorCreators: String? = null,
    val publishDate: String? = null,
    val errorMessage: String? = null
)
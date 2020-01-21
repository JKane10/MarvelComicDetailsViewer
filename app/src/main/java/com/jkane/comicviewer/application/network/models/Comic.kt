package com.jkane.comicviewer.application.network.models

data class Comic(
    val id: Int,
    val digitalId: Int?,
    val title: String?,
    val issueNumber: Int?,
    val description: String?,
    val pageCount: Int?,
    val thumbnail: MarvelImage?,
    val dates: List<MarvelDates>?,
    val creators: MarvelCreatorData?
)

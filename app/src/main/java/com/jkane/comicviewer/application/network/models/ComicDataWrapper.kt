package com.jkane.comicviewer.application.network.models

data class ComicDataWrapper(
    val code: Int,
    val status: String,
    val copyright: String,
    val attributionText: String,
    val etag: String,
    val data: ComicDataContainer
)

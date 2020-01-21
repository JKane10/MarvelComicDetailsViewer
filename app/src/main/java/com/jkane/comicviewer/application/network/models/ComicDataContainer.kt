package com.jkane.comicviewer.application.network.models

data class ComicDataContainer(
    val total: Int,
    val count: Int,
    val results: List<Comic>
)

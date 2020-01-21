package com.jkane.comicviewer.application.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ComicEntity(
    @PrimaryKey val id: Int,
    val title: String?,
    val description: String?,
    val coverImageUrl: String?,
    val coverCreators: String?,
    val interiorCreators: String?,
    val publishDate: String?
)

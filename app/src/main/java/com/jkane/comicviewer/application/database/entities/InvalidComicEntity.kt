package com.jkane.comicviewer.application.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class InvalidComicEntity(
    @PrimaryKey val id: Int
)
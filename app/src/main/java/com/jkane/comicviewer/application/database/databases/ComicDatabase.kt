package com.jkane.comicviewer.application.database.databases

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jkane.comicviewer.application.database.daos.ComicDao
import com.jkane.comicviewer.application.database.entities.ComicEntity
import com.jkane.comicviewer.application.database.entities.InvalidComicEntity

@Database(entities = [ComicEntity::class, InvalidComicEntity::class], version = 1)
abstract class ComicDatabase : RoomDatabase() {
    abstract fun comicDao(): ComicDao
}
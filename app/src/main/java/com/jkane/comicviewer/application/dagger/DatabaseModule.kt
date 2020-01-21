package com.jkane.comicviewer.application.dagger

import android.content.Context
import androidx.room.Room
import com.jkane.comicviewer.BuildConfig
import com.jkane.comicviewer.application.database.databases.ComicDatabase
import dagger.Module
import dagger.Provides

@Module
class DatabaseModule constructor(private val context: Context) {

    @Provides
    fun provideComicDatabase(): ComicDatabase {
        return Room.databaseBuilder(
            context,
            ComicDatabase::class.java,
            BuildConfig.DB_NAME
        ).build()
    }
}
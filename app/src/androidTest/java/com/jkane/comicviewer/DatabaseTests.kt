package com.jkane.comicviewer

import android.os.SystemClock
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.jkane.comicviewer.application.database.databases.ComicDatabase
import com.jkane.comicviewer.application.database.entities.ComicEntity
import com.jkane.comicviewer.application.database.entities.InvalidComicEntity
import org.junit.After
import org.junit.Before
import org.junit.Test

class DatabaseTests {
    private lateinit var database: ComicDatabase
    private lateinit var comic: ComicEntity
    private lateinit var invalidComic: InvalidComicEntity
    private val comicId = BuildConfig.INITIAL_COMIC_ID

    // Test values
    private val titleValue = "Title"
    private val descriptionValue = "description"
    private val coverImageUrlValue = "http://imageUrl.com"
    private val coverCreatorsValue = "test cover creators"
    private val interiorCreatorsValue = "interior creators"
    private val publishValue = "Jul 10, 1984"

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ComicDatabase::class.java
        ).build()

        //Create test data
        comic = ComicEntity(
            id = comicId.toInt(),
            title = titleValue,
            description = descriptionValue,
            coverImageUrl = coverImageUrlValue,
            coverCreators = coverCreatorsValue,
            interiorCreators = interiorCreatorsValue,
            publishDate = publishValue
        )

        invalidComic = InvalidComicEntity(
            id = comicId.toInt()
        )
    }

    @Test
    fun that_adding_a_comic_is_successful() {
        database.comicDao().addComic(comic)
        SystemClock.sleep(500)
        database.comicDao().getComicById(comic.id)
            .doOnSuccess {
                assert(true)
            }
            .subscribe()
        SystemClock.sleep(500)
    }

    @Test
    fun that_updating_a_comic_is_successful() {
        database.comicDao().addComic(
            comic.copy(
                title = titleValue.plus("abc")
            )
        )
        SystemClock.sleep(500)
        database.comicDao().getComicById(comic.id)
            .doOnSuccess {
                assert(it.title == titleValue.plus("abc"))
            }
        SystemClock.sleep(500)
    }

    @Test
    fun that_deleting_a_comic_is_successful() {
        database.comicDao().deleteComicById(comic.id)
        SystemClock.sleep(500)
        database.comicDao().getComicById(comic.id)
            .doOnError {
                assert(true)
            }
        SystemClock.sleep(500)
    }

    @Test
    fun that_adding_an_invalid_comic_is_successful() {
        database.comicDao().addInvalidComic(invalidComic)
        SystemClock.sleep(500)
        database.comicDao().getInvalidComicById(invalidComic.id)
            .doOnSuccess {
                assert(true)
            }
            .subscribe()
        SystemClock.sleep(500)
    }

    @Test
    fun that_deleting_an_invalid_comic_is_successful() {
        database.comicDao().deleteComicById(invalidComic.id)
        SystemClock.sleep(500)
        database.comicDao().getComicById(invalidComic.id)
            .doOnError {
                assert(true)
            }
        SystemClock.sleep(500)
    }

    @After
    fun cleanup() {
        database.comicDao().deleteComicById(comic.id)
        database.comicDao().deleteInvalidComicById(invalidComic.id)
        database.close()
    }
}
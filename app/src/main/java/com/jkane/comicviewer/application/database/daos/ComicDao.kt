package com.jkane.comicviewer.application.database.daos

import androidx.room.*
import com.jkane.comicviewer.application.database.entities.ComicEntity
import com.jkane.comicviewer.application.database.entities.InvalidComicEntity
import io.reactivex.Single

@Dao
interface ComicDao {

    // Adds comic view data to DB
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addComic(comic: ComicEntity)

    // Returns comic view data from DB; if it exists
    @Query("SELECT * FROM ComicEntity WHERE id = (:comicId)")
    fun getComicById(comicId: Int): Single<ComicEntity>

    // Adds comic view dat to invalid DB
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addInvalidComic(comic: InvalidComicEntity)

    // Returns comic view data from DB, if it exists
    @Query("SELECT * FROM InvalidComicEntity WHERE id = (:comicId)")
    fun getInvalidComicById(comicId: Int): Single<InvalidComicEntity>

    @Query("DELETE FROM InvalidComicEntity WHERE id = (:comicId)")
    fun deleteInvalidComicById(comicId: Int)

    @Query("DELETE FROM ComicEntity WHERE id = (:comicId)")
    fun deleteComicById(comicId: Int)
}
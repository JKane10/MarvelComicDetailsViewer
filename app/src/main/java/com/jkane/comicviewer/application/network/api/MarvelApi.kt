package com.jkane.comicviewer.application.network.api

import com.jkane.comicviewer.application.network.models.ComicDataWrapper
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MarvelApi {

    @GET("v1/public/comics/{comicId}")
    fun getComic(
        @Path("comicId") comicId: String,
        @Query("ts") timeStamp: String,
        @Query("apikey") apiKey: String,
        @Query("hash") hash: String
    ): Observable<ComicDataWrapper>
}

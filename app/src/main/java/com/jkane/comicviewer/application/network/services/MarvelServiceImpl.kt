package com.jkane.comicviewer.application.network.services

import com.jkane.comicviewer.BuildConfig
import com.jkane.comicviewer.application.network.api.MarvelApi
import com.jkane.comicviewer.application.network.models.ComicDataWrapper
import com.jkane.comicviewer.application.utils.generateNetworkHash
import io.reactivex.Observable
import javax.inject.Inject

class MarvelServiceImpl @Inject constructor(private val marvelApi: MarvelApi) : MarvelService {

    override fun getRandomComic(): Observable<ComicDataWrapper> {
        val timeStampString = System.currentTimeMillis().toString()
        return marvelApi.getComic(
            (1..20000).random().toString(),
            timeStampString,
            BuildConfig.MARVEL_API_PUBLIC_KEY,
            generateNetworkHash(timeStampString)
        )
    }

    override fun getComicDetails(comicId: String): Observable<ComicDataWrapper> {
        val timeStampString = System.currentTimeMillis().toString()
        return marvelApi.getComic(
            comicId,
            timeStampString,
            BuildConfig.MARVEL_API_PUBLIC_KEY,
            generateNetworkHash(timeStampString)
        )
    }
}

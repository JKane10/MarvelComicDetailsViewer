package com.jkane.comicviewer.application.network.services

import com.jkane.comicviewer.application.network.models.ComicDataWrapper
import io.reactivex.Observable

interface MarvelService {

    fun getRandomComic(): Observable<ComicDataWrapper>

    fun getComicDetails(comicId: String): Observable<ComicDataWrapper>
}

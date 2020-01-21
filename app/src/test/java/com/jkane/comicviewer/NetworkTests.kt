package com.jkane.comicviewer

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jkane.comicviewer.application.network.api.MarvelApi
import com.jkane.comicviewer.application.network.models.ComicDataWrapper
import com.jkane.comicviewer.application.network.services.MarvelService
import com.jkane.comicviewer.application.network.services.MarvelServiceImpl
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.Schedulers

import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import io.reactivex.schedulers.TestScheduler
import java.util.concurrent.TimeUnit

/**
 * Tests various network request / responses
 */
class NetworkTests {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val testComicId = "36956"

    private lateinit var marvelService: MarvelService

    @Before
    fun setup() {
        marvelService = MarvelServiceImpl(
            Retrofit.Builder()
                .baseUrl("https://gateway.marvel.com")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(MarvelApi::class.java)
        )
    }

    @Test
    fun `verify getComicDetails call is successful`() {
        val observer = TestObserver<ComicDataWrapper>()
        val scheduler = TestScheduler()
        marvelService.getComicDetails(testComicId)
            .subscribeOn(scheduler)
            .doOnNext {
                assertTrue(true)
            }.doOnError {
                assertTrue(false)
            }.subscribe(observer)
        scheduler.advanceTimeBy(10, TimeUnit.SECONDS) // request timed out
    }
}

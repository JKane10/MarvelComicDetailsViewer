package com.jkane.comicviewer.application.dagger

import android.content.Context
import com.jkane.comicviewer.BuildConfig
import com.jkane.comicviewer.application.network.api.MarvelApi
import com.jkane.comicviewer.application.network.services.MarvelService
import com.jkane.comicviewer.application.network.services.MarvelServiceImpl
import com.readystatesoftware.chuck.ChuckInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
class NetworkModule constructor(private val context: Context) {

    private val baseUrl = BuildConfig.BASE_URL

    @Provides
    fun provideMarvelApi(): MarvelApi {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(OkHttpClient.Builder().addInterceptor(ChuckInterceptor(context)).build())
            .build()
            .create(MarvelApi::class.java)
    }

    @Provides
    fun provideMarvelService(service: MarvelServiceImpl): MarvelService {
        return service
    }
}

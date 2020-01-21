package com.jkane.comicviewer.application

import android.app.Application
import com.facebook.stetho.Stetho
import com.jkane.comicviewer.application.dagger.AppComponent
import com.jkane.comicviewer.application.dagger.DaggerAppComponent
import com.jkane.comicviewer.application.dagger.DatabaseModule
import com.jkane.comicviewer.application.dagger.NetworkModule

class Application : Application() {
    companion object {
        private lateinit var application: com.jkane.comicviewer.application.Application

        fun get(): com.jkane.comicviewer.application.Application = application
    }

    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        Stetho.initializeWithDefaults(this)
        appComponent = DaggerAppComponent
            .builder()
            .networkModule(NetworkModule(applicationContext))
            .databaseModule(DatabaseModule(applicationContext))
            .build()
        appComponent.inject(this)
    }

    fun getAppComponent(): AppComponent = appComponent
}

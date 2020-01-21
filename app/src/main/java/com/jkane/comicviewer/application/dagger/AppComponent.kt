package com.jkane.comicviewer.application.dagger

import com.jkane.comicviewer.application.Application
import com.jkane.comicviewer.main.screens.comicdetails.ComicDetailsFragment
import dagger.Component

@Component(
    modules = [
        NetworkModule::class,
        DatabaseModule::class
    ]
)
interface AppComponent {
    fun inject(app: Application)

    fun inject(comicDetailsFragment: ComicDetailsFragment)
}

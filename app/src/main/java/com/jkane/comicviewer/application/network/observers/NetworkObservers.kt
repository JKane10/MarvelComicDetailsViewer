package com.jkane.comicviewer.application.network.observers

import android.util.Log
import com.jkane.comicviewer.application.network.models.ComicDataWrapper
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

class NetworkObserver : Observer<ComicDataWrapper> {
    override fun onComplete() {

    }

    override fun onSubscribe(d: Disposable) {

    }

    override fun onNext(t: ComicDataWrapper) {

    }

    override fun onError(e: Throwable) {
        Log.d("NetworkObserver", e.toString())
    }
}
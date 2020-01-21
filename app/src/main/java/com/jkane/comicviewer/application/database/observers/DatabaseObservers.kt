package com.jkane.comicviewer.application.database.observers

import android.util.Log
import com.jkane.comicviewer.application.database.entities.ComicEntity
import com.jkane.comicviewer.application.database.entities.InvalidComicEntity
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable

class ComicEntityObserver : SingleObserver<ComicEntity> {
    override fun onSuccess(t: ComicEntity) {

    }

    override fun onSubscribe(d: Disposable) {

    }

    override fun onError(e: Throwable) {
        Log.d("ComicEntityObserver", e.toString())
    }
}

class InvalidComicEntityObserver : SingleObserver<InvalidComicEntity> {
    override fun onSuccess(t: InvalidComicEntity) {

    }

    override fun onSubscribe(d: Disposable) {

    }

    override fun onError(e: Throwable) {
        Log.d("InvalidComicEntityObs", e.toString())
    }

}
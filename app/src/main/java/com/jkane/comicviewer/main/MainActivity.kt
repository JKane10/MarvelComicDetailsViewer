package com.jkane.comicviewer.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import com.google.android.material.snackbar.Snackbar
import com.jkane.comicviewer.R
import com.jkane.comicviewer.main.screens.comicdetails.ComicDetailsFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.container,
                    ComicDetailsFragment.newInstance()
                )
                .commitNow()
        }
    }

    fun showLoading(show: Boolean) {
        findViewById<FrameLayout>(R.id.loading_overlay)
            .visibility = if (show) View.VISIBLE else View.GONE
    }

    fun showError(errorMsg: String) =
        Snackbar.make(
            findViewById(R.id.container),
            errorMsg,
            Snackbar.LENGTH_SHORT
        ).show()
}

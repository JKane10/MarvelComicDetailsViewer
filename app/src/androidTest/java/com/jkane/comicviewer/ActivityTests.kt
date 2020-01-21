package com.jkane.comicviewer

import android.os.SystemClock
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.jkane.comicviewer.main.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun title_is_displayed() {
        onView(withId(R.id.title_text)).check(matches(isDisplayed()))
        // TODO - remove this if tests grow too large as this will slow automated testing run times
        // To give view time to load data from API / DB before checking value
        SystemClock.sleep(3000)
        onView(withId(R.id.title_text)).check(matches(withText("Amazing Spider-Man Annual (2011) #38")))
    }

    @Test
    fun image_is_displayed() {
        onView(withId(R.id.featured_image)).check(matches(isDisplayed()))
    }

    @Test
    fun read_now_cta_is_displayed() {
        onView(withId(R.id.read_cta)).check(matches(isDisplayed()))
    }

    @Test
    fun description_is_displayed() {
        onView(withId(R.id.description_text)).check(matches(isDisplayed()))
    }
}

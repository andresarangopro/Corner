package com.cornershop.counterstest


import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.platform.app.InstrumentationRegistry


inline fun waitUntilLoaded(crossinline recyclerProvider: () -> RecyclerView) {
    Espresso.onIdle()

    lateinit var recycler: RecyclerView

    InstrumentationRegistry.getInstrumentation().runOnMainSync {
        recycler = recyclerProvider()
    }

    while (recycler.hasPendingAdapterUpdates()) {
        Thread.sleep(10)
    }
}


fun getText(matcher: ViewInteraction): String {
    var text = String()
    matcher.perform(object : ViewAction {
        override fun getConstraints(): org.hamcrest.Matcher<View>? {
            return isAssignableFrom(TextView::class.java)
        }

        override fun getDescription(): String {
            return "Text of the view"
        }

        override fun perform(uiController: UiController, view: View) {
            val tv = view as TextView
            text = tv.text.toString()
        }
    })

    return text
}
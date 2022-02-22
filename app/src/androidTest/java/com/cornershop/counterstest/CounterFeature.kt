package com.cornershop.counterstest

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.adevinta.android.barista.internal.matcher.DrawableMatcher.Companion.withDrawable
import org.hamcrest.CoreMatchers
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class CounterFragmentShould : BaseUITest() {

    private val stringToBeTyped = "TestOnSearch"
    private val newCounterTest = "Test"
    private val expectedNoCounterText = "No results"
    private val delete = "DELETE"

    @Test
    fun navigateToCreateCounterScreen() {
        onView(withId(R.id.btnAddCounter)).perform(click())
        assertDisplayed(R.id.createCounterView)
    }

    @Test
    fun showErrorOnNoResults() {
        onView(withId(R.id.searchView))
            .perform(typeText(stringToBeTyped), closeSoftKeyboard())

        onView(withId(R.id.tvMessage))
            .check(matches(withText(expectedNoCounterText)))
    }


    @Test
    fun createdANewCounterWithNetworkSuccessResponseTest() {
        createANewCounter(1000)
        deleteCounter()
    }

    @Test
    fun createdANewCounterWithNetworkErrorResponseTest() {
        createANewCounter(1000)
        deleteCounter()
    }

    @Test
    fun showItemTimesWhenThereAreCountersItems() {
        createANewCounter(1000)

        onView(withId(R.id.viewTimesItems))
            .check(matches(isDisplayed()))

        deleteCounter()
    }

    @Test
    fun showResultsOnSuccesSearch() {
        createANewCounter(1000)

        onView(withId(R.id.searchView))
            .perform(typeText(newCounterTest), closeSoftKeyboard())

        onView(
            CoreMatchers.allOf(
                withId(R.id.tvCounterName),
                isDescendantOfA(nthChildOf(withId(R.id.recyclerView), 0))
            )
        )
            .check(matches(withText(newCounterTest)))
            .check(matches(isDisplayed()))

        deleteCounter()
    }

    @Test
    fun deleteCounterTest() {
        createANewCounter(1000)
        deleteCounter()

    }

    @Test
    fun increaseItemOnRecyclerView() {
        createANewCounter(1000)
        val count = getText(
            onView(
                CoreMatchers.allOf(
                    withId(R.id.tvCount),
                    isDescendantOfA(nthChildOf(withId(R.id.recyclerView), 0))
                )
            )
        ).toInt()

        onView(
            CoreMatchers.allOf(
                withId(R.id.ivInc),
                isDescendantOfA(nthChildOf(withId(R.id.recyclerView), 0))
            )
        )
            .perform(click())


        onView(
            CoreMatchers.allOf(
                withId(R.id.tvCount),
                isDescendantOfA(nthChildOf(withId(R.id.recyclerView), 0))
            )
        ).check(matches(withText("${count + 1}")))

        Thread.sleep(2000)

        deleteCounter()
    }

    @Test
    fun decreaseItemOnRecyclerView() {
        createANewCounter(1000)
        val count = getText(
            onView(
                CoreMatchers.allOf(
                    withId(R.id.tvCount),
                    isDescendantOfA(nthChildOf(withId(R.id.recyclerView), 0))
                )
            )
        ).toInt()

        onView(
            CoreMatchers.allOf(
                withId(R.id.ivDec),
                isDescendantOfA(nthChildOf(withId(R.id.recyclerView), 0))
            )
        )
            .perform(click())


        onView(
            CoreMatchers.allOf(
                withId(R.id.tvCount),
                isDescendantOfA(nthChildOf(withId(R.id.recyclerView), 0))
            )
        ).check(matches(withText("${if (count > 0) count - 1 else 0}")))

        Thread.sleep(2000)

        deleteCounter()
    }

    private fun createANewCounter(threadSleep: Long) {
        onView(withId(R.id.btnAddCounter)).perform(click())
        onView(withId(R.id.etCounter))
            .perform(typeText(newCounterTest), closeSoftKeyboard())
        onView(withId(R.id.tvSave)).perform(click())

        Thread.sleep(threadSleep)

        onView(withId(R.id.ivCancel)).perform(click())

        onView(withId(R.id.recyclerView)).check(matches(isDisplayed()))

        Thread.sleep(threadSleep)

        onView(
            CoreMatchers.allOf(
                withId(R.id.tvCounterName),
                isDescendantOfA(nthChildOf(withId(R.id.recyclerView), 0))
            )
        )
            .check(matches(withText(newCounterTest)))
            .check(matches(isDisplayed()))
    }


    private fun deleteCounter() {

        selectCounterItem()


        onView(withId(R.id.ivDeleteCounter))
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withText(delete)).perform(click())
    }

    private fun selectCounterItem() {
        onView(
            CoreMatchers.allOf(
                withId(R.id.tvCounterName),
                isDescendantOfA(nthChildOf(withId(R.id.recyclerView), 0))
            )
        )
            .check(matches(withText(newCounterTest)))
            .check(matches(isDisplayed()))
            .perform(click())

        Thread.sleep(1500)

        onView(
            CoreMatchers.allOf(
                withId(R.id.ivCheck),
                isDescendantOfA(nthChildOf(withId(R.id.recyclerView), 0))
            )
        )
            .check(matches(withDrawable(R.drawable.ic_check)))
            .check(matches(isDisplayed()))

        onView(withId(R.id.clSelected))
            .check(matches(isDisplayed()))


    }

}


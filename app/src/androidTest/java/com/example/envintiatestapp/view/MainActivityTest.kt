package com.example.envintiatestapp.view

import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.contrib.RecyclerViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.envintiatestapp.R
import org.hamcrest.Matchers.*
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.text.SimpleDateFormat
import java.util.Calendar

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val scenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun formAndSubmitAreDisplayed() {
        onView(withId(R.id.editText))
            .check(matches(isDisplayed()))
    }

    @Test
    fun submitButtonIsDisplayed() {
        onView(withId(R.id.submitButton))
            .check(matches(isDisplayed()))
    }

    @Test
    fun formCanReceiveText() {
        onView(withId(R.id.editText))
            .perform(typeText("red"))
            .check(matches(isFocused()))
    }

    @Test
    fun submittedTextIsDisplayed() {
        val text = "green"
        val time = SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT).format(Calendar.getInstance().time)
        val entry = "$time - $text"
        onView(withId(R.id.editText))
            .perform(typeText(text))

        onView(withId(R.id.submitButton))
            .perform(click())

        onView(withText(entry))
            .check(matches(isDisplayed()))
    }
}
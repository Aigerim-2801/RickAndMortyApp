package com.example.retrofitapp

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.retrofitapp.presentation.character.CharacterFragment

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.retrofitapp", appContext.packageName)
    }


    @Test
    fun testSettingsButtonClick() {
        val scenario = launchFragmentInContainer<CharacterFragment>(themeResId = R.style.Theme_RetrofitApp)
        onView(withId(R.id.open_settings_btn)).perform(click())

        scenario.onFragment { fragment ->
            assertTrue(fragment.isButtonClickHandled())
        }
    }

    @Test
    fun testFilterButtonClick() {
        val scenario = launchFragmentInContainer<CharacterFragment>(themeResId = R.style.Theme_RetrofitApp)
        onView(withId(R.id.filter_btn)).perform(click())

        scenario.onFragment { fragment ->
            assertTrue(fragment.isButtonClickHandled())
        }
    }
}
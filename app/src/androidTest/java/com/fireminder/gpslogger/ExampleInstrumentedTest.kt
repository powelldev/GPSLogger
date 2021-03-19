package com.fireminder.gpslogger

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import androidx.test.uiautomator.Until
import junit.framework.TestCase.assertEquals
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

private const val LAUNCH_TIMEOUT = 5000L

@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    private lateinit var device: UiDevice

    @Before
    fun startSimpleViewActivity() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

        // Start from home screen
        device.pressHome()

        // Wait for launcher
        val launcherPackage = device.launcherPackageName
        device.wait(
            Until.hasObject(By.pkg(launcherPackage).depth(0)),
            LAUNCH_TIMEOUT
        )

        // Launch the app
        val context = ApplicationProvider.getApplicationContext<Context>()
        val intent = context.packageManager.getLaunchIntentForPackage(
            "com.fireminder.gpslogger.dev.debug")?.apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        context.startActivity(intent)

        device.wait(
            Until.hasObject(By.pkg("com.fireminder.gpslogger.dev.debug").depth(0)),
            LAUNCH_TIMEOUT
        )
    }

    @Test
    fun clickStartLoggingTogglesButton() {
        val toggleButton = device.findObject(UiSelector().text("Start Logging"))
        toggleButton.click()
        val changedText = device.wait(Until.findObject(By.text("Stop Logging")), 500)
        assertThat(changedText.text, `is`(equalTo("Stop Logging")))
    }
}

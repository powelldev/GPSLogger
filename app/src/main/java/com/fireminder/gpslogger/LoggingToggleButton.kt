package com.fireminder.gpslogger

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton

class LoggingToggleButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?= null,
    defStyleAttr: Int = 0
): AppCompatButton(context, attrs, defStyleAttr) {

    fun startLogging() {
        text = context.getString(R.string.stop_logging)
    }

    fun stopLogging() {
        text = context.getString(R.string.start_logging)
    }

    enum class State {
        STARTED_LOGGED, STOPPED_LOGGING
    }
}

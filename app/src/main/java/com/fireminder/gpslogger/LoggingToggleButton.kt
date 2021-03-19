package com.fireminder.gpslogger

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.Observer

class LoggingToggleButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatButton(context, attrs, defStyleAttr), View.OnClickListener,
    Observer<LoggingToggleButton.State> {

    lateinit var viewModel: SimpleViewViewModel

    init {
        setOnClickListener(this)
    }

    fun onAttach(viewModel: SimpleViewViewModel) {
        this.viewModel = viewModel

        viewModel.toggleButtonState().observeForever(this)
    }

    fun onDetach() {
        viewModel.toggleButtonState().removeObserver(this)
    }

    private fun startLogging() {
        text = context.getString(R.string.stop_logging)
    }

    private fun stopLogging() {
        text = context.getString(R.string.start_logging)
    }

    override fun onClick(v: View?) {
        viewModel.onToggleButtonClicked()
    }

    enum class State {
        STARTED_LOGGED, STOPPED_LOGGING
    }

    override fun onChanged(state: State?) {
        when (state) {
            State.STARTED_LOGGED -> startLogging()
            State.STOPPED_LOGGING -> stopLogging()
            else -> TODO()
        }
    }
}

package com.fireminder.gpslogger

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.fireminder.gpslogger.databinding.SimpleViewScreenBinding

class SimpleViewActivity : AppCompatActivity() {

    private lateinit var binding: SimpleViewScreenBinding
    private lateinit var viewModel: SimpleViewViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SimpleViewScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[SimpleViewViewModel::class.java]

        binding.loggingToggleButton.setOnClickListener {
            viewModel.onToggleButtonClicked()
        }
        viewModel.toggleButtonState().observe(this) { state ->
            when (state) {
                LoggingToggleButton.State.STARTED_LOGGED -> binding.loggingToggleButton.startLogging()
                LoggingToggleButton.State.STOPPED_LOGGING -> binding.loggingToggleButton.stopLogging()
                else -> TODO()
            }
        }

    }

}

class SimpleViewViewModel : ViewModel() {
    private val toggleButtonState: MutableLiveData<LoggingToggleButton.State> by lazy {
        MutableLiveData<LoggingToggleButton.State>(LoggingToggleButton.State.STOPPED_LOGGING)
    }

    fun toggleButtonState(): LiveData<LoggingToggleButton.State> = toggleButtonState

    fun onToggleButtonClicked() {
        when (toggleButtonState.value) {
            LoggingToggleButton.State.STARTED_LOGGED -> toggleButtonState.value = LoggingToggleButton.State.STOPPED_LOGGING
            LoggingToggleButton.State.STOPPED_LOGGING -> toggleButtonState.value = LoggingToggleButton.State.STARTED_LOGGED
        }
    }
}
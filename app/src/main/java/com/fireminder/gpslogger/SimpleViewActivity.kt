package com.fireminder.gpslogger

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fireminder.gpslogger.LoggingToggleButton.State
import com.fireminder.gpslogger.databinding.SimpleViewScreenBinding
import java.lang.IllegalArgumentException

class SimpleViewActivity : AppCompatActivity() {

    private lateinit var binding: SimpleViewScreenBinding
    private lateinit var viewModel: SimpleViewViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SimpleViewScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[SimpleViewViewModel::class.java]

        binding.loggingToggleButton.onAttach(viewModel)
    }

    override fun onDestroy() {
        binding.loggingToggleButton.onDetach()
        super.onDestroy()
    }

}

class SimpleViewViewModel : ViewModel() {
    private val toggleButtonState: MutableLiveData<State> by lazy {
        MutableLiveData<State>(State.STOPPED_LOGGING)
    }

    fun toggleButtonState(): LiveData<State> = toggleButtonState

    fun onToggleButtonClicked() {
        toggleButtonState.value =
        when (toggleButtonState.value) {
            State.STARTED_LOGGED -> State.STOPPED_LOGGING
            State.STOPPED_LOGGING -> State.STARTED_LOGGED
            else -> throw IllegalArgumentException("$toggleButtonState value was null.")
        }
    }
}
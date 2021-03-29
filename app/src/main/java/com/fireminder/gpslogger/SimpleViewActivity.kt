package com.fireminder.gpslogger

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.*
import com.fireminder.gps.DefaultGpsLog
import com.fireminder.gps.GpsLog
import com.fireminder.gpslogger.databinding.SimpleViewScreenBinding

class SimpleViewActivity : AppCompatActivity() {

    private lateinit var binding: SimpleViewScreenBinding
    private lateinit var viewModel: SimpleViewViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SimpleViewScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[SimpleViewViewModel::class.java]

        binding.loggingToggleButton.setOnClickListener {
            toggleLogging()
        }

        viewModel.toggleButtonState().observe(this) { state ->
            binding.loggingToggleButton.text = when (state) {
                GpsLog.State.STARTED -> "Stop Logging"
                GpsLog.State.STOPPED -> "Start Logging"
            }
        }
        viewModel.text().observe(this@SimpleViewActivity) { text ->
            binding.latlngCoordsTextView.text = text
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        Log.d("MICHAEL_DEBUG", "onRequestPermissionResult called.")
        if (requestCode == 42) {
            if (grantResults.any { it == PackageManager.PERMISSION_DENIED }) {
                Toast.makeText(this, "Permission denied.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun toggleLogging() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            viewModel.onToggleButtonClicked()
        } else {
            requestGeoPermission()
        }
    }

    private fun requestGeoPermission() {
        if (shouldRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            Toast.makeText(this, "Rationale for location access: We're a GPS logging app.", Toast.LENGTH_LONG).show()
        }
        requestAllPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 42)
    }

}

fun AppCompatActivity.checkPermission(permission: String) =
    ActivityCompat.checkSelfPermission(this, permission)

fun AppCompatActivity.shouldRequestPermissionRationale(permission: String) =
    ActivityCompat.shouldShowRequestPermissionRationale(this, permission)

fun AppCompatActivity.requestAllPermissions(permissionsArray: Array<String>, requestCode: Int) {
    ActivityCompat.requestPermissions(this, permissionsArray, requestCode)
}

class SimpleViewViewModel(app: Application) : AndroidViewModel(app) {

    private val log =
        DefaultGpsLog(app.getSystemService(Context.LOCATION_SERVICE) as LocationManager)

    init {
        log.registerListener(object : GpsLog.Listener {
            override fun onNewEntry(entry: GpsLog.Entry) {
                text.value = entry.toString()
            }

            override fun onStateChanged(state: GpsLog.State) {
                toggleButtonState.value = state
            }

        })

    }

    private val text: MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }

    private val toggleButtonState: MutableLiveData<GpsLog.State> by lazy {
        MutableLiveData<GpsLog.State>(GpsLog.State.STOPPED)
    }

    fun toggleButtonState(): LiveData<GpsLog.State> = toggleButtonState

    fun text(): LiveData<String> = text

    fun onToggleButtonClicked() {
        when (log.state()) {
            GpsLog.State.STARTED -> log.stop()
            GpsLog.State.STOPPED -> log.start()
        }
    }
}
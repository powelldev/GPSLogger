package com.fireminder.gps

import android.annotation.SuppressLint
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.location.LocationManager.GPS_PROVIDER

class DefaultGpsLog(
    private val locationManager: LocationManager
) : GpsLog, LocationListener {

    private val entries = mutableListOf<DefaultEntry>()
    private var state: GpsLog.State = GpsLog.State.STOPPED

    @SuppressLint("MissingPermission")
    override fun start() {
        locationManager.requestLocationUpdates(
            GPS_PROVIDER,
            5_000L,
            0.0f,
            this
        )

        state = GpsLog.State.STARTED
        listeners.onStateChanged(state)
    }


    override fun stop() {
        locationManager.removeUpdates(this)
        state = GpsLog.State.STOPPED
        listeners.onStateChanged(state)
    }

    override fun latestEntry(): GpsLog.Entry? = entries.lastOrNull()

    override fun entries(): List<GpsLog.Entry> = entries

    override fun state(): GpsLog.State {
        return state
    }

    override fun onLocationChanged(location: Location) {
        with(location) {
            val entry = DefaultEntry(LatLng(latitude, longitude), location.time)
            entries.add(entry)
            listeners.onNewEntry(entry)
        }
    }

    private val listeners: GpsLogListener = GpsLogListener()

    override fun registerListener(listener: GpsLog.Listener) {
        listeners.registerListener(listener)
    }
    override fun removeListener(listener: GpsLog.Listener) {
        listeners.removeListener(listener)
    }

}

class DefaultEntry(private val latLng: LatLng, private val timestamp: Long) : GpsLog.Entry {
    override fun latLng(): LatLng = latLng
    override fun timestamp(): Long = timestamp
    override fun toString(): String {
        return "$latLng + $timestamp"
    }
}

class GpsLogListener : GpsLog.Listener {
    private val listeners = mutableSetOf<GpsLog.Listener>()
    fun registerListener(listener: GpsLog.Listener) {
        listeners.add(listener)
    }
    fun removeListener(listener: GpsLog.Listener) {
        listeners.remove(listener)
    }
    override fun onNewEntry(entry: GpsLog.Entry) {
        listeners.forEach { it.onNewEntry(entry) }
    }

    override fun onStateChanged(state: GpsLog.State) {
        listeners.forEach { it.onStateChanged(state) }
    }

}
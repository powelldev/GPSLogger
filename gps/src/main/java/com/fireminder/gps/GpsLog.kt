package com.fireminder.gps

interface GpsLog {
    fun start()
    fun stop()
    fun latestEntry(): Entry?
    fun entries(): List<Entry>
    fun state(): State
    fun registerListener(listener: GpsLog.Listener)
    fun removeListener(listener: GpsLog.Listener)

    enum class State {
        STARTED, STOPPED
    }

    interface Entry {
        fun latLng(): LatLng
        fun timestamp(): Long
    }

    interface Listener {
        fun onNewEntry(entry: Entry)
        fun onStateChanged(state: State)
    }
}


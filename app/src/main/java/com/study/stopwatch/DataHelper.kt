package com.study.stopwatch

import android.content.Context
import java.text.SimpleDateFormat
import java.util.*

class DataHelper(context: Context) {
    private var sharedPref = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
    private var dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
    private var timeCounting = false
    private var startTime: Date? = null
    private var stopTime: Date? = null
    fun stopTime(): Date? = stopTime
    fun timeCounting(): Boolean = timeCounting
    fun startTime(): Date? = startTime
    fun setStartTime(date: Date?) {
        startTime = date
        with(sharedPref.edit()) {
            val newDate = if (date == null) null else dateFormat.format(date)
            putString(START_TIME_KEY, newDate)
            apply()
        }
    }

    fun setStopTime(date: Date?) {
        stopTime = date
        with(sharedPref.edit()) {
            val newDate = if (date == null) null else dateFormat.format(date)
            putString(STOP_TIME_KEY, newDate)
            apply()
        }
    }

    fun setTimeCounting(isCounting: Boolean) {
        timeCounting = isCounting
        with(sharedPref.edit()) {
            putBoolean(COUNTING_KEY, isCounting)
            apply()
        }
    }

    init {
        timeCounting = sharedPref.getBoolean(COUNTING_KEY, false)
        val startString = sharedPref.getString(START_TIME_KEY, null)
        if (startString != null) {
            startTime = dateFormat.parse(startString)
        }
        val stopString = sharedPref.getString(STOP_TIME_KEY, null)
        if (stopString != null) {
            startTime = dateFormat.parse(startString)
        }
    }

    companion object {
        const val PREFERENCES = "prefs"
        const val START_TIME_KEY = "startKey"
        const val STOP_TIME_KEY = "stopKey"
        const val COUNTING_KEY = "countKey"
    }
}
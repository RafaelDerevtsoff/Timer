package com.study.stopwatch

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.study.stopwatch.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var dataHelper: DataHelper
    private val timer = Timer()
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        dataHelper = DataHelper(applicationContext)
        binding.apply {
            btnStart.setOnClickListener {
                startStopAction()
            }
            btnReset.setOnClickListener {
                resetAction()
            }
        }
        if (dataHelper.timeCounting()) {
            startTimer()
        } else {
            stopTimer()
            if (dataHelper.startTime() != null && dataHelper.stopTime() != null) {
                val time = Date().time - calculateRestartTime().time
                binding.tvTimer.text = timeStringFromDouble(time)
            }
        }
        timer.scheduleAtFixedRate(TimeTask(), 0, 500)
    }
    private inner class TimeTask() : TimerTask() {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun run() {
            if (dataHelper.timeCounting()) {
                val time = Date().time - dataHelper.startTime()!!.time
               runOnUiThread {  binding.tvTimer.text = Date().toInstant().toString() }
            }
        }


    }

    private fun resetAction() {
        dataHelper.setStartTime(null)
        dataHelper.setStopTime(null)
        stopTimer()
        binding.tvTimer.text = timeStringFromDouble(0)
    }

    private fun timeStringFromDouble(ms: Long): String {
        val seconds = (ms / 1000) % 60
        val minutes = (ms / (1000 * 60) % 60)
        val hours = (ms / (1000 * 60 * 60) % 24)
        return makeTimeString(hours, minutes, seconds)
    }

    private fun makeTimeString(hours: Long, minutes: Long, seconds: Long): String =
        String.format("%02d:%02d:%02d", hours, minutes, seconds)

    private fun stopTimer() {
        dataHelper.setTimeCounting(false)
        binding.btnStart.text = getString(R.string.stop)
    }

    private fun startTimer() {
        dataHelper.setTimeCounting(true)
        binding.btnStart.text = getString(R.string.start)
    }

    private fun startStopAction() {
        if (dataHelper.timeCounting()) {
            dataHelper.setStopTime(Date())
            stopTimer()
        } else {
            if (dataHelper.stopTime() != null) {
                dataHelper.setStartTime(calculateRestartTime())
                dataHelper.setStopTime(null)
            } else {
                dataHelper.setStartTime(Date())
            }
            startTimer()
        }
    }

    private fun calculateRestartTime(): Date {
        val diff = dataHelper.startTime()!!.time - dataHelper.stopTime()!!.time
        return Date(System.currentTimeMillis() + diff)
    }
}
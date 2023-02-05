package com.example.fithpath

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class StopWatch(private var textView: TextView) : AppCompatActivity(), Runnable {
    private var startTime: Long = 0
    private var isRunning: Boolean = false
    private var elapsedTime: Long = 0
    private var thread: Thread = Thread(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.run_details)
    }

    fun start() {
        startTime = Calendar.getInstance().timeInMillis
        isRunning = true
        thread.start()
    }

    fun stop() {
        if (isRunning) {
            elapsedTime += Calendar.getInstance().timeInMillis - startTime
            isRunning = false
        }
    }

    fun reset() {
        elapsedTime = 0
        isRunning = false
        textView.text = "00:00:00"
    }

    fun getElapsedTime(): Long {
        return Calendar.getInstance().timeInMillis-startTime
    }

    override fun run() {
        while(isRunning) {
            val dt = Date(getElapsedTime())
            val obj = SimpleDateFormat("HH:mm:ss")
            obj.timeZone = TimeZone.getTimeZone("UTC")
            textView.text = obj.format(dt)
        }
    }
}
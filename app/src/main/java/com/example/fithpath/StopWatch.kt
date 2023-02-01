package com.example.fithpath

import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

open class StopWatch: AppCompatActivity, Runnable {
    private var startTime: Long = 0
    private var isRunning: Boolean = false
    private var elapsedTime: Long = 0
    private var thread: Thread

    private var textView: TextView


    constructor(textView: TextView):super() {
        this.textView = textView
        thread = Thread(this);
    }

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
            val dt = Date(getElapsedTime());
            val obj = SimpleDateFormat("HH:mm:ss")
            obj.timeZone = TimeZone.getTimeZone("UTC");
            textView.text = obj.format(dt)
        }
    }
}
package com.ghost.finder.detector.radar.tracker.ads.logger

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.mobile.hataki_ad_lib.event_tracker.EventTracker
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HKTFileLogger: EventTracker {
    private val LOG_FILE_NAME = "app_log.txt"
    private val logBuffer = StringBuilder()
    private val lock = Any()

    fun log(context: Context, tag: String? = null,  message: String) {
        synchronized(lock) {
            val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

            logBuffer.append("$timestamp: $tag - $message\n")
            if (logBuffer.length > 1024) { // flush every 1KB
                flushToFile(context)
            }
        }

        Log.d("HKTFileLogger", message)
    }

    fun flushToFile(context: Context) {
        synchronized(lock) {
            if (logBuffer.isEmpty()) return
            val file = File(context.filesDir, LOG_FILE_NAME)
            file.appendText(logBuffer.toString())
            logBuffer.clear()
        }
    }

    fun getLogFile(context: Context): File {
        flushToFile(context)
        return File(context.filesDir, LOG_FILE_NAME)
    }

    fun clearLogs(context: Context) {
        val file = File(context.filesDir, LOG_FILE_NAME)
        if (file.exists()) file.delete()
    }

    override fun log(
        context: Context?,
        name: String,
        bundle: Bundle
    ) {
        val fullName = name + if (bundle.isEmpty.not()) " $bundle" else ""
        context?.let {
            log(it, message = fullName)
        }
    }
}
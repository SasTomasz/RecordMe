package com.example.android.recordme.utils

import android.app.Application
import android.media.MediaRecorder
import android.util.Log
import com.example.android.recordme.R
import com.example.android.recordme.data.Record
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class Recorder(val application: Application) {
    private lateinit var calendar: Calendar
    private val tag: String = Recorder::class.java.simpleName
    private var recorder: MediaRecorder? = null
    private var record: Record? = null

    private fun prepareRecorder() {
        record = Record(recordPath = getDataAndTime())
        val audiofile = File(application.filesDir, record!!.recordName)
        recorder = MediaRecorder()

        Log.i(tag, "audiofile path: ${audiofile.absolutePath}")

        recorder?.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setOutputFile(audiofile.absolutePath)
        }

        try {
            recorder?.prepare()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
            Log.e(tag, "prepareRcorder() fail")
        }
    }

    private fun getDataAndTime(): String {
        calendar = Calendar.getInstance()
        val data = SimpleDateFormat("yyyy-MM-dd-HH-mm")
        val stringData = data.format(calendar.time)
        Log.i(tag, "string time is: $stringData")
        return stringData
    }

    fun startRecord(): String? {
        var message: String? = null
        prepareRecorder()
        try {
            recorder?.start()

        } catch (e: java.lang.IllegalStateException) {
            Log.e(tag, "MediaRecorder.start() fail")
            message = application.getString(R.string.errorMessage1)
        }
        return message
    }

    fun stopRecord(): Record {
        recorder?.stop()
        recorder?.release()
        recorder = null
        record!!.usersName = getDataAndTime()
        return record as Record
    }

    fun releaseRecorder() {
        recorder?.release()
    }
}

// TODO 08 Figure out if it's possible to check microphone status before
//  MediaRecorder.prepare()
//  - If it's possible check it before
//  - Move error message to that check

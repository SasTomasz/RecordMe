package com.example.android.recordme.utils

import android.app.Application
import android.media.MediaRecorder
import android.util.Log
import com.example.android.recordme.R
import com.example.android.recordme.data.Record
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class Recorder(private val application: Application) {
    private lateinit var calendar: Calendar
    private val tag: String = "Recorder"
    private var recorder: MediaRecorder? = null
    private var record: Record? = null

    private fun prepareRecorder(): Boolean {
        record = Record(recordPath = getDataAndTime())
        val audioFile = File(application.filesDir, record!!.recordName)
        recorder = MediaRecorder()
        // TODO 22: Check if the device has a microphone before start recording
        //  - see https://www.techotopia.com/index.php/An_Android_Studio_Recording_and_Playback_Example_using_MediaPlayer_and_MediaRecorder
        //   -> Checking for Microphone Availability
        Log.i(tag, "audiofile path: ${audioFile.absolutePath}")

        recorder?.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setOutputFile(audioFile.absolutePath)
        }

        try {
            recorder?.prepare()
        } catch (e: IllegalStateException) {
            Log.d(tag, "IllegalStateException preparing MediaRecorder: ${e.message}")
            releaseRecorder()
            return false
        } catch (e: IOException) {
            Log.d(tag, "IOException preparing MediaRecorder: ${e.message}")
            releaseRecorder()
            return false
        }
        return true
    }

    private fun getDataAndTime(): String {
        calendar = Calendar.getInstance()
        // TODO 18: Resolve this warning
        val data = SimpleDateFormat("yyyy-MM-dd-HH-mm")
        val stringData = data.format(calendar.time)
        Log.i(tag, "string time is: $stringData")
        return stringData
    }

    fun startRecord(): String? {
        var message: String? = null
        if (prepareRecorder()) {
            try {
                recorder?.start()

            } catch (e: IllegalStateException) {
                Log.d(tag, "IllegalStateException starting MediaRecorder: ${e.message}")
                message = application.getString(R.string.errorMessage1)
            }
        }
        return message
    }

    fun stopRecord(): Record {
        try {
            recorder?.stop()
        } catch (e: IllegalStateException) {
            Log.d(tag, "IllegalStateException when stop MediaRecorder: ${e.message}")
        }

        releaseRecorder()
        return record as Record
        // TODO 20: Handle all Exceptions in this class
    }

    fun releaseRecorder() {
        if (recorder != null) {
            recorder?.reset()
            recorder?.release()
            recorder = null
        }
    }
}

// TODO 19 Test app features with phone rotation changes
//  - Test when user record something
//  - Test when user play record
//  - Test how recyclerview behave on landscape

// TODO 21: Handle with following events
//  - User click rec button when recording sth
//  - User click stop button when nothing is recorded
//  - User change phone position during recording
//  - User change phone position during playing

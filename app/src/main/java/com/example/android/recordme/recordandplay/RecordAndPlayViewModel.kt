package com.example.android.recordme.recordandplay

import android.Manifest
import android.app.Application
import android.media.MediaRecorder
import android.os.Environment
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import java.io.File

class RecordAndPlayViewModel(val application: Application) : ViewModel() {
    private var recorder: MediaRecorder? = MediaRecorder()
    private val audiofile = File(Environment.getExternalStorageDirectory(), "audiotest")

    init {
        Log.i("RecordAndPlayViewModel", "init RecordAndPlayViewModel")
    }

    private fun resetRecorder(){
        recorder?.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)           //todo figure out how to ask the user for permission to record audio for first time
            setOutputFormat(MediaRecorder.OutputFormat.DEFAULT)
            setOutputFile(audiofile.absolutePath)
            setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT)
        }

        try {
            recorder?.prepare()
        } catch (e: IllegalStateException){
            e.printStackTrace()
        }
        // todo continue with set recorder (see https://developer.android.com/guide/topics/media/mediarecorder
        //  https://stackoverflow.com/questions/11005859/record-audio-via-mediarecorder)
    }

    fun onClickRecor(){
        resetRecorder()
        recorder?.start()
    }

    fun onClickStop(){
        recorder?.stop()
        recorder?.release()
        recorder = null
    }

    private fun checkPermission() {
        ContextCompat.checkSelfPermission(application, Manifest.permission.RECORD_AUDIO)
    }



    // TODO: Implement the ViewModel
}
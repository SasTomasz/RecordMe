package com.example.android.recordme.recordandplay

import android.content.Context
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.util.Log
import androidx.lifecycle.ViewModel
import java.io.File

class RecordAndPlayViewModel : ViewModel() {
    private var recorder: MediaRecorder? = null
    private lateinit var audiofile: File

    private fun prepareRecorder(context: Context) {
        audiofile = File(context.filesDir, "audiotest")
        recorder = MediaRecorder()

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
        }
    }

    fun onClickRecord(context: Context) {
        prepareRecorder(context)
        recorder?.start()
    }

    fun onClickStop() {
        recorder?.stop()
        recorder?.release()
        recorder = null
        play()
    }

    private fun play() {
        val mediaPlayer = MediaPlayer()
        try {
            mediaPlayer.setDataSource(audiofile.absolutePath)
            mediaPlayer.prepare()
            mediaPlayer.start()
        } catch (e: Exception) {
            Log.e("RecordAndPlayViewModel", "Problem with mediaPlayer")
        }
    }
}
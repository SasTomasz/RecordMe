package com.example.android.recordme.utils

import android.content.Context
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.util.Log
import com.example.android.recordme.data.Record
import java.io.File

class RecorderAndPlayer {
    private var recorder: MediaRecorder? = null
    private lateinit var audiofile: File
    private lateinit var record: Record

    private fun prepareRecorder(context: Context) {
        audiofile = File(context.filesDir, "audiotest")
        recorder = MediaRecorder()
        record = Record(recordPath = audiofile.absolutePath)

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
//        play()
    }

    private fun play() {
        val mediaPlayer = MediaPlayer()
        try {
            Log.i("RecorderAndPlayer", "file path = ${audiofile.absolutePath}")
            mediaPlayer.setDataSource(audiofile.absolutePath)
            mediaPlayer.prepare()
            mediaPlayer.start()
        } catch (e: Exception) {
            Log.e("RecordAndPlayViewModel", "Problem with mediaPlayer")
        }

        mediaPlayer.setOnCompletionListener { mediaPlayer.release() }
    }

}
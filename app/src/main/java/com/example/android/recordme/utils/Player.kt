package com.example.android.recordme.utils

import android.app.Application
import android.media.MediaPlayer
import android.util.Log
import com.example.android.recordme.data.Record
import java.io.File
import java.io.IOException

class Player(private val application: Application) {
    private val tag = Player::class.java.simpleName
    private var mediaPlayer: MediaPlayer? = MediaPlayer()

    fun play(record: Record) {
        try {
            if (mediaPlayer != null) {
                mediaPlayer?.reset()
            }
        } catch (e: IllegalStateException) {
            Log.e(tag, e.message ?: "Problem with reset()")
        }

        val audioFile = File(application.filesDir, record.recordName)

        try {
            if (mediaPlayer == null) mediaPlayer = MediaPlayer()
            mediaPlayer?.setDataSource(audioFile.absolutePath)
        } catch (illegalArgumentException: IllegalStateException) {
            Log.e(tag, "IllegalStateException in MediaPlayer.setDataSource()")
        } catch (ioException: IOException) {
            Log.e(tag, "IOException in MediaPlayer.setDataSource()")
        }


        try {
            Log.i("RecordAndPlayViewModel", "file path = ${audioFile.absolutePath}")
            mediaPlayer?.prepare()
            mediaPlayer?.start()
        } catch (e: Exception) {
            Log.e("RecordAndPlayViewModel", "Problem with mediaPlayer ${e.cause.toString()}")

        }
        mediaPlayer?.setOnCompletionListener {
            release()
            mediaPlayer = null
        }
    }

    fun release() {
        mediaPlayer?.release()
    }

}

// TODO 11 Make recordings available to the User outside the app

// TODO 12 Show User that recording started
//  - Add some time representation in UI that will appear when recording started
package com.example.android.recordme.recordandplay

import android.app.Application
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.recordme.data.Record
import com.example.android.recordme.database.MainDatabase
import java.io.File

class RecordAndPlayViewModel(application: Application) : AndroidViewModel(application) {
    private var recorder: MediaRecorder? = null
    private lateinit var audiofile: File
    private lateinit var record: Record
    private var permissionsGranted = false

    private var _listOfRecordings = listOf("abc", "def", "ghi")
    val listOfRecordings
        get() = _listOfRecordings

    private val recordings: LiveData<List<Record>>

    private val _checkPermissions = MutableLiveData<Boolean>()
    val checkPermissions: LiveData<Boolean>
        get() = _checkPermissions

    init {
        val recordDao = MainDatabase.getDatabase(application).RecordDao()
        recordings = recordDao.getAllRecords()
    }

    fun onClickRecord() {
        checkPermissions()
        prepareRecorder()
        recorder?.start()

    }

    fun onClickStop() {
        recorder?.stop()
        recorder?.release()
        recorder = null
        play()
    }

    private fun prepareRecorder() {
        audiofile = File(getApplication<Application>().filesDir, "audiotest")
        recorder = MediaRecorder()
        record = Record(recordPath = audiofile.absolutePath)



        Log.i("RecordAndPlayViewModel", "audiofile path: ${audiofile.absolutePath}")

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

    private fun play() {
        val mediaPlayer = MediaPlayer()
        try {
            Log.i("RecordAndPlayViewModel", "file path = ${audiofile.absolutePath}")
            mediaPlayer.setDataSource(audiofile.absolutePath)
            mediaPlayer.prepare()
            mediaPlayer.start()
        } catch (e: Exception) {
            Log.e("RecordAndPlayViewModel", "Problem with mediaPlayer")
        }

        mediaPlayer.setOnCompletionListener { mediaPlayer.release() }
    }

    fun checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && permissionsGranted == true) {
            _checkPermissions.value = true

        }
    }

    fun permissionsChecked() {
        _checkPermissions.value = false
    }

    fun permissionsGranted() {
        permissionsGranted = true
    }

    // TODO 01 Testing all permissions cases:
    //  - User has permissions
    //  - User has denied permissions once but he didn't click "Never Show Again" checkbox
    //  - User has never seen a permission Dialog
    //  - Permissions is now granted
    //  - Permissions are not granted by User
    //  - User clicked "Never Show Again" checkbox
}
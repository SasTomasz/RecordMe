package com.example.android.recordme.recordandplay

import android.app.Application
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.recordme.Repository
import com.example.android.recordme.data.Record
import com.example.android.recordme.database.MainDatabase
import com.example.android.recordme.database.RecordDao
import kotlinx.coroutines.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class RecordAndPlayViewModel(application: Application) : AndroidViewModel(application) {
    private var recorder: MediaRecorder? = null
    private var permissionsGranted = false
    private val tag = this.javaClass.simpleName
    private val databaseDao: RecordDao = MainDatabase.getInstance(application).recordDao
    private val repository: Repository = Repository(databaseDao)
    val recordings = repository.recordingsLiveData
    private lateinit var calendar: Calendar
    private var record: Record? = null
    private var mediaPlayer = MediaPlayer()


    // Coroutines
    val viewModeljob = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + viewModeljob)

    private val _checkPermissions = MutableLiveData<Boolean>()
    val checkPermissions: LiveData<Boolean>
        get() = _checkPermissions


    fun onClickRecord() {
        checkPermissions()
    }

    fun onClickStop() {
        recorder?.stop()
        recorder?.release()
        recorder = null
        record!!.usersName = getDataAndTime()
//        updateRecordMetadata()
        val listSize = recordings.value?.size
        Log.i(tag, "Example Live Data: $listSize")
        if (listSize != null && listSize >= 3) {
            Log.i(tag, "Last LiveData: ${recordings.value?.get(listSize.minus(1))?.recordPath}")
            Log.i(tag, "Before LiveData: ${recordings.value?.get(listSize.minus(2))?.recordPath}")
            Log.i(tag, "Before LiveData: ${recordings.value?.get(listSize.minus(3))?.recordPath}")
        }
    }

    private fun prepareRecorder() {
        record = Record(recordPath = getDataAndTime())
        saveRecordMetadata(record!!)

        val audiofile = File(getApplication<Application>().filesDir, record!!.recordName)
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
        }
    }

    private fun getDataAndTime(): String {
        calendar = Calendar.getInstance()
        val data = SimpleDateFormat("yyyy-MM-dd-HH-mm")
        val stringData = data.format(calendar.time)
        Log.i(tag, "string time is: $stringData")
        return stringData
    }

    fun play(record: Record) {
        mediaPlayer.reset()
        val audiofile = File(getApplication<Application>().filesDir, record.recordName)

        try {
            Log.i("RecordAndPlayViewModel", "file path = ${audiofile.absolutePath}")
            mediaPlayer.setDataSource(audiofile.absolutePath)
            mediaPlayer.prepare()
            mediaPlayer.start()
        } catch (e: Exception) {
            Log.e("RecordAndPlayViewModel", "Problem with mediaPlayer ${e.cause.toString()}")

        }

        mediaPlayer.setOnCompletionListener {
            mediaPlayer.release()
        }
    }

    private fun checkPermissions() {
        //Check there is need to check permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && permissionsGranted) {
            startRecord()

        } else {
            _checkPermissions.value = true
        }
    }

    fun permissionsChecked() {
        _checkPermissions.value = false
    }

    fun permissionsGranted() {
        permissionsGranted = true
        startRecord()
    }

    private fun startRecord() {
        prepareRecorder()
        recorder?.start()
    }

    // TODO 06 Refactor viewModel:
    //  - Move all record and play logic to separate class
    //      * record logic
    //      * play logic

    private fun saveRecordMetadata(record: Record) {
        uiScope.launch {
            insert(record)
        }
    }

    private suspend fun insert(record: Record) {
        withContext(Dispatchers.IO) {
            databaseDao.insert(record)
        }
    }

    override fun onCleared() {
        super.onCleared()
        recorder?.release()
        uiScope.cancel()
    }


}
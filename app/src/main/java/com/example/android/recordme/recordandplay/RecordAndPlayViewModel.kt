package com.example.android.recordme.recordandplay

import android.app.Application
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.recordme.Repository
import com.example.android.recordme.data.Record
import com.example.android.recordme.database.MainDatabase
import com.example.android.recordme.database.RecordDao
import com.example.android.recordme.utils.Player
import com.example.android.recordme.utils.Recorder
import kotlinx.coroutines.*

class RecordAndPlayViewModel(application: Application) : AndroidViewModel(application) {
    private var permissionsGranted = false
    private val databaseDao: RecordDao = MainDatabase.getInstance(application).recordDao
    private val repository: Repository = Repository(databaseDao)
    val recordings = repository.recordingsLiveData
    private var record: Record? = null
    private var myRecorder: Recorder = Recorder(getApplication<Application>())

    // TODO 17 Fix problem with init MediaPlayer:
    //  E/MediaPlayer-JNI: JNIMediaPlayerFactory: bIsQCMediaPlayerPresent 0
    private var player: Player = Player(getApplication<Application>())


    // Coroutines
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _checkPermissions = MutableLiveData<Boolean>()
    val checkPermissions: LiveData<Boolean>
        get() = _checkPermissions

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _isRecording = MutableLiveData<Boolean>(false)
    val isRecording: LiveData<Boolean>
        get() = _isRecording


    fun onClickRecord() {
        checkPermissions()
    }

    fun onClickStop() {
        record = myRecorder.stopRecord()
        _isRecording.value = false
        saveRecordMetadata(record!!)
    }

    private fun startRecord() {
        val errorMessage: String? = myRecorder.startRecord()
        if (errorMessage != null) {
            makeErrorMessage(errorMessage)
        } else _isRecording.value = true
    }

    fun play(record: Record) {
        player.play(record)
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
        uiScope.cancel()
        myRecorder.releaseRecorder()
        player.release()
    }

    /**
     * Create error message to user
     */
    private fun makeErrorMessage(message: String) {
        _errorMessage.value = message
    }

    /**
     * User showed error message
     */
    fun errorMessageShowed() {
        _errorMessage.value = null
    }
}

// TODO 08: Determine MVP
//  - See other recorder app in Google play (min 3) and thing about pros and cons that app
//  - Write down what minimum features do you want in this app
//  - In evernote create new RecordMe folder
//  - Create inside any notes or draws you need to clearly specify how MVP should look like

// TODO 09: Upgrade buttons behavior
//  - Add some animations with motionLayout
//  - Add some play icon to every item in recycler to inform user there is possibility to play sound

// TODO 10: Add Settings menu:
//  - Add new activity settings
//  - Add new button in options menu
//  - When user click settings in options menu move him to settings activity
//  - In options menu add feature -> Stop or pause recording when calls income or block income calls

// TODO 19 Resolve "ClassLoader referenced unknown path" and "Before Android 4.1,
//  method android.graphic.PorterDuffColorFilter..." warnings
//  - start from https://stackoverflow.com/questions/45044405/classloader-referenced-unknown-path
//  - find on page "instant Apps support" https://developer.android.com/studio/releases
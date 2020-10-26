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
    private val player: Player = Player(getApplication<Application>())


    // Coroutines
    private val viewModeljob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModeljob)

    private val _checkPermissions = MutableLiveData<Boolean>()
    val checkPermissions: LiveData<Boolean>
        get() = _checkPermissions

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage


    fun onClickRecord() {
        checkPermissions()
    }

    fun onClickStop() {
        record = myRecorder.stopRecord()
        saveRecordMetadata(record!!)
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

    private fun startRecord() {
        val errorMessage: String? = myRecorder.startRecord()
        if (errorMessage != null) makeErrorMessage(errorMessage)
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

// TODO 09: Upgrade buttons behavior
//  - Button "STOP" is available only when user record something
//  - Add some play icon to every item in recycler to inform user there is possibility to play sound

// TODO 10: Add Settings menu:
//  - Add new activity settings
//  - Add new button in options menu
//  - When user click settings in options menu move him to settings activity
//  - In options menu add feature -> Stop or pause recording when calls income or block income calls
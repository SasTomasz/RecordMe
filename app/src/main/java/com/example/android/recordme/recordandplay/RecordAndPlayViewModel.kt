package com.example.android.recordme.recordandplay

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RecordAndPlayViewModel : ViewModel() {

    private val _startRecord = MutableLiveData<Boolean>()
    val startRecord: LiveData<Boolean>
        get() = _startRecord

    private val _stopRecord = MutableLiveData<Boolean>()
    val stopRecord: LiveData<Boolean>
        get() = _stopRecord

    private var _listOfRecordings = listOf("abc", "def", "ghi")
    val listOfRecordings
        get() = _listOfRecordings

    fun onClickRecord() {
        _startRecord.value = true
    }

    fun onClickPlay() {
        _stopRecord.value = true
    }

    fun recordStarted() {
        _startRecord.value = false
    }

    fun recordStopped() {
        _stopRecord.value = false
    }
}
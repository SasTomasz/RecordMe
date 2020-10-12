package com.example.android.recordme

import com.example.android.recordme.database.RecordDao

class Repository(recordDao: RecordDao) {

    val recordingsLiveData = recordDao.getAllRecords()
}
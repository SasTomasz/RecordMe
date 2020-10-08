package com.example.android.recordme

import com.example.android.recordme.database.RecordDao

class Repository(private recordDao: RecordDao) {

    val recordings = recordDao.getAllRecords()
}
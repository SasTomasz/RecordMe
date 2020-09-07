package com.example.android.recordme.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.android.recordme.data.Record

@Dao
interface RecordDao {
    //todo continue create roomdatabase and it's component

    @Insert
    fun insertAll(vararg records: Record)

    @Delete
    fun deleteRecord(record: Record)

    @Query("SELECT * FROM record")
    fun getAllRecords(): LiveData<List<Record>>
}
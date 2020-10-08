package com.example.android.recordme.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.recordme.data.Record

@Dao
interface RecordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(record: Record)

    @Delete
    fun deleteRecord(record: Record)

    @Query("SELECT * FROM record")
    fun getAllRecords(): LiveData<List<Record>>

    @Update
    fun updateRecord(record: Record)
}
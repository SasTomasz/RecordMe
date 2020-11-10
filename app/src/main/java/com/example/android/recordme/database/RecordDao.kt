package com.example.android.recordme.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.android.recordme.data.Record

@Dao
interface RecordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(record: Record)

    @Query("SELECT * FROM record")
    fun getAllRecords(): LiveData<List<Record>>
}
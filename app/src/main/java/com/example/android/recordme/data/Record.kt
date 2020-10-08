package com.example.android.recordme.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "record")
data class Record(
    @PrimaryKey(autoGenerate = true)
    var recordId: Int = 0,
    @ColumnInfo(name = "record_path")
    val recordPath: String,
    @ColumnInfo(name = "record_name")
    val recordName: String = recordPath,
    @ColumnInfo(name = "users_name")
    var usersName: String = "User's name"
)

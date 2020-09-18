package com.example.android.recordme.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "record")
data class Record(
    @PrimaryKey(autoGenerate = true)
    val recordId: Int = 1,
    @ColumnInfo(name = "record_path")
    val recordPath: String,
    @ColumnInfo(name = "users_name")
    val usersName: String = "Unknown"
)
// TODO 02 Create multiple audio files:
//  - Add new files and its metadata every time user click Rec button
//  - Set recyclerview to show all recordings
//  - Add new feature -> when user click current name, he will hear record


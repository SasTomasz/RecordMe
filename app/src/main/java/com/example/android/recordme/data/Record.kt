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
) {

    // this causes problem with build app "A failure occurred while executing org.jetbrains.kotlin.gradle.internal.KaptExecution"
//    @ColumnInfo(name = "default_name")
//    private val defaultName: String
//
//    init {
//        val cal = Calendar.getInstance()
//        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//        defaultName = df.format(cal.time)
//    }
}  // todo continue create multiple audio files
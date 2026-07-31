package com.vimal.theaisemanticlog.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vimal.theaisemanticlog.data.local.dao.LogDao
import com.vimal.theaisemanticlog.data.local.entity.LogEntity

@Database(
    entities = [LogEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun logDao():LogDao
}
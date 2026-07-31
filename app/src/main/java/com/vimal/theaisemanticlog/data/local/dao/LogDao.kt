package com.vimal.theaisemanticlog.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vimal.theaisemanticlog.data.local.entity.LogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LogDao {

    @Query("SELECT * FROM logs")
    fun getLogs(): Flow<List<LogEntity>>

    // Replaces existing rows with matching primary keys without deleting the table first
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLogs(logs:List<LogEntity>)

    @Query("DELETE FROM logs")
    suspend fun clearLogs()

    @Query("SELECT EXISTS(SELECT 1 FROM logs)")
    suspend fun hasLogs(): Boolean
}
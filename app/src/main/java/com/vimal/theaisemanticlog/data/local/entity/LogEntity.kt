package com.vimal.theaisemanticlog.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vimal.theaisemanticlog.data.local.DatabaseConstants.LOG_TABLE_NAME

@Entity(tableName = LOG_TABLE_NAME)
data class LogEntity(
    @PrimaryKey
    val id:String,
    val timestamp:String,
    val severity:String,
    val tag:String,
    val message:String,
    val latencyMs:Int,
    val aiGenerated:Boolean
)
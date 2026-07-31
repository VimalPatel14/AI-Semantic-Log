package com.vimal.theaisemanticlog.data.remote.dto

data class LogResponseDto(
    val total_count:Int,
    val session_id:String,
    val data:List<LogDto>
)

data class LogDto(
    val id:String,
    val timestamp:String,
    val severity:String,
    val tag:String,
    val message:String,
    val metadata:MetadataDto
)
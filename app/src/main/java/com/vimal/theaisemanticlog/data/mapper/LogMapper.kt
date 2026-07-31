package com.vimal.theaisemanticlog.data.mapper

import com.vimal.theaisemanticlog.data.local.entity.LogEntity
import com.vimal.theaisemanticlog.data.remote.dto.LogDto
import com.vimal.theaisemanticlog.domain.model.Log

fun LogDto.toDomain():Log{
    return Log(
        id=id,
        timestamp=timestamp,
        severity=severity,
        tag=tag,
        message=message,
        latencyMs=metadata.latency_ms,
        aiGenerated=metadata.is_ai_generated
    )
}

fun Log.toEntity(): LogEntity {
    return LogEntity(
        id=id,
        timestamp=timestamp,
        severity=severity,
        tag=tag,
        message=message,
        latencyMs=latencyMs,
        aiGenerated=aiGenerated
    )
}

fun LogEntity.toDomain(): Log {
    return Log(
        id = id,
        timestamp = timestamp,
        severity = severity,
        tag = tag,
        message = message,
        latencyMs = latencyMs,
        aiGenerated = aiGenerated
    )
}
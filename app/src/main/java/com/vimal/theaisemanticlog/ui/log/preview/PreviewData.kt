package com.vimal.theaisemanticlog.ui.log.preview

import com.vimal.theaisemanticlog.domain.model.Log

object PreviewData {

    val errorLog = Log(
        id = "1",
        timestamp = "2026-01-22T15:48:17Z",
        severity = Log.Severity.ERROR,
        tag = "Network",
        message = "Connection timed out while connecting to the remote server.",
        latencyMs = 2040,
        aiGenerated = true
    )

    val warningLog = Log(
        id = "2",
        timestamp = "2026-01-22T16:20:00Z",
        severity = Log.Severity.WARNING,
        tag = "Database",
        message = "Database response is slower than expected.",
        latencyMs = 980,
        aiGenerated = false
    )

    private val warnLog = Log(
        id = "3",
        timestamp = "2026-01-22T17:10:12Z",
        severity = Log.Severity.WARN,
        tag = "Authentication",
        message = "User session is about to expire.",
        latencyMs = 620,
        aiGenerated = false
    )

    val infoLog = Log(
        id = "4",
        timestamp = "2026-01-22T18:30:45Z",
        severity = Log.Severity.INFO,
        tag = "UI",
        message = "User logged in successfully.",
        latencyMs = 140,
        aiGenerated = false
    )

    val unknownLog = Log(
        id = "5",
        timestamp = "2026-01-22T19:05:18Z",
        severity = "TEST",
        tag = "Testing",
        message = "This is a sample log for preview purposes.",
        latencyMs = 350,
        aiGenerated = true
    )

    val logs = listOf(
        errorLog,
        warningLog,
        warnLog,
        infoLog,
        unknownLog,
    )
}
package com.vimal.theaisemanticlog.domain.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.vimal.theaisemanticlog.R
import com.vimal.theaisemanticlog.domain.model.Log.Severity.ERROR
import com.vimal.theaisemanticlog.domain.model.Log.Severity.INFO
import com.vimal.theaisemanticlog.domain.model.Log.Severity.WARN
import com.vimal.theaisemanticlog.domain.model.Log.Severity.WARNING

data class Log(
    val id:String,
    val timestamp:String,
    val severity:String,
    val tag:String,
    val message:String,
    val latencyMs:Int,
    val aiGenerated:Boolean
) {
    @Composable
    fun getSeveritySurfaceColor(): Color {
        return when (severity.uppercase()) {
            ERROR -> colorResource(R.color.severityErrorSurface)
            WARN, WARNING -> colorResource(R.color.severityWarningSurface)
            INFO -> colorResource(R.color.severityInfoSurface)
            else -> colorResource(R.color.severityUnknownSurface)
        }
    }

    @Composable
    fun getSeverityIndicatorColor(): Color {
        return when (severity.uppercase()) {
            ERROR -> colorResource(R.color.severityError)
            WARN, WARNING -> colorResource(R.color.severityWarning)
            INFO -> colorResource(R.color.severityInfo)
            else -> colorResource(R.color.severityUnknown)
        }
    }

    object Severity {
        const val ERROR = "ERROR"
        const val WARN = "WARN"
        const val WARNING = "WARNING"
        const val INFO = "INFO"
    }
}
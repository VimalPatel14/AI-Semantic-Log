package com.vimal.theaisemanticlog.ui.log.component.common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.vimal.theaisemanticlog.ui.log.preview.PreviewData.errorLog
import com.vimal.theaisemanticlog.ui.log.preview.PreviewData.infoLog
import com.vimal.theaisemanticlog.ui.log.preview.PreviewData.unknownLog
import com.vimal.theaisemanticlog.ui.log.preview.PreviewData.warningLog

@Composable
fun SeverityIndicator(
    indicatorColor : Color
) {
    Canvas(
        modifier = Modifier.size(40.dp)
    ) {

        drawCircle(
            color = Color.LightGray,
            style = Stroke(10f)
        )

        drawArc(
            color = indicatorColor,
            startAngle = -90f,
            sweepAngle = 270f,
            useCenter = false,
            style = Stroke(
                width = 10f,
                cap = StrokeCap.Round
            )
        )

        drawCircle(
            color = indicatorColor,
            radius = 6f,
            center = Offset(size.width / 2, size.height / 2)
        )
    }
}

@PreviewLightDark
@Composable
private fun SeverityIndicatorPreview() {
    Column (
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        SeverityIndicator(errorLog.getSeverityIndicatorColor())
        SeverityIndicator(warningLog.getSeverityIndicatorColor())
        SeverityIndicator(infoLog.getSeverityIndicatorColor())
        SeverityIndicator(unknownLog.getSeverityIndicatorColor())
    }
}
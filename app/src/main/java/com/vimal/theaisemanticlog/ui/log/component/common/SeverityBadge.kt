package com.vimal.theaisemanticlog.ui.log.component.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vimal.theaisemanticlog.R
import com.vimal.theaisemanticlog.ui.log.preview.PreviewData.errorLog
import com.vimal.theaisemanticlog.ui.log.preview.PreviewData.infoLog
import com.vimal.theaisemanticlog.ui.log.preview.PreviewData.unknownLog
import com.vimal.theaisemanticlog.ui.log.preview.PreviewData.warningLog

@Composable
fun SeverityBadge(
    severity: String,
    surfaceColor : Color
) {
    Surface(
        shape = RoundedCornerShape(50),
        color = surfaceColor
    ) {
        Text(
            text = severity.uppercase(),
            modifier = Modifier.padding(
                horizontal = 10.dp,
                vertical = 5.dp
            ),
            color = colorResource(R.color.textColor),
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.5.sp
            )
        )
    }
}

@PreviewLightDark
@Composable
private fun SeverityBadgePreview() {
    Column (
        modifier = Modifier.padding(15.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        SeverityBadge(errorLog.severity, surfaceColor = errorLog.getSeveritySurfaceColor())
        SeverityBadge(warningLog.severity, surfaceColor = warningLog.getSeveritySurfaceColor())
        SeverityBadge(infoLog.severity, surfaceColor = infoLog.getSeveritySurfaceColor())
        SeverityBadge(unknownLog.severity, surfaceColor = unknownLog.getSeveritySurfaceColor())
    }
}

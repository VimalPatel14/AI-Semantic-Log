package com.vimal.theaisemanticlog.ui.log.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vimal.theaisemanticlog.R
import com.vimal.theaisemanticlog.domain.model.Log
import com.vimal.theaisemanticlog.ui.log.component.common.SeverityBadge
import com.vimal.theaisemanticlog.ui.log.component.common.SeverityIndicator
import com.vimal.theaisemanticlog.ui.log.component.common.TagChip
import com.vimal.theaisemanticlog.ui.log.preview.PreviewData.errorLog
import com.vimal.theaisemanticlog.ui.log.preview.PreviewData.infoLog
import com.vimal.theaisemanticlog.ui.log.preview.PreviewData.unknownLog
import com.vimal.theaisemanticlog.ui.log.preview.PreviewData.warningLog
import com.vimal.theaisemanticlog.ui.log.extenstion.noRippleClickable
import com.vimal.theaisemanticlog.ui.theme.TheAISemanticLogTheme

@Composable
fun LogItem(
    log: Log,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 10.dp)
            .noRippleClickable { onClick() }, // avoid ripple effect on click
        colors = CardDefaults.cardColors(colorResource(R.color.cardBackground)),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Row(
            modifier = Modifier.padding(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            SeverityIndicator(indicatorColor = log.getSeverityIndicatorColor())

            Spacer(modifier = Modifier.width(15.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = log.message,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 0.1.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = colorResource(R.color.textColor),
                )

                Spacer(modifier = Modifier.height(5.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SeverityBadge(severity = log.severity, surfaceColor = log.getSeveritySurfaceColor())

                    Spacer(modifier = Modifier.width(10.dp))

                    TagChip(tag = log.tag)
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun LogItemPreview() {
    TheAISemanticLogTheme {
        Column (
            modifier = Modifier.padding(16.dp).background(color = colorResource(R.color.background)),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            LogItem(log = errorLog, onClick = {})
            LogItem(log = warningLog, onClick = {})
            LogItem(log = infoLog, onClick = {})
            LogItem(log = unknownLog, onClick = {})
        }
    }
}
package com.vimal.theaisemanticlog.ui.log.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.vimal.theaisemanticlog.R
import com.vimal.theaisemanticlog.core.util.formatLogTimestamp
import com.vimal.theaisemanticlog.domain.model.Log
import com.vimal.theaisemanticlog.ui.log.component.common.SeverityBadge
import com.vimal.theaisemanticlog.ui.log.component.common.SeverityIndicator
import com.vimal.theaisemanticlog.ui.log.component.common.TagChip
import com.vimal.theaisemanticlog.ui.log.preview.PreviewData.errorLog
import com.vimal.theaisemanticlog.ui.log.preview.PreviewData.infoLog
import com.vimal.theaisemanticlog.ui.log.preview.PreviewData.unknownLog
import com.vimal.theaisemanticlog.ui.log.preview.PreviewData.warningLog
import com.vimal.theaisemanticlog.ui.theme.TheAISemanticLogTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogDetailBottomSheet(
    log: Log,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = colorResource(R.color.background),
    ) {
        LogDetailContent(log = log)
    }
}

@Composable
private fun LogDetailContent(
    log: Log
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp)
            .padding(bottom = 20.dp)
    ) {
        LogHeaderCard(log)

        Spacer(modifier = Modifier.height(20.dp))

        LogMetadataCard(log)
    }
}

@Composable
private fun LogHeaderCard(
    log: Log
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colorResource(R.color.cardBackground)),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {

        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                SeverityIndicator(indicatorColor = log.getSeverityIndicatorColor())

                Spacer(modifier = Modifier.width(10.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = log.severity.uppercase(),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = colorResource(R.color.textColor)
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


            Spacer(modifier = Modifier.height(20.dp))


            Text(
                text = "Message",
                style = MaterialTheme.typography.labelMedium,
                color = colorResource(R.color.textColor)
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = log.message,
                style = MaterialTheme.typography.bodyLarge,
                color = colorResource(R.color.textColor)
            )
        }
    }
}

@Composable
private fun LogMetadataCard(
    log: Log
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colorResource(R.color.cardBackground)),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {

        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            DetailItem(
                title = "Timestamp",
                value = log.timestamp.formatLogTimestamp()
            )

            HorizontalDivider()

            DetailItem(
                title = "Latency",
                value = "${log.latencyMs} ms"
            )

            HorizontalDivider()

            DetailItem(
                title = "AI Generated",
                value = if (log.aiGenerated) "Yes" else "No"
            )

            HorizontalDivider()

            DetailItem(
                title = "Log ID",
                value = log.id
            )
        }
    }
}

@Composable
private fun DetailItem(
    title: String,
    value: String
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
    ) {

        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
            color = colorResource(R.color.textColor)
        )

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            color = colorResource(R.color.textColor)
        )
    }
}

@PreviewLightDark
@Composable
private fun LogDetailBottomSheetErrorPreview() {
    TheAISemanticLogTheme {
        Surface(
            modifier = Modifier.fillMaxSize().statusBarsPadding(),
            color = colorResource(R.color.background),
        ) {
            LogDetailContent(log = errorLog)
        }
    }
}

@PreviewLightDark
@Composable
private fun LogDetailBottomSheetWarningPreview() {
    TheAISemanticLogTheme {
        Surface(
            modifier = Modifier.fillMaxSize().statusBarsPadding(),
            color = colorResource(R.color.background),
        ) {
            LogDetailContent(log = warningLog)
        }
    }
}

@PreviewLightDark
@Composable
private fun LogDetailBottomSheetInfoPreview() {
    TheAISemanticLogTheme {
        Surface(
            modifier = Modifier.fillMaxSize().statusBarsPadding(),
            color = colorResource(R.color.background),
        ) {
            LogDetailContent(log = infoLog)
        }
    }
}

@PreviewLightDark
@Composable
private fun LogDetailBottomSheetPreview() {
    TheAISemanticLogTheme {
        Surface(
            modifier = Modifier.fillMaxSize().statusBarsPadding(),
            color = colorResource(R.color.background),
        ) {
            LogDetailContent(log = unknownLog)
        }
    }
}
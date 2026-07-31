package com.vimal.theaisemanticlog.ui.log.preview

import com.vimal.theaisemanticlog.presentation.logs.LogState

object PreviewLogState {

    val loading = LogState(loading = true)

    val error = LogState(loading = false, error = "Unable to load logs. Please try again.")

    val success = LogState(loading = false, logs = PreviewData.logs)

    val singleLog = LogState(loading = false, logs = listOf(PreviewData.errorLog))

    val empty = LogState(loading = false)
}
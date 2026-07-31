package com.vimal.theaisemanticlog.presentation.logs

import com.vimal.theaisemanticlog.domain.model.Log

sealed interface LogIntent {
    data object LoadLogs : LogIntent

    data class Search(val query: String) : LogIntent

    data class ShowDetails(val log: Log) : LogIntent

    data object HideDetails : LogIntent
}
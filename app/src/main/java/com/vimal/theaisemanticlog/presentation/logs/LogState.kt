package com.vimal.theaisemanticlog.presentation.logs

import com.vimal.theaisemanticlog.domain.model.Log

data class LogState(
    val loading: Boolean = true,
    val logs: List<Log> = emptyList(),
    val search: String = "",
    val selectedLog: Log? = null,
    val error: String? = null,
    val showBottomSheet: Boolean = false,
    val isSearchEnabled: Boolean = false
)
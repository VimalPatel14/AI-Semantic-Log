package com.vimal.theaisemanticlog.presentation.logs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vimal.theaisemanticlog.domain.model.Log
import com.vimal.theaisemanticlog.domain.usecase.GetLogsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class LogViewModel @Inject constructor(
    private val getLogsUseCase: GetLogsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LogState())
    val state = _state.asStateFlow()

    //Holds the complete dataset received from the repository.
    private var allLogs: List<Log> = emptyList()
    // Stores the latest search query entered by the user.
    private val searchQuery = MutableStateFlow("")

    init {
        observeSearch() // Start observing search queries as soon as the ViewModel is created.
    }

    // Handles all user actions from the UI.
    fun onIntent(intent: LogIntent) {
        when (intent) {
            LogIntent.LoadLogs -> loadLogs()
            is LogIntent.Search -> searchQuery.value = intent.query
            is LogIntent.ShowDetails -> updateUiState(selectedLog = intent.log, showBottomSheet = true)
            LogIntent.HideDetails -> updateUiState(selectedLog = null, showBottomSheet = false)
        }
    }

    //Observes search text changes and applies a debounce to prevent unnecessary filtering while the user is typing.
    private fun observeSearch() {
        viewModelScope.launch {
            searchQuery
                .debounce(300)
                .distinctUntilChanged()
                .collect { query ->
                    search(query)
                }
        }
    }

    //Filters logs using the original dataset.
    private fun search(query: String) {
        val filteredLogs = if (query.isBlank()) { //If the query is empty, all logs are displayed.
            allLogs
        } else {
            allLogs.filter {
                it.message.contains(query, ignoreCase = true) ||
                        it.tag.contains(query, ignoreCase = true) ||
                        it.severity.contains(query, ignoreCase = true)

            }
        }
        updateUiState(search = query, logs = filteredLogs)
    }

    //Fetches logs from the domain layer and updates the UI
    // for loading, success, and error states.
    private fun loadLogs() {
        viewModelScope.launch {
            getLogsUseCase().onStart {
                    updateUiState(loading = true, error = null)
                }.collect { result ->
                    result.onSuccess { logs ->
                        // Preserve the complete dataset for future searches.
                        allLogs = logs
                        updateUiState(
                            loading = false,
                            logs = logs,
                            error = null,
                            isSearchEnabled = logs.isNotEmpty()
                        )
                    }
                    result.onFailure { throwable ->
                        updateUiState(
                            loading = false,
                            error = throwable.message ?: "Unknown error"
                        )
                }
            }
        }
    }

    //Centralizes all UI state updates.
    private fun updateUiState(
        loading: Boolean = _state.value.loading,
        logs: List<Log> = _state.value.logs,
        search: String = _state.value.search,
        error: String? = _state.value.error,
        selectedLog: Log? = _state.value.selectedLog,
        showBottomSheet: Boolean = _state.value.showBottomSheet,
        isSearchEnabled: Boolean = _state.value.isSearchEnabled,
    ) {
        _state.update {
            it.copy(
                loading = loading,
                logs = logs,
                search = search,
                error = error,
                selectedLog = selectedLog,
                showBottomSheet = showBottomSheet,
                isSearchEnabled = isSearchEnabled
            )
        }
    }
}
package com.vimal.theaisemanticlog.ui.log

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.vimal.theaisemanticlog.R
import com.vimal.theaisemanticlog.presentation.logs.LogIntent
import com.vimal.theaisemanticlog.presentation.logs.LogState
import com.vimal.theaisemanticlog.ui.log.component.EmptyContent
import com.vimal.theaisemanticlog.ui.log.component.ErrorContent
import com.vimal.theaisemanticlog.ui.log.component.LogItem
import com.vimal.theaisemanticlog.ui.log.component.LogListShimmer
import com.vimal.theaisemanticlog.ui.log.detail.LogDetailBottomSheet
import com.vimal.theaisemanticlog.ui.log.preview.PreviewLogState
import com.vimal.theaisemanticlog.ui.theme.TheAISemanticLogTheme

@Composable
fun LogScreen(
    modifier: Modifier = Modifier,
    state: LogState,
    event: (LogIntent) -> Unit = {},
) {

    val listState = rememberLazyListState()
    var searchText by rememberSaveable { mutableStateOf("") }
    val showSearchBar = state.error == null

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        if (showSearchBar) {
            SearchBar(
                value = searchText,
                enabled = state.isSearchEnabled,
                onValueChange = {
                    searchText = it
                    event(LogIntent.Search(it))
                }
            )
        }

        when {

            state.loading -> LogListShimmer(modifier = Modifier.weight(1f))

            state.error != null -> ErrorContent(message = state.error, buttonText = "Retry") {
                event(LogIntent.LoadLogs)
            }

            !state.loading && state.logs.isEmpty()  -> EmptyContent(
                title = "No logs found",
                message = "There are no logs matching your search."
            )

            else -> {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(bottom = 15.dp)
                ) {
                    items(
                        items = state.logs,
                        key = { it.id }

                    ){ log ->
                        LogItem(
                            log = log,
                            onClick = { event(LogIntent.ShowDetails(log)) }
                        )
                    }
                }
            }
        }
    }


    if(state.showBottomSheet && state.selectedLog != null){
        LogDetailBottomSheet(
            log = state.selectedLog,
            onDismiss = { event(LogIntent.HideDetails) }
        )
    }
}

@Composable
private fun SearchBar(
    value:String,
    enabled: Boolean = true,
    onValueChange:(String) -> Unit
) {

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        placeholder = {
            Text("Search logs...")
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
            )
        },
        trailingIcon = {
            if (value.isNotEmpty()) {
                IconButton(
                    onClick = {
                        onValueChange("")
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear search"
                    )
                }
            }
        },
        singleLine = true,
        shape = MaterialTheme.shapes.large
    )

}

@PreviewLightDark
@Composable
private fun LogScreenLoadingPreview() {
    TheAISemanticLogTheme {
        Surface(
            modifier = Modifier.fillMaxSize().statusBarsPadding(),
            color = colorResource(R.color.background),
        ) {
            LogScreen(
                state = PreviewLogState.loading
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun LogScreenErrorPreview() {
    TheAISemanticLogTheme {
        Surface(
            modifier = Modifier.fillMaxSize().statusBarsPadding(),
            color = colorResource(R.color.background),
        ) {
            LogScreen(
                state = PreviewLogState.error
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun LogScreenSuccessPreview() {
    TheAISemanticLogTheme {
        Surface(
            modifier = Modifier.fillMaxSize().statusBarsPadding(),
            color = colorResource(R.color.background),
        ) {
            LogScreen(
                state = PreviewLogState.success
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun LogScreenSingleLogPreview() {
    TheAISemanticLogTheme {
        Surface(
            modifier = Modifier.fillMaxSize().statusBarsPadding(),
            color = colorResource(R.color.background),
        ) {
            LogScreen(
                state = PreviewLogState.singleLog
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun LogScreenEmptyPreview() {
    TheAISemanticLogTheme {
        Surface(
            modifier = Modifier.fillMaxSize().statusBarsPadding(),
            color = colorResource(R.color.background),
        ) {
            LogScreen(
                state = PreviewLogState.empty
            )
        }
    }
}
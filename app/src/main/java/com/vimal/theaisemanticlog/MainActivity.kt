package com.vimal.theaisemanticlog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import com.vimal.theaisemanticlog.presentation.logs.LogIntent
import com.vimal.theaisemanticlog.presentation.logs.LogViewModel
import com.vimal.theaisemanticlog.ui.log.LogScreen
import com.vimal.theaisemanticlog.ui.theme.TheAISemanticLogTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: LogViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TheAISemanticLogTheme {
                val state = viewModel.state.collectAsState().value
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = colorResource(id = R.color.background),
                ) { innerPadding ->
                    LaunchedEffect(Unit) {
                        handleClickEvent(LogIntent.LoadLogs)
                    }
                    LogScreen(
                        modifier = Modifier.padding(innerPadding),
                        state = state,
                        event = ::handleClickEvent
                    )
                }
            }
        }
    }

    private fun handleClickEvent(logIntent: LogIntent) {
        when(logIntent) {
            is LogIntent.Search -> {
                viewModel.onIntent(LogIntent.Search(logIntent.query))
            }

            is LogIntent.ShowDetails -> {
                viewModel.onIntent(LogIntent.ShowDetails(logIntent.log))
            }

            is  LogIntent.LoadLogs -> {
                viewModel.onIntent(LogIntent.LoadLogs)
            }

            is LogIntent.HideDetails -> {
                viewModel.onIntent(LogIntent.HideDetails)
            }
        }
    }
}
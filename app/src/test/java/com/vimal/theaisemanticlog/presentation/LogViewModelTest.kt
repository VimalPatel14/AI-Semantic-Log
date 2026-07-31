package com.vimal.theaisemanticlog.presentation

import com.vimal.theaisemanticlog.MainDispatcherRule
import com.vimal.theaisemanticlog.domain.model.Log
import com.vimal.theaisemanticlog.domain.usecase.GetLogsUseCase
import com.vimal.theaisemanticlog.presentation.logs.LogIntent
import com.vimal.theaisemanticlog.presentation.logs.LogViewModel
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LogViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)

    private val getLogsUseCase: GetLogsUseCase = mockk()

    private lateinit var viewModel: LogViewModel

    @Before
    fun setup() {
        viewModel = LogViewModel(getLogsUseCase)
    }

    private val logs = listOf(
        Log(
            id = "1",
            timestamp = "2026-01-22T15:48:17.892472Z",
            severity = "ERROR",
            tag = "network",
            message = "Network timeout",
            latencyMs = 2000,
            aiGenerated = false
        ),
        Log(
            id = "2",
            timestamp = "2026-01-22T15:50:00.000000Z",
            severity = "INFO",
            tag = "ui",
            message = "User logged in",
            latencyMs = 150,
            aiGenerated = true
        )
    )

    @Test
    fun `LoadLogs should set loading state while fetching`() = runTest(testDispatcher) {

        every {
            getLogsUseCase.invoke()
        } returns flow {
            delay(1_000)
            emit(Result.success(logs))
        }

        viewModel.onIntent(LogIntent.LoadLogs)
        runCurrent()

        assertTrue(viewModel.state.value.loading)

        advanceTimeBy(1_000)
        runCurrent()

        assertFalse(viewModel.state.value.loading)
        assertEquals(logs, viewModel.state.value.logs)
    }

    @Test
    fun `LoadLogs should update state with logs`() = runTest {

        every {
            getLogsUseCase.invoke()
        } returns flowOf(Result.success(logs))

        viewModel.onIntent(LogIntent.LoadLogs)

        advanceUntilIdle()

        val state = viewModel.state.value

        assertFalse(state.loading)
        assertEquals(logs, state.logs)
        assertNull(state.error)
    }

    @Test
    fun `LoadLogs should update error when repository fails`() = runTest {

        every {
            getLogsUseCase.invoke()
        } returns flowOf(
            Result.failure(Exception("Something went wrong"))
        )

        viewModel.onIntent(LogIntent.LoadLogs)

        advanceUntilIdle()

        val state = viewModel.state.value

        assertFalse(state.loading)
        assertEquals("Something went wrong", state.error)
    }

    @Test
    fun `Search should filter logs`() = runTest {

        every {
            getLogsUseCase.invoke()
        } returns flowOf(Result.success(logs))

        viewModel.onIntent(LogIntent.LoadLogs)

        advanceUntilIdle()

        viewModel.onIntent(
            LogIntent.Search("network")
        )

        advanceTimeBy(300)
        advanceUntilIdle()

        val state = viewModel.state.value

        assertEquals(1, state.logs.size)
        assertEquals("network", state.logs.first().tag)
    }

    @Test
    fun `Empty search should restore all logs`() = runTest {

        every {
            getLogsUseCase.invoke()
        } returns flowOf(Result.success(logs))

        viewModel.onIntent(LogIntent.LoadLogs)

        advanceUntilIdle()

        viewModel.onIntent(
            LogIntent.Search("")
        )

        advanceTimeBy(300)
        advanceUntilIdle()

        assertEquals(2, viewModel.state.value.logs.size)
    }

    @Test
    fun `ShowDetails should open bottom sheet`() = runTest {

        viewModel.onIntent(
            LogIntent.ShowDetails(logs.first())
        )

        val state = viewModel.state.value

        assertTrue(state.showBottomSheet)
        assertEquals(logs.first(), state.selectedLog)
    }

    @Test
    fun `HideDetails should close bottom sheet`() = runTest {

        viewModel.onIntent(
            LogIntent.ShowDetails(logs.first())
        )

        viewModel.onIntent(
            LogIntent.HideDetails
        )

        val state = viewModel.state.value

        assertFalse(state.showBottomSheet)
        assertNull(state.selectedLog)
    }
}
package com.vimal.theaisemanticlog.domain.usecase

import app.cash.turbine.test
import com.vimal.theaisemanticlog.domain.model.Log
import com.vimal.theaisemanticlog.domain.repository.LogRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test


class GetLogsUseCaseTest {

    private val repository: LogRepository = mockk()

    private lateinit var getLogsUseCase: GetLogsUseCase


    @Before
    fun setup() {
        getLogsUseCase = GetLogsUseCase(repository = repository)
    }


    private val logs = listOf(
        Log(
            id = "1",
            timestamp = "2026-01-22T15:48:17.892472Z",
            severity = "ERROR",
            tag = "network",
            message = "Connection timeout",
            latencyMs = 2000,
            aiGenerated = false
        )
    )


    @Test
    fun `invoke should return logs from repository`() = runTest {
        // Given
        val expectedResult = Result.success(logs)
        every { repository.getLogs() } returns flowOf(expectedResult)

        // When
        val resultFlow = getLogsUseCase()

        // Then
        resultFlow.test {
            val result = awaitItem()
            assertEquals(expectedResult, result)
            awaitComplete()
        }
    }

    @Test
    fun `invoke should return failure when repository fails`() = runTest {
        // Given
        val exception = Exception("Unable to load logs")
        every { repository.getLogs() } returns flowOf(Result.failure(exception))

        // When
        val resultFlow = getLogsUseCase()

        // Then
        resultFlow.test {
            val result = awaitItem()
            assertEquals(exception, result.exceptionOrNull())
            awaitComplete()
        }
    }
}
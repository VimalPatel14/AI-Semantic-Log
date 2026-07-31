package com.vimal.theaisemanticlog.data.repository

import app.cash.turbine.test
import com.vimal.theaisemanticlog.data.local.dao.LogDao
import com.vimal.theaisemanticlog.data.local.entity.LogEntity
import com.vimal.theaisemanticlog.data.remote.api.LogApiService
import com.vimal.theaisemanticlog.data.remote.dto.LogDto
import com.vimal.theaisemanticlog.data.remote.dto.LogResponseDto
import com.vimal.theaisemanticlog.data.remote.dto.MetadataDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class LogRepositoryImplTest {

    private lateinit var repository: LogRepositoryImpl

    private val api: LogApiService = mockk()
    private val dao: LogDao = mockk(relaxed = true)

    @Before
    fun setup() {
        repository = LogRepositoryImpl(api, dao)
    }

    @Test
    fun `getLogs emits cache then remote when cache exists and api succeeds`() = runTest {

        val cachedLogs = listOf(
            LogEntity(
                id = "1",
                timestamp = "2026-08-01T10:00:00Z",
                severity = "INFO",
                tag = "Auth",
                message = "Cached log",
                latencyMs = 100,
                aiGenerated = false
            )
        )

        every {
            dao.getLogs()
        } returns flowOf(cachedLogs)

        val remoteLogs = listOf(
            LogDto(
                id = "2",
                timestamp = "2026-08-02T10:00:00Z",
                severity = "ERROR",
                tag = "Network",
                message = "Remote log",
                metadata = MetadataDto(
                    latency_ms = 250,
                    is_ai_generated = true
                )
            )
        )

        coEvery {
            api.getLogs()
        } returns LogResponseDto(
            total_count = 1,
            session_id = "session-1",
            data = remoteLogs
        )

        repository.getLogs().test {

            val cache = awaitItem()
            assertTrue(cache.isSuccess)
            assertEquals("Cached log", cache.getOrNull()!!.first().message)

            val remote = awaitItem()
            assertTrue(remote.isSuccess)
            assertEquals("Remote log", remote.getOrNull()!!.first().message)

            awaitComplete()
        }

        coVerify(exactly = 1) {
            dao.saveLogs(
                match {
                    it.size == 1 &&
                            it.first().id == "2" &&
                            it.first().message == "Remote log" &&
                            it.first().latencyMs == 250 &&
                            it.first().aiGenerated
                }
            )
        }
    }

    @Test
    fun `getLogs emits remote when cache is empty and api succeeds`() = runTest {

        every {
            dao.getLogs()
        } returns flowOf(emptyList())

        val remoteLogs = listOf(
            LogDto(
                id = "2",
                timestamp = "2026-08-02T10:00:00Z",
                severity = "ERROR",
                tag = "Network",
                message = "Remote log",
                metadata = MetadataDto(
                    latency_ms = 250,
                    is_ai_generated = true
                )
            )
        )

        coEvery {
            api.getLogs()
        } returns LogResponseDto(
            total_count = 1,
            session_id = "session-1",
            data = remoteLogs
        )

        repository.getLogs().test {

            val remote = awaitItem()

            assertTrue(remote.isSuccess)
            assertEquals(1, remote.getOrNull()!!.size)
            assertEquals("Remote log", remote.getOrNull()!!.first().message)

            awaitComplete()
        }

        coVerify(exactly = 1) {
            dao.saveLogs(any())
        }
    }

    @Test
    fun `getLogs emits cache only when api fails`() = runTest {

        val cachedLogs = listOf(
            LogEntity(
                id = "1",
                timestamp = "2026-08-01T10:00:00Z",
                severity = "INFO",
                tag = "Auth",
                message = "Cached log",
                latencyMs = 100,
                aiGenerated = false
            )
        )

        every {
            dao.getLogs()
        } returns flowOf(cachedLogs)

        coEvery {
            api.getLogs()
        } throws RuntimeException("Network Error")

        repository.getLogs().test {

            val cache = awaitItem()

            assertTrue(cache.isSuccess)
            assertEquals("Cached log", cache.getOrNull()!!.first().message)

            awaitComplete()
        }

        coVerify(exactly = 0) {
            dao.saveLogs(any())
        }
    }

    @Test
    fun `getLogs emits failure when cache empty and api fails`() = runTest {

        every {
            dao.getLogs()
        } returns flowOf(emptyList())

        coEvery {
            api.getLogs()
        } throws RuntimeException("Network Error")

        repository.getLogs().test {

            val result = awaitItem()

            assertTrue(result.isFailure)
            assertEquals(
                "Network Error",
                result.exceptionOrNull()?.message
            )

            awaitComplete()
        }

        coVerify(exactly = 0) {
            dao.saveLogs(any())
        }
    }
}
package com.vimal.theaisemanticlog.data.repository

import com.vimal.theaisemanticlog.data.local.dao.LogDao
import com.vimal.theaisemanticlog.data.mapper.toDomain
import com.vimal.theaisemanticlog.data.mapper.toEntity
import com.vimal.theaisemanticlog.data.remote.api.LogApiService
import com.vimal.theaisemanticlog.domain.model.Log
import com.vimal.theaisemanticlog.domain.repository.LogRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class LogRepositoryImpl @Inject constructor(
    private val api: LogApiService,
    private val dao: LogDao
) : LogRepository {
    override fun getLogs(): Flow<Result<List<Log>>> = flow {
        val cachedLogs = dao.getLogs().first().map { it.toDomain() }
        val hasCache = cachedLogs.isNotEmpty()
        if (hasCache) {
            // Emit existing cached logs immediately (if available)
            emit(Result.success(cachedLogs))
        }

        // API call
        try {
            val response = api.getLogs()
            val remoteEntities = response.data.map { it.toDomain() }
                .sortedByDescending { it.timestamp }

            // save logs into db
            dao.saveLogs(remoteEntities.map { it.toEntity() })

            emit(Result.success(remoteEntities))
        } catch (e: Exception) {
            //   If we didn't have cache, emit failure to show the error screen.
            if (!hasCache) {
                emit(Result.failure(e))
            }
        }
    }.flowOn(Dispatchers.IO)
}
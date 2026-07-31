package com.vimal.theaisemanticlog.domain.repository

import com.vimal.theaisemanticlog.domain.model.Log
import kotlinx.coroutines.flow.Flow

interface LogRepository {
    fun getLogs(): Flow<Result<List<Log>>>
}
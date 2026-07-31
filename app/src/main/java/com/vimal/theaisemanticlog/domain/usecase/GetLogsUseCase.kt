package com.vimal.theaisemanticlog.domain.usecase

import com.vimal.theaisemanticlog.domain.repository.LogRepository
import javax.inject.Inject

class GetLogsUseCase @Inject constructor(
    private val repository:LogRepository
){
    operator fun invoke() = repository.getLogs()
}
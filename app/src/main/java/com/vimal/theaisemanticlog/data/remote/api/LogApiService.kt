package com.vimal.theaisemanticlog.data.remote.api

import com.vimal.theaisemanticlog.data.remote.ApiConstants
import com.vimal.theaisemanticlog.data.remote.dto.LogResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface LogApiService {
    @GET(ApiConstants.LOGS_PATH)
    suspend fun getLogs(
        @Query("alt") alt:String = ApiConstants.QUERY_ALT,
        @Query("token") token:String = ApiConstants.QUERY_TOKEN
    ):LogResponseDto

}
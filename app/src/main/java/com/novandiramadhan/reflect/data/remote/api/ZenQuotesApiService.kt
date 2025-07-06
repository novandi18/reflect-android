package com.novandiramadhan.reflect.data.remote.api

import com.novandiramadhan.reflect.data.remote.response.ZenQuotesResponse
import retrofit2.http.GET

interface ZenQuotesApiService {
    @GET("today")
    suspend fun getQuote(): List<ZenQuotesResponse>
}
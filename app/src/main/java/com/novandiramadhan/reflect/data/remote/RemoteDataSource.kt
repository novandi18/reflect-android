package com.novandiramadhan.reflect.data.remote

import android.content.Context
import com.novandiramadhan.reflect.R
import com.novandiramadhan.reflect.data.remote.api.ZenQuotesApiService
import com.novandiramadhan.reflect.data.remote.response.ZenQuotesResponse
import com.novandiramadhan.reflect.data.resource.ApiResource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(
    @ApplicationContext private val context: Context,
    private val zenQuotesApiService: ZenQuotesApiService
) {
    fun getDailyQuote(): Flow<ApiResource<ZenQuotesResponse>> = channelFlow {
        try {
            val response = zenQuotesApiService.getQuote()
            trySend(ApiResource.Success(response[0]))
        } catch (e: HttpException) {
            e.printStackTrace()
            trySend(ApiResource.Error(context.getString(R.string.generic_error)))
        } catch (e: Exception) {
            e.printStackTrace()
            trySend(ApiResource.Error(context.getString(R.string.generic_error)))
        }
    }
}
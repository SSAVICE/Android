package com.ssavice.datastore.repository

import com.ssavice.model.chat.ChattingServiceSummary
import com.ssavice.model.chat.ChattingUserInfo
import kotlinx.coroutines.flow.Flow

interface ChattingMetadataRepository {
    suspend fun getUserInfo(userId: Long): Result<ChattingUserInfo>

    suspend fun getServiceSummary(serviceId: Long): Result<ChattingServiceSummary>

    suspend fun setUserInfo(userInfo: ChattingUserInfo)

    suspend fun setUserInfos(userInfos: List<ChattingUserInfo>)

    suspend fun setServiceSummary(serviceSummary: ChattingServiceSummary)

    suspend fun clearUserInfo()

    suspend fun clearServiceSummary()

    fun getUserInfoFlow(): Flow<Map<Long, ChattingUserInfo>>

    fun getServiceSummaryFlow(): Flow<Map<Long, ChattingServiceSummary>>

}

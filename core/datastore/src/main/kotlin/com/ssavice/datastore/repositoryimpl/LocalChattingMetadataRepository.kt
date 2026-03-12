package com.ssavice.datastore.repositoryimpl

import androidx.datastore.core.DataStore
import com.ssavice.datastore.preferences.ServiceMetadataPreferences
import com.ssavice.datastore.preferences.UserInfoPreferences
import com.ssavice.datastore.repository.ChattingMetadataRepository
import com.ssavice.model.chat.ChattingServiceSummary
import com.ssavice.model.chat.ChattingUserInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.FileNotFoundException
import javax.inject.Inject

class LocalChattingMetadataRepository
    @Inject
    constructor(
        private val serviceDataStore: DataStore<ServiceMetadataPreferences>,
        private val userInfoDataStore: DataStore<UserInfoPreferences>,
    ) : ChattingMetadataRepository {
        override suspend fun getUserInfo(userId: Long): Result<ChattingUserInfo> =
            try {
                val data =
                    userInfoDataStore.data
                        .map {
                            it.infos[userId]
                        }.first()

                if (data != null) {
                    Result.success(data)
                } else {
                    Result.failure(FileNotFoundException())
                }
            } catch (e: Exception) {
                Result.failure(e)
            }

        override suspend fun getServiceSummary(serviceId: Long): Result<ChattingServiceSummary> =
            try {
                val data =
                    serviceDataStore.data
                        .map {
                            it.infos[serviceId]
                        }.first()

                if (data != null) {
                    Result.success(data)
                } else {
                    Result.failure(FileNotFoundException())
                }
            } catch (e: Exception) {
                Result.failure(e)
            }

        override suspend fun setUserInfo(userInfo: ChattingUserInfo) {
            userInfoDataStore.updateData { preferences ->
                preferences.copy(
                    infos = preferences.infos + (userInfo.id to userInfo),
                )
            }
        }

        override suspend fun setUserInfos(userInfos: List<ChattingUserInfo>) {
            userInfoDataStore.updateData { preferences ->
                preferences.copy(
                    infos = preferences.infos + userInfos.associateBy { it.id },
                )
            }
        }

        override suspend fun setServiceSummary(serviceSummary: ChattingServiceSummary) {
            serviceDataStore.updateData {
                it.copy(
                    infos = it.infos + (serviceSummary.serviceId to serviceSummary),
                )
            }
        }

        override suspend fun clearUserInfo() {
            userInfoDataStore.updateData {
                it.copy(
                    infos = mapOf(),
                )
            }
        }

        override suspend fun clearServiceSummary() {
            serviceDataStore.updateData {
                it.copy(
                    infos = mapOf(),
                )
            }
        }

        override fun getUserInfoFlow(): Flow<Map<Long, ChattingUserInfo>> =
            userInfoDataStore.data.map {
                it.infos
            }

        override fun getServiceSummaryFlow(): Flow<Map<Long, ChattingServiceSummary>> =
            serviceDataStore.data.map {
                it.infos
            }
    }

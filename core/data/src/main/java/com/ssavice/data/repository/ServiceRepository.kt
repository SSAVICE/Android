package com.ssavice.data.repository

import com.ssavice.model.ServiceInfo

interface ServiceRepository {
    suspend fun postService(service: ServiceInfo): Result<Long>
}

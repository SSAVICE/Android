package com.ssavice.data.repository

import com.ssavice.model.service.ServiceAddForm

interface ServiceRepository {
    suspend fun postService(service: ServiceAddForm): Result<Long>
}

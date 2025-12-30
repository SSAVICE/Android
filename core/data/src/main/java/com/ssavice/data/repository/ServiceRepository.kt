package com.ssavice.data.repository

import com.ssavice.model.Service.ServiceAddForm

interface ServiceRepository {
    suspend fun postService(service: ServiceAddForm): Result<Long>
}

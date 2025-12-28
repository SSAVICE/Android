package com.ssavice.data.repositoryimpl

import com.ssavice.data.repository.ServiceRepository
import com.ssavice.model.ServiceInfo
import kotlinx.coroutines.delay

class DemoServiceRepository: ServiceRepository {
    override suspend fun postService(service: ServiceInfo): Result<Long> {
        delay(300L)
        return Result.success(1L)
    }
}

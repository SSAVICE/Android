package com.ssavice.data.repositoryimpl

import com.ssavice.data.repository.ServiceRepository
import com.ssavice.data.service.ServiceRetrofitService
import com.ssavice.model.ServiceInfo
import com.ssavice.network.model.AddServiceDTO
import com.ssavice.network.processResponse
import javax.inject.Inject

class RemoteServiceRepository
    @Inject
    constructor(
        private val serviceRetrofitService: ServiceRetrofitService,
    ) : ServiceRepository {
        override suspend fun postService(service: ServiceInfo): Result<Long> =
            processResponse(
                serviceRetrofitService
                    .postService(AddServiceDTO.fromModel(service)),
            ).map { it.serviceId }
    }

package com.ssavice.data.repositoryimpl

import com.ssavice.data.repository.ServiceRepository
import com.ssavice.data.service.ServiceRetrofitService
import com.ssavice.model.service.SearchQuery
import com.ssavice.model.service.SearchResult
import com.ssavice.model.service.ServiceAddForm
import com.ssavice.network.model.AddServiceDTO
import com.ssavice.network.processResponseOnResponseData
import javax.inject.Inject

class RemoteServiceRepository
    @Inject
    constructor(
        private val serviceRetrofitService: ServiceRetrofitService,
    ) : ServiceRepository {
        override suspend fun postService(service: ServiceAddForm): Result<Long> =
            processResponseOnResponseData(
                serviceRetrofitService
                    .postService(AddServiceDTO.fromModel(service)),
            ).map { it.serviceId }

    override suspend fun searchService(
        query: SearchQuery,
        nextId: Long,
        searchCount: Int,
        startIndex: Int
    ): Result<SearchResult> {
        TODO("Not yet implemented")
    }
}

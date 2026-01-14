package com.ssavice.data.repositoryimpl

import com.ssavice.data.repository.ServiceRepository
import com.ssavice.data.service.ServiceRetrofitService
import com.ssavice.model.service.SearchQuery
import com.ssavice.model.service.SearchResult
import com.ssavice.model.service.ServiceAddForm
import com.ssavice.model.service.ServiceDetail
import com.ssavice.network.model.AddServiceDTO
import com.ssavice.network.model.SearchServiceDTO
import com.ssavice.network.processResponseOnResponseData
import javax.inject.Inject

class RemoteServiceRepository
    @Inject
    constructor(
        private val serviceRetrofitService: ServiceRetrofitService, // TODO: 이미지 처리 추가 필요
    ) : ServiceRepository {
        override suspend fun postService(service: ServiceAddForm): Result<Long> =
            processResponseOnResponseData(
                serviceRetrofitService
                    .postService(AddServiceDTO.fromModel(service, listOf())),
            ).map { it.serviceId }

        override suspend fun searchService(
            query: SearchQuery,
            nextId: Long,
            searchCount: Int,
            startIndex: Int,
        ): Result<SearchResult> =
            processResponseOnResponseData(
                serviceRetrofitService
                    .searchService(
                        SearchServiceDTO
                            .fromModel(
                                query = query,
                                nextId = nextId,
                                searchCount = searchCount,
                            ).toMap(),
                    ),
            ).map {
                it.toModel()
            }

        override suspend fun searchService(
            query: SearchQuery,
            searchCount: Int,
            startIndex: Int,
        ): Result<SearchResult> =
            processResponseOnResponseData(
                serviceRetrofitService
                    .searchService(
                        SearchServiceDTO
                            .fromModel(
                                query = query,
                                nextId = null,
                                searchCount = searchCount,
                            ).toMap(),
                    ),
            ).map {
                it.toModel()
            }

        override suspend fun getService(id: Long): Result<ServiceDetail> =
            processResponseOnResponseData(
                serviceRetrofitService.getService(id),
            ).map { it.toModel() }
    }

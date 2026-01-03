package com.ssavice.data.repository

import com.ssavice.model.service.SearchQuery
import com.ssavice.model.service.SearchResult
import com.ssavice.model.service.ServiceAddForm

interface ServiceRepository {
    suspend fun postService(service: ServiceAddForm): Result<Long>

    suspend fun searchService(
        query: SearchQuery,
        nextId: Long,
        searchCount: Int,
        startIndex: Int,
    ): Result<SearchResult>

    suspend fun searchService(
        query: SearchQuery,
        searchCount: Int,
        startIndex: Int,
    ): Result<SearchResult>
}

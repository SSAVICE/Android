package com.ssavice.data.repository

import com.ssavice.model.ImageUploadProgress
import com.ssavice.model.ResizableImage
import com.ssavice.model.service.SearchQuery
import com.ssavice.model.service.SearchResult
import com.ssavice.model.service.ServiceAddForm
import com.ssavice.model.service.ServiceDetail
import kotlinx.coroutines.flow.Flow

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

    suspend fun getService(id: Long): Result<ServiceDetail>

    fun addServiceImage(image: ResizableImage): Flow<ImageUploadProgress>

    suspend fun applyService(id: Long): Result<Unit>

    suspend fun cancelService(id: Long): Result<Unit>

    suspend fun reviewService(
        id: Long,
        content: String,
        rating: Int,
    ): Result<Unit>
}

package com.ssavice.data.repositoryimpl

import android.util.Log
import com.ssavice.data.repository.ServiceRepository
import com.ssavice.model.Date
import com.ssavice.model.ImageUploadProgress
import com.ssavice.model.Region
import com.ssavice.model.ResizableImage
import com.ssavice.model.service.SearchQuery
import com.ssavice.model.service.SearchResult
import com.ssavice.model.service.SearchResultItem
import com.ssavice.model.service.ServiceAddForm
import com.ssavice.model.service.ServiceDetail
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlin.math.min
import kotlin.random.Random
import kotlin.random.nextUInt

class DemoServiceRepository
@Inject
constructor() : ServiceRepository {
    override suspend fun postService(service: ServiceAddForm): Result<Long> {
        delay(300L)
        Log.d("KSC", "Uploaded Service Images: ${service.imageObjectKeys}")
        return Result.success(1L)
    }

    private fun generateRandomPrice(
        min: Int,
        max: Int,
    ): Int = (min / 100..max / 100).random() * 100

    private fun generateRandomRegion2(): String {
        val regions =
            listOf(
                "광진구",
                "달서구",
                "강남구",
                "해운대구",
                "서대문구",
                "유성구",
                "수성구",
                "남동구",
                "중구",
                "동대문구",
                "성동구",
                "종로구",
            )
        return regions.random()
    }

    private fun generateRandomRegion1(): String {
        val regions =
            listOf(
                "서울특별시",
                "부산광역시",
                "대구광역시",
                "광주광역시",
                "포항시",
                "대전광역시",
            )
        return regions.random()
    }

    private fun generateRandomName(): String {
        val adjectives =
            listOf(
                "실리콘밸리",
                "하버드",
                "제주도",
                "뉴욕",
                "미쉐린",
                "구글",
                "올림픽",
                "월스트리트",
                "스위스",
                "NASA",
                "파리",
                "테슬라",
            )
        val nouns =
            listOf(
                "인사이트",
                "스타일",
                "피트니스",
                "메서드",
                "네트워킹",
                "워크샵",
                "레시피",
                "디자인",
                "부트캠프",
                "챌린지",
                "투자 클럽",
                "익스피리언스",
            )
        return "${adjectives.random()} ${nouns.random()}"
    }

    private fun generateRandomTag(): String {
        val tags =
            listOf(
                "건강",
                "취미",
                "스포츠",
                "여행",
                "교육",
                "음악",
                "미술",
                "디자인",
                "IT",
                "비즈니스",
                "금융",
                "외국어",
                "요리",
                "뷰티",
                "커리어",
                "자기계발",
                "댄스",
                "사진",
                "영상",
                "재테크",
            )

        val shuffledTags = tags.shuffled()
        val numberOfTags = (1..3).random()
        val selectedTags = shuffledTags.take(numberOfTags)

        return selectedTags.joinToString(", ")
    }

    private fun generateRandomCompanyName(): String {
        val prefixes =
            listOf(
                "주식회사",
                "(주)",
                "유한회사",
                "(유)",
                "합자회사",
                "합명회사",
            )
        val suffixes =
            listOf(
                "테크",
                "솔루션",
                "네트웍스",
                "이노베이션",
                "파트너스",
                "그룹",
                "랩",
                "스튜디오",
                "컨설팅",
                "모빌리티",
                "홀딩스",
                "에듀",
            )

        // 접두사는 50% 확률로 붙이거나 안 붙임
        val prefix = if (Math.random() > 0.5) prefixes.random() + " " else ""
        val noun = generateRandomName().split(" ")[1] // '피트니스', '디자인' 등 명사 부분만 사용

        return "$prefix${noun}${suffixes.random()}"
    }

    override suspend fun searchService(
        query: SearchQuery,
        nextId: Long,
        searchCount: Int,
        startIndex: Int,
    ): Result<SearchResult> {
        val virtualDbSize = 30
        val count = min(virtualDbSize - startIndex, searchCount)
        delay((7..15).random() * 100L)
        val results =
            List(count) {
                val price = generateRandomPrice(query.minPrice, query.maxPrice).toLong()
                val discountRate = (0..16).random() * 5
                val discountedPrice = price - (price * discountRate / 100)
                val name = generateRandomName()
                SearchResultItem(
                    id = startIndex.toLong() + it,
                    name = name,
                    tag = generateRandomTag(),
                    image = "https://picsum.photos/seed/$name/400",
                    category = query.category.value,
                    minimumMember = (10..25).random(),
                    currentMember = (0..40).random(),
                    basePrice = price,
                    discountRatio = discountRate,
                    discountedPrice = discountedPrice,
                    deadLine = Date.now().addDay((1..10).random()),
                    region =
                        Region(
                            latitude = 0.0,
                            longitude = 0.0,
                            region1 = generateRandomRegion1(),
                            region2 = generateRandomRegion2(),
                        ),
                    companyName = generateRandomCompanyName(),
                    companyId = (0..100).random().toLong(),
                )
            }

        return Result.success(
            SearchResult(
                items = results,
                hasNext = startIndex + count < virtualDbSize,
                nextCursor = startIndex + results.size.toLong(),
            ),
        )
    }

    override suspend fun searchService(
        query: SearchQuery,
        searchCount: Int,
        startIndex: Int,
    ): Result<SearchResult> =
        searchService(
            query = query,
            nextId = 0,
            searchCount = searchCount,
            startIndex = startIndex,
        )

    override suspend fun getService(id: Long): Result<ServiceDetail> {
        delay((3..15).random() * 100L)
        val discount = (0..10).random() * 5
        val price = generateRandomPrice(10000, 100000)
        val discountedPrice = price - (price * discount / 100)

        val day = (1..10).random()
        return Result.success(
            ServiceDetail(
                id = id,
                name = generateRandomName(),
                deadLine = Date.now().addDay(day),
                startDate = Date.now().addDay(day + (1..10).random()),
                endDate = Date.now().addDay(day + (1..10).random()),
                imageUrls = List(3) { "https://picsum.photos/id/${id * (it + 1)}/750/400" },
                basePrice = price,
                discountedPrice = discountedPrice,
                discountRatio = discount,
                minimumMember = 10,
                maximumMember = 25,
                currentMember = 5,
                category = "카테고리",
                tag = generateRandomTag(),
                companyId = (0..100).random().toLong(),
                description = "서비스 설명란입니다.",
                liked = false,
                status = "STATUS",
                createdAt = Date.now(),
                regionInfo =
                    Region(
                        latitude = 0.0,
                        longitude = 0.0,
                        region1 = "강남구 역삼동",
                        region2 = "역삼동",
                    ),
            ),
        )
    }

    override fun addServiceImage(image: ResizableImage): Flow<ImageUploadProgress> {
        return flow {
            emit(ImageUploadProgress.Preprocessing)

            delay(200L)
            for (i in 0..10) {
                emit(ImageUploadProgress.Progress(i * 10))
                delay(200L)
            }
            emit(
                ImageUploadProgress.Done((Random.Default.nextUInt().toString()))
            )
        }
    }
}

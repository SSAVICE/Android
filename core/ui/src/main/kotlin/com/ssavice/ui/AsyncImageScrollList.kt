package com.ssavice.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ssavice.designsystem.theme.SsaviceTheme
import kotlin.math.absoluteValue

/**
 * URL 리스트를 받아 가로로 스크롤되는 이미지 갤러리를 표시하는 컴포저블.
 * 하단에 현재 페이지를 나타내는 인디케이터가 포함됩니다.
 *
 * @param modifier 이 컴포저블에 적용할 Modifier.
 * @param imageUrls 표시할 이미지 URL 목록.
 * @param onImageClick 이미지를 클릭했을 때 호출될 콜백. 클릭된 이미지의 Url이 전달됩니다.
 * @param pagerState 외부에서 페이저의 상태를 제어할 필요가 있을 때 사용합니다.
 * @param imageAspectRatio 이미지의 가로세로 비율. `null`이면 Modifier에 따라 채워집니다.
 * @param contentScale 이미지의 스케일링 방식을 결정합니다.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AsyncImageScrollList(
    modifier: Modifier = Modifier,
    imageUrls: List<String>,
    onImageClick: (String) -> Unit = {},
    pagerState: PagerState = rememberPagerState { imageUrls.size },
    imageAspectRatio: Float? = null,
    contentScale: ContentScale = ContentScale.Crop,
) {
    // 이미지가 없을 경우 아무것도 표시하지 않음
    if (imageUrls.isEmpty()) {
        return
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomCenter
    ) {
        // 1. 가로 스크롤이 가능한 이미지 페이저
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp), // 좌우에 다음/이전 이미지가 살짝 보이도록 패딩 설정
        ) { pageIndex ->
            val pageOffset = (pagerState.currentPage - pageIndex) + pagerState.currentPageOffsetFraction
            val alpha = lerp(
                start = 0.5f, // 좌우 이미지의 최소 투명도
                stop = 1f,    // 중앙 이미지의 투명도
                fraction = 1f - pageOffset.absoluteValue.coerceIn(0f, 1f)
            )
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrls[pageIndex])
                    .crossfade(true) // 부드러운 이미지 로딩 효과
                    .build(),
                contentDescription = "Image $pageIndex",
                contentScale = contentScale,
                modifier = Modifier
                    .graphicsLayer {
                        this.alpha = alpha
                    }
                    .run {
                        if (imageAspectRatio != null) {
                            aspectRatio(imageAspectRatio)
                        } else {
                            this
                        }
                    }
                    .fillMaxWidth()
                    .clickable { onImageClick(imageUrls[pageIndex]) }
            )
        }

        // 2. 인디케이터 (이미지가 2개 이상일 때만 표시)
        if (imageUrls.size > 1) {
            PagerIndicator(
                modifier = Modifier.padding(bottom = 10.dp),
                pagerState = pagerState
            )
        }
    }
}

/**
 * 페이저의 현재 상태를 점(dot) 형태로 표시하는 인디케이터.
 *
 * @param modifier 이 컴포저블에 적용할 Modifier.
 * @param pagerState 관찰할 PagerState.
 * @param activeColor 활성화된 인디케이터 점의 색상.
 * @param inactiveColor 비활성화된 인디케이터 점의 색상.
 * @param indicatorSize 인디케이터 점의 크기.
 * @param spacing 점 사이의 간격.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagerIndicator(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    activeColor: Color = Color.DarkGray,
    inactiveColor: Color = Color.LightGray,
    indicatorSize: Dp = 8.dp,
    spacing: Dp = 8.dp,
) {
    Row(
        modifier = modifier.wrapContentHeight(),
        horizontalArrangement = Arrangement.spacedBy(spacing),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pagerState.pageCount) { iteration ->
            val color = if (pagerState.currentPage == iteration) activeColor else inactiveColor
            Box(
                modifier = Modifier
                    .size(indicatorSize)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}

// --- Preview 코드 ---

@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true, name = "Image Scroll List Preview")
@Composable
private fun AsyncImageScrollListPreview() {
    val fakeImageUrls = listOf(
        // 미리보기용 플레이스홀더 이미지 URL
        "https://via.placeholder.com/600/92c952",
        "https://via.placeholder.com/600/771796",
        "https://via.placeholder.com/600/24f355",
        "https://via.placeholder.com/600/d32776",
    )

    SsaviceTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            AsyncImageScrollList(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                imageUrls = fakeImageUrls,
            )
        }
    }
}

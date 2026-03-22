package com.ssavice.service_detail.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.ssavice.designsystem.theme.shimmerBrush

@Composable
fun ServiceDetailSkeleton(
    modifier: Modifier = Modifier,
    brush: Brush = shimmerBrush() // designsystem의 shimmerBrush 사용
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // 1. 상단 이미지 영역
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .height(250.dp)
                .background(brush)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            // 2. 제목 자리
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(28.dp)
                    .clip(MaterialTheme.shapes.small)
                    .background(brush)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 3. 태그들
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                repeat(3) {
                    Box(
                        modifier = Modifier
                            .size(width = 50.dp, height = 20.dp)
                            .clip(MaterialTheme.shapes.small)
                            .background(brush)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 4. 가격 카드 스켈레톤
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(100.dp)
                .clip(MaterialTheme.shapes.medium)
                .background(brush)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 5. 정보 로우 (위치, 인원, 기간)
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            repeat(3) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(20.dp)
                        .background(brush))
                }
            }
        }
    }
}

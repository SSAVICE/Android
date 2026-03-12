package com.ssavice.chat_list.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp

@Composable
fun ChatRoomSkeletonItem(brush: Brush) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 프로필 이미지 자리
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(brush)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            // 방 이름 자리
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .size(20.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(brush)
            )
            Spacer(modifier = Modifier.size(8.dp))
            // 마지막 메시지 자리
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .size(14.dp)
                    .clip(androidx.compose.foundation.shape.RoundedCornerShape(4.dp))
                    .background(brush)
            )
        }

        // 날짜/시간 자리
        Box(
            modifier = Modifier
                .size(width = 40.dp, height = 14.dp)
                .clip(androidx.compose.foundation.shape.RoundedCornerShape(4.dp))
                .background(brush)
        )
    }
}

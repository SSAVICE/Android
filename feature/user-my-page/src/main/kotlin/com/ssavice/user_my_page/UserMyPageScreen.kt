package com.ssavice.user_my_page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.LibraryAddCheck
import androidx.compose.material.icons.outlined.PersonOff
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssavice.designsystem.component.SsaviceElevatedCard
import com.ssavice.designsystem.theme.SsaviceTheme
import com.ssavice.user_my_page.ui.ParticipationSummary
import com.ssavice.user_my_page.ui.ProfileSummary

@Composable
fun MyPageScreen(
    modifier: Modifier,
    myPageUiState: MyPageUiState,
    onEditProfileButtonClick: () -> Unit = {},
    onParticipatedServiceButtonClick: () -> Unit = {},
    onLikedServiceButtonClick: () -> Unit = {},
    onHelpButtonClick: () -> Unit = {},
    onLogoutButtonClick: () -> Unit = {},
    onWithdrawButtonClick: () -> Unit = {}
) {
    @Composable
    fun Element(
        icon: ImageVector,
        title: String,
        description: String,
        onClick: () -> Unit = {}
    ) {
        SsaviceElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .padding(5.dp),
            onClick = onClick,
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                verticalArrangement = spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = icon,
                    modifier = Modifier.size(20.dp),
                    contentDescription = title,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }

    @Composable
    fun SmallElement(
        icon: ImageVector,
        title: String,
        red: Boolean = false,
        onClick: () -> Unit = {}
    ) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(5.dp),
            onClick = onClick,
            colors =
                if(!red) CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                ) else CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError,
                ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        ) {
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = icon,
                    modifier = Modifier.size(16.dp),
                    contentDescription = title,
                    tint = if(red) MaterialTheme.colorScheme.onError else MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

    }
    Column(modifier = modifier) {
        ProfileSummary(
            modifier = Modifier.padding(8.dp),
            profileState = myPageUiState.profile,
            onEditClick = onEditProfileButtonClick)
        Spacer(modifier = Modifier.height(8.dp))

        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = spacedBy(5.dp)
        ) {
            ParticipationSummary(myPageUiState.participation)
            Spacer(modifier = Modifier.height(8.dp))
            Element(
                icon = Icons.Outlined.LibraryAddCheck,
                title = "참여 서비스",
                description = "진행 중인 서비스 & 예약",
                onClick = onParticipatedServiceButtonClick
            )
            Element(
                icon = Icons.Outlined.FavoriteBorder,
                title = "관심 서비스",
                description = "좋아요 누른 서비스 목록",
                onClick = onLikedServiceButtonClick
            )
            Element(
                icon = Icons.AutoMirrored.Outlined.HelpOutline,
                title = "문의",
                description = "고객지원",
                onClick = onHelpButtonClick
            )
            Spacer(modifier = Modifier.height(6.dp))

            SmallElement(
                icon = Icons.AutoMirrored.Outlined.Logout,
                title = "로그아웃",
                onClick = onLogoutButtonClick
            )
            SmallElement(
                icon = Icons.Outlined.PersonOff,
                title = "회원 탈퇴",
                onClick = onWithdrawButtonClick,
                red = true
            )
        }
    }
}

@Preview
@Composable
fun MyPagePreview() {
    val state by remember {
        mutableStateOf(
            MyPageUiState(
                profile = ProfileState(
                    "권성찬",
                    locationInfo = "대구 달서구 송현1동",
                    description = "설명창 입니다.\n두 번째 줄 입니다.",
                    createdAt = "가입일: 2026-01-06",
                    profileUrl = "https://picsum.photos/200",
                ),
                participation = ParticipationState(
                    onProgress = 10,
                    done = 21,
                    total = 75
                ),
                profileState = MyPageState.Done,
                participationState = MyPageState.Done
            )
        )
    }

    SsaviceTheme {
        Scaffold { innerPadding ->
            MyPageScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(innerPadding),
                myPageUiState = state
            )
        }
    }
}

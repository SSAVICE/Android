package com.ssavice.user_my_page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.LibraryAddCheck
import androidx.compose.material.icons.outlined.PersonOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ssavice.designsystem.theme.SsaviceTheme
import com.ssavice.model.enums.ServiceState
import com.ssavice.user_my_page.ui.MyPageItem
import com.ssavice.user_my_page.ui.MyPageSmallItem
import com.ssavice.user_my_page.ui.ParticipationSummary
import com.ssavice.user_my_page.ui.ProfileSummary

@Composable
fun MyPageRoute(
    modifier: Modifier = Modifier,
    viewModel: UserMyPageViewModel = hiltViewModel(),
    onEditProfileButtonClick: (ProfileState?) -> Unit = {},
    onParticipatedServiceButtonClick: () -> Unit = {},
    onLikedServiceButtonClick: () -> Unit = {},
    onHelpButtonClick: () -> Unit = {},
    onLogoutButtonClick: () -> Unit = {},
    onWithdrawButtonClick: () -> Unit = {},
    onServiceSummaryClick: (ServiceState) -> Unit = {},
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.profileState) {
        if (state.profileState is MyPageState.Waiting) {
            viewModel.loadProfile()
        }
    }
    LaunchedEffect(state.participationState) {
        if (state.participationState is MyPageState.Waiting) {
            viewModel.loadParticipationInfo()
        }
    }

    MyPageScreen(
        modifier =
            modifier
                .background(MaterialTheme.colorScheme.background),
        myPageUiState = state,
        onEditProfileButtonClick = onEditProfileButtonClick,
        onParticipatedServiceButtonClick = onParticipatedServiceButtonClick,
        onLikedServiceButtonClick = onLikedServiceButtonClick,
        onHelpButtonClick = onHelpButtonClick,
        onLogoutButtonClick = viewModel::onLogout,
        onWithdrawButtonClick = onWithdrawButtonClick,
        onServiceSummaryClick = onServiceSummaryClick,
    )
}

@Composable
fun MyPageScreen(
    modifier: Modifier,
    myPageUiState: MyPageUiState,
    onEditProfileButtonClick: (ProfileState?) -> Unit = {},
    onParticipatedServiceButtonClick: () -> Unit = {},
    onLikedServiceButtonClick: () -> Unit = {},
    onHelpButtonClick: () -> Unit = {},
    onLogoutButtonClick: () -> Unit = {},
    onWithdrawButtonClick: () -> Unit = {},
    onServiceSummaryClick: (ServiceState) -> Unit = {},
) {
    var showLogoutDialog by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        ProfileSummary(
            modifier = Modifier.padding(8.dp),
            profileState = myPageUiState.profile,
            onEditClick = { onEditProfileButtonClick(myPageUiState.profile) },
        )
        Spacer(modifier = Modifier.height(8.dp))

        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = spacedBy(5.dp),
        ) {
            ParticipationSummary(myPageUiState.participation, onServiceSummaryClick)
            Spacer(modifier = Modifier.height(8.dp))
            MyPageItem(
                icon = Icons.Outlined.LibraryAddCheck,
                title = "참여 서비스",
                description = "진행 중인 서비스 & 예약",
                onClick = onParticipatedServiceButtonClick,
            )
            MyPageItem(
                icon = Icons.Outlined.FavoriteBorder,
                title = "관심 서비스",
                description = "좋아요 누른 서비스 목록",
                onClick = onLikedServiceButtonClick,
            )
            MyPageItem(
                icon = Icons.AutoMirrored.Outlined.HelpOutline,
                title = "문의",
                description = "고객지원",
                onClick = onHelpButtonClick,
            )
            Spacer(modifier = Modifier.height(6.dp))

            MyPageSmallItem(
                icon = Icons.AutoMirrored.Outlined.Logout,
                title = "로그아웃",
                onClick = { showLogoutDialog = true },
            )
            MyPageSmallItem(
                icon = Icons.Outlined.PersonOff,
                title = "회원 탈퇴",
                onClick = onWithdrawButtonClick,
                red = true,
            )
        }
    }

    if (showLogoutDialog) {
        LogoutAlertDialog(
            onApply = {
                showLogoutDialog = false
                onLogoutButtonClick()
            },
            onDismiss = { showLogoutDialog = false },
        )
    }
}

@Composable
fun LogoutAlertDialog(
    onApply: () -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "경고") },
        text = { Text(text = "로그아웃 하시겠습니까?") },
        confirmButton = {
            Button(onClick = onApply) {
                Text("확인")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("취소")
            }
        },
    )
}

@Preview
@Composable
fun MyPagePreview() {
    val state by remember {
        mutableStateOf(
            MyPageUiState(
                profile =
                    ProfileState(
                        "권성찬",
                        locationInfo = "대구 달서구 송현1동",
                        description = "설명창 입니다.\n두 번째 줄 입니다.",
                        createdAt = "가입일: 2026-01-06",
                        profileUrl = "https://picsum.photos/200",
                        email = "ksc1008@naver.com",
                        phoneNumber = "010-1234-1234",
                    ),
                participation =
                    ParticipationState(
                        onProgress = 10,
                        done = 21,
                        total = 75,
                    ),
                profileState = MyPageState.Done,
                participationState = MyPageState.Done,
            ),
        )
    }

    SsaviceTheme {
        Scaffold { innerPadding ->
            MyPageScreen(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(innerPadding),
                myPageUiState = state,
            )
        }
    }
}

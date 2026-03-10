package com.ssavice.seller_my_page

import android.content.ClipData
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.LibraryAddCheck
import androidx.compose.material.icons.outlined.PersonOff
import androidx.compose.material.icons.outlined.Reviews
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.toClipEntry
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ssavice.designsystem.theme.SsaviceTheme
import com.ssavice.seller_my_page.ui.MyPageItem
import com.ssavice.seller_my_page.ui.MyPageSmallItem
import com.ssavice.seller_my_page.ui.ParticipationSummary
import com.ssavice.seller_my_page.ui.ProfileSummary
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun SellerMyPageRoute(
    modifier: Modifier = Modifier,
    viewModel: SellerMyPageViewModel = hiltViewModel(),
    onEditProfileButtonClick: (ProfileState?) -> Unit = {},
    onParticipatedServiceButtonClick: () -> Unit = {},
    onHelpButtonClick: () -> Unit = {},
    onLogoutButtonClick: () -> Unit = {},
    onWithdrawButtonClick: () -> Unit = {},
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
        onHelpButtonClick = onHelpButtonClick,
        onLogoutButtonClick = viewModel::onLogout,
        onWithdrawButtonClick = onWithdrawButtonClick,
    )
}

@Composable
fun MyPageScreen(
    modifier: Modifier,
    myPageUiState: MyPageUiState,
    onEditProfileButtonClick: (ProfileState?) -> Unit = {},
    onParticipatedServiceButtonClick: () -> Unit = {},
    onHelpButtonClick: () -> Unit = {},
    onReviewButtonClick: () -> Unit = {},
    onLogoutButtonClick: () -> Unit = {},
    onWithdrawButtonClick: () -> Unit = {},
) {
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showHelpDialog by remember { mutableStateOf(false) }
    var showWithdrawMessageInHelpDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current
    val clipboardManager = LocalClipboard.current

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
            ParticipationSummary(myPageUiState.participation)
            Spacer(modifier = Modifier.height(8.dp))
            MyPageItem(
                icon = Icons.Outlined.LibraryAddCheck,
                title = "내 서비스",
                description = "내가 생성한 서비스 목록",
                onClick = onParticipatedServiceButtonClick,
            )
            MyPageItem(
                icon = Icons.Outlined.Reviews,
                title = "리뷰",
                description = "고객이 남긴 리뷰 목록",
                onClick = onReviewButtonClick,
            )
            MyPageItem(
                icon = Icons.AutoMirrored.Outlined.HelpOutline,
                title = "문의",
                description = "고객지원",
                onClick = {

                    showWithdrawMessageInHelpDialog = false
                    showHelpDialog = true
                },
            )
            Spacer(modifier = Modifier.height(6.dp))

            MyPageSmallItem(
                icon = Icons.AutoMirrored.Outlined.Logout,
                title = "로그아웃",
                onClick = { showLogoutDialog = true },
            )
            MyPageSmallItem(
                icon = Icons.Outlined.PersonOff,
                title = "탈퇴 문의",
                onClick = {
                    showWithdrawMessageInHelpDialog = true
                    showHelpDialog = true
                },
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

    if (showHelpDialog) {
        HelpInquiryDialog(
            onDismiss = { showHelpDialog = false },
            onCopyEmail = { email ->
                val data = ClipData.newPlainText("문의 메일", email)
                CoroutineScope(Dispatchers.Main).launch {
                    clipboardManager.setClipEntry(
                        data.toClipEntry(),
                    )
                    Toast
                        .makeText(
                            context,
                            "이메일이 복사되었습니다.",
                            Toast.LENGTH_SHORT,
                        ).show()
                }
                showHelpDialog = false
            },
            onOpenForm = { url ->
                try {
                    uriHandler.openUri(url)
                } catch (e: Exception) {
                    val data = ClipData.newPlainText("문의 폼", url)
                    CoroutineScope(Dispatchers.Main).launch {
                        clipboardManager.setClipEntry(
                            data.toClipEntry(),
                        )
                        Toast
                            .makeText(
                                context,
                                "URL 열기에 실패했습니다.\n문의 폼 주소가 복사되었습니다.",
                                Toast.LENGTH_SHORT,
                            ).show()
                    }
                } finally {
                    showHelpDialog = false
                }
            },
            showWithdrawMessage = showWithdrawMessageInHelpDialog
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
            Button(
                onClick = onDismiss,
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    ),
            ) {
                Text("취소")
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
    )
}


@Composable
fun HelpInquiryDialog(
    onDismiss: () -> Unit,
    onCopyEmail: (String) -> Unit,
    onOpenForm: (String) -> Unit,
    showWithdrawMessage: Boolean = false
) {
    val developerEmail = "ssavice.contact@gmail.com"
    val inquiryFormUrl =
        "https://docs.google.com/forms/d/e/1FAIpQLSc3rhc" +
                "aLfT3zhwiTrsBJ3L6DNh21WS4WGAqfh6cLnVHXF" +
                "O46A/viewform?usp=publish-editor"

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "문의하기",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
            )
        },
        text = {
            Column(verticalArrangement = spacedBy(16.dp)) {
                if (showWithdrawMessage) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f),
                                shape = MaterialTheme.shapes.small
                            )
                            .padding(12.dp),
                        verticalArrangement = spacedBy(4.dp)
                    ) {
                        Text(
                            text = "⚠️ 탈퇴 문의 안내",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "판매자 탈퇴는 정산 및 진행 중인 서비스 확인이 필요합니다. 아래 '문의 폼'을 통해 접수해 주시면 확인 후 처리를 도와드리겠습니다.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Column(verticalArrangement = spacedBy(4.dp)) {
                    Text(
                        text = "이메일 문의",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .clickable { onCopyEmail(developerEmail) }
                                .background(
                                    MaterialTheme.colorScheme.surfaceDim,
                                    shape = MaterialTheme.shapes.small,
                                )
                                .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = developerEmail,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                        Text(
                            text = "복사",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.secondary,
                        )
                    }
                }

                Column(verticalArrangement = spacedBy(4.dp)) {
                    Text(
                        text = "문의 폼 접수",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Button(
                        onClick = { onOpenForm(inquiryFormUrl) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.small,
                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceDim,
                                contentColor = MaterialTheme.colorScheme.onSurface,
                            ),
                    ) {
                        Text("문의 폼 열기", fontWeight = FontWeight.Bold)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("닫기")
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
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
                        profileUrl = "https://picsum.photos/200",
                        phoneNumber = "010-1234-1234",
                        detail = "",
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

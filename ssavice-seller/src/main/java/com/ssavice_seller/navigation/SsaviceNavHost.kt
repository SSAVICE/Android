package com.ssavice_seller.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.ssavice.post_service.navigation.addServiceScreen
import com.ssavice.post_service.navigation.navigateToAddService
import com.ssavice.seller_main.navigation.MainRoute
import com.ssavice.seller_main.navigation.mainScreen
import com.ssavice.seller_main.navigation.navigateToMain
import com.ssavice.seller_register.navigation.registerScreen
import com.ssavice.ui.navigation.ScaffoldConfig
import kotlinx.serialization.Serializable

@Composable
fun SsaviceNavHost(modifier: Modifier = Modifier,
                   navController: NavHostController,
                   startDestination: @Serializable Any,
                   onScaffoldConfigResolved: (ScaffoldConfig) -> Unit) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        mainScreen(
            onAddClick = {
                navController.navigateToAddService()
            },
            onScreenResolved = {
                onScaffoldConfigResolved(
                    ScaffoldConfig.TitleAndDefaultBottom(
                        title = "대시보드"
                    )
                )
            }
        )
        registerScreen(
            onSubmit = {
                navController.navigateToMain()
            },
            onScreenResolved = {
                onScaffoldConfigResolved(
                    ScaffoldConfig.TitleWithCustomBottom(
                        title = "판매자 회원가입"
                    )
                )
            }
        )

        addServiceScreen(
            onDismiss = {
                navController.popBackStack()
            },
            onSubmit = {
                navController.popBackStack()
            },
            onScreenResolved = {
                onScaffoldConfigResolved(
                    ScaffoldConfig.TitleWithCustomBottom(
                        title = "서비스 등록",
                        onBackButtonClick = {
                            navController.navigateUp()
                        }
                    ),
                )
            }
        )
    }
}

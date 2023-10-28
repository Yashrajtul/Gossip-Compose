package com.example.gossip.navigation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.gossip.firebaseauth.ui.AuthViewModel
import com.example.gossip.ui.SplashScreen
import com.example.gossip.ui.home.ChatScreen
import com.example.gossip.ui.home.SettingScreen
import com.example.gossip.ui.phonelogin.DetailsLogin
import com.example.gossip.ui.phonelogin.Login
import com.example.gossip.ui.phonelogin.LoginViewModel
import com.example.gossip.ui.phonelogin.OtpScreen

@Composable
fun NavigationGraph(activity: Activity) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "auth") {
        composable("splashscreen") {
//            SplashScreen {
//
//            }
        }
        navigation(
            startDestination = "login",
            route = "auth"
        ) {
            composable("login") {
                val viewModel = it.sharedViewModel<LoginViewModel>(navController)
                val loginState = viewModel.loginUiState.collectAsStateWithLifecycle()

                Login(
                    phoneNumber = loginState.value.phoneNumber,
                    isError = loginState.value.isError,
                    isButtonEnabled = loginState.value.isButtonEnabled,
                    isDialog = loginState.value.isDialog,
                    getPhoneNumber = viewModel::getPhoneNumber,
                    sendOtp = {
                        viewModel.sendOtp(activity)
//                        if(loginState.value.otpSent)
                        navController.navigate("otp")
                    }
                )
            }
            composable("otp") {
                val viewModel = it.sharedViewModel<LoginViewModel>(navController)
                val loginState = viewModel.loginUiState.collectAsStateWithLifecycle()
                OtpScreen(
                    otp = loginState.value.otp,
                    isDialog = loginState.value.isDialog,
                    getOtp = viewModel::getOtp,
                    verifyOtp = {
                        if (loginState.value.otp.length == 6) {
                            viewModel.verifyOtp(activity)
//                                if(loginState.value.otpVerified)
                            navController.navigate("userinfo")
                        }
                    }
                )
            }
            composable("userinfo") {
                val viewModel = it.sharedViewModel<LoginViewModel>(navController)
                val loginState = viewModel.loginUiState.collectAsStateWithLifecycle()
                DetailsLogin()
            }
        }
        navigation(
            startDestination = "chats",
            route = "home"
        ) {
            composable("chats") {

            }
            composable("settings") {

            }
        }
    }
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(
    navController: NavHostController
): T {
    val navGraphRoute = destination.parent?.route ?: return hiltViewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return hiltViewModel(parentEntry)
}
package com.example.gossip.navigation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import com.example.gossip.ui.splash.SplashScreen1
import com.example.gossip.ui.phonelogin.DetailsLogin
import com.example.gossip.ui.phonelogin.Login
import com.example.gossip.ui.phonelogin.LoginViewModel
import com.example.gossip.ui.phonelogin.OtpScreen
import com.example.gossip.ui.settings.SettingScreen
import com.example.gossip.ui.settings.SettingsViewModel
import com.example.gossip.ui.splash.SplashViewModel

@Composable
fun NavigationGraph(activity: Activity) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = GossipScreen.SplashScreen.name) {
        composable(GossipScreen.SplashScreen.name) {
            val viewModel = hiltViewModel<SplashViewModel>()
            SplashScreen1 {
                navController.navigate(
                    if (viewModel.isLoggedIn && viewModel.usernameEntered) GossipScreen.Home.name
                    else if (viewModel.isLoggedIn) AuthScreen.DetailEntry.name
                    else GossipScreen.Auth.name)
                { popUpTo(0) }
            }
        }
        navigation(
            startDestination = AuthScreen.PhoneEntry.name,
            route = GossipScreen.Auth.name
        ) {
            composable(AuthScreen.PhoneEntry.name) {
                val viewModel = it.sharedViewModel<LoginViewModel>(navController)
                val loginState by viewModel.loginUiState.collectAsStateWithLifecycle()
                LaunchedEffect(key1 = loginState.navigate) {
                    if (loginState.navigate) {
                        viewModel.updateNavigationState()
                        navController.navigate(AuthScreen.OtpEntry.name)
                    }
                }
                Login(
                    phoneNumber = loginState.phoneNumber,
                    isError = loginState.isError,
                    isDialog = loginState.isDialog,
                    getPhoneNumber = viewModel::getPhoneNumber,
                    login = { viewModel.login(activity) }
                )
            }
            composable(AuthScreen.OtpEntry.name) {
                val viewModel = it.sharedViewModel<LoginViewModel>(navController)
                val loginState by viewModel.loginUiState.collectAsStateWithLifecycle()
                LaunchedEffect(key1 = loginState.navigate) {
                    if (loginState.navigate) {
                        viewModel.updateNavigationState()
                        if(loginState.username.isEmpty())
                            navController.navigate(AuthScreen.DetailEntry.name){ popUpTo(0) }
                        else
                            navController.navigate(GossipScreen.Home.name){ popUpTo(0) }
                    }
                }
                OtpScreen(
                    otp = loginState.otp,
                    timer = loginState.timer,
                    isError = loginState.isError,
                    isDialog = loginState.isDialog,
                    isButtonEnabled = loginState.isButtonEnabled,
                    getOtp = { otp ->
                        viewModel.getOtp(otp)
                        if (otp.length == 6) {
                            viewModel.verifyOtp(activity)
                        }
                    },
                    updateTimer = viewModel::updateTimer,
                    resendOtp = { viewModel.resendOtp(activity) }
                )
            }
            composable(AuthScreen.DetailEntry.name) {
                val viewModel = it.sharedViewModel<LoginViewModel>(navController)
                val loginState by viewModel.loginUiState.collectAsStateWithLifecycle()
                LaunchedEffect(key1 = loginState.navigate){
                    if (loginState.navigate){
                        viewModel.updateNavigationState()
                        navController.navigate(GossipScreen.Home.name) { popUpTo(0) }
                    }
                }
                DetailsLogin(
                    username = loginState.username,
                    image = loginState.image,
                    isDialog = loginState.isDialog,
                    isError = loginState.isError,
                    getUserName = viewModel::getUserName,
                    getImage = viewModel::getImage,
                    updateProfile = {
                        viewModel.updateProfile(activity)
                    }
                )
            }
        }
        navigation(
            startDestination = HomeScreen.Profile.route,
            route = GossipScreen.Home.name
        ) {
            composable(HomeScreen.Chat.route) {

            }
            composable(HomeScreen.Profile.route) {
                val viewModel = hiltViewModel<SettingsViewModel>()
                val settingsState by viewModel.settingUiState.collectAsStateWithLifecycle()
                SettingScreen(
                    username = settingsState.username,
                    phoneNumber = settingsState.phoneNumber,
                    image = settingsState.image,
                    isDialog = settingsState.isDialog,
                    isError = settingsState.isError,
                    getUserName = viewModel::getUserName,
                    getImage = viewModel::getImage,
                    logout = {
                        viewModel.signOut()
                        navController.navigate(GossipScreen.SplashScreen.name) { popUpTo(0) }
                    },
                    updateProfile = {
                        viewModel.updateProfile(activity)
                    }
                )
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

//@Composable
//fun NavigationGraph1(activity: Activity) {
//    val navController = rememberNavController()
//    NavHost(navController = navController, startDestination = "splashscreen") {
//        composable("splashscreen") {
//            val viewModel = hiltViewModel<SplashViewModel>()
//            SplashScreen1 {
//                if(viewModel.isLoggedIn)
//                    navController.navigate("home") { popUpTo(0) }
//                else
//                    navController.navigate("auth") { popUpTo(0) }
//            }
//        }
//        navigation(
//            startDestination = "login",
//            route = "auth"
//        ) {
//            composable("login") {
//                val viewModel = it.sharedViewModel<LoginViewModel>(navController)
//                val loginState by viewModel.loginUiState.collectAsStateWithLifecycle()
//                LaunchedEffect(key1 = loginState.navigate){
//                    if(loginState.navigate) {
//                        viewModel.updateNavigationState()
//                        navController.navigate("otp")
//                    }
//                }
//                Login(
//                    phoneNumber = loginState.phoneNumber,
//                    isError = loginState.isError,
//                    isDialog = loginState.isDialog,
//                    getPhoneNumber = viewModel::getPhoneNumber,
//                    sendOtp = { viewModel.sendOtp(activity) }
//                )
//            }
//            composable("otp") {
//                val viewModel = it.sharedViewModel<LoginViewModel>(navController)
//                val loginState by viewModel.loginUiState.collectAsStateWithLifecycle()
//                LaunchedEffect(key1 = loginState.navigate){
//                    if(loginState.navigate) {
//                        viewModel.updateNavigationState()
//                        navController.navigate("userinfo")
//                    }                }
//                OtpScreen(
//                    otp = loginState.otp,
//                    timer = loginState.timer,
//                    isError = loginState.isError,
//                    isDialog = loginState.isDialog,
//                    isButtonEnabled = loginState.isButtonEnabled,
//                    getOtp = { otp ->
//                        viewModel.getOtp(otp)
//                        if(otp.length == 6){
//                            viewModel.verifyOtp(activity)
//                        }
//                    },
//                    updateTimer = viewModel::updateTimer,
//                    resendOtp = { viewModel.resendOtp(activity) }
//                )
//            }
//            composable("userinfo") {
//                val viewModel = it.sharedViewModel<LoginViewModel>(navController)
//                val loginState by viewModel.loginUiState.collectAsStateWithLifecycle()
//                DetailsLogin(
//                    username = loginState.username,
//                    image = loginState.image,
//                    isDialog = loginState.isDialog,
//                    isError = loginState.isError,
//                    getUserName = viewModel::getUserName,
//                    getImage = viewModel::getImage,
//                    updateProfile = {
//                        viewModel.updateProfile(activity)
//                        navController.navigate("home"){ popUpTo(0) }
//                    }
//                )
//            }
//        }
//        navigation(
//            startDestination = "settings",
//            route = "home"
//        ) {
//            composable("chats") {
//
//            }
//            composable("settings") {
//                val viewModel = hiltViewModel<SettingsViewModel>()
//                val settingsState by viewModel.settingUiState.collectAsStateWithLifecycle()
//                SettingScreen(
//                    username = settingsState.username,
//                    phoneNumber = settingsState.phoneNumber,
//                    image = settingsState.image,
//                    isDialog = settingsState.isDialog,
//                    isError = settingsState.isError,
//                    getUserName = viewModel::getUserName,
//                    getImage = viewModel::getImage,
//                    logout = {
//                        viewModel.signOut()
//                        navController.navigate("splashscreen"){ popUpTo(0) }
//                    },
//                    updateProfile = {
//                        viewModel.updateProfile(activity)
//                    }
//                )
//            }
//        }
//    }
//}


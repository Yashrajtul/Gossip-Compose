package com.example.gossip.navigation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gossip.ui.chat.ChatScreenContent
import com.example.gossip.ui.chat.ChatScreenViewModel
import com.example.gossip.ui.home.HomeScreen
import com.example.gossip.ui.home.HomeScreensViewModel
import com.example.gossip.ui.home.search.SearchScreen
import com.example.gossip.ui.home.search.SearchViewModel
import com.example.gossip.ui.splash.SplashScreen1
import com.example.gossip.ui.phonelogin.DetailsLogin
import com.example.gossip.ui.phonelogin.DetailsLoginViewModel
import com.example.gossip.ui.phonelogin.Login
import com.example.gossip.ui.phonelogin.LoginViewModel
import com.example.gossip.ui.phonelogin.OtpScreen
import com.example.gossip.ui.settings.SettingScreen
import com.example.gossip.ui.settings.SettingsViewModel
import com.example.gossip.ui.splash.SplashViewModel
import com.example.gossip.utils.showMsg
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NavigationGraph(activity: Activity) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = GossipScreen.SplashScreen.name) {
        composable(GossipScreen.SplashScreen.name) {
            val viewModel = hiltViewModel<SplashViewModel>()
            SplashScreen1 {
                navController.navigate(
                    if (viewModel.isLoggedIn && viewModel.usernameEntered) GossipScreen.Home.name
                    else if (viewModel.isLoggedIn) GossipScreen.DetailEntry.name
                    else GossipScreen.Auth.name
                )
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
                        if (loginState.username.isEmpty())
                            navController.navigate(GossipScreen.DetailEntry.name) { popUpTo(0) }
                        else
                            navController.navigate(GossipScreen.Home.name) { popUpTo(0) }
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
        }
        composable(GossipScreen.DetailEntry.name) {
            val viewModel = hiltViewModel<DetailsLoginViewModel>()
            val detailsState by viewModel.detailsState.collectAsStateWithLifecycle()
            LaunchedEffect(key1 = detailsState.navigate) {
                if (detailsState.navigate) {
                    navController.navigate(GossipScreen.Home.name) { popUpTo(0) }
                }
            }
            LaunchedEffect(key1 = detailsState.userId){
                if (detailsState.userId != ""){
                    viewModel.getUserData(activity)
                }
            }
            DetailsLogin(
                username = detailsState.username,
                image = detailsState.image,
                isDialog = detailsState.isDialog,
                isError = detailsState.isError,
                getUserName = viewModel::getUserName,
                getImage = viewModel::getImage,
                updateProfile = {
                    viewModel.updateProfile(activity)
                }
            )
        }
        navigation(
            startDestination = HomeScreen.Home.route,  //"search",
            route = GossipScreen.Home.name
        ) {
            composable(HomeScreen.Home.route) {
                val viewModel = hiltViewModel<HomeScreensViewModel>()
                val homeState by viewModel.homeState.collectAsStateWithLifecycle()
                val lifecycleOwner = LocalLifecycleOwner.current
                LaunchedEffect(key1 = true) {
                    viewModel.toastEvent.collectLatest { message ->
                        activity.showMsg(message)
                    }
                }
                DisposableEffect(key1 = lifecycleOwner) {
                    val observer = LifecycleEventObserver { _, event ->
                        if (event == Lifecycle.Event.ON_START) {
                            viewModel.getChatRooms()
                        }
//                        else if (event == Lifecycle.Event.ON_STOP) {
//                            viewModel.disconnect()
//                        }
                    }
                    lifecycleOwner.lifecycle.addObserver(observer)
                    onDispose {
                        lifecycleOwner.lifecycle.removeObserver(observer)
                    }
                }
                HomeScreen(
                    chatUsers = homeState.chatUsers,
                    myUserId = viewModel.myUserId,
                    settings = { navController.navigate(HomeScreen.Profile.route) },
                    search = { navController.navigate("search") },
                    navigate = {
                        navController.navigate("chatroom/$it")
                    }
                )
            }
            composable(route = "search") {
                val viewModel = hiltViewModel<SearchViewModel>()
                val searchState by viewModel.searchState.collectAsStateWithLifecycle()
                SearchScreen(
                    searchText = searchState.searchText,
                    users = searchState.users,
                    onSearchTextChange = viewModel::onSearchTextChange,
                    onBackArrowClick = navController::navigateUp,
                    onClick = {
                        navController.navigate("chatroom/$it")
                    }
                )
            }
            composable(
                route = "chatroom/{userId}",
                arguments = listOf(
                    navArgument(name = "userId"){
                        type = NavType.StringType
                        defaultValue = ""
                    }
                )
            ){entry->
                val userId = entry.arguments?.getString("userId")!!
                val viewModel = hiltViewModel<ChatScreenViewModel>()
                LaunchedEffect(key1 = true) {
                    viewModel.toastEvent.collectLatest { message ->
                        activity.showMsg(message)
                    }
                }
                val lifecycleOwner = LocalLifecycleOwner.current
                DisposableEffect(key1 = lifecycleOwner) {
                    val observer = LifecycleEventObserver { _, event ->
                        if (event == Lifecycle.Event.ON_START) {
                            viewModel.getChatRoom(userId)
                        } else if (event == Lifecycle.Event.ON_STOP) {
                            viewModel.disconnect()
                        }
                    }
                    lifecycleOwner.lifecycle.addObserver(observer)
                    onDispose {
                        lifecycleOwner.lifecycle.removeObserver(observer)
                    }
                }
//                LaunchedEffect(key1 = userId){
//                    viewModel.getChatRoom(userId)
//                }
//                viewModel.updateMessages()
                val chatState by viewModel.chatState.collectAsStateWithLifecycle()
                ChatScreenContent(
                    name = chatState.name,
                    input = chatState.messageInput,
                    messages = chatState.messages,
                    messages1 = chatState.messages1,
                    keyboardController = null,
                    getInput = viewModel::onMessageChange,
                    sendMessage = viewModel::sendMessage1,
                    isChatInputFocus = {},
                    navigateUp = {navController.navigateUp()}
                )
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
                    onBackArrowClick = navController::navigateUp,
                    updateProfile = {
                        viewModel.updateProfile(activity)
                    }
                )
            }
        }
    }
}
//{
//    val navController = rememberNavController()
//    NavHost(navController = navController, startDestination = GossipScreen.SplashScreen.name) {
//        composable(GossipScreen.SplashScreen.name) {
//            val viewModel = hiltViewModel<SplashViewModel>()
//            SplashScreen1 {
//                navController.navigate(
//                    if (viewModel.isLoggedIn && viewModel.usernameEntered) GossipScreen.Home.name
////                    else if (viewModel.isLoggedIn) AuthScreen.DetailEntry.name
//                    else GossipScreen.Auth.name)
//                { popUpTo(0) }
//            }
//        }
//        navigation(
//            startDestination = AuthScreen.PhoneEntry.name,
//            route = GossipScreen.Auth.name
//        ) {
//            composable(AuthScreen.PhoneEntry.name) {
//                val viewModel = it.sharedViewModel<LoginViewModel>(navController)
//                val loginState by viewModel.loginUiState.collectAsStateWithLifecycle()
//                LaunchedEffect(key1 = loginState.navigate, key2 = loginState.isLoggedIn) {
//                    if (loginState.isLoggedIn) {
//                        viewModel.getUserData(activity)
//                        navController.navigate(AuthScreen.DetailEntry.name) { popUpTo(0) }
//                    }else if (loginState.navigate) {
//                        viewModel.updateNavigationState()
//                        navController.navigate(AuthScreen.OtpEntry.name)
//                    }
//                }
//                Login(
//                    phoneNumber = loginState.phoneNumber,
//                    isError = loginState.isError,
//                    isDialog = loginState.isDialog,
//                    getPhoneNumber = viewModel::getPhoneNumber,
//                    login = { viewModel.login(activity) }
//                )
//            }
//            composable(AuthScreen.OtpEntry.name) {
//                val viewModel = it.sharedViewModel<LoginViewModel>(navController)
//                val loginState by viewModel.loginUiState.collectAsStateWithLifecycle()
//                LaunchedEffect(key1 = loginState.username) {
//                    if (loginState.navigate) {
//                        viewModel.updateNavigationState()
//                        if(loginState.username.isEmpty())
//                            navController.navigate(AuthScreen.DetailEntry.name){ popUpTo(0) }
//                        else
//                            navController.navigate(GossipScreen.Home.name){ popUpTo(0) }
//                    }
//                }
//                OtpScreen(
//                    otp = loginState.otp,
//                    timer = loginState.timer,
//                    isError = loginState.isError,
//                    isDialog = loginState.isDialog,
//                    isButtonEnabled = loginState.isButtonEnabled,
//                    getOtp = { otp ->
//                        viewModel.getOtp(otp)
//                        if (otp.length == 6) {
//                            viewModel.verifyOtp(activity)
//                        }
//                    },
//                    updateTimer = viewModel::updateTimer,
//                    resendOtp = { viewModel.resendOtp(activity) }
//                )
//            }
//            composable(AuthScreen.DetailEntry.name) {
//                val viewModel = it.sharedViewModel<LoginViewModel>(navController)
//                val loginState by viewModel.loginUiState.collectAsStateWithLifecycle()
//                LaunchedEffect(key1 = loginState.navigate){
//                    if (loginState.navigate){
////                        viewModel.updateNavigationState()
//                        navController.navigate(GossipScreen.Home.name) { popUpTo(0) }
//                    }
//                }
//                DetailsLogin(
//                    username = loginState.username,
//                    image = loginState.image,
//                    isDialog = loginState.isDialog,
//                    isError = loginState.isError,
//                    getUserName = viewModel::getUserName,
//                    getImage = viewModel::getImage,
//                    updateProfile = {
//                        viewModel.updateProfile(activity)
//                    }
//                )
//            }
//        }
//        navigation(
//            startDestination = HomeScreen.Profile.route,
//            route = GossipScreen.Home.name
//        ) {
//            composable(HomeScreen.Chat.route) {
//
//            }
//            composable(HomeScreen.Profile.route) {
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
//                        navController.navigate(GossipScreen.SplashScreen.name) { popUpTo(0) }
//                    },
//                    updateProfile = {
//                        viewModel.updateProfile(activity)
//                    }
//                )
//            }
//        }
//    }
//}

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


package com.example.gossip.navigation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.gossip.firebaseauth.screens.PhoneAuthScreen
import com.example.gossip.firebaseauth.ui.AuthViewModel
import com.example.gossip.ui.SplashScreen
import com.example.gossip.ui.home.ChatScreen
import com.example.gossip.ui.home.SettingScreen

@Composable
fun NavigationGraph(activity: Activity) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "splashscreen"){
        composable("splashscreen"){
            SplashScreen {

            }
        }
        navigation(
            startDestination = "login",
            route = "auth"
        ){
            composable("login"){
                val viewModel = it.sharedViewModel<AuthViewModel>(navController)
//                val authUiState by viewModel.authUiState.collectAsStateWithLifecycle()
                PhoneAuthScreen(activity = activity, viewModel = viewModel)
            }
            composable("otp"){
                val viewModel = it.sharedViewModel<AuthViewModel>(navController)

            }
            composable("userinfo"){
                val viewModel = it.sharedViewModel<AuthViewModel>(navController)

            }
        }
        navigation(
            startDestination = "chats",
            route = "home"
        ){
            composable("chats"){

            }
            composable("settings"){

            }
        }
    }
}

@Composable
inline fun <reified T: ViewModel> NavBackStackEntry.sharedViewModel(
    navController: NavHostController
): T {
    val navGraphRoute = destination.parent?.route?: return hiltViewModel()
    val parentEntry = remember(this){
        navController.getBackStackEntry(navGraphRoute)
    }
    return hiltViewModel(parentEntry)
}
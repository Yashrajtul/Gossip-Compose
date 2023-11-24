package com.example.gossip

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gossip.navigation.NavigationGraph
import com.example.gossip.presentation.chat.ChatScreen
import com.example.gossip.presentation.username.UsernameScreen
import com.example.gossip.ui.theme.GossipTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GossipTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
//                    color = Color(0xFF202020)
                ) {
//                    ImagePickerScreen()

                    NavigationGraph(activity = this)

//                    TestNav()

                }
            }
        }
    }
}

@Composable
fun TestNav() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "username_screen"){
        composable("username_screen"){
            UsernameScreen(onNavigate = navController::navigate)
        }
        composable(
            route = "chat_screen/{username}",
            arguments = listOf(
                navArgument(name = "username"){
                    type = NavType.StringType
                    nullable = true
                }
            )
        ){
            val username = it.arguments?.getString("username")
            ChatScreen(username = username)
        }
    }
}
package com.example.gossip

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gossip.firebaseauth.common.ImagePickerScreen
import com.example.gossip.firebaseauth.screens.PhoneAuthScreen
//import com.example.gossip.ui.MainScreen
import com.example.gossip.ui.SplashScreen
import com.example.gossip.ui.phonelogin.Login
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
                    modifier = Modifier.fillMaxSize(),
//                    color = Color(0xFF202020)
                ) {
//                    Navigation() {
//                        startActivity(MessageActivity.getIntent(this, it))
//                    }

//                    PhoneAuthScreen(
//                        activity = this
//                    )

//                    ImagePickerScreen()

                    Login()
                }
            }
        }
    }
}

//@Preview
//@Composable
//fun PreviewScreen() {
//    Surface(
//        modifier = Modifier.fillMaxSize(),
//        color = Color(0xFF202020)
//    ) {
//        Navigation()
//    }
//}

@Composable
fun Navigation(context: Context = LocalContext.current, onClick: (name: String) -> Unit) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "splash_screen") {
        composable("splash_screen") {
            SplashScreen {
                navController.navigate("main_screen") { popUpTo(0) }
            }
        }
        composable("main_screen") {
//            MainScreen(context, onClick)
        }
    }
}


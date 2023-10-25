package com.example.gossip.firebaseauth

import android.app.Activity
import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import com.example.gossip.firebaseauth.screens.PhoneAuthScreen
import com.example.gossip.ui.theme.GossipTheme
import dagger.hilt.android.AndroidEntryPoint

//@AndroidEntryPoint
//class AuthActivity : ComponentActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
//        super.onCreate(savedInstanceState, persistentState)
//        setContent {
//            GossipTheme {
//                Surface {
//                    Text(text = "hello")
//                    PhoneAuthScreen(
//                        activity = this
//                    )
//                }
//            }
//        }
//    }
//}

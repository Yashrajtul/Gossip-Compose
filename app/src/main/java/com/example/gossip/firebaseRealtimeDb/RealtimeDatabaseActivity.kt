package com.example.gossip.firebaseRealtimeDb

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.gossip.ui.theme.GossipTheme

class RealtimeDatabaseActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContent {
            GossipTheme {
                Surface (
                    modifier = Modifier.fillMaxSize()
                ){

                }
            }
        }
    }
}
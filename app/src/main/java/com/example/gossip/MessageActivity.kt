package com.example.gossip

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.gossip.ui.MessageScreen
import com.example.gossip.ui.theme.GossipTheme

class MessageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val name = intent.getStringExtra("name")
        setContent {
            GossipTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF202020)
                ) {
                    if (name != null) {
                        MessageScreen(this, name)
                    }
                }
            }
        }
    }

    companion object {
        private const val NAME = "name"

        fun getIntent(context: Context, name: String): Intent {
            return Intent(context, MessageActivity::class.java).apply {
                putExtra(NAME, name)
            }
        }
    }
}
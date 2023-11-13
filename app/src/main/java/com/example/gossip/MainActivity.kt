package com.example.gossip

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.gossip.navigation.NavigationGraph
import com.example.gossip.ui.home.SearchScreen
import com.example.gossip.ui.home.SearchViewModel
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

//                    val viewModel: SearchViewModel by viewModels()
//                    val searchState by viewModel.searchState.collectAsStateWithLifecycle()
//                    SearchScreen(
//                        searchText = searchState.searchText,
//                        users = searchState.users,
//                        onSearchTextChange = viewModel::onSearchTextChange
//                    )

//                    SplashScreen1 {
//                        this.showMsg("Navigate")
//                    }


                }
            }
        }
    }
}
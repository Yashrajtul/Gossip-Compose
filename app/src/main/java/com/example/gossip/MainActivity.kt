package com.example.gossip

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.gossip.navigation.NavigationGraph
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
//                    ImagePickerScreen()

                    NavigationGraph(activity = this)

//                    SplashScreen1 {
//                        this.showMsg("Navigate")
//                    }

//                    val viewModel : SettingsViewModel = hiltViewModel()
//                    val settingsState by viewModel.settingUiState.collectAsStateWithLifecycle()
//                    SettingScreen(
//                        username = settingsState.username,
//                        phoneNumber = settingsState.phoneNumber,
//                        image = settingsState.image,
//                        isDialog = false,
//                        isError = false,
//                        getUserName = viewModel::getUserName,
//                        getImage = viewModel::getImage,
//                        logout = {
//                                 viewModel.signOut()
//                        },
//                        updateProfile = {
//                            viewModel.updateProfile(this)
//                        }
//                    )
                }
            }
        }
    }
}
package com.example.gossip.navigation

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.gossip.R

enum class GossipScreen(@StringRes val title: Int) {
    SplashScreen(title = R.string.SplashScreen),
    Auth(title = R.string.Auth),
    Home(title = R.string.app_name)
}
sealed class HomeScreen(val route: String, @StringRes val title: Int) {
    object Chat : HomeScreen("chat", R.string.Chat)
    object Profile : HomeScreen("profile", R.string.OtpEntry)
//    object Message: HomeScreen("message", R.string.DetailEntry)
}
enum class AuthScreen(@StringRes val title: Int, val canNavigateBack: Boolean){
    PhoneEntry(title = R.string.PhoneEntry, false),
    OtpEntry(title = R.string.OtpEntry, true),
    DetailEntry(title = R.string.DetailEntry, true),
}

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeCount: Int? = null
)
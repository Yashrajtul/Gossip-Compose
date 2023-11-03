package com.example.gossip.navigation

import androidx.annotation.StringRes
import com.example.gossip.R

enum class GossipScreen(@StringRes val title: Int) {
    PhoneEntry(title = R.string.PhoneEntry),
    OtpEntry(title = R.string.OtpEntry),
    DetailEntry(title = R.string.DetailEntry),
    Profile(title = R.string.Profile),
    Home(title = R.string.app_name)
}
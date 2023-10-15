package com.example.gossip.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gossip.R
import com.example.gossip.screens.ButtonComponent
import com.example.gossip.screens.ClickableLoginTextComponent
import com.example.gossip.screens.DividerTextComponent
import com.example.gossip.screens.HeadingTextComponent
import com.example.gossip.screens.MyTextField
import com.example.gossip.screens.NormalTextComponent
import com.example.gossip.screens.PasswordTextField
import com.example.gossip.screens.UnderLinedTextComponent


@Composable
fun LoginScreen() {
    Surface(
        color = Color.White,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(28.dp)

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            NormalTextComponent(value = stringResource(id = R.string.login))
            HeadingTextComponent(value = stringResource(id = R.string.welcome))
            Spacer(modifier = Modifier.height(20.dp))
            MyTextField(
                labelValue = stringResource(id = R.string.email),
                painterResource(id = R.drawable.baseline_email_24)
            )
            PasswordTextField(
                labelValue = stringResource(id = R.string.password),
                painterResource = painterResource(
                    id = R.drawable.baseline_lock_24
                )
            )
            Spacer(modifier = Modifier.height(40.dp))

            UnderLinedTextComponent(value = stringResource(id = R.string.forgot_password))
            Spacer(modifier = Modifier.height(40.dp))
            ButtonComponent(value = stringResource(id = R.string.login))
            Spacer(modifier = Modifier.height(40.dp))
            DividerTextComponent()
            ClickableLoginTextComponent(tryingToLogin = false, onTextSelected = {
                // GossipAppRouter.navigateTo(Screen.SignupScreen)
            })
        }
    }
    //SystemBackButtonHandler {
    //   GossipAppRouter.navigateTo(Screen.SignupScreen)
    // }
}

@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}
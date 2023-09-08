package com.example.gossip.screens

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


@Composable
fun SignupScreen(){
    Surface(
        color= Color.White,
        modifier= Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(28.dp)

    )
    {
        Column(modifier=Modifier.fillMaxSize()) {
            NormalTextComponent(value = stringResource(id = R.string.hello))
            HeadingTextComponent(value = stringResource(id = R.string.create_account))
            Spacer(modifier=Modifier.height(20.dp))
            MyTextField(
                labelValue = stringResource(id = R.string.first_name),
                painterResource(id = R.drawable.baseline_person_24)
            )
            MyTextField(
                labelValue = stringResource(id = R.string.last_name),
                painterResource(id = R.drawable.baseline_person_24))
            MyTextField(
                labelValue = stringResource(id = R.string.email),
                painterResource(id = R.drawable.baseline_email_24))

            PasswordTextField(
                labelValue = stringResource(id = R.string.password), painterResource = painterResource(
                    id = R.drawable.baseline_lock_24)
            )
            CheckboxComponent(value = stringResource(id = R.string.terms_conditions),
                onTextSelected = {
                  //  GossipAppRouter.navigateTo(Screen.TermsAndConditionsScreens)

                })
            Spacer(modifier = Modifier.height(40.dp))
            ButtonComponent(value = stringResource(id = R.string.register))
            Spacer(modifier = Modifier.height(30.dp))

            DividerTextComponent()
            Spacer(modifier = Modifier.height(30.dp))
            ClickableLoginTextComponent(tryingToLogin = true,onTextSelected = {
                //GossipAppRouter.navigateTo(Screen.LoginScreen)

            })





        }

    }

}
@Preview
@Composable
fun DefaultPreviewOfSignupScreen(){
    SignupScreen()

}
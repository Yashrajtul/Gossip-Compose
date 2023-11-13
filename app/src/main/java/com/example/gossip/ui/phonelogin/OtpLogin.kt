package com.example.gossip.ui.phonelogin

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gossip.R
import com.example.gossip.common.CommonDialog
import com.example.gossip.common.OTPTextFields
import kotlinx.coroutines.delay

@Composable
fun OtpScreen(
    otp: String,
    timer: Long,
    isError: Boolean,
    isDialog: Boolean,
    isButtonEnabled: Boolean,
    getOtp: (otp: String) -> Unit,
    resendOtp: () -> Unit,
    updateTimer: (timer: Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    if (isDialog)
        CommonDialog()
    LaunchedEffect(key1 = timer){
        delay(1000L)
        if(timer != 0L)
            updateTimer(timer-1)
    }
    Column (
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Column(
            modifier = Modifier.size(400.dp)
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.baseline_password_24),
                contentDescription = "Phone Icon",
                contentScale = ContentScale.Crop,
                modifier = Modifier
//                    .sizeIn(
//                        maxWidth = 200.dp,
//                        maxHeight = 200.dp,
//                        minWidth = 100.dp,
//                        minHeight = 100.dp
//                    )
                    .weight(1.5f)
//                    .fillMaxSize()
            )

            Spacer(modifier = Modifier.height(16.dp).weight(.08f))

            OTPTextFields(
                otp = otp,
                isError = isError,
                length = 6,
                getOtp = { getOtp(it) },
                modifier = Modifier.focusRequester(focusRequester)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    resendOtp()
//                focusManager.clearFocus()
                },
                enabled = isButtonEnabled,
                modifier = Modifier
                    .size(width = 250.dp, height = 40.dp),
                colors = ButtonDefaults.buttonColors()
            ) {
                Text(
                    text = "Resend OTP",
                    fontSize = 18.sp,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(20.dp).weight(.08f))
            Text(
                text = if (timer == 0L) "" else "Resend OTP in ${timer.toString()} sec",
                fontSize = 17.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OtpScreenPreview() {
    OtpScreen(
        otp = "",
        timer = 60L,
        isError = false,
        isDialog = false,
        isButtonEnabled = false,
        getOtp = {},
        resendOtp = {},
        updateTimer = {}
    )
}
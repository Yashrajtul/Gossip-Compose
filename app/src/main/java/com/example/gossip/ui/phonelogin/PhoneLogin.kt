package com.example.gossip.ui.phonelogin

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gossip.R
import com.example.gossip.common.CommonDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login(
    phoneNumber: String,
    isDialog: Boolean,
    isError: Boolean,
    getPhoneNumber: (phoneNumber: String) -> Unit,
    login: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    if(isDialog)
        CommonDialog()
    Column (
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Column(
            modifier = Modifier.size(400.dp)
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.baseline_phone_android_24),
                contentDescription = "Phone Icon",
                contentScale = ContentScale.Crop,
                modifier = Modifier
//                    .sizeIn(
//                        maxWidth = 180.dp,
//                        maxHeight = 180.dp,
//                        minWidth = 100.dp,
//                        minHeight = 100.dp
//                    )
                    .weight(1.5f)
//                    .fillMaxSize()
            )

            Spacer(modifier = Modifier.height(16.dp).weight(.08f))

            OutlinedTextField(
                value = phoneNumber,
                onValueChange = {
                    getPhoneNumber(it)
                },
                label = { Text("Phone Number") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = "Phone Icon",
                        tint = Color.Gray
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        login()
                        focusManager.clearFocus()
                    }
                ),
                isError = isError,
                singleLine = true,
                modifier = Modifier
//                    .widthIn(max = 400.dp)
                    .fillMaxWidth()
                    .padding(8.dp)
                    .focusRequester(focusRequester)
                    .onFocusChanged { newFocusState ->
                        if (newFocusState.isFocused) {
//                        phoneSelection()
                        } else {
                        }
                    }
            )
            Spacer(modifier = Modifier.height(16.dp).weight(.08f))

            Button(
                onClick = {
                    focusManager.clearFocus()
                    login()
                },
                modifier = Modifier
                    .size(width = 250.dp, height = 40.dp),
//                    .fillMaxSize(),
                colors = ButtonDefaults.buttonColors()
            ) {
                Text(
                    text = "Login",
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
        }

    }
}

@Preview(showBackground = true)
@Preview(device = Devices.PIXEL_C, showBackground = true)
@Composable
fun LoginPreview() {
    Login(
        phoneNumber = "9914",
        isDialog = false,
        isError = true,
        getPhoneNumber = {},
        login = {},
    )
}
@Preview(showBackground = true)
@Composable
fun LoginPreview1() {
    Login(
        phoneNumber = "",
        isDialog = false,
        isError = false,
        getPhoneNumber = {},
        login = {},
    )
}
@Preview(showBackground = true)
@Composable
fun LoginPreview2() {
    Login(
        phoneNumber = "",
        isDialog = true,
        isError = false,
        getPhoneNumber = {},
        login = {},
    )
}

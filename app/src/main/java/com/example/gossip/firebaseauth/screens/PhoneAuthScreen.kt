package com.example.gossip.firebaseauth.screens
//
//import android.app.Activity
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Phone
//import androidx.compose.material3.Button
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Icon
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.focus.FocusRequester
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.ImeAction
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.lifecycle.compose.collectAsStateWithLifecycle
//import com.example.gossip.R
//import com.example.gossip.firebaseauth.common.CommonDialog
//import com.example.gossip.firebaseauth.common.OTPTextFields
//import com.example.gossip.firebaseauth.ui.AuthViewModel
//import com.example.gossip.ui.theme.Purple80
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun PhoneAuthScreen(
//    activity: Activity,
//    modifier: Modifier = Modifier,
//    viewModel: AuthViewModel = hiltViewModel()
//) {
//    val authUiState by viewModel.authUiState.collectAsStateWithLifecycle()
//    var mobile by remember { mutableStateOf("") }
//    var otp by remember { mutableStateOf("") }
//
//    if(viewModel.isDialog)
//        CommonDialog()
//
//    Column(
//        modifier = modifier
//            .fillMaxSize()
//            .padding(horizontal = 20.dp)
//    ) {
//        Column(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Text(
//                text = "OTP Screen",
//                fontSize = 20.sp,
//                fontWeight = FontWeight.Bold
//            )
//        }
//        Column(
//            modifier = Modifier
//                .fillMaxSize(),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
//        )
//        {
//            Image(
//                painter = painterResource(id = R.drawable.baseline_phone_android_24),
//                contentDescription = "otp image",
//                modifier = Modifier
//                    .width(100.dp)
//                    .height(100.dp)
//            )
//            Spacer(modifier = Modifier.height(50.dp))
//            OutlinedTextField(
//                value = mobile,
//                onValueChange = { mobile = it },
//                label = { Text(text = "Phone Number") },
//                placeholder = { Text(text = "Phone Number") },
//                leadingIcon = { Icon(Icons.Filled.Phone, contentDescription = "Phone Number") },
//                singleLine = true,
//                keyboardOptions = KeyboardOptions.Default.copy(
//                    keyboardType = KeyboardType.Number,
//                    imeAction = ImeAction.Done
//
//                ),
//            )
//            Spacer(modifier = Modifier.height(20.dp))
//            Button(
//                onClick = {
//                    viewModel.sendOtp(mobile, activity)
//                },
//                modifier = Modifier
//                    .fillMaxWidth(0.8f)
//                    .height(45.dp)
//                    .clip(RoundedCornerShape(10.dp))
//                    .background(Purple80)
//            ) {
//                Text(text = "Send Otp", fontSize = 15.sp, color = Color.White)
//
//            }
//            Spacer(modifier = Modifier.height(40.dp))
//
//            Text(
//                text = "Enter the OTP",
//                fontSize = 20.sp,
//                fontWeight = FontWeight.Bold
//            )
//            Spacer(modifier = Modifier.height(10.dp))
//            OTPTextFields(
//                otp = "",
//                length = 6
//            ) { getOpt ->
//                otp = getOpt
//
//            }
//            Spacer(modifier = Modifier.height(30.dp))
//            Button(
//                onClick = {
//                    viewModel.verifyOtp(otp, activity)
//                }, modifier = Modifier
//                    .fillMaxWidth(0.8f)
//                    .height(45.dp)
//                    .clip(RoundedCornerShape(10.dp))
//                    .background(Purple80),
//                shape = RoundedCornerShape(10.dp)
//            ) {
//                Text(
//                    text = "Otp Verify",
//                    fontSize = 13.sp,
//                    color = Color.White
//                )
//            }
//        }
//    }
//}
//
//
////@Preview(showSystemUi = true)
////@Composable
////fun PhoneAuthScreenPreview() {
////    PhoneAuthScreen(
//////        LocalContext.current as Activity
////    )
////}
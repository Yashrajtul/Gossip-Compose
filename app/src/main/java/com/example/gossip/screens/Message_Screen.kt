package com.example.gossip.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import com.example.gossip.MessageActivity
import com.example.gossip.R

//class MessageActivity : ComponentActivity(){
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            MessageScreen()
//        }
//    }
//    companion object{
//        private const val NAME = "name"
//
//        fun getIntent(context: Context, name: String): Intent{
//            return Intent(context, MessageActivity::class.java).apply {
//                putExtra(NAME, name)
//            }
//        }
//    }
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageScreen(activity: MessageActivity, name: String) {
    Scaffold(
        topBar = { MessageTopAppBar(activity, name) },
        bottomBar = { MessageBottomAppBar1() }
    ) {
        MessageContent(modifier = Modifier.padding(it))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageTopAppBar(activity: MessageActivity, name: String) {
    var showMenu by remember { mutableStateOf(false) }
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_background),
                    contentDescription = "",
                    modifier = Modifier
                        .clip(CircleShape)
                        .height(48.dp)
                )
                Spacer(modifier = Modifier.padding(4.dp))
                Text(text = name)
            }
        },
        navigationIcon = {
            IconButton(onClick = {
                activity.finish()
            }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Go Back"
                )
            }
        },
        actions = {
            IconButton(onClick = { showMenu = !showMenu }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Menu"
                )
            }
            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false },
                properties = PopupProperties(focusable = true)
            ) {

                DropdownMenuItem(text = {
                    Text(text = "Clear Chats")
                }, onClick = {
                    showMenu = false
                })
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun MessageBottomAppBar() {
    var textFieldState by remember { mutableStateOf("") }
    BottomAppBar(
        actions = {
            TextField(
                value = textFieldState,
                onValueChange = {
                    textFieldState = it
                },
                placeholder = { Text(text = "Message") },
                maxLines = 6,
                leadingIcon = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Default.Face, contentDescription = "")
                    }
                },
                shape = CircleShape,
                trailingIcon = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "")
                    }
                },
                modifier = Modifier.fillMaxWidth(.80f)
            )
            Spacer(modifier = Modifier.padding(8.dp))
            FloatingActionButton(
                onClick = { /*TODO*/ },
                shape = CircleShape
            ) {
                Icon(imageVector = Icons.Default.Send, contentDescription = "")
            }
        }
//        floatingActionButton = {
//            FloatingActionButton(onClick = { /*TODO*/ }) {
//                Icon(
//                    imageVector = Icons.Default.Send,
//                    contentDescription = ""
//                )
//            }
//        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun MessageBottomAppBar1() {
    var textFieldState by remember { mutableStateOf("") }
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {

        TextField(
            value = textFieldState,
            onValueChange = {
                textFieldState = it
            },
            placeholder = { Text(text = "Message") },
            maxLines = 6,
            leadingIcon = {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Default.Face, contentDescription = "")
                }
            },
            shape = CircleShape,
            trailingIcon = {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "")
                }
            },
            modifier = Modifier.fillMaxWidth(.85f)
        )
        FloatingActionButton(
            onClick = { /*TODO*/ },
            shape = CircleShape
        ) {
            Icon(imageVector = Icons.Default.Send, contentDescription = "")
        }
    }
//        floatingActionButton = {
//            FloatingActionButton(onClick = { /*TODO*/ }) {
//                Icon(
//                    imageVector = Icons.Default.Send,
//                    contentDescription = ""
//                )
//            }
//        }
}

@Composable
fun MessageContent(modifier: Modifier) {
    LazyColumn(
        modifier = modifier
    ) {
        items(9) {
            Message(it)
        }
    }
}

@Composable
fun Message(it: Int) {
    val contentAlignment: Alignment = if (it % 2 == 0)
        Alignment.CenterStart
    else
        Alignment.CenterEnd



    Box(
        contentAlignment = contentAlignment,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box {
            Card(
                elevation = CardDefaults.cardElevation(5.dp),
                modifier = Modifier
                    .padding(8.dp)
                    .wrapContentSize()
                    .widthIn(max = (LocalConfiguration.current.screenWidthDp*.8).dp)
            ) {
                Box(
                    contentAlignment = Alignment.BottomEnd,
                    propagateMinConstraints = false
                ) {
                    Text(
                        text = "Hi aastha kaisi ho,Ye kaisa laga, yashwardhan tulsyan hi hjhkjb hjvbj",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(8.dp, 4.dp, 16.dp, 14.dp)
                    )
                    Text(
                        text = "2:40",
                        fontSize = 12.sp,
                        textAlign = TextAlign.End,
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                    )
                }
//            Text(
//                text = "Hi aastha kaisi ho,\nYe kaisa laga",
//                fontSize = 16.sp,
//                modifier = Modifier.padding(horizontal = 8.dp)
//            )
//            Text(
//                text = "2:40",
//                textAlign = TextAlign.End,
//                modifier = Modifier
//                    .padding(horizontal = 8.dp)
//            )
            }

        }


    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewMessage() {
    MessageContent(modifier = Modifier)
}
package com.example.gossip.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.example.gossip.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(context: Context, onClick: (name: String) -> Unit) {
    var showMenu by remember { mutableStateOf(false) }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "GOSSIP")
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
//                navigationIcon = {
//                    IconButton(
//                        onClick = { /*TODO*/ }
//                    ) {
//                        Icon(
//                            imageVector = Icons.Filled.Menu,
//                            contentDescription = "Menu"
//                        )
//                    }
//                },
                actions = {
                    IconButton(onClick = { showMenu = !showMenu }) {
                        Icon(imageVector = Icons.Default.MoreVert, "")
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false },
                        properties = PopupProperties(focusable = true)
                    ) {

                        DropdownMenuItem(text = {
                            Text(text = "GroupChat")
                        }, onClick = {
                            Toast.makeText(context, "GroupChat", Toast.LENGTH_SHORT).show()
                            showMenu = false
                        })
                        DropdownMenuItem(text = {
                            Text(text = "Settings")
                        }, onClick = {
                            Toast.makeText(context, "Settings", Toast.LENGTH_SHORT).show()
                            showMenu = false
                        })
                        DropdownMenuItem(text = {
                            Text(text = "Help")
                        }, onClick = {
                            Toast.makeText(context, "Help", Toast.LENGTH_SHORT).show()
                            showMenu = false
                        })
                        DropdownMenuItem(text = {
                            Text(text = "Help")
                        }, onClick = {
                            Toast.makeText(context, "Help", Toast.LENGTH_SHORT).show()
                            showMenu = false
                        })
                        DropdownMenuItem(text = {
                            Text(text = "Help")
                        }, onClick = {
                            Toast.makeText(context, "Help", Toast.LENGTH_SHORT).show()
                            showMenu = false
                        })
                        DropdownMenuItem(text = {
                            Text(text = "Help")
                        }, onClick = {
                            Toast.makeText(context, "Help", Toast.LENGTH_SHORT).show()
                            showMenu = false
                        })

                    }
                }
            )
        }
    ) {
        MainChats(modifier = Modifier.padding(it), onClick)
    }
}


@Composable
fun MainChats(
    modifier: Modifier,
    onClick: (name: String) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(100) {
            ChatUnit(text = it.toString(), onClick)
        }
    }
}

@Composable
fun ChatUnit(
    text: String,
    onClick: (name: String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .clickable {
                onClick(text)
            }
    ) {
        Spacer(modifier = Modifier.padding(4.dp))
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = "",
            modifier = Modifier
                .clip(CircleShape)
                .fillMaxHeight(.9f)
                .clickable {

                }
        )
        Spacer(modifier = Modifier.padding(4.dp))
        Text(text = text)
    }
}

//@Preview
//@Composable
//fun PreviewChatUnit() {
//    ChatUnit(text = "Yash", onClick = onClick)
//}
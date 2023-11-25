package com.example.gossip.ui.chat.chatAppBar

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@Preview
@Composable
fun ChatAppBarPreview() {
    ChatAppBar(title = "Yash", description = "Hi")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatAppBar(
    modifier: Modifier = Modifier,
    title: String = "Title",
    description: String = "Description",
    pictureUrl: String? = null,
    onUserNameClick: (() -> Unit)? = null,
    onBackArrowClick: (() -> Unit)? = null,
    onUserProfilePictureClick: (() -> Unit)? = null,
    onMoreDropDownBlockUserClick: (() -> Unit)? = null,
) {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    TopAppBar(title = {
        Row(
            modifier = Modifier.height(50.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = CircleShape,
                color = Color.LightGray
            ) {
                if (pictureUrl != null) {
                    Image(
                        painter = rememberAsyncImagePainter(pictureUrl),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxHeight()
                            .aspectRatio(1f)
                            .clickable { onUserProfilePictureClick?.invoke() })
                } else {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxHeight()
                            .aspectRatio(1f)
                            .clickable { onUserProfilePictureClick?.invoke() })
                }
            }
            Column(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .clickable {
                        onUserNameClick?.invoke()
                    },
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (description != "")
                    Text(
                        text = description,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
            }
        }
    },
        modifier = modifier.height(50.dp),
        navigationIcon = {
            IconButton(onClick = { onBackArrowClick?.invoke() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Localized description"
                )
            }
        },
        actions = {
//            IconButton(
//                onClick = {
//                    Toast.makeText(
//                        context,
//                        "Videochat Clicked.\n(Not Available)",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }) {
//                Icon(imageVector = Icons.Filled.VideoCall, contentDescription = null)
//            }
//            IconButton(
//                onClick = {
//                    Toast.makeText(
//                        context,
//                        "Voicechat Clicked.\n(Not Available)",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }) {
//                Icon(imageVector = Icons.Filled.Call, contentDescription = null)
//            }
            IconButton(
                onClick = {
                    expanded = true
                }) {
                Icon(imageVector = Icons.Filled.MoreVert, contentDescription = null)
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }) {
                    DropdownMenuItem(
                        text = {
                            Text(text = "Block User")
                        },
                        onClick = {
                            onMoreDropDownBlockUserClick?.invoke()
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.PriorityHigh,
                                contentDescription = null
                            )
                        }
                    )
                }
            }
        })
}
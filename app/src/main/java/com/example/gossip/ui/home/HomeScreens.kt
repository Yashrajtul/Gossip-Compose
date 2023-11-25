package com.example.gossip.ui.home

import android.net.Uri
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.gossip.R
import com.example.gossip.model.ChatRoomModel
import com.example.gossip.model.UserDataModelResponse
import com.example.gossip.navigation.HomeScreen
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class BottomNavItem(
    val name: String,
    val route: String,
    val icon: ImageVector,
    val badgeCount: Int = 0
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigationBar(
    items: List<BottomNavItem>,
    navController: NavController,
    modifier: Modifier = Modifier,
    onItemClick: (BottomNavItem) -> Unit
) {
    val backStackEntry = navController.currentBackStackEntryAsState()
    BottomAppBar(
        modifier = modifier,
        containerColor = Color.DarkGray,
        tonalElevation = 5.dp
    ) {
        items.forEach { item ->
            val selected = item.route == backStackEntry.value?.destination?.route
            NavigationBarItem(
                selected = selected,
                onClick = { onItemClick(item) },
                colors = NavigationBarItemDefaults.colors(if (selected) Color.Green else Color.Gray),
                icon = {
                    Column(
                        horizontalAlignment = CenterHorizontally
                    ) {
                        if (item.badgeCount > 0) {
                            BadgedBox(
                                content = {
                                    Text(text = item.badgeCount.toString())
                                },
                                badge = {
                                    Icon(
                                        imageVector = item.icon,
                                        contentDescription = item.name
                                    )
                                }
                            )
                        } else {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.name
                            )
                        }
                        if (selected) {
                            Text(
                                text = item.name,
                                textAlign = TextAlign.Center,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun HomeScreen(
    chatUsers: List<UserDataModelResponse>,
    myUserId: String = "",
    settings: () -> Unit,
    search: () -> Unit,
    navigate: (userId: String) -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        HomeScreenAppBar(
            search = search,
            settings = settings
        )
        Spacer(modifier = Modifier.height(3.dp))
        if(chatUsers.isEmpty()){
            Column (
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                Text(text = "No chats available.")
                Text(text = "Start chatting!")
            }
        }
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
//                .padding(top = 50.dp)
                .focusable()
                .pointerInput(Unit) {
                    detectTapGestures(onLongPress = {})
                }
        ) {
            items(chatUsers){user->
                val sdf = remember {
                    SimpleDateFormat("hh:mm a".lowercase(), Locale.ROOT)
                }
                val time = user.userRoom?.lastUpdated?.toDate()!!
                Chat(
                    name = user.user?.username!!,
                    image = user.profilePic,
                    message = if (user.userRoom?.lastMessageSenderId == myUserId) {
                        ("You: " + user.userRoom?.lastMessage) ?: ""
                    }else user.userRoom?.lastMessage?: "",
                    time = sdf.format(time).lowercase(),
                    navigate = { navigate(user.key!!) }
                )
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenAppBar(
    modifier: Modifier = Modifier,
    title: String = "GOSSIP",
    search: () -> Unit,
    settings: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    TopAppBar(
        title = {
            Column(
                modifier = Modifier.height(50.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        modifier = modifier.height(50.dp),
        actions = {
            IconButton(
                onClick = search
            ) {
                Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")
            }
            IconButton(onClick = {
                expanded = true
            }) {
                Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "more")
                DropdownMenu(expanded = expanded,
                    onDismissRequest = { expanded = false }) {
                    DropdownMenuItem(
                        text = {
                            Text(text = "Settings")
                        },
                        onClick = settings,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Settings,
                                contentDescription = "Settings"
                            )
                        }
                    )
                }
            }
        }
    )
}

@Composable
fun Chat(
    name: String,
    image: Uri? = null,
    message: String = "",
    time: String = "",
    navigate: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
    ) {
        Card(
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = 6.dp, vertical = 3.dp)
                .clickable(onClick = navigate)
//                .pointerInput(Unit) {
//                    detectTapGestures { keyboardController?.hide() }
//                }
        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically
            ){
                Spacer(modifier = Modifier.width(4.dp))
                Card(
                    shape = CircleShape
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .size(50.dp)
                            .clickable { },
                        model = image ?: R.drawable.baseline_account_circle_24,
                        contentDescription = "Avatar Image",
                        contentScale = ContentScale.Crop,
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = name
                        )
                        if (message.isNotBlank()) {
                            Text(
                                text = message
                            )
                        }
                    }
                    Column (
                        horizontalAlignment = Alignment.End
                    ){
                        Text(text = time)
                        Text(text = "")

                    }

                }
                Spacer(modifier = Modifier.width(16.dp))

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatPreview() {
    Chat(
        name = "Yash",
        time = "12:12",
        navigate = {}
    )
}

@Preview
@Composable
fun HomeScreenAppBarPreview() {
    HomeScreenAppBar(
        search = {},
        settings = {}
    )
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        chatUsers = listOf(
            UserDataModelResponse(
                user = UserDataModelResponse.User(
                    username = "Yash",
                    phone = "1234567890",
                    userId = "",
                    createdTimestamp = Timestamp.now()
                ),
                userRoom = ChatRoomModel.ChatRoom(
                    lastMessage = "Hi",
                    lastMessageSenderId = "2",
                    lastUpdated = Timestamp.now()
                )
            ),
            UserDataModelResponse(
                user = UserDataModelResponse.User(
                    username = "Aastha",
                    phone = "1234567891",
                    userId = "",
                    createdTimestamp = Timestamp.now()
                ),
                userRoom = ChatRoomModel.ChatRoom(
                    lastMessage = "Hi",
                    lastMessageSenderId = "1",
                    lastUpdated = Timestamp.now()
                )
            ),

        ),
        myUserId = "1",
        search = {},
        settings = {},
        navigate = {}
    )
}
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview1() {
    HomeScreen(
        chatUsers = emptyList(),
        myUserId = "1",
        search = {},
        settings = {},
        navigate = {}
    )
}

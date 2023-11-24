package com.example.gossip.ui.chat

import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.chatwithme.presentation.chat.chatrow.ReceivedMessageRow
import com.example.gossip.domain.model.Message
import com.example.gossip.model.ChatMessageModel
import com.example.gossip.model.MessageRegister
import com.example.gossip.model.MessageRegister1
import com.example.gossip.model.MessageStatus
import com.example.gossip.ui.chat.chatAppBar.ChatAppBar
import com.example.gossip.ui.chat.chatInput.ChatInput
import com.example.gossip.ui.chat.chatRow.SentMessageRow
import com.example.gossip.utils.showMsg
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ChatScreenContent(
    name: String,
    input: TextFieldValue,
    messages: List<MessageRegister>,
    messages1: List<MessageRegister1>,
    keyboardController: SoftwareKeyboardController?,
    getInput: (input: TextFieldValue) -> Unit,
    sendMessage: () -> Unit,
    isChatInputFocus: (Boolean) -> Unit,
    navigateUp: () -> Unit
) {
    val scrollState = rememberLazyListState()
    val context = LocalContext.current

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ){
        ChatAppBar(
            title = name,
            description = "",
            pictureUrl = null,
            onUserNameClick = {
                context.showMsg("User Profile Clicked")
            },
            onBackArrowClick = navigateUp,
            onUserProfilePictureClick = {},
            onMoreDropDownBlockUserClick = {}
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 50.dp)
            .focusable()
            .wrapContentHeight()
            .imePadding()
            .pointerInput(Unit) {
                detectTapGestures { keyboardController?.hide() }
            }
    ) {
//        ChatAppBar(
//            title = name,
//            description = "",
//            pictureUrl = null,
//            onUserNameClick = {
//                context.showMsg("User Profile Clicked")
//            },
//            onBackArrowClick = navigateUp,
//            onUserProfilePictureClick = {},
//            onMoreDropDownBlockUserClick = {}
//        )
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            reverseLayout = true,
            state = scrollState
        ) {
            items(messages) { message ->
                val sdf = remember {
                    SimpleDateFormat("hh:mm a".lowercase(), Locale.ROOT)
                }

                when (message.isMessageFromOpponent) {
                    true -> {
                        ReceivedMessageRow(
                            text = message.chatMessage.messageContent,
                            opponentName = name,
                            messageTime = sdf.format(
                                message.chatMessage.timestamp.toDate().time
                                    ?: ""
                            )
                        )
                    }

                    false -> {
                        SentMessageRow(
                            text = message.chatMessage.messageContent,
                            messageTime =sdf.format(
                                message.chatMessage.timestamp.toDate().time
                                    ?: ""
                            ),
                            messageStatus = MessageStatus.PENDING
                        )
                    }
                }

            }
            items(messages1) { message ->
                val sdf = remember {
                    SimpleDateFormat("hh:mm", Locale.ROOT)
                }

                when (message.isMyMessage) {
                    true -> {
                        SentMessageRow(
                            text = message.chatMessage.messageContent,
                            messageTime = sdf.format(
                                message.chatMessage.time// timestamp.toDate().time ,
                            ),
                            messageStatus = MessageStatus.PENDING
                        )
                    }

                    false -> {
                        ReceivedMessageRow(
                            text = message.chatMessage.messageContent,
                            opponentName = name,
                            messageTime = sdf.format(
                                message.chatMessage.time// timestamp.toDate().time ,
                            )
                        )
                    }

                }

            }
        }
        ChatInput(
            input = input,
            getInput = getInput,
            sendMessage = sendMessage,
            onFocusEvent = { isChatInputFocus(it) }
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Preview(showBackground = true)
@Preview(showBackground = true, device = Devices.PIXEL_C)
@Composable
fun ChatScreenContentPreview() {
    ChatScreenContent(
        name = "Yash",
        input = TextFieldValue(""),
        messages = listOf(
            MessageRegister(
                ChatMessageModel.Message(
                        "", "", "I am fine, how are you doing?", MessageStatus.READ.name, Timestamp.now()
                ),
                isMessageFromOpponent = false
            ),
            MessageRegister(
                ChatMessageModel.Message(
                        "", "", "Hi there, how are you?", MessageStatus.RECEIVED.name, Timestamp.now()
                ),
                isMessageFromOpponent = true
            ),
            MessageRegister(
                ChatMessageModel.Message(
                        "", "", "I am fine, how are you doing?", MessageStatus.READ.name, Timestamp.now()
                ),
                isMessageFromOpponent = false
            ),
            MessageRegister(
                ChatMessageModel.Message(
                        "", "", "Hi there, how are you?", MessageStatus.RECEIVED.name, Timestamp.now()
                ),
                isMessageFromOpponent = true
            ),
            MessageRegister(
                ChatMessageModel.Message(
                        "", "", "I am fine, how are you doing?", MessageStatus.READ.name, Timestamp.now()
                ),
                isMessageFromOpponent = false
            ),
            MessageRegister(
                ChatMessageModel.Message(
                        "", "", "Hi there, how are you?", MessageStatus.RECEIVED.name, Timestamp.now()
                ),
                isMessageFromOpponent = true
            ),
            MessageRegister(
                ChatMessageModel.Message(
                        "", "", "I am fine, how are you doing?", MessageStatus.READ.name, Timestamp.now()
                ),
                isMessageFromOpponent = false
            ),
            MessageRegister(
                ChatMessageModel.Message(
                        "", "", "Hi there, how are you?", MessageStatus.RECEIVED.name, Timestamp.now()
                ),
                isMessageFromOpponent = true
            ),
            MessageRegister(
                ChatMessageModel.Message(
                        "", "", "I am fine, how are you doing?", MessageStatus.READ.name, Timestamp.now()
                ),
                isMessageFromOpponent = false
            ),
            MessageRegister(
                ChatMessageModel.Message(
                        "", "", "Hi there, how are you?", MessageStatus.RECEIVED.name, Timestamp.now()
                ),
                isMessageFromOpponent = true
            ),
            MessageRegister(
                ChatMessageModel.Message(
                        "", "", "I am fine, how are you doing?", MessageStatus.READ.name, Timestamp.now()
                ),
                isMessageFromOpponent = false
            ),
            MessageRegister(
                ChatMessageModel.Message(
                        "", "", "Hi there, how are you?", MessageStatus.RECEIVED.name, Timestamp.now()
                ),
                isMessageFromOpponent = true
            ),
            MessageRegister(
                ChatMessageModel.Message(
                        "", "", "I am fine, how are you doing?", MessageStatus.READ.name, Timestamp.now()
                ),
                isMessageFromOpponent = false
            ),
            MessageRegister(
                ChatMessageModel.Message(
                        "", "", "Hi there, how are you?", MessageStatus.RECEIVED.name, Timestamp.now()
                ),
                isMessageFromOpponent = true
            ),
            MessageRegister(
                ChatMessageModel.Message(
                        "", "", "I am fine, how are you doing?", MessageStatus.READ.name, Timestamp.now()
                ),
                isMessageFromOpponent = false
            ),
            MessageRegister(
                ChatMessageModel.Message(
                        "", "", "Hi there, how are you?", MessageStatus.RECEIVED.name, Timestamp.now()
                ),
                isMessageFromOpponent = true
            ),
            MessageRegister(
                ChatMessageModel.Message(
                        "", "", "I am fine, how are you doing?", MessageStatus.READ.name, Timestamp.now()
                ),
                isMessageFromOpponent = false
            ),
            MessageRegister(
                ChatMessageModel.Message(
                        "", "", "Hi there, how are you?", MessageStatus.RECEIVED.name, Timestamp.now()
                ),
                isMessageFromOpponent = true
            ),
            MessageRegister(
                ChatMessageModel.Message(
                        "", "", "I am fine, how are you doing?", MessageStatus.READ.name, Timestamp.now()
                ),
                isMessageFromOpponent = false
            ),
            MessageRegister(
                ChatMessageModel.Message(
                        "", "", "Hi there, how are you?", MessageStatus.RECEIVED.name, Timestamp.now()
                ),
                isMessageFromOpponent = true
            ),
            MessageRegister(
                ChatMessageModel.Message(
                        "", "", "I am fine, how are you doing?", MessageStatus.READ.name, Timestamp.now()
                ),
                isMessageFromOpponent = false
            ),
            MessageRegister(
                ChatMessageModel.Message(
                        "", "", "Hi there, how are you?", MessageStatus.RECEIVED.name, Timestamp.now()
                ),
                isMessageFromOpponent = true
            ),
            MessageRegister(
                ChatMessageModel.Message(
                        "", "", "I am fine, how are you doing?", MessageStatus.READ.name, Timestamp.now()
                ),
                isMessageFromOpponent = false
            ),
            MessageRegister(
                ChatMessageModel.Message(
                        "", "", "Hi there, how are you?", MessageStatus.RECEIVED.name, Timestamp.now()
                ),
                isMessageFromOpponent = true
            ),
            MessageRegister(
                ChatMessageModel.Message(
                        "", "", "I am fine, how are you doing?", MessageStatus.READ.name, Timestamp.now()
                ),
                isMessageFromOpponent = false
            ),
            MessageRegister(
                ChatMessageModel.Message(
                        "", "", "Hi there, how are you?", MessageStatus.RECEIVED.name, Timestamp.now()
                ),
                isMessageFromOpponent = true
            ),
            MessageRegister(
                ChatMessageModel.Message(
                        "", "", "I am fine, how are you doing?", MessageStatus.READ.name, Timestamp.now()
                ),
                isMessageFromOpponent = false
            ),
            MessageRegister(
                ChatMessageModel.Message(
                        "", "", "Hi there, how are you?", MessageStatus.RECEIVED.name, Timestamp.now()
                ),
                isMessageFromOpponent = true
            ),
            MessageRegister(
                ChatMessageModel.Message(
                        "", "", "I am fine, how are you doing?", MessageStatus.READ.name, Timestamp.now()
                ),
                isMessageFromOpponent = false
            ),
            MessageRegister(
                ChatMessageModel.Message(
                        "", "", "Hi there, how are you?", MessageStatus.RECEIVED.name, Timestamp.now()
                ),
                isMessageFromOpponent = true
            ),
            MessageRegister(
                ChatMessageModel.Message(
                        "", "", "I am fine, how are you doing?", MessageStatus.READ.name, Timestamp.now()
                ),
                isMessageFromOpponent = false
            ),
            MessageRegister(
                ChatMessageModel.Message(
                        "", "", "Hi there, how are you?", MessageStatus.RECEIVED.name, Timestamp.now()
                ),
                isMessageFromOpponent = true
            ),
            MessageRegister(
                ChatMessageModel.Message(
                        "", "", "I am fine, how are you doing?", MessageStatus.READ.name, Timestamp.now()
                ),
                isMessageFromOpponent = false
            ),
            MessageRegister(
                ChatMessageModel.Message(
                        "", "", "Hi there, how are you?", MessageStatus.RECEIVED.name, Timestamp.now()
                ),
                isMessageFromOpponent = true
            ),
            MessageRegister(
                ChatMessageModel.Message(
                        "", "", "I am fine, how are you doing?", MessageStatus.READ.name, Timestamp.now()
                ),
                isMessageFromOpponent = false
            ),
            MessageRegister(
                ChatMessageModel.Message(
                        "", "", "Hi there, how are you?", MessageStatus.RECEIVED.name, Timestamp.now()
                ),
                isMessageFromOpponent = true
            ),


        ),
        messages1 = emptyList(),
        keyboardController = LocalSoftwareKeyboardController.current!!,
        getInput = {},
        sendMessage = {},
        isChatInputFocus = {},
        navigateUp = {}
    )
}
@OptIn(ExperimentalComposeUiApi::class)
@Preview(showBackground = true)
@Preview(showBackground = true, device = Devices.PIXEL_C)
@Composable
fun ChatScreenContentPreview1() {
    ChatScreenContent(
        name = "Yash",
        input = TextFieldValue(""),
        messages = emptyList(),
//            MessageRegister(
//                ChatMessageModel.Message(
//                        "", "", "I am fine, how are you doing?", MessageStatus.READ.name, Timestamp.now()
//                ),
//                isMessageFromOpponent = false
//            ),
//            MessageRegister(
//                ChatMessageModel.Message(
//                        "", "", "Hi there, how are you?", MessageStatus.RECEIVED.name, Timestamp.now()
//                ),
//                isMessageFromOpponent = true
//            ),
//        ),
        messages1 = listOf(
            MessageRegister1(
                chatMessage = Message(
                    "I am fine, how are you doing?",System.currentTimeMillis(),""
                ),
                isMyMessage = true
            ),
            MessageRegister1(
                chatMessage = Message(
                    "Hi there, how are you?",System.currentTimeMillis(),""
                ),
                isMyMessage = false
            )

        ),
        keyboardController = LocalSoftwareKeyboardController.current!!,
        getInput = {},
        sendMessage = {},
        isChatInputFocus = {},
        navigateUp = {}
    )
}
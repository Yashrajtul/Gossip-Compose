package com.example.gossip.ui.chat

import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import com.example.chatwithme.presentation.chat.chatrow.ReceivedMessageRow
import com.example.gossip.model.ChatMessageModel
import com.example.gossip.model.MessageRegister
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
    messages: List<MessageRegister>,
    keyboardController: SoftwareKeyboardController,
    isChatInputFocus: (Boolean) -> Unit
) {
    val scrollState = rememberLazyListState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .focusable()
            .wrapContentHeight()
            .imePadding()
            .pointerInput(Unit) {
                detectTapGestures(onTap = { keyboardController.hide() })
            }
    ) {
        val context = LocalContext.current

        ChatAppBar(
            title = "Yash",
            description = "",
            pictureUrl = null,
            onUserNameClick = {
                context.showMsg("User Profile Clicked")
            },
            onBackArrowClick = {},
            onUserProfilePictureClick = {},
            onMoreDropDownBlockUserClick = {}
        )
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            state = scrollState
        ) {
            items(messages) { message ->
                val sdf = remember {
                    SimpleDateFormat("hh:mm", Locale.ROOT)
                }

                when (message.isMessageFromOpponent) {
                    true -> {
                        ReceivedMessageRow(
                            text = message.chatMessage.message?.text!!,
                            opponentName = "Yash",
                            messageTime = sdf.format(
                                message.chatMessage.message?.timestamp?.toDate()?.time
                                    ?: ""
                            )
                        )
                    }

                    false -> {
                        SentMessageRow(
                            text = message.chatMessage.message?.text!!,
                            messageTime =sdf.format(
                                message.chatMessage.message?.timestamp?.toDate()?.time
                                    ?: ""
                            ),
                            messageStatus = MessageStatus.PENDING
                        )
                    }
                }

            }
        }
        ChatInput(onMessageChange = {},
            onFocusEvent = { isChatInputFocus(it) })
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Preview(showBackground = true)
@Composable
fun ChatScreenContentPreview() {
    ChatScreenContent(
        messages = listOf(
            MessageRegister(
                ChatMessageModel(
                    message = ChatMessageModel.Message(
                        "", "", "Hi there, how are you?", Timestamp.now()
                    ),
                    key = ""
                ),
                isMessageFromOpponent = true
            ),
            MessageRegister(
                ChatMessageModel(
                    message = ChatMessageModel.Message(
                        "", "", "I am fine, how are you doing?", Timestamp.now()
                    ),
                    key = ""
                ),
                isMessageFromOpponent = false
            ),
            MessageRegister(
                ChatMessageModel(
                    message = ChatMessageModel.Message(
                        "", "", "Hi there, how are you?", Timestamp.now()
                    ),
                    key = ""
                ),
                isMessageFromOpponent = true
            ),
            MessageRegister(
                ChatMessageModel(
                    message = ChatMessageModel.Message(
                        "", "", "I am fine, how are you doing?", Timestamp.now()
                    ),
                    key = ""
                ),
                isMessageFromOpponent = false
            ),
            MessageRegister(
                ChatMessageModel(
                    message = ChatMessageModel.Message(
                        "", "", "Hi there, how are you?", Timestamp.now()
                    ),
                    key = ""
                ),
                isMessageFromOpponent = true
            ),
            MessageRegister(
                ChatMessageModel(
                    message = ChatMessageModel.Message(
                        "", "", "I am fine, how are you doing?", Timestamp.now()
                    ),
                    key = ""
                ),
                isMessageFromOpponent = false
            ),
            MessageRegister(
                ChatMessageModel(
                    message = ChatMessageModel.Message(
                        "", "", "Hi there, how are you?", Timestamp.now()
                    ),
                    key = ""
                ),
                isMessageFromOpponent = true
            ),
            MessageRegister(
                ChatMessageModel(
                    message = ChatMessageModel.Message(
                        "", "", "I am fine, how are you doing?", Timestamp.now()
                    ),
                    key = ""
                ),
                isMessageFromOpponent = false
            ),
            MessageRegister(
                ChatMessageModel(
                    message = ChatMessageModel.Message(
                        "", "", "Hi there, how are you?", Timestamp.now()
                    ),
                    key = ""
                ),
                isMessageFromOpponent = true
            ),
            MessageRegister(
                ChatMessageModel(
                    message = ChatMessageModel.Message(
                        "", "", "I am fine, how are you doing?", Timestamp.now()
                    ),
                    key = ""
                ),
                isMessageFromOpponent = false
            ),
            MessageRegister(
                ChatMessageModel(
                    message = ChatMessageModel.Message(
                        "", "", "Hi there, how are you?", Timestamp.now()
                    ),
                    key = ""
                ),
                isMessageFromOpponent = true
            ),
            MessageRegister(
                ChatMessageModel(
                    message = ChatMessageModel.Message(
                        "", "", "I am fine, how are you doing?", Timestamp.now()
                    ),
                    key = ""
                ),
                isMessageFromOpponent = false
            ),
            MessageRegister(
                ChatMessageModel(
                    message = ChatMessageModel.Message(
                        "", "", "Hi there, how are you?", Timestamp.now()
                    ),
                    key = ""
                ),
                isMessageFromOpponent = true
            ),
            MessageRegister(
                ChatMessageModel(
                    message = ChatMessageModel.Message(
                        "", "", "I am fine, how are you doing?", Timestamp.now()
                    ),
                    key = ""
                ),
                isMessageFromOpponent = false
            ),
            MessageRegister(
                ChatMessageModel(
                    message = ChatMessageModel.Message(
                        "", "", "Hi there, how are you?", Timestamp.now()
                    ),
                    key = ""
                ),
                isMessageFromOpponent = true
            ),
            MessageRegister(
                ChatMessageModel(
                    message = ChatMessageModel.Message(
                        "", "", "I am fine, how are you doing?", Timestamp.now()
                    ),
                    key = ""
                ),
                isMessageFromOpponent = false
            ),
            MessageRegister(
                ChatMessageModel(
                    message = ChatMessageModel.Message(
                        "", "", "Hi there, how are you?", Timestamp.now()
                    ),
                    key = ""
                ),
                isMessageFromOpponent = true
            ),
            MessageRegister(
                ChatMessageModel(
                    message = ChatMessageModel.Message(
                        "", "", "I am fine, how are you doing?", Timestamp.now()
                    ),
                    key = ""
                ),
                isMessageFromOpponent = false
            ),
            MessageRegister(
                ChatMessageModel(
                    message = ChatMessageModel.Message(
                        "", "", "Hi there, how are you?", Timestamp.now()
                    ),
                    key = ""
                ),
                isMessageFromOpponent = true
            ),
            MessageRegister(
                ChatMessageModel(
                    message = ChatMessageModel.Message(
                        "", "", "I am fine, how are you doing?", Timestamp.now()
                    ),
                    key = ""
                ),
                isMessageFromOpponent = false
            ),
            MessageRegister(
                ChatMessageModel(
                    message = ChatMessageModel.Message(
                        "", "", "Hi there, how are you?", Timestamp.now()
                    ),
                    key = ""
                ),
                isMessageFromOpponent = true
            ),
            MessageRegister(
                ChatMessageModel(
                    message = ChatMessageModel.Message(
                        "", "", "I am fine, how are you doing?", Timestamp.now()
                    ),
                    key = ""
                ),
                isMessageFromOpponent = false
            ),

        ),
        keyboardController = LocalSoftwareKeyboardController.current!!,
        isChatInputFocus = {}
    )
}
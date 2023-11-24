package com.example.gossip.model

import com.example.gossip.domain.model.Message

data class MessageRegister(
    var chatMessage: ChatMessageModel.Message,
    var isMessageFromOpponent: Boolean
)
data class MessageRegister1(
    var chatMessage: Message,
    var isMyMessage: Boolean
)
package com.example.gossip.model

data class MessageRegister(
    var chatMessage: ChatMessageModel,
    var isMessageFromOpponent: Boolean
)
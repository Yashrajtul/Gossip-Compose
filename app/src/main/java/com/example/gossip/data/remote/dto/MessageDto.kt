package com.example.gossip.data.remote.dto

import com.example.gossip.domain.model.Message
import kotlinx.serialization.Serializable
import java.sql.Time
import java.text.DateFormat

@Serializable
data class MessageDto(
    val messageContent: String,
    val timestamp: Long,
    val userId: String,
    val roomId: String,
    val id: String
){
    fun toMessage() : Message{
//        val date = Date(timestamp)
//        val formattedDate =  DateFormat.getDateInstance(DateFormat.DEFAULT)
//            .format(date)
//        val time = Time(timestamp)
//        val formattedTime = DateFormat.getTimeInstance(DateFormat.DEFAULT)
//            .format(time)
        return Message(
            messageContent = messageContent,
            time = timestamp,
            userId = userId
        )
    }
}

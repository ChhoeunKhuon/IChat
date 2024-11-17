package com.num.ichat.model

data class MessageModel(
    val senderId: String? = "",
    val message: String? = "",
    val timeStamp: Long? = 0
)

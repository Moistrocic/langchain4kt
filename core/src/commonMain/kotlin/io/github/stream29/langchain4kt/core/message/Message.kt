package io.github.stream29.langchain4kt.core.message

data class Message(
    val sender: MessageSender,
    val content: String
)
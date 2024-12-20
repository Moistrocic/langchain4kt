package io.github.stream29.langchain4kt.core

import io.github.stream29.langchain4kt.core.message.MessageSender

interface Respondent {
    suspend fun chat(message: String): String
}

fun ChatApiProvider<*>.asRespondent(systemInstruction: String? = null) =
    SimpleRespondent(this, systemInstruction)

fun Respondent.wrap(wrapper: (String) -> String) =
    WrappedRespondent(this, wrapper)

data class SimpleRespondent(
    val apiProvider: ChatApiProvider<*>,
    val systemInstruction: String? = null
) : Respondent {
    override suspend fun chat(message: String): String {
        return apiProvider.generateFrom {
            systemInstruction(systemInstruction)
            MessageSender.User.chat(message)
        }.message
    }
}

data class WrappedRespondent(
    val baseRespondent: Respondent,
    val wrapper: (String) -> String
) : Respondent {
    override suspend fun chat(message: String): String {
        return baseRespondent.chat(wrapper(message))
    }
}

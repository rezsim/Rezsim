package com.project.rezsim.ui.view.message

data class MessageParameter(
    val title: String? = null,
    val message: String,
    val messageType: MessageType,
    val severity: MessageSeverity,
    val runnable: Runnable? = null
)

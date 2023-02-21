package com.project.rezsim.ui.view.message

import android.view.View

data class MessageParameter(
    val title: String? = null,
    val message: String,
    val messageType: MessageType,
    val severity: MessageSeverity,
    val rootView: View? = null,
    val runnable: Runnable? = null
)

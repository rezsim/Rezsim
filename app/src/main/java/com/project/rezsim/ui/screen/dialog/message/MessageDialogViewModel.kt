package com.project.rezsim.ui.screen.dialog.message

import android.os.Bundle
import com.project.rezsim.R
import com.project.rezsim.base.RezsimViewModel
import com.project.rezsim.device.StringRepository
import com.project.rezsim.ui.view.message.MessageType
import org.koin.core.component.inject

class MessageDialogViewModel : RezsimViewModel() {

    var title: String? = null
    var message: String = ""
    var type: MessageType = MessageType.DIALOG_OK

    private val stringRepository: StringRepository by inject()

    fun parseArguments(arg: Bundle?) {
        arg?.let {
            title = it.getString(MessageDialogFragment.ARG_TITLE)
            message = it.getString(MessageDialogFragment.ARG_MESSAGE) ?: ""
            it.getString(MessageDialogFragment.ARG_TYPE)?.let {
                MessageType.valueOf(it)
            }?.also { type = it }
        }
    }

    fun isPositiveButtonVisible() = type != MessageType.DIALOG_OK

    fun negativeButtonText() = stringRepository.getById(if (type == MessageType.DIALOG_OK) R.string.ok else R.string.cancel)

    fun positiveButtonText() = when (type) {
            MessageType.DIALOG_OK_CANCEL -> stringRepository.getById(R.string.cancel)
            MessageType.DIALOG_YES_CANCEL -> stringRepository.getById(R.string.yes)
            else -> ""
        }

}
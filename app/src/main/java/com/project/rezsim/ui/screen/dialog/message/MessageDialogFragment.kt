package com.project.rezsim.ui.screen.dialog.message

import android.os.Bundle
import com.project.rezsim.R
import com.project.rezsim.base.RezsimDialogFragment
import org.koin.core.component.inject

class MessageDialogFragment : RezsimDialogFragment() {

    override val contentId = R.layout.message_dialog

    private val viewModel: MessageDialogViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.parseArguments(arguments)
    }





    companion object {
        const val TAG = "MessageDialogFragment"
        const val ARG_TITLE = "ArgTitle"
        const val ARG_MESSAGE = "ArgMessage"
        const val ARG_TYPE = "ArgType"

        fun newInstance(argument: Bundle?) = MessageDialogFragment().apply {
            this.arguments = arguments
        }
    }

}
package com.project.rezsim.ui.screen.dialog.message

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
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

    override fun setupViews() {
        super.setupViews()
        view?.findViewById<ConstraintLayout>(R.id.cContainer)?.let {
            it.findViewById<AppCompatTextView>(R.id.tvTitle).text = viewModel.title
            it.findViewById<AppCompatTextView>(R.id.tvMessage).text = viewModel.message
            it.findViewById<AppCompatButton>(R.id.btNegative).apply {
                text = viewModel.negativeButtonText()
                setOnClickListener { onButtonClick(it.id) }
            }
            it.findViewById<AppCompatButton>(R.id.btPositive).apply {
                text = viewModel.positiveButtonText()
                visibility = if (viewModel.isPositiveButtonVisible()) View.VISIBLE else View.GONE
                setOnClickListener { onButtonClick(it.id) }
            }
        }
    }

    private fun onButtonClick(id: Int) {
        if (id == R.id.btPositive) {
            resultLiveData?.value = true
        }
        dismiss()
    }



    companion object {
        const val TAG = "MessageDialogFragment"
        const val ARG_TITLE = "ArgTitle"
        const val ARG_MESSAGE = "ArgMessage"
        const val ARG_TYPE = "ArgType"

        fun newInstance(argument: Bundle?) = MessageDialogFragment().apply {
            this.arguments = argument
        }
    }

}
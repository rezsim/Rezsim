package com.project.rezsim.ui.screen.dialog.user

import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.project.rezsim.R
import com.project.rezsim.base.RezsimDialogFragment
import org.koin.android.ext.android.inject

class UserDialogFragment : RezsimDialogFragment() {

    override val contentId = R.layout.user_dialog

    private val viewModel: UserDialogViewModel by inject()

    override fun setupViews() {
        super.setupViews()
        view?.let {
            it.findViewById<AppCompatTextView>(R.id.tvUserData).text = viewModel.user()
        }
    }


    companion object {
        const val TAG = "UserDialogFragment"
        fun newInstance() = UserDialogFragment()
    }
}
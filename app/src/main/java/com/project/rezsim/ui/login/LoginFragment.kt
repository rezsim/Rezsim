package com.project.rezsim.ui.login

import com.project.rezsim.R
import com.project.rezsim.base.RezsimFragment

class LoginFragment : RezsimFragment() {

    override val contentId = R.layout.login_fragment

    companion object {
        const val TAG = "LoginFragment"
        fun newInstance() = LoginFragment()
    }


}
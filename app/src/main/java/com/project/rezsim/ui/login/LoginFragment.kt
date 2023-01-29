package com.project.rezsim.ui.login

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import com.project.rezsim.R
import com.project.rezsim.base.RezsimFragment
import org.koin.android.ext.android.inject

class LoginFragment : RezsimFragment() {

    override val contentId = R.layout.login_fragment

    private val viewModel: LoginViewModel by inject()

    private lateinit var editEmail: AppCompatEditText
    private lateinit var editPassword: AppCompatEditText
    private lateinit var buttonLogin: AppCompatButton
/*
    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(p0: Editable?) {

        }
    }
*/
    override fun setupViews() {
        super.setupViews()
        view?.let {
            editEmail = it.findViewById<AppCompatEditText?>(R.id.etEmail).apply {
                doAfterTextChanged { inputChanged() }
            }
            editPassword = it.findViewById<AppCompatEditText?>(R.id.etPassword).apply {
                doAfterTextChanged { inputChanged() }
            }
            buttonLogin = it.findViewById(R.id.btLogin)
        }
        viewModel.start()
    }

    override fun subscribeObservers() {
        super.subscribeObservers()
        viewModel.emailLiveData.observe(this) { editEmail.setText(it) }
        viewModel.passwordLiveData.observe(this) { editPassword.setText(it) }
        viewModel.loginButtonEnabledLiveData.observe(this) { setLoginButtonState(it) }
    }

    private fun inputChanged() {
        viewModel.inputChanged(editEmail.text.toString(), editPassword.text.toString())
    }

    private fun setLoginButtonState(enabled: Boolean) {
        buttonLogin.apply {
            isEnabled = enabled
            backgroundTintList = if (enabled) resources.getColorStateList(R.color.material_orange_8) else null
        }

    }


    companion object {
        const val TAG = "LoginFragment"
        fun newInstance() = LoginFragment()
    }


}
package com.project.rezsim.ui.screen.login

import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import com.project.rezsim.R
import com.project.rezsim.base.RezsimFragment
import com.project.rezsim.device.ColorRepository
import org.koin.android.ext.android.inject

class LoginFragment : RezsimFragment() {

    override val contentId = R.layout.login_fragment

    private val viewModel: LoginViewModel by inject()
    private val colorRepository: ColorRepository by inject()

    private lateinit var textTitle: AppCompatTextView
    private lateinit var textDescription: AppCompatTextView
    private lateinit var editEmail: AppCompatEditText
    private lateinit var editPassword: AppCompatEditText
    private lateinit var textPasswordAgain: AppCompatTextView
    private lateinit var editPasswordAgain: AppCompatEditText
    private lateinit var buttonLogin: AppCompatButton
    private lateinit var buttonRegistration: AppCompatButton
    private lateinit var textModeDescription: AppCompatTextView
    private lateinit var buttonMode: AppCompatButton

    override fun setupViews() {
        super.setupViews()
        view?.let {
            textTitle = it.findViewById(R.id.tvTitle)
            textDescription = it.findViewById(R.id.tvDescription)
            editEmail = it.findViewById<AppCompatEditText?>(R.id.etEmail).apply {
                doAfterTextChanged { inputChanged() }
            }
            textPasswordAgain = it.findViewById(R.id.tvPasswordAgainTitle)
            editPassword = it.findViewById<AppCompatEditText?>(R.id.etPassword).apply {
                doAfterTextChanged { inputChanged() }
            }
            editPasswordAgain = it.findViewById<AppCompatEditText?>(R.id.etPasswordAgain).apply {
                doAfterTextChanged { inputChanged() }
            }
            buttonLogin = it.findViewById<AppCompatButton?>(R.id.btLogin).apply {
                setOnClickListener { loginButtonClicked() }
            }
            textModeDescription = it.findViewById(R.id.tvMode)
            buttonMode = it.findViewById<AppCompatButton?>(R.id.btMode).apply {
                setOnClickListener { modeButtonClicked() }
            }
        }
        viewModel.start()
    }

    override fun subscribeObservers() {
        super.subscribeObservers()
        viewModel.emailLiveData.observe(this) { editEmail.setText(it) }
        viewModel.passwordLiveData.observe(this) { editPassword.setText(it) }
        viewModel.passwordAgainLiveData.observe(this) { editPasswordAgain.setText(it)}
        viewModel.loginButtonEnabledLiveData.observe(this) { setLoginButtonState(it) }
        viewModel.inputEnabledLiveData.observe(this) { setInputState(it) }
        viewModel.modeLiveData.observeForever { setViewsAsMode(it) }
    }

    private fun inputChanged() {
        viewModel.inputChanged(editEmail.text.toString(), editPassword.text.toString(), editPasswordAgain.text.toString())
    }

    private fun setLoginButtonState(enabled: Boolean) {
        buttonLogin.apply {
            isEnabled = enabled
            backgroundTintList = if (enabled) colorRepository.stateList(R.color.material_orange_8) else null
        }
    }

    private fun setInputState(enabled: Boolean) {
        editEmail.isEnabled = enabled
        editPassword.isEnabled = enabled
        editPasswordAgain.isEnabled = enabled
        buttonMode.isEnabled = enabled
    }

    private fun loginButtonClicked() {
        viewModel.loginButtonClicked(editEmail.text.toString(), editPassword.text.toString(), editPasswordAgain.text.toString())
    }

    private fun modeButtonClicked() {
        viewModel.changeMode()
    }

    private fun setViewsAsMode(mode: String) {
        val registration = mode == REGISTRATION_MODE
        textTitle.setText(if (registration) R.string.login_registration_title else R.string.login_title)
        textDescription.setText(if (registration) R.string.login_registration_description else R.string.login_description)
        textPasswordAgain.visibility = if (registration) View.VISIBLE else View.GONE
        editPasswordAgain.visibility = if (registration) View.VISIBLE else View.GONE
        buttonLogin.setText(if (registration) R.string.login_button_registration else R.string.login_button)
        textModeDescription.setText(if (registration) R.string.login_mode_registration_description else R.string.login_mode_login_description)
        buttonMode.setText(if (registration) R.string.login_mode_registration_button else R.string.login_mode_login_button)
    }

    companion object {
        const val TAG = "LoginFragment"
        const val LOGIN_MODE = "login"
        const val REGISTRATION_MODE = "registration"
        fun newInstance() = LoginFragment()
    }


}
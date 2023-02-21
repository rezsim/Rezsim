package com.project.rezsim.ui.screen.dialog.meter

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import com.project.rezsim.R
import com.project.rezsim.base.RezsimDialogFragment
import com.project.rezsim.device.DrawableRepository
import com.project.rezsim.device.StringRepository
import com.project.rezsim.tool.DateHelper
import com.project.rezsim.ui.screen.activity.MainActivityViewModel
import org.koin.core.component.inject

class MeterDialogFragment : RezsimDialogFragment() {

    override val contentId = R.layout.meter_dialog

    private val viewModel: MeterDialogViewModel by inject()
    private val activityViewModel: MainActivityViewModel by inject()
    private val drawableRepository: DrawableRepository by inject()
    private val stringRepository: StringRepository by inject()

    private lateinit var rootView: View
    private lateinit var editValue: AppCompatEditText
    private lateinit var buttonSave: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.parseArguments(arguments)
    }

    override fun setupDialog() {
        super.setupDialog()
        requireDialog().setCanceledOnTouchOutside(false)
    }

    override fun setupViews() {
        super.setupViews()
        view?.let {
            rootView = it.findViewById(R.id.clRoot)
            it.findViewById<AppCompatImageView>(R.id.ivIcon).setImageDrawable(drawableRepository.getById(viewModel.iconRes()))
            it.findViewById<AppCompatTextView>(R.id.tvMessage).text = viewModel.message()
            it.findViewById<AppCompatEditText>(R.id.etDate).setText(DateHelper.calendarToDisplayDateString(viewModel.date))
            it.findViewById<AppCompatTextView>(R.id.tvLastValue).apply {
                viewModel.lastValue().let {
                    it?.let {
                        text = stringRepository.getById(R.string.dialog_meter_read_last_value, viewModel.lastValue().toString())
                    }
                    visibility = if (it != null) View.VISIBLE else View.GONE
                }
            }
            it.findViewById<AppCompatButton>(R.id.btNegative).setOnClickListener { dismiss() }
            buttonSave = it.findViewById<AppCompatButton>(R.id.btPositive).apply {
                setOnClickListener { save() }
            }
            editValue = it.findViewById<AppCompatEditText>(R.id.etValue).apply {
                doAfterTextChanged { s ->
                    buttonSave.isEnabled = !s.toString().isEmpty()
                }
            }
        }
    }

    override fun subscribeObservers() {
        super.subscribeObservers()
        viewModel.progressLiveData.observe(this) { setProgress(it) }
    }

    fun save() {
        activityViewModel.hideKeyboard(editValue.windowToken)
        viewModel.save(editValue.text.toString().toInt(), rootView)
    }

    companion object {
        const val TAG = "MeterDialogFragment"
        const val ARG_UTILITY = "ArgUtility"
        const val ARG_HOUSEHOLD = "ArgHousehold"

        fun newInstance(argument: Bundle?) = MeterDialogFragment().apply {
            this.arguments = argument
        }
    }

}
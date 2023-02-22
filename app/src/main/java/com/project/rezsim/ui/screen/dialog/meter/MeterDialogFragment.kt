package com.project.rezsim.ui.screen.dialog.meter

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.ImageViewCompat
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import com.project.rezsim.R
import com.project.rezsim.base.RezsimDialogFragment
import com.project.rezsim.device.DrawableRepository
import com.project.rezsim.device.StringRepository
import com.project.rezsim.tool.DateHelper
import com.project.rezsim.ui.screen.activity.MainActivityViewModel
import org.koin.core.component.inject
import java.util.*

class MeterDialogFragment : RezsimDialogFragment() {

    override val contentId = R.layout.meter_dialog

    private val viewModel: MeterDialogViewModel by inject()
    private val drawableRepository: DrawableRepository by inject()
    private val stringRepository: StringRepository by inject()

    private lateinit var rootView: View
    private lateinit var editValue: AppCompatEditText
    private lateinit var editDate: AppCompatEditText
    private lateinit var editComment: AppCompatEditText
    private lateinit var buttonSave: AppCompatButton

    private val datePickerListener =
        DatePickerDialog.OnDateSetListener { picker, year, month, day ->
            val calendar = Calendar.getInstance(Locale.getDefault()).apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month)
                set(Calendar.DAY_OF_MONTH, day)
            }
            editDate.setText(DateHelper.calendarToDisplayDateString(calendar))
        }

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
        view?.findViewById<ConstraintLayout>(R.id.cContainer)?.let {
            rootView = it.findViewById(R.id.clRoot)
            it.findViewById<AppCompatImageView>(R.id.ivIcon).setImageDrawable(drawableRepository.getById(viewModel.iconRes()))
            it.findViewById<AppCompatTextView>(R.id.tvMessage).text = viewModel.message()
            editDate = it.findViewById<AppCompatEditText>(R.id.etDate).apply {
                setText(DateHelper.calendarToDisplayDateString(viewModel.date))
            }
//            it.findViewById<View>(R.id.vDateClickProvider).setOnClickListener { selectDate(editDate.text.toString()) }
            it.findViewById<AppCompatTextView>(R.id.tvLastValue).apply {
                viewModel.lastValue().let {
                    it?.let {
                        text = stringRepository.getById(R.string.dialog_meter_read_last_value, viewModel.lastValue().toString())
                    }
                    visibility = if (it != null) View.VISIBLE else View.GONE
                }
            }
            editComment = it.findViewById(R.id.etComment)
            it.findViewById<AppCompatImageView>(R.id.ivComment).setOnClickListener { editComment.visibility = View.VISIBLE }
            it.findViewById<AppCompatButton>(R.id.btNegative).setOnClickListener { dismiss() }
            buttonSave = it.findViewById<AppCompatButton>(R.id.btPositive).apply {
                setOnClickListener { save() }
            }
            editValue = it.findViewById<AppCompatEditText>(R.id.etValue).apply {
                doAfterTextChanged { s ->
                    buttonSave.isEnabled = !s.toString().isEmpty()
                }
                requestFocus()
                activityViewModel.showKeyboard(requireActivity())
            }
        }
    }

    override fun subscribeObservers() {
        super.subscribeObservers()
        viewModel.progressLiveData.observe(this) { setProgress(it) }
        viewModel.finishLiveData.observe(this) { dismiss() }
    }

    private fun save() {
        editValue.clearFocus()
        activityViewModel.hideKeyboard(editValue.windowToken)
        val value = editValue.text.toString().toInt()
        val comment = editComment.text.toString().let {
            if (it.isNotBlank()) it else null
        }
        viewModel.save(value, comment, rootView)
    }

    private fun selectDate(currentDateString: String) {
        val calendar = DateHelper.displayDateStringToCalendar(currentDateString)
        DatePickerDialog(
            requireContext(),
            datePickerListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
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
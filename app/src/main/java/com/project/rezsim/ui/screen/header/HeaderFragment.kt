package com.project.rezsim.ui.screen.header

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.project.rezsim.R
import com.project.rezsim.base.RezsimFragment
import org.koin.android.ext.android.inject

class HeaderFragment : RezsimFragment() {

    override val contentId = R.layout.header_fragment

    private val viewModel: HeaderViewModel by inject()

    private lateinit var imageBack: AppCompatImageView
    private lateinit var imageUser: AppCompatImageView
    private lateinit var imageCalendar: AppCompatImageView
    private lateinit var textTitle: AppCompatTextView

    private val buttonIds = intArrayOf(R.id.ivCalendar, R.id.ivUser)

    override fun setupViews() {
        super.setupViews()
        view?.let {
            textTitle = it.findViewById(R.id.tvTitle)
            imageBack = it.findViewById<AppCompatImageView>(R.id.ivBack).apply {
                setOnClickListener { viewModel.onBackPressed() }
            }
            imageUser = it.findViewById<AppCompatImageView>(R.id.ivUser).apply {
                setOnClickListener { viewModel.onButtonPressed(R.id.ivUser) }
            }
            imageCalendar = it.findViewById<AppCompatImageView>(R.id.ivCalendar).apply {
                setOnClickListener { viewModel.onButtonPressed(R.id.ivCalendar) }
            }
        }
    }

    override fun subscribeObservers() {
        super.subscribeObservers()
        viewModel.titleLiveData.observe(this) { textTitle.text = it }
        viewModel.buttonsLiveData.observe(this) { setButtonsVisibility(it) }
    }

    private fun setButtonsVisibility(buttons: IntArray) {
        buttonIds.forEach {
            view?.findViewById<AppCompatImageView>(it)?.visibility = if (buttons.contains(it)) View.VISIBLE else View.GONE
        }
    }



    companion object {
        const val TAG = "HeaderFragment"
        fun newInstance() = HeaderFragment()
    }
}
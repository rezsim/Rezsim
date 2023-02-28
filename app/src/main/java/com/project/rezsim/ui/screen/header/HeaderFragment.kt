package com.project.rezsim.ui.screen.header

import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.project.rezsim.R
import com.project.rezsim.base.RezsimFragment
import com.project.rezsim.device.ColorRepository
import com.project.rezsim.device.dp
import org.koin.android.ext.android.inject

class HeaderFragment : RezsimFragment() {

    override val contentId = R.layout.header_fragment

    private val viewModel: HeaderViewModel by inject()
    private val colorRepository: ColorRepository by inject()

    private lateinit var imageBack: AppCompatImageView
    private lateinit var imageUser: AppCompatImageView
    private lateinit var textTitle: AppCompatTextView

    override fun setupViews() {
        super.setupViews()
        view?.let {
            textTitle = it.findViewById(R.id.tvTitle)
            imageBack = it.findViewById<AppCompatImageView>(R.id.ivBack).apply {
                setOnClickListener { viewModel.onBackPressed() }
            }
        }
    }

    override fun subscribeObservers() {
        super.subscribeObservers()
        viewModel.titleLiveData.observe(this) { textTitle.text = it }
        viewModel.toolButtonsLiveData.observe(this) { setToolButtons(it) }
        viewModel.buttonColorLiveData.observe(this) { setToolButtonColor(it.first, it.second) }
    }

    private fun setToolButtons(buttons: IntArray) {
        view?.let {
            it.findViewById<LinearLayout>(R.id.llTools).apply {
                removeAllViews()
                buttons.forEach {
                    addView(createToolButton(it))
                }
            }
        }
    }

    private fun createToolButton(iconResId: Int) = AppCompatImageView(requireContext()).apply {
        id = iconResId
        setImageResource(iconResId)
        imageTintList = colorRepository.stateList(R.color.material_grey_8)
        layoutParams = LinearLayout.LayoutParams(resources.getDimension(R.dimen.icon_size_header).toInt(), resources.getDimension(R.dimen.icon_size_header).toInt()).apply {
            marginEnd = 8.dp
            marginStart = 8.dp
        }
        scaleType = ImageView.ScaleType.FIT_CENTER
        setOnClickListener { viewModel.onToolButtonPressed(id) }
    }

    private fun setToolButtonColor(buttonResId: Int, colorResId: Int) {
        view?.findViewById<AppCompatImageView>(buttonResId)?.imageTintList = colorRepository.stateList(colorResId)
    }



    companion object {
        const val TAG = "HeaderFragment"
        fun newInstance() = HeaderFragment()
    }
}
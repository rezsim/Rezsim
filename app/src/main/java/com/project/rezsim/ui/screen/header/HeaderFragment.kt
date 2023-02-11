package com.project.rezsim.ui.screen.header

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
    private lateinit var textTitle: AppCompatTextView

    override fun setupViews() {
        super.setupViews()
        view?.let {
            imageBack = it.findViewById<AppCompatImageView>(R.id.ivBack).apply {
                setOnClickListener { viewModel.onBackPressed() }
            }
            imageUser = it.findViewById<AppCompatImageView>(R.id.ivUser).apply {
                setOnClickListener { viewModel.onUserPressed() }
            }
        }
    }



    companion object {
        const val TAG = "HeaderFragment"
        fun newInstance() = HeaderFragment()
    }
}
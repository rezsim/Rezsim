package com.project.rezsim.ui.screen.footer

import androidx.appcompat.widget.AppCompatImageView
import com.project.rezsim.R
import com.project.rezsim.base.RezsimFragment
import com.project.rezsim.device.ColorRepository
import org.koin.android.ext.android.inject

class FooterFragment : RezsimFragment() {

    override val contentId = R.layout.footer_fragment

    private val viewModel: FooterViewModel by inject()
    private val colorRepository: ColorRepository by inject()

    private lateinit var imageHome: AppCompatImageView
    private lateinit var imageElectricity: AppCompatImageView
    private lateinit var imageGas: AppCompatImageView

    private val colorNormal = colorRepository.stateList(R.color.material_grey_5)
    private val colorSelected = colorRepository.stateList(R.color.material_indigo_5)

    override fun setupViews() {
        super.setupViews()
        view?.let {
            imageHome = it.findViewById<AppCompatImageView>(R.id.ivHome).apply {
                setOnClickListener { viewModel.onButtonPressed(FooterButton.HOME) }
            }
            imageElectricity = it.findViewById<AppCompatImageView>(R.id.ivElectricity).apply {
                setOnClickListener { viewModel.onButtonPressed(FooterButton.ELECTRICITY) }
            }
            imageGas = it.findViewById<AppCompatImageView>(R.id.ivGas).apply {
                setOnClickListener { viewModel.onButtonPressed(FooterButton.GAS) }
            }
        }
    }

    override fun subscribeObservers() {
        super.subscribeObservers()
        viewModel.buttonSelectedLiveData.observe(this) { selectButton(it) }
    }

    private fun selectButton(button: FooterButton) {
        imageHome.imageTintList = if (button == FooterButton.HOME) colorSelected else colorNormal
        imageElectricity.imageTintList = if (button == FooterButton.ELECTRICITY) colorSelected else colorNormal
        imageGas.imageTintList = if (button == FooterButton.GAS) colorSelected else colorNormal
    }

    companion object {
        const val TAG = "FooterFragment"
        fun newInstance() = FooterFragment()
    }
}
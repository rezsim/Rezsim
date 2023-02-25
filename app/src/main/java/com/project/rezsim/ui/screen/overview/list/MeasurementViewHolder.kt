package com.project.rezsim.ui.screen.overview.list

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PorterDuff
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.project.rezsim.R
import com.project.rezsim.device.DrawableRepository
import com.project.rezsim.device.StringRepository
import com.project.rezsim.server.dto.measurement.Level
import com.project.rezsim.server.dto.measurement.Measurement
import com.project.rezsim.server.dto.measurement.Utility
import com.project.rezsim.tool.DateHelper
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MeasurementViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), KoinComponent {

    private val drawableRepository: DrawableRepository by inject()
    private val stringRepository: StringRepository by inject()

    private val iconLayout: FrameLayout = itemView.findViewById(R.id.flIcon)
    private val iconImageView: AppCompatImageView = itemView.findViewById(R.id.ivIcon)
    private val consumptionTextView: AppCompatTextView = itemView.findViewById(R.id.tvConsumption)
    private val commentTextView: AppCompatTextView = itemView.findViewById(R.id.tvComment)
    private val meterTextView: AppCompatTextView = itemView.findViewById(R.id.tvMeter)
    private val dateTextView: AppCompatTextView = itemView.findViewById(R.id.tvDate)

    @SuppressLint("ResourceAsColor")
    fun bindView(measurement: Measurement) {
        val limitColor = if (measurement.level == Level.UNDERLIMIT.value) R.color.normal_comsumption_color else R.color.overhead_comsumption_color
        val valueResId = if (measurement.utility == Utility.GAS.value) R.string.gas_value else R.string.electricity_value
        iconLayout.apply {
            backgroundTintList = ContextCompat.getColorStateList(context, limitColor)
            backgroundTintMode = PorterDuff.Mode.ADD
        }
        iconImageView.apply {
            val drawableId = if (measurement.utility == Utility.GAS.value) R.drawable.ic_gas else R.drawable.ic_electricity
            setImageDrawable(drawableRepository.getById(drawableId))
            imageTintList = ContextCompat.getColorStateList(context, limitColor)
        }
        consumptionTextView.apply {
            text = stringRepository.getById(valueResId, measurement.consumption)
            setTextColor(limitColor)
        }
        commentTextView.apply {
            text = measurement.comment ?: ""
        }
        meterTextView.apply {
            text = stringRepository.getById(valueResId, measurement.position)
        }
        dateTextView.apply {
            text = measurement.date
        }
    }



}
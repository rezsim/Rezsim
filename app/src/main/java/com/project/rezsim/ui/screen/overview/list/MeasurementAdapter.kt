package com.project.rezsim.ui.screen.overview.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.rezsim.R
import com.project.rezsim.server.dto.measurement.Measurement

class MeasurementAdapter() : RecyclerView.Adapter<MeasurementViewHolder>() {

    private val measurements: MutableList<Measurement> = mutableListOf()

    fun setItems(items: List<Measurement>) {
        measurements.apply {
            clear()
            addAll(items)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeasurementViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.measurement_item, parent, false)
        return MeasurementViewHolder(view)
    }

    override fun onBindViewHolder(holder: MeasurementViewHolder, position: Int) {
        holder.bindView(measurements[position])
    }

    override fun getItemCount(): Int = measurements.size

}
package com.project.rezsim.ui.screen.overview.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.rezsim.R
import com.project.rezsim.server.dto.measurement.Measurement

class MeasurementAdapter() : RecyclerView.Adapter<MeasurementViewHolder>() {

    private val measurements: MutableList<Measurement> = mutableListOf()

    private var listView: RecyclerView? = null

    fun setItems(items: List<Measurement>) {
        measurements.apply {
            clear()
            addAll(items)
        }
        notifyDataSetChanged()
        listView?.post { listView?.scrollToPosition(0) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeasurementViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.measurement_item, parent, false)
        return MeasurementViewHolder(view)
    }

    override fun onBindViewHolder(holder: MeasurementViewHolder, position: Int) {
        holder.bindView(measurements[position])
    }

    override fun getItemCount(): Int = measurements.size

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        listView = recyclerView
    }

}
package com.project.rezsim.ui.view.spinner

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.project.rezsim.R
import com.project.rezsim.device.ColorRepository

class TextSpinnerAdapter<String>(context: Context, items: List<String>)
    : ArrayAdapter<String>(context, R.layout.spinner_item, items) {

    override fun isEnabled(position: Int) = position != 0

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return (super.getDropDownView(position, convertView, parent) as TextView).apply {
            if (position == 0) {
                setTextColor(ColorRepository().color(R.color.edit_hint_color))
            }
        }
    }

}
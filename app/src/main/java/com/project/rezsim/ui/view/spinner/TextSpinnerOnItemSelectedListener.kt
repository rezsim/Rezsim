package com.project.rezsim.ui.view.spinner

import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.project.rezsim.R

class TextSpinnerOnItemSelectedListener(private val originalListener: AdapterView.OnItemSelectedListener? = null) : AdapterView.OnItemSelectedListener {
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (position == 0) {
            (view as TextView).setTextColor(ContextCompat.getColor(view.context, R.color.edit_hint_color))
        }
        originalListener?.onItemSelected(parent, view, position - 1, id)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

}
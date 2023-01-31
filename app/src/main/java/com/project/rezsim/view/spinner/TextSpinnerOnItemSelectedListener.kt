package com.project.rezsim.view.spinner

import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.project.rezsim.R

class TextSpinnerOnItemSelectedListener : AdapterView.OnItemSelectedListener {
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (position == 0) {
            (view as TextView).setTextColor(ContextCompat.getColor(view.context, R.color.edit_hint_color))
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

}
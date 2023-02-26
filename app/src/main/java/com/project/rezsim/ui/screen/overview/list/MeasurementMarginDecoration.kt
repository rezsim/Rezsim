package com.project.rezsim.ui.screen.overview.list

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.project.rezsim.device.dp

class MeasurementMarginDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        with(outRect) {
            if (parent.getChildAdapterPosition(view) == 0) {
                top = 8.dp
            }
            left = 8.dp
            right = 8.dp
            bottom = if (parent.getChildAdapterPosition(view) == (parent.adapter?.itemCount ?: 0) - 1) {
                80.dp
            } else {
                16.dp
            }
        }
    }
}
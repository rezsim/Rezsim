package com.project.rezsim.ui.view.message

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.SnackbarContentLayout
import com.project.rezsim.R

object Message {

    private var snackbar: Snackbar? = null

    fun show(activity: Activity, view: View? = null, messageResId: Int, messageType: MessageType = MessageType.SNACKBAR_CLOSEABLE_AND_MANUALCLOSE, severity: MessageSeverity = MessageSeverity.INFO, runnable: Runnable? = null) {
        show(activity, view, activity.resources.getString(messageResId), messageType, severity, runnable)
    }

    fun show(activity: Activity, view: View? = null, message: String, messageType: MessageType = MessageType.SNACKBAR_CLOSEABLE_AND_MANUALCLOSE, severity: MessageSeverity = MessageSeverity.INFO, runnable: Runnable? = null) {
        when (messageType) {
            MessageType.TOAST_SHORT -> Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
            MessageType.TOAST_LONG -> Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
            else -> showSnackbarMessage(activity, view, message, messageType, severity, runnable)
        }
    }

    fun clear() {
        snackbar?.dismiss()
        snackbar = null
    }

    private fun showSnackbarMessage(activity: Activity, view: View? = null, message: String, messageType: MessageType, severity: MessageSeverity, runnable: Runnable? = null) {
        val v: View = view ?: activity.findViewById(R.id.clRoot)
        val time = when (messageType) {
            MessageType.SNACKBAR_AUTOCLOSE -> Snackbar.LENGTH_SHORT
            MessageType.SNACKBAR_MANUALCLOSE -> Snackbar.LENGTH_INDEFINITE
            else -> Snackbar.LENGTH_LONG
        }
        snackbar = Snackbar.make(v, message, time).apply {
            if (messageType == MessageType.SNACKBAR_MANUALCLOSE || messageType == MessageType.SNACKBAR_CLOSEABLE_AND_MANUALCLOSE) {
                setAction("OK", View.OnClickListener {
                    runnable?.run()
                })
            }
            val bkg = if (severity == MessageSeverity.ERROR) R.drawable.background_snackbar_error else R.drawable.background_snackbar_info
            val ctxt = if (severity == MessageSeverity.ERROR) R.color.snackbar_text_color_error else R.color.snackbar_text_color_info
            val cbut = if (severity == MessageSeverity.ERROR) R.color.snackbar_button_color_error else R.color.snackbar_button_color_info

            (getView() as Snackbar.SnackbarLayout).apply {
                setBackgroundResource(bkg)
                setTextColor(activity.resources.getColor(ctxt))
                val tv = (getChildAt(0) as SnackbarContentLayout).findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
                tv.isSingleLine = false
                setActionTextColor(activity.resources.getColor(cbut))
            }
            show()
        }
    }





}
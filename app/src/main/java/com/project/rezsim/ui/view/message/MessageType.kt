package com.project.rezsim.ui.view.message

enum class MessageType {
    TOAST_SHORT,
    TOAST_LONG,
    SNACKBAR_AUTOCLOSE,
    SNACKBAR_MANUALCLOSE,
    SNACKBAR_CLOSEABLE_AND_MANUALCLOSE,
    DIALOG_OK,
    DIALOG_OK_CANCEL,
    DIALOG_YES_CANCEL
}
fun MessageType.isDialog() = this == MessageType.DIALOG_OK || this == MessageType.DIALOG_OK_CANCEL || this == MessageType.DIALOG_YES_CANCEL
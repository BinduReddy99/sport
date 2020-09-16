package com.binduinfo.sports.ui.dialog

import android.app.Dialog
import android.content.Context
import com.binduinfo.sports.R
import com.binduinfo.sports.base.BaseDialog

import kotlinx.android.synthetic.main.layout_dialogue.*

class AlertDialogue(
    override val context: Context,
    val message: String,
    var negativeButton: String = "CANCEL",
    var positiveButton: String = "CONFIRM",
    var alertClick: AlertClickable? = null
) : BaseDialog(context) {
    override fun layoutResouceId(): Int = R.layout.layout_dialogue

    override fun handleUi(dialog: Dialog) {
        super.handleUi(dialog)
        dialog.alertMessage.text = message
        dialog.lay_dia_cancel.text = negativeButton
        dialog.lay_dia_confirm.text = positiveButton

        dialog.lay_dia_cancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.lay_dia_confirm.setOnClickListener {
            alertClick?.alertClickable()
            dialog.dismiss()
        }
    }

    interface AlertClickable {
        fun alertClickable()
    }


}
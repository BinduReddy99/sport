package com.binduinfo.sports.base

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat

abstract class BaseDialog(open val context: Context, val center: Boolean = true) {
    private lateinit var dialog: Dialog
    private lateinit var inflater: LayoutInflater

    open fun handleDisplayFeature(): Dialog {
        dialog = Dialog(context)
        dialog.window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        dialog.window?.statusBarColor = ContextCompat.getColor(context, android.R.color.transparent)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        dialog.setContentView(layoutResouceId())
        if (center)
            this.setLayoutParameters()
        else
            this.setLayoutParametersNoCenter()
        handleUi(dialog)
        return dialog
    }

    open fun handleUi(dialog: Dialog) {

    }

    protected abstract fun layoutResouceId(): Int
    private fun setLayoutParametersNoCenter() {
        val layoutParameters = dialog.window?.attributes
        layoutParameters?.width = RelativeLayout.LayoutParams.MATCH_PARENT
        layoutParameters?.height = RelativeLayout.LayoutParams.MATCH_PARENT

    }

    private fun setLayoutParameters() {
        val layoutParameters = dialog.window?.attributes
        layoutParameters?.width = RelativeLayout.LayoutParams.MATCH_PARENT
        layoutParameters?.height = RelativeLayout.LayoutParams.MATCH_PARENT

        layoutParameters?.gravity = Gravity.CENTER_VERTICAL
        layoutParameters?.gravity = Gravity.CENTER_HORIZONTAL

    }

    fun showdialogue() {
        this.handleDisplayFeature()
        if (!dialog.isShowing)
            dialog.show()
    }

    fun hiddeDialogue() {
        if (dialog.isShowing)
            dialog.dismiss()
    }
}
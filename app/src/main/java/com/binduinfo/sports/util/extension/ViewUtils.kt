package com.binduinfo.sports.util.extension

import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatButton

fun ProgressBar.hide() {
    visibility = View.GONE
}

fun ProgressBar.show() {
    visibility = View.VISIBLE
}

fun AppCompatButton.hide() {
    visibility = View.GONE
}

fun AppCompatButton.show() {
    visibility = View.VISIBLE
}
package com.binduinfo.sports.util

import android.content.Context


class Constant {
    companion object {
        fun calculateNoOfColumns(context: Context): Int {
            val displayMetrics = context.resources.displayMetrics
            val dpWidth = displayMetrics.widthPixels / displayMetrics.density
            return (dpWidth / 180).toInt()
        }
    }
}
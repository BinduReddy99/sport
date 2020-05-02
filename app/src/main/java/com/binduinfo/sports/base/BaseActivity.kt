package com.binduinfo.sports.base

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity: AppCompatActivity() {
    abstract fun uiHandle()
    protected fun showToast(message: String){// String message
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
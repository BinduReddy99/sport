package com.binduinfo.sports.util

import com.google.android.material.textfield.TextInputLayout

interface TextLayoutViewErrorHandle {
    fun errHandle(inputValue: String, testLayotInput: TextInputLayout?)
}
package com.binduinfo.sports.util

import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputLayout

class MyTextWater(val layoutInput: TextInputLayout?, val textLayoutViewErrorHandle: TextLayoutViewErrorHandle?) : TextWatcher {

    override fun afterTextChanged(s: Editable?) {
        textLayoutViewErrorHandle!!.errHandle(s.toString(), layoutInput)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }
}
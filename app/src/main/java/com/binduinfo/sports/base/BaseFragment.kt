package com.binduinfo.sports.base

import android.widget.Toast
import androidx.fragment.app.Fragment

abstract class BaseFragment:Fragment() {
    protected fun showToast(message: String){// String message
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
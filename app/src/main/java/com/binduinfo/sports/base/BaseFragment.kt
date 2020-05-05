package com.binduinfo.sports.base

import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputLayout

abstract class BaseFragment : Fragment() {
    protected fun showToast(message: String) {// String message
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    protected fun validateMobileNumber(
        userInput: String,
        testLayoutInput: TextInputLayout?
    ): Boolean {
        if (userInput.length != 10) {
            testLayoutInput?.error = "Enter valid mobile number"
            return false
        }
        testLayoutInput?.isErrorEnabled = false
        return true
    }

    protected fun validateName(userInput: String, testLayoutInput: TextInputLayout?): Boolean {
        if (userInput.length < 3) {
            testLayoutInput?.error = "Enter valid name"
            return false
        }
        testLayoutInput?.isErrorEnabled = false
        return true
    }

    protected fun validatePassword(userInput: String, testLayoutInput: TextInputLayout?): Boolean {
        if (userInput.length !in 5..8) {
            testLayoutInput?.error = "Enter valid password"
            return false
        }
        testLayoutInput?.isErrorEnabled = false
        return true
    }

    protected fun comparePassword(
        password: String,
        userInput: String,
        testLayoutInput: TextInputLayout?
    ): Boolean {
        if (password != userInput) {
            testLayoutInput?.error = "Enter match password"
            return false
        }
        testLayoutInput?.isErrorEnabled = false
        return true
    }
}
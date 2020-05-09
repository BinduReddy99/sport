package com.binduinfo.sports.base

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
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

    protected fun emailValidate(email: String, errorText: TextInputLayout): Boolean {
        if (email.isEmpty() || !isValidEmail(email)) {
            errorText.error = "Enter valid email id"
           // requestFocus(errorText)
            return false
        }
        errorText.isErrorEnabled = false
        return true
    }

    private fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    protected fun hideKeyBoard(activity: Activity){
        val view = activity?.currentFocus?: View(activity)
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)

    }

    protected fun hideKeyBoard(){
        val imm = activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            if (imm.isAcceptingText)
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
    }


}
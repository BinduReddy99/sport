package com.binduinfo.sports.util.extension

import android.content.Context.INPUT_METHOD_SERVICE
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.bottomnavigation.BottomNavigationView

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

fun View.hideKeyboard() {
    val inputMethodManager = context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}

fun BottomNavigationView.hide(){
    visibility = View.GONE
}

fun BottomNavigationView.show(){
    visibility = View.VISIBLE
}
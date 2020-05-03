package com.binduinfo.sports.ui

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import com.binduinfo.sports.R
import com.binduinfo.sports.base.BaseActivity
import kotlinx.android.synthetic.main.activity_second.*

class SecondActivity: BaseActivity() {
    override fun uiHandle() {
        showToast("I am Second Screen")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        val mobileNumber = intent.getStringExtra("mobile_Number")
        mobile_number.text = mobileNumber
        Log.d("mobile ==========", mobileNumber)
        uiHandle()
    }

    override fun onStart() {
        super.onStart()
        Log.d("onStart - 2","running")
    }

    override fun onPause() {
        super.onPause()
        Log.d("onPause -2","running")

    }

    override fun onResume() {
        super.onResume()
        Log.d("onResume -2","running")

    }

    override fun onRestart() {
        super.onRestart()
        Log.d("onRestart -2","running")

    }

    override fun onStop() {
        super.onStop()
        Log.d("onStop -2","running")

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("onDestroy -2","running")

    }
}
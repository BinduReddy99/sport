package com.binduinfo.sports.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.binduinfo.sports.R
import com.binduinfo.sports.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {


    override fun uiHandle() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        uiHandle()
    }


}

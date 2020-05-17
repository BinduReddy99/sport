package com.binduinfo.sports.ui.activity

import android.content.Intent
import android.os.Bundle
import com.binduinfo.sports.R
import com.binduinfo.sports.base.BaseActivity


class MainActivity : BaseActivity() {
    override fun uiHandle() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        if(BaseApplication.instance!!.getSharedPreferenceObj()?.getsharedBoolean(IS_LOGGED_IN)!!){
//            intent()
//        }
        hideToolbar()

        setContentView(R.layout.activity_main)
        uiHandle()

    }


    private fun intent(){
        val intent = Intent(this, HomeActivity::class.java)
        intent.let {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(it)
        }
    }


}

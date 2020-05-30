package com.binduinfo.sports.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.binduinfo.sports.R
import com.binduinfo.sports.base.BaseActivity
import com.binduinfo.sports.data.model.address.AddressRequest
import com.binduinfo.sports.ui.fragment.signupfetchlocation.LOCATION_REQUEST_CODE


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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //Log.d("Location Request", data.toString())
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == LOCATION_REQUEST_CODE){
                val address = data?.getParcelableExtra<AddressRequest>(ADDRESS)
                Log.d("Location Request", address.toString())
            }
        }
    }


}

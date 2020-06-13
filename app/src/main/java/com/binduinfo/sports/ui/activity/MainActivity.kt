package com.binduinfo.sports.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.binduinfo.sports.R
import com.binduinfo.sports.base.BaseActivity
import com.binduinfo.sports.data.model.address.AddressRequest
import com.binduinfo.sports.ui.fragment.selectinterestedsports.SELECT_SPORTS_ACTIVITY
import com.binduinfo.sports.ui.fragment.signupfetchlocation.LOCATION_REQUEST_CODE


class MainActivity : BaseActivity() {
    private  var fragment: Fragment? = null
    override fun uiHandle() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideToolbar()
        setContentView(R.layout.activity_main)
       // window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        uiHandle()
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //Log.d("err===", data.toString())
        if(fragment == null)
            fragment = this.supportFragmentManager.findFragmentById(R.id.main_nav_host)
                ?.childFragmentManager?.fragments?.get(0)

        if (resultCode == Activity.RESULT_OK){
                fragment?.onActivityResult(requestCode, resultCode, data)
        }
    }


}

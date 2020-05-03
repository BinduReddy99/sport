package com.binduinfo.sports.ui

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import com.binduinfo.sports.R
import com.binduinfo.sports.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {

    private lateinit var mobileNumber: String
    private lateinit var passwordEdt: String

    override fun uiHandle() {
        //login_edt_password.transformationMethod = PasswordTransformationMethod()
        //val bundle = Bundle()
        val intent: Intent = Intent(this, SecondActivity::class.java)
        sign_in.setOnClickListener {
            mobileNumber = login_edt_mob_num.text.toString() //edittext
            passwordEdt = login_edt_password.text.toString()
            when {
                mobileNumber.length != 10 -> {
                    showToast("Enter valid mobile number")
                    return@setOnClickListener
                }

                passwordEdt.length !in 5..8 -> {
                    showToast("Enter password between 5 to 8 character")
                    return@setOnClickListener
                }
                else ->{

                }
            }

            intent.putExtra("mobile_Number", mobileNumber)
            startActivity(intent)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("onCreate - 1", "running")
        setContentView(R.layout.activity_main)
        uiHandle()
    }

//    override fun onStart() {
//        super.onStart()
//        Log.d("onStart - 1","running")
//    }

//    override fun onPause() {
//        super.onPause()
//        Log.d("onPause -1","running")
//
//    }
//
//    override fun onResume() {
//        super.onResume()
//        Log.d("onResume -1","running")
//
//    }
//
//    override fun onRestart() {
//        super.onRestart()
//        Log.d("onRestart -1","running")
//
//    }
//
//    override fun onStop() {
//        super.onStop()
//        Log.d("onStop -1","running")
//
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        Log.d("onDestroy -1","running")
//
//    }


}

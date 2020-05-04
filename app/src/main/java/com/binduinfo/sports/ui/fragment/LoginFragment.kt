package com.binduinfo.sports.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.binduinfo.sports.R
import com.binduinfo.sports.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_sign_up.*


class LoginFragment : BaseFragment() {
    private lateinit var mobileNumber: String
    private lateinit var passwordEdt: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //uiHandle()

        return inflater.inflate(R.layout.fragment_login, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        uiHandle()
    }

    private fun uiHandle() {
        //login_edt_password.transformationMethod = PasswordTransformationMethod()
        //val bundle = Bundle()
        login_edt_mob_num.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(login_edt_mob_num.text.toString().length != 10){
                    login_mobile_number_lay.error = "Enter valid mobile number"
                }else{
                    login_mobile_number_lay.isErrorEnabled = false
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
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
                else -> {

                }
            }
            login_edt_mob_num.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if(login_edt_mob_num.text.toString().length != 10){
                        login_mobile_number_lay.error = "Enter valid mobile number"
                    }else{
                        login_mobile_number_lay.isErrorEnabled = false
                    }
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
            //findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }

    }



}

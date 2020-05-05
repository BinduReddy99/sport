package com.binduinfo.sports.ui.fragment

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.binduinfo.sports.R
import com.binduinfo.sports.base.BaseFragment
import com.binduinfo.sports.util.MyTextWater
import com.binduinfo.sports.util.TextLayoutViewErrorHandle
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_sign_up.*


class LoginFragment : BaseFragment(), TextLayoutViewErrorHandle{
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
        login_edt_mob_num.addTextChangedListener(MyTextWater(login_mobile_number_lay, this))

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
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }

    }

    override fun errHandle(inputValue: String, testLayotInput: TextInputLayout?) {
        when(testLayotInput?.id){
            R.id.login_mobile_number_lay ->{
                validateMobileNumber(inputValue, testLayotInput)
            }
        }
    }


}

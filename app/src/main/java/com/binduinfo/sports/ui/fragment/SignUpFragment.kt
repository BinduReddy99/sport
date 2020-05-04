package com.binduinfo.sports.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.navigation.fragment.findNavController

import com.binduinfo.sports.R
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlin.text.contains as contains1

/**
 * A simple [Fragment] subclass.
 */
class SignUpFragment : Fragment() {
    private lateinit var name:String
    private lateinit var emailId:String
    private lateinit var password : String
    private lateinit var confirmPassword : String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        uiHandle()
    }

    private fun uiHandle() {
        //val edt = view?.findViewById<EditText>(R.id.sign_up_edit_name)
        sign_up_edit_mobile.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if(sign_up_edit_mobile.text.toString().length != 10){
                    sign_up_lay_mobile.error = "Enter valid mobile number"
                }else{
                    sign_up_lay_mobile.isErrorEnabled = false
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        sign_up_edit_name.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if(sign_up_edit_name.text.toString().length < 2){
                    sign_up_lay_name.error = "Enter valid name"

                }else{
                    sign_up_lay_name.isErrorEnabled = false
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        sign_up_lay_edt_pass.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if(sign_up_lay_edt_pass.text.toString().length < 4)
                {
                    sign_up_lay_confirm.error = "Your password is too short"

                }else{
                    sign_up_lay_confirm.isErrorEnabled = false
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        sign_up_btn.setOnClickListener {
          password = sign_up_lay_edt_pass.text.toString()
            confirmPassword = sign_up_edt_confirm_pass.text.toString()
           //checking whether password==confirm_password
            if (password !=confirmPassword){
                showToast("the password should match with above typed")
                return@setOnClickListener
            }
            else
            {

            }

        }





//        (sign_up_edit_name as EditText).addTextChangedListener(object : TextWatcher{
//            override fun afterTextChanged(s: Editable?) {
//                sign_up_lay_name.setError("errror")
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//            }
//
//        })
//        sign_in.setOnClickListener {
//            name = login_edt_mob_num.text.toString() //edittext
//            emailId = login_edt_password.text.toString()
//            when {
//                name.length >= 50-> {
//                    showToast("Enter valid name")
//                    return@setOnClickListener
//                }
//
//                emailId.length >= 150 -> {
//                    showToast("Enter valid email id")
//                    return@setOnClickListener
//                }
//                else -> {
//
//                }
//            }
//
//            fun showToast(s: String) {
//           // findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
//            //findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
//        }
//
//    }

    }

    private fun showToast(s: String) {

    }

}

package com.binduinfo.sports.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

import com.binduinfo.sports.R
import kotlinx.android.synthetic.main.fragment_login.*

/**
 * A simple [Fragment] subclass.
 */
class SignUpFragment : Fragment() {
     private lateinit var name:String
    private lateinit var emailId:String
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
        //login_edt_password.transformationMethod = PasswordTransformationMethod()
        //val bundle = Bundle()
        sign_in.setOnClickListener {
            name = login_edt_mob_num.text.toString() //edittext
            emailId = login_edt_password.text.toString()
            when {
                name.length >= 50-> {
                    showToast("Enter valid name")
                    return@setOnClickListener
                }

                emailId.length >= 150 -> {
                    showToast("Enter valid email id")
                    return@setOnClickListener
                }
                else -> {

                }
            }

            fun showToast(s: String) {
           // findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
            //findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }

    }

    }

    private fun showToast(s: String) {

    }

}

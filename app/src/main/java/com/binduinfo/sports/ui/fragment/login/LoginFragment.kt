package com.binduinfo.sports.ui.fragment.login

import android.content.Intent
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.binduinfo.sports.R
import com.binduinfo.sports.app.BaseApplication
import com.binduinfo.sports.base.BaseFragment
import com.binduinfo.sports.ui.activity.HomeActivity
import com.binduinfo.sports.util.MyTextWater
import com.binduinfo.sports.util.TextLayoutViewErrorHandle
import com.binduinfo.sports.util.extension.hide
import com.binduinfo.sports.util.extension.show
import com.binduinfo.sports.util.network.model.LoginResponse
import com.binduinfo.sports.util.network.retrofit.NetworkInterFace
import com.binduinfo.sports.data.preference.IS_LOGGED_IN
import com.binduinfo.sports.data.preference.LOGIN_TOKEN
import com.google.android.material.textfield.TextInputLayout
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : BaseFragment(), TextLayoutViewErrorHandle{
    private  var mobileNumber: String = ""
    private  var passwordEdt: String = ""
    private val mCompositeDisposable: CompositeDisposable = CompositeDisposable()
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
        if(BaseApplication.instance!!.getSharedPreferenceObj()?.getsharedBoolean(IS_LOGGED_IN)!!){
            intent()
            //findNavController().navigate(R.id.action_loginFragment_to_selectInterestedSports)
            return
        }
        //action_signUpFragment_to_selectInterestedSports
        login_edt_mob_num.addTextChangedListener(MyTextWater(login_mobile_number_lay, this))
        login_edt_password.addTextChangedListener(MyTextWater(login_text_input_password, this))
        sign_in_btn.setOnClickListener {
            mobileNumber = login_edt_mob_num.text.toString() //edittext
            passwordEdt = login_edt_password.text.toString()
            when {
                mobileNumber.length != 10 -> {
                    login_mobile_number_lay.error = "Enter valid mobile number"
                    login_mobile_number_lay.isErrorEnabled = true
                }

                passwordEdt.isEmpty() -> {
                    login_text_input_password.error = "Enter password"
                    login_text_input_password.isErrorEnabled = true
                    return@setOnClickListener
                }
                else -> {
                    login()
                }
            }
        }
        login_sign_up_btn.setOnClickListener {

            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }

    }

    private fun login(){
        hideKeyBoard(requireActivity())
        if(isInternetAvailable(requireContext())) {
            sign_in_progress_bar.show()
            sign_in_btn.hide()
            val networkCall = NetworkInterFace.povideApi(
                NetworkInterFace.retrofitConnection(
                    mobileNumber,
                    passwordEdt
                )
            )
            mCompositeDisposable.add(
                networkCall.signIn().observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(this::handleResponse, this::handleError)
            )
        }else{
            showToast(resources.getString(R.string.internet_check))
        }
    }

     private fun handleResponse(response: LoginResponse){
         if (response.success == 1){

             BaseApplication.instance!!.getSharedPreferenceObj()?.storeValue(LOGIN_TOKEN, response.token!!)
             BaseApplication.instance!!.getSharedPreferenceObj()?.storeValue(IS_LOGGED_IN, true)
             intent()
             return
         }
         textView3.visibility = View.VISIBLE
         textView3.text = response.message
         sign_in_progress_bar.hide()
         sign_in_btn.show()

         //showToast(response.token!!)
    }

    private fun intent(){
        val intent = Intent(requireContext(), HomeActivity::class.java)
        intent.let {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            requireActivity().startActivity(it)
        }
    }

    private fun handleError(throwable: Throwable){
        sign_in_progress_bar.hide()
        sign_in_btn.show()
    }

    override fun onStop() {
        super.onStop()
        mCompositeDisposable.clear()
    }

    override fun errHandle(inputValue: String, testLayotInput: TextInputLayout?) {
        when(testLayotInput?.id){
            R.id.login_mobile_number_lay -> {
                textView3.text = ""
                mobileNumber = inputValue
                validateMobileNumber(inputValue, testLayotInput)
            }
            R.id.login_text_input_password -> {
                passwordEdt = inputValue
                textView3.text = ""
                if (passwordEdt.isNotEmpty())
                    testLayotInput.isErrorEnabled = false
            }
        }
    }


}

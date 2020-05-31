package com.binduinfo.sports.ui.fragment.signup

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.binduinfo.sports.R
import com.binduinfo.sports.base.BaseFragment
import com.binduinfo.sports.ui.bottomSheet.BottomSheetOtp
import com.binduinfo.sports.util.MyTextWater
import com.binduinfo.sports.util.TextLayoutViewErrorHandle
import com.binduinfo.sports.util.extension.hide
import com.binduinfo.sports.util.extension.show
import com.binduinfo.sports.util.network.model.GenerateOTP
import com.binduinfo.sports.util.network.model.SignUpRequest
import com.binduinfo.sports.util.network.model.SportResponse
import com.binduinfo.sports.util.network.retrofit.NetworkInterFace
import com.google.android.material.textfield.TextInputLayout
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_sign_up.*


/**
 * A simple [Fragment] subclass.
 */
class SignUpFragment : BaseFragment(), TextLayoutViewErrorHandle,
    BottomSheetOtp.SignUpFragmentInteraction {
    private var name: String = ""
    private var emailId: String = ""
    private var password: String = ""
    private var confirmPassword: String = ""
    private var mobileNumber: String = ""
    private var gender: String = ""

    private var isNameValid: Boolean = false
    private var isEmailValid: Boolean = true
    private var isMobileValid: Boolean = false
    private var isConfirmPasswordValid: Boolean = false
    private var isPasswordValid: Boolean = false
    val networkCall = NetworkInterFace.povideApi(NetworkInterFace.retrofitConnection())
    private var mCompositeDisposable: CompositeDisposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mCompositeDisposable = CompositeDisposable()
    }

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

        sign_up_edit_mobile.addTextChangedListener(MyTextWater(sign_up_lay_mobile, this))

        sign_up_edit_name.addTextChangedListener(MyTextWater(sign_up_lay_name, this))

        sign_up_lay_edt_pass.addTextChangedListener(MyTextWater(sign_up_lay_confirm, this))

        sign_up_edt_confirm_pass.addTextChangedListener(MyTextWater(sign_up_lay_confirm_pass, this))

        sign_up_edit_email.addTextChangedListener(MyTextWater(sign_up_lay_email, this))


        signup_gender_radio.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.sign_up_gender_male_radio -> {
                    gender = "male"
                }
                R.id.sign_up_gender_female_radio -> {
                    gender = "female"
                }

                R.id.sign_up_gender_others_radio -> {
                    gender = "other"
                }
            }
        }

        sign_up_btn.setOnClickListener {
            otpRequest()
        }

    }

    /**
     * For populating custom dialog
     */
    private var bottomDialog: BottomSheetOtp? = null
    private fun showDialog() {
        bottomDialog = BottomSheetOtp(this, mobileNumber)
        bottomDialog?.show(requireActivity().supportFragmentManager, bottomDialog?.tag)
//        val dialog = Dialog(requireContext(),  android.R.style.Theme_Translucent_NoTitleBar_Fullscreen)
//        dialog.setContentView(layoutInflater.inflate(R.layout.otp, null))
//        dialog.show()
    }

    private fun otpRequest() {

        when {
            !isNameValid -> {
                requestFocus(sign_up_lay_name, name)
                return
            }

            gender.isEmpty() -> {
                showToast("Select gender")
            }

            !isMobileValid -> {
                requestFocus(sign_up_lay_mobile, mobileNumber)
                return
            }

            !isPasswordValid -> {
                requestFocus(sign_up_lay_confirm, password)
                return
            }

            !isConfirmPasswordValid -> {
                requestFocus(sign_up_lay_confirm_pass, confirmPassword)
                return
            }

            !isEmailValid -> {
                requestFocus(sign_up_lay_email, emailId)
                return
            }

            else -> {
                hideKeyBoard(requireActivity())
                serverRequest(mobileNumber)
                //showDialog()
            }
        }
    }


    private fun requestFocus(view: View, input: String) {
        if (view.requestFocus()) {
            errHandle(input, view as TextInputLayout)
            requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        }
    }


    override fun errHandle(inputValue: String, testLayotInput: TextInputLayout?) {
        when (testLayotInput?.id) {
            R.id.sign_up_lay_mobile -> {
                mobileNumber = inputValue
                isMobileValid = validateMobileNumber(inputValue, testLayotInput)
            }

            R.id.sign_up_lay_name -> {
                name = inputValue
                isNameValid = validateName(inputValue, testLayotInput)
            }

            R.id.sign_up_lay_confirm -> {
                password = inputValue
                isPasswordValid = validatePassword(inputValue, testLayotInput)
                isConfirmPasswordValid =
                    comparePassword(password, confirmPassword, sign_up_lay_confirm_pass)
            }

            R.id.sign_up_lay_confirm_pass -> {
                confirmPassword = inputValue
                isConfirmPasswordValid = comparePassword(password, confirmPassword, testLayotInput)
            }

            R.id.sign_up_lay_email -> {
                emailId = inputValue
                if (emailId.isNotEmpty())
                    isEmailValid = emailValidate(inputValue, testLayotInput)
                else {
                    sign_up_lay_email.isErrorEnabled = false
                    isEmailValid = true
                }
            }

        }

    }


    private fun serverRequest(mobileNumber: String) {
        sign_up_btn.hide()
        sign_up_progress_bar.show()
        if (isInternetAvailable(requireContext())) {
            mCompositeDisposable!!.add(
                networkCall.requestOtp(GenerateOTP(mobileNumber))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(this::handleResponse, this::handleError)
            )
        } else {
            showToast(resources.getString(R.string.internet_check))
        }
    }


    private fun handleResponse(response: SportResponse) {
        sign_up_btn.show()
        sign_up_progress_bar.hide()
        if (response.success == 0) {
            showToast(response.message)
        } else {
            showDialog()
        }
    }

    private fun handleError(err: Throwable) {
        sign_up_btn.show()
        sign_up_progress_bar.hide()
    }

    override fun onDestroy() {
        super.onDestroy()
        mCompositeDisposable!!.clear()
    }

    override fun clickable(
        otp: String,
        progress: ProgressBar,
        button: AppCompatButton,
        error_text: TextView
    ) {
        //showToast(otp)
        if (isInternetAvailable(requireContext())) {
            progress.show()
            button.hide()
            mCompositeDisposable?.add(
                networkCall.signUp(
                    signUpRequest = SignUpRequest(
                        confirmPassword,
                        emailId,
                        gender,
                        mobileNumber,
                        name,
                        otp,
                        password
                    )
                )
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        registerHandleResponse(it, progress, button, error_text)
                    }, {
                        progress.hide()
                        button.show()
                        handleError(it)
                    })
            )

        } else {
            showToast(resources.getString(R.string.internet_check))
        }

    }

    override fun onResume() {
        super.onResume()
     //   activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

//    override fun onPause() {
//        super.onPause()
//        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
//        activity?.window?.decorView?.systemUiVisibility = 0
//    }

    private fun registerHandleResponse(
        response: SportResponse,
        progress: ProgressBar,
        button: AppCompatButton,
        error_text: TextView
    ) {
        progress.hide()
        button.show()
        if (response.success == 1) {
            bottomDialog?.dismiss()
            //requireActivity().onBackPressed()
            findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
        } else {
            error_text.text = response.message
        }
        //showToast(response.message)
    }

    override fun hideKey(activity: Activity) {
        hideKeyBoard()
    }

    override fun disposalClose() {
        mCompositeDisposable?.clear()
    }

}

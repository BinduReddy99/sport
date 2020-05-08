package com.binduinfo.sports.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.binduinfo.sports.R
import com.binduinfo.sports.base.BaseFragment
import com.binduinfo.sports.util.MyTextWater
import com.binduinfo.sports.util.TextLayoutViewErrorHandle
import com.binduinfo.sports.util.network.model.GenerateOTP
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
class SignUpFragment : BaseFragment(), TextLayoutViewErrorHandle {
    private lateinit var name:String
    private lateinit var emailId:String
    private lateinit var password : String
    private lateinit var confirmPassword : String
    private lateinit var mobileNumber: String
    private lateinit var gender: String
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

        sign_up_btn.setOnClickListener {
            serverRequest(sign_up_edit_mobile.text.toString())
        }

    }

    private  fun otpRequest(){

    }


    override fun errHandle(inputValue: String, testLayotInput: TextInputLayout?) {
        when(testLayotInput?.id){
            R.id.sign_up_lay_mobile -> {
                validateMobileNumber(inputValue, testLayotInput)
            }

            R.id.sign_up_lay_name -> {
                validateName(inputValue, testLayotInput)
            }

            R.id.sign_up_lay_confirm ->{
                password = inputValue
                validatePassword(inputValue, testLayotInput)
            }

            R.id.sign_up_lay_confirm_pass -> {
                comparePassword(password, inputValue, testLayotInput)
            }

        }

    }


    fun serverRequest(mobileNumber: String){
         val networkCall = NetworkInterFace.retrofitConnection().create(NetworkInterFace::class.java)
        mCompositeDisposable!!.add(networkCall.requestOtp(GenerateOTP(mobileNumber))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(this::handleResponse, this::handleError))
    }


    fun handleResponse(response: SportResponse){

    }

    fun  handleError(err: Throwable){

    }

    override fun onDestroy() {
        super.onDestroy()
        mCompositeDisposable!!.clear()
    }

}

package com.binduinfo.sports.ui.bottomSheet

import `in`.aabhasjindal.otptextview.OTPListener
import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import com.binduinfo.sports.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.otp.*

class BottomSheetOtp(
    val signUpFragmentInteraction: SignUpFragmentInteraction,
    val mobilNumber: String
) : BottomSheetDialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.otp, container, false)
    }


    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        isCancelable = false
        var otpStr: String? = null

        // conccat use $ symbol
        textView2.text = "${resources.getString(R.string.otp_will_receive_to)} $mobilNumber"
        otpTextView.otpListener = object : OTPListener {
            override fun onOTPComplete(otp: String?) {
                otpStr = otp
                signUpFragmentInteraction.hideKey(requireActivity())
            }

            override fun onInteractionListener() {
                error_text.text = ""
            }

        }
        dialogue_mobile_number_editText.setOnClickListener {
            dismiss()
            signUpFragmentInteraction.disposalClose()
        }
        cancel_bottom_sheet.setOnClickListener {
            dismiss()
            signUpFragmentInteraction.disposalClose()
        }
        dialog_verify_otp.setOnClickListener {

            signUpFragmentInteraction.clickable(
                otpStr!!,
                progress_bar,
                dialog_verify_otp,
                error_text
            )
        }

    }


    interface SignUpFragmentInteraction {
        fun clickable(
            otp: String,
            progress: ProgressBar,
            button: AppCompatButton,
            error_text: TextView
        )

        fun hideKey(activity: Activity)
        fun disposalClose()
    }
}
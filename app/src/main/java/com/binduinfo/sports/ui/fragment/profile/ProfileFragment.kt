package com.binduinfo.sports.ui.fragment.profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.binduinfo.sports.R
import com.binduinfo.sports.util.cropimage.CropImage
import com.binduinfo.sports.util.cropimage.CropImageView
import com.bumptech.glide.Glide
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    private lateinit var notificationsViewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_profile, container, false)
//        val textView: TextView = root.findViewById(R.id.text_notifications)
//        notificationsViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profile_pic.setOnClickListener {
            Dexter.withActivity(requireActivity()).withPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    report?.let {
                        if (it.areAllPermissionsGranted()) {
                            CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON)
                                .setMaxCropResultSize(1240, 1240)
                                .setMinCropResultSize(820, 820)
                                .start(
                                    requireActivity(),
                                    CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
                                )
                        }else{
                            Toast.makeText(requireContext(), "Enable Permission", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.let {
                        token.continuePermissionRequest()
                    }
                }

            }).check()
//            CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON)
//                .setMaxCropResultSize(1240,1240)
//                .setMinCropResultSize(820, 820)
//                .start(requireActivity(), CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
            /*.start(requireContext(), CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)*/
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("requestCode", requestCode.toString())
        Log.d("data", data.toString())
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                    val result = CropImage.getActivityResult(data)
                    Glide.with(requireActivity()).asBitmap().load(result.uri).into(profile_pic)
                }
            }
        } else {
            Toast.makeText(requireContext(), "Try again", Toast.LENGTH_SHORT).show()
        }
    }

}

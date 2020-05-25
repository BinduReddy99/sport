package com.binduinfo.sports.ui.fragment.profile

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xwray.groupie.ViewHolder
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.binduinfo.sports.R
import com.binduinfo.sports.data.model.Sport
import com.binduinfo.sports.data.preference.PreferenceProvider
import com.binduinfo.sports.data.repositores.ProfileRepository
import com.binduinfo.sports.databinding.FragmentProfileBinding
import com.binduinfo.sports.ui.fragment.profile.adapter.SportSelectedItem
import com.binduinfo.sports.util.cropimage.CropImage
import com.binduinfo.sports.util.cropimage.CropImageView
import com.bumptech.glide.Glide
import com.example.mvvmsample.util.Coroutines
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.miziontrix.kmo.data.network.api.mvvm.MyApi
import com.miziontrix.kmo.data.network.api.mvvm.NetworkConnectionInterceptor
import com.xwray.groupie.GroupAdapter
import kotlinx.android.synthetic.main.user_profile_layout.*

class ProfileFragment : Fragment(), ProfileHandler {

    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var binding: FragmentProfileBinding
    private lateinit var api: MyApi
    private lateinit var networkConnectionInterceptor: NetworkConnectionInterceptor
    private lateinit var preferenceProvider: PreferenceProvider
    private lateinit var profileRepository: ProfileRepository
    private lateinit var factory: ProfileViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        preferenceProvider = PreferenceProvider(requireContext())
        networkConnectionInterceptor =
            NetworkConnectionInterceptor(requireContext(), preferenceProvider)
        api = MyApi(networkConnectionInterceptor)
        profileRepository = ProfileRepository(api)
        factory = ProfileViewModelFactory(profileRepository)
         binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)//DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        profileViewModel =
            ViewModelProvider(this, factory).get(ProfileViewModel::class.java)
        profileViewModel.profileHandler = this
        binding.lifecycleOwner = this
        binding.profileViewModel =  profileViewModel

//        val textView: TextView = root.findViewById(R.id.text_notifications)
//        notificationsViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadPersonInformation()

    }

    private fun loadPersonInformation() {
        profileViewModel.serverRequest.value = true
        profileViewModel.profileInfo.observe(viewLifecycleOwner, Observer {
            binding.userInfo = it
            profileViewModel.serverRequest.value = false
            initRecyclerView(it.profile.sports.toSportItem())
        })

//           binding.userInfo = profileViewModel.profileInfo.value
    }

    //Transformation
    private fun List<Sport>.toSportItem(): List<SportSelectedItem>{
        return this.map {
            SportSelectedItem(it)
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
                    uploadImage(requireContext(), result.uri)
                }
            }
        } else {
            Toast.makeText(requireContext(), "Try again", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadImage(context: Context, uri: Uri) {
        Coroutines.main {
            profileViewModel.uploadImage(context, uri)
            profileViewModel.progMutable.value = false
           // Log.d("done========", "done")
        }

    }
    private fun initRecyclerView(sports: List<SportSelectedItem>) {
        val mLayoutManager = FlexboxLayoutManager(requireContext())
        mLayoutManager.flexDirection = FlexDirection.ROW
        mLayoutManager.justifyContent = JustifyContent.CENTER
        val mAdapter = GroupAdapter<ViewHolder>().apply {
            addAll(sports)
        }
        sports_selected_list.apply {
            layoutManager = mLayoutManager
            setHasFixedSize(true)
            adapter = mAdapter
        }
    }

    override fun profilePic() {
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
    }

    override fun profilePicUpdate() {

    }

}

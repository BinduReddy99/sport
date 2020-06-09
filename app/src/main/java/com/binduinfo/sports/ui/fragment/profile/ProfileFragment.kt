package com.binduinfo.sports.ui.fragment.profile

import android.Manifest
import android.app.Activity
import android.app.ActivityOptions
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
import androidx.navigation.fragment.findNavController
import com.binduinfo.sports.R
import com.binduinfo.sports.data.model.About
import com.binduinfo.sports.data.model.Sport
import com.binduinfo.sports.data.model.UpdateProfile
import com.binduinfo.sports.data.preference.PreferenceProvider
import com.binduinfo.sports.data.repositores.ProfileRepository
import com.binduinfo.sports.databinding.FragmentProfileBinding
import com.binduinfo.sports.ui.activity.MainActivity
import com.binduinfo.sports.ui.dialog.AlertDialogue
import com.binduinfo.sports.ui.fragment.profile.adapter.SportSelectedItem
import com.binduinfo.sports.util.cropimage.CropImage
import com.binduinfo.sports.util.cropimage.CropImageView
import com.bumptech.glide.Glide
import com.example.mvvmsample.util.Coroutines
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.xwray.groupie.GroupAdapter
import kotlinx.android.synthetic.main.user_profile_layout.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class ProfileFragment() : Fragment(), ProfileHandler, AlertDialogue.AlertClickable, OnMapReadyCallback, KodeinAware {
    override val kodein by kodein()


    private val preference: PreferenceProvider by instance<PreferenceProvider>()
    private val factory: ProfileViewModelFactory by instance<ProfileViewModelFactory>()
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var binding: FragmentProfileBinding
    private var dialogue: AlertDialogue? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)//DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        profileViewModel =
            ViewModelProvider(this, factory).get(ProfileViewModel::class.java)
        profileViewModel.profileHandler = this
        binding.lifecycleOwner = this
        binding.profileViewModel =  profileViewModel
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
                            .setMaxCropResultSize(4096, 4096)
                            .setMinCropResultSize(2048, 2048)
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

    override fun logout() {
        if(dialogue == null)
        dialogue =
            AlertDialogue(context = requireContext(), negativeButton = "NO", positiveButton = "YES", message = "Would you like logout from app ?", alertClick = this)

        dialogue?.showdialogue()
    }

    override fun selectSport() {
        //findNavController().navigate(R.id.action_navigation_profile_to_selectInterestedSportsFragment)

    }

    override fun updateProfileInfo(updateProfileInfo: UpdateProfile) {
        val action = ProfileFragmentDirections.actionNavigationProfileToProfileEditFragment(updateProfileInfo)
        findNavController().navigate(action)
    }



   override fun updateAboutInProfile(updateAboutInProfile:About) {
//        val action = ProfileFragmentDirections.actionNavigationProfileToProfileEditFragment(updateAboutInProfile)
//        findNavController().navigate(action)
    }

    override fun profileLocationEdit() {
        findNavController().navigate(R.id.action_navigation_profile_to_instructLocationFetch2)
    }

    override fun alertClickable() {
        preference.clearAllData()
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.let {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            requireActivity().startActivity(it, ActivityOptions.makeSceneTransitionAnimation(requireActivity()).toBundle())
        }
    }

    override fun onMapReady(p0: GoogleMap?) {

    }



}

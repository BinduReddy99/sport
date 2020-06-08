package com.binduinfo.sports.ui.fragment.editprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.binduinfo.sports.R
import com.binduinfo.sports.base.BaseFragment
import com.binduinfo.sports.data.model.UpdateProfile
import com.binduinfo.sports.databinding.FragmentProfileBinding
import com.binduinfo.sports.databinding.ProfileEditFragmentBinding
import com.binduinfo.sports.ui.fragment.profile.ProfileFragmentDirections
import org.kodein.di.android.x.kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class ProfileEditFragment : BaseFragment(), KodeinAware, EditProfileHandler {
    override val kodein by kodein()

    private val factory: ProfileEditFactory by instance<ProfileEditFactory>()
    private lateinit var viewModel: ProfileEditViewModel
    private val args: ProfileEditFragmentArgs by navArgs()
    private lateinit var binding: ProfileEditFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, factory).get(ProfileEditViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.profile_edit_fragment, container, false)
        //binding = DataBindingUtil.inflate(inflater, R.layout.profile_descrip_lay_edt, container, false)
        //DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        binding.viewModel = viewModel
      //  binding.viewModel =
        viewModel.editProfileHandler = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val updateInfo = args.updateProfile
   //     val aboutInfo =args.about
        if(updateInfo != null) {
          //  binding.userInfo = updateInfo
            viewModel.setData(updateInfo)
       }
//        if(aboutInfo !=null){
//           viewModel.setAbout(aboutInfo)
//       }


    }

    override fun radio() {
        showToast("Checking")
    }


}
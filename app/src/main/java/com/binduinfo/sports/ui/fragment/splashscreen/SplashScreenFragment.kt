package com.binduinfo.sports.ui.fragment.splashscreen

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.binduinfo.sports.R
import com.binduinfo.sports.base.BaseFragment
import com.binduinfo.sports.data.preference.ADD_ADDRESS
import com.binduinfo.sports.data.preference.ADD_INTERESTED_SPORT
import com.binduinfo.sports.data.preference.IS_LOGGED_IN
import com.binduinfo.sports.data.preference.PreferenceProvider
import com.binduinfo.sports.ui.activity.HomeActivity
import com.example.mvvmsample.util.Coroutines
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class SplashScreenFragment : BaseFragment(), KodeinAware {
    override val kodein by kodein()
    private val preference: PreferenceProvider by instance<PreferenceProvider>()
    private var job: Job? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // hideKeyBoard()
        //hideKeyBoard(activity = requireActivity())

    }

    private suspend fun handleFragments() {
        delay(3000)
        if(preference.getsharedBoolean(IS_LOGGED_IN)){
            when {
                !preference.getsharedBoolean(
                    ADD_INTERESTED_SPORT) -> {
                    findNavController().navigate(R.id.action_splashScreenFragment_to_selectInterestedSports)
                }
                !preference.getsharedBoolean(
                    ADD_ADDRESS) -> {
                    findNavController().navigate(R.id.action_splashScreenFragment_to_instructLocationFetch)
                }
                else -> {
                    intent()
                }
            }
        }else{
            findNavController().navigate(R.id.action_splashScreenFragment_to_loginFragment)
        }
    }

    private fun intent(){
        val intent = Intent(requireContext(), HomeActivity::class.java)
        intent.let {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            requireActivity().startActivity(it)
        }
    }

    override fun onResume() {
        super.onResume()
        if (job == null)
         job = Coroutines.main {
            handleFragments()
        }

        //}
       // activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    override fun onPause() {
        super.onPause()
        if (job != null) {
            job?.cancel()
            job = null
        }
//        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
//        activity?.window?.decorView?.systemUiVisibility = 0

    }

}
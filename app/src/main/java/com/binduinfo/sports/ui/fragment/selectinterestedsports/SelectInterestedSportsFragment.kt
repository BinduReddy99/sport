package com.binduinfo.sports.ui.fragment.selectinterestedsports

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.binduinfo.sports.R
import com.binduinfo.sports.base.BaseFragment
import com.binduinfo.sports.data.model.BasicModel
import com.binduinfo.sports.data.preference.ADD_INTERESTED_SPORT
import com.binduinfo.sports.data.preference.PreferenceProvider
import com.binduinfo.sports.ui.activity.selectsport.SelectInterestedSportActivity
import com.binduinfo.sports.util.extension.hide
import com.binduinfo.sports.util.extension.show
import com.example.mvvmsample.util.Coroutines
import kotlinx.android.synthetic.main.select_interested_sports_fragment.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import timber.log.Timber

const val SELECT_SPORTS_ACTIVITY = 10001
const val SELECT_SPORT_CONSTANT = 10101

class SelectInterestedSportsFragment(override var selectSport: String?) : BaseFragment(), KodeinAware, SelectSportsInterface {

    override val kodein by kodein()
    private val preference: PreferenceProvider by instance<PreferenceProvider>()
    private val factory: SelectInterestedSportsViewModelFactory by instance<SelectInterestedSportsViewModelFactory>()
    private lateinit var viewModel: SelectInterestedSportsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =
            ViewModelProvider(this, factory).get(SelectInterestedSportsViewModel::class.java)
        viewModel.selectSportInterface = this
        return inflater.inflate(R.layout.select_interested_sports_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // recyclerViewInit()
        onUIHandle()
    }

    private fun onUIHandle() {
        select_spots_btn.setOnClickListener {
            val intent = Intent(requireActivity(), SelectInterestedSportActivity::class.java)
            intent.putExtra("SELECT_SPORTS_ACTIVITY", selectSport);
            requireActivity().startActivityForResult(intent, SELECT_SPORTS_ACTIVITY)
            Timber.d("infrag${selectSport.toString()}")

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_SPORTS_ACTIVITY) {
                //showToast("working")
                address_load_progress.show()
                select_spots_btn.hide()
                Coroutines.io {
                    viewModel.sendSelectedSportList()
                }

            } else {
                showToast("Try again")
            }
        }

    }

    override fun throwable(throwable: Throwable) {
        Coroutines.main {
            address_load_progress.hide()
            select_spots_btn.show()
            throwable.message?.let { showToast(it) }
        }

    }

    override fun sportSelectedUpdate(basicModel: BasicModel) {
        Coroutines.main {
            if (basicModel.success == 1) {
                preference.storeValue(ADD_INTERESTED_SPORT, true)
                findNavController().navigate(R.id.action_selectInterestedSports_to_instructLocationFetch)
                return@main
            } else {

            }
            address_load_progress.hide()
            select_spots_btn.show()
            showToast(basicModel.message)
        }
    }

}

package com.binduinfo.sports.ui.fragment.selectinterestedsports

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.binduinfo.sports.R
import com.binduinfo.sports.base.BaseFragment
import com.binduinfo.sports.data.db.entity.AppDataBase
import com.binduinfo.sports.data.preference.PreferenceProvider
import com.binduinfo.sports.data.repositores.SportsRepository
import com.binduinfo.sports.ui.fragment.selectinterestedsports.recyclerAdapter.SportsListAdapter
import com.binduinfo.sports.util.extension.hide
import com.binduinfo.sports.util.extension.show
import com.binduinfo.sports.util.network.model.Sport
import com.example.mvvmsample.util.Coroutines
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.miziontrix.kmo.data.network.api.mvvm.MyApi
import com.miziontrix.kmo.data.network.api.mvvm.NetworkConnectionInterceptor
import kotlinx.android.synthetic.main.select_interested_sports_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch


class SelectInterestedSportsFragment : BaseFragment(), RecyleListFetchListener,
    SportsListAdapter.ItemClickable {
    companion object {
        fun newInstance() =
            SelectInterestedSportsFragment()
    }
    private var sportType = ""
    private lateinit var factory: SelectInterestedSportsViewModelFactory
    private lateinit var db: AppDataBase
    private lateinit var viewModel: SelectInterestedSportsViewModel
    private lateinit var sportsAdapter: SportsListAdapter
    private lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var sportList: List<Sport>
    private lateinit var api: MyApi
    private lateinit var networkConnectionInterceptor: NetworkConnectionInterceptor
    private lateinit var preferenceProvider: PreferenceProvider
    private lateinit var sportsRepository: SportsRepository
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        preferenceProvider = PreferenceProvider(requireContext())
        networkConnectionInterceptor =
            NetworkConnectionInterceptor(requireContext(), preferenceProvider)
        api = MyApi(networkConnectionInterceptor)
        db = AppDataBase(context = requireContext())
        sportsRepository = SportsRepository(api, db)
        factory = SelectInterestedSportsViewModelFactory(sportsRepository)
        viewModel =
            ViewModelProvider(this, factory).get(SelectInterestedSportsViewModel::class.java)
        viewModel.recyleListFetchListener = this
        return inflater.inflate(R.layout.select_interested_sports_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // sports_list_progress_bar.show()
        recyclerViewInit()
        onUIHandle()
    }

    private fun recyclerViewInit() {
        val layoutManager = FlexboxLayoutManager(requireContext())
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = JustifyContent.CENTER
        sports_recycler_view.layoutManager =
            layoutManager
        if (!::sportsAdapter.isInitialized) {
            sportsAdapter = SportsListAdapter(requireContext(), this)
            sports_recycler_view.adapter = sportsAdapter
        }
    }

    private fun onUIHandle() {
        sports_list_progress_bar.show()
        CoroutineScope(Main).launch {
            viewModel.sports.await().observe(viewLifecycleOwner, Observer {
                if (sportType == "")
                sports(it)
            })
        }
        sports_search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                sportsAdapter.filter.filter(newText)
                return false
            }
        })
        select_sports_type.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.select_all -> {
                    sportType = ""
                    Coroutines.main {
                        viewModel.sportType().value.await().observe(viewLifecycleOwner, Observer {
                            Log.d("working123--======", it.toString())
                            if(sportType.isNullOrEmpty())
                            sports(it)
                        })
                    }
                }

                R.id.select_indoor -> {
                    sportType = "indoor"
                    Coroutines.main {
                        viewModel.sportType("indoor").value.await()
                            .observe(viewLifecycleOwner, Observer {
                                Log.d("working123-i======", it.toString())
                                if (sportType == "indoor")
                                sports(it)
                            })
                    }
                }

                R.id.select_outdoor -> {
                    sportType = "outdoor"
                    Coroutines.main {
                        viewModel.sportType("outdoor").value.await()
                            .observe(viewLifecycleOwner, Observer {
                               Log.d("working123-o======", it.toString())
                                if (sportType == "outdoor")
                                sports(it)
                            })
                    }
                }

            }
        }
    }

    override fun sports(sportsList: List<Sport>) {
        Log.d("working======", sportsList.toString())
        sportsAdapter.setSports(sportsList)
        sports_list_progress_bar.hide()
    }

    override fun throwable(throwable: Throwable) {
        sports_list_progress_bar.hide()
    }

    override fun filter(sportsList: List<Sport>) {
        sportsAdapter.updateList(sportsList)
    }

    override fun updateItem(_id: String, isSelect: Boolean) {
        sports_list_progress_bar.show()
        sports_search.setQuery("", false)
        sports_search.clearFocus()
        Coroutines.main {
            sports_search
            viewModel.updateItem(_id, isSelect)
        }

    }


}

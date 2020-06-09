package com.binduinfo.sports.ui.activity.selectsport

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.binduinfo.sports.R
import com.binduinfo.sports.base.BaseActivity
import com.binduinfo.sports.base.BaseFragment
import com.binduinfo.sports.data.model.BasicModel
import com.binduinfo.sports.data.preference.ADD_INTERESTED_SPORT
import com.binduinfo.sports.data.preference.PreferenceProvider
import com.binduinfo.sports.ui.activity.selectsport.recyclerAdapter.SportsListAdapter
import com.binduinfo.sports.util.extension.hide
import com.binduinfo.sports.util.extension.show
import com.binduinfo.sports.util.network.model.Sport
import com.example.mvvmsample.util.Coroutines
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import kotlinx.android.synthetic.main.select_interested_sports_fragment.*
import kotlinx.android.synthetic.main.sport_list_toolbar.*
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class SelectInterestedSportActivity() : BaseActivity(), RecyleListFetchListener,                        //BaseFragment()
SportsListAdapter.ItemClickable, KodeinAware,
    com.binduinfo.sports.ui.fragment.selectinterestedsports.recyclerAdapter.SportsListAdapter.ItemClickable {
    override val kodein by kodein()
    private val preference: PreferenceProvider by instance<PreferenceProvider>()
    private val factory: SelectInterestedSportsViewModelFactory by instance<SelectInterestedSportsViewModelFactory>()

//    companion object {
//        fun newInstance() =
//            SelectInterestedSportsFragment()
//    }
    private lateinit var job: CompletableJob
    private var sportType = ""
    private lateinit var viewModel: SelectInterestedSportsViewModel
    private lateinit var sportsAdapter: com.binduinfo.sports.ui.fragment.selectinterestedsports.recyclerAdapter.SportsListAdapter
    private lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var sportList: List<Sport>
    override fun uiHandle() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =
            ViewModelProvider(this, factory).get(SelectInterestedSportsViewModel::class.java)
        viewModel.recyleListFetchListener = this
        setContentView(R.layout.activity_select_interested_sport)
        recyclerViewInit()
        onUIHandle()
    }

    private fun recyclerViewInit() {
        val layoutManager = FlexboxLayoutManager(this)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = JustifyContent.CENTER
        sports_recycler_view.layoutManager =
            layoutManager
        if (!::sportsAdapter.isInitialized) {
            sportsAdapter =
                com.binduinfo.sports.ui.fragment.selectinterestedsports.recyclerAdapter.SportsListAdapter(
                    this,
                   this
                )
            sports_recycler_view.adapter = sportsAdapter
        }
    }

    private fun onUIHandle() {
        sports_list_layout.visibility = View.GONE
        selected_item.visibility = View.GONE
        sports_list_progress_bar.show()
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.sports.await().observe(this@SelectInterestedSportActivity, Observer {
                if (sportType == "")
                    sports(it)
            })
        }
        selected_item.setOnClickListener {
            sports_list_layout.visibility = View.GONE
            selected_item.visibility = View.GONE
            sports_list_progress_bar.visibility = View.VISIBLE
            Coroutines.io {
                viewModel.sendSelectedSportList()
            }
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
                        viewModel.sportType().value.await().observe(this, Observer {
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
                            .observe(this, Observer {
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
                            .observe(this, Observer {
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
        sports_list_layout.visibility = View.VISIBLE
        selected_item.visibility = View.VISIBLE
        sports_list_progress_bar.hide()
    }

    override fun throwable(throwable: Throwable) {
        sports_list_layout.visibility = View.VISIBLE
        selected_item.visibility = View.VISIBLE
        //sports_list_progress_bar.visibility = View.VISIBLE
        sports_list_progress_bar.hide()
        var message = ""
        throwable.let {
            message = it.message!!
        }
        Coroutines.main {
            showToast(message)
        }

    }

    override fun filter(sportsList: List<Sport>) {
        sportsAdapter.updateList(sportsList)
    }

    override fun sportSelectedUpdate(basicModel: BasicModel) {
        Coroutines.main {
            if(basicModel.success == 1){
                preference.storeValue(ADD_INTERESTED_SPORT, true)

               // findNavController().navigate(this,R.id.action_selectInterestedSports_to_instructLocationFetch)
                return@main
            }else{

            }
            showToast(basicModel.message)
            sports_list_layout.visibility = View.VISIBLE
            selected_item.visibility = View.VISIBLE
            sports_list_progress_bar.hide()
        }

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


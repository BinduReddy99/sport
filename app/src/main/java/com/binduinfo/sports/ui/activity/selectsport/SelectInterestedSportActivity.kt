package com.binduinfo.sports.ui.activity.selectsport

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.binduinfo.sports.R
import com.binduinfo.sports.base.BaseActivity
import com.binduinfo.sports.data.preference.PreferenceProvider
import com.binduinfo.sports.ui.activity.selectsport.recyclerAdapter.SportsListAdapter
import com.binduinfo.sports.util.Constant
import com.binduinfo.sports.util.Constant.Companion.SELECT_SPORTS_KEY
import com.binduinfo.sports.util.extension.hide
import com.binduinfo.sports.util.extension.show
import com.binduinfo.sports.util.network.model.Sport
import com.example.mvvmsample.util.Coroutines
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import kotlinx.android.synthetic.main.activity_select_interested_sport.*
import kotlinx.android.synthetic.main.sport_list_toolbar.*
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import timber.log.Timber

const val SELECT_SPORT_CONSTANT = 10101

class SelectInterestedSportActivity() : BaseActivity(), RecyleListFetchListener,
    SportsListAdapter.ItemClickable, KodeinAware {
    override val kodein by kodein()
    private val preference: PreferenceProvider by instance<PreferenceProvider>()
    private val factory: SelectInterestedSportsViewModelFactoryActivity by instance<SelectInterestedSportsViewModelFactoryActivity>()
    private lateinit var job: CompletableJob
    private var sportType = ""
    private lateinit var viewModel: SelectInterestedSportsViewModelActivity
    private lateinit var sportsAdapter: SportsListAdapter
    private lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var sportList: List<Sport>
    private lateinit var selectSport: String
    private var temp: Boolean = false

    override fun uiHandle() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideToolbar()
        setContentView(R.layout.activity_select_interested_sport)
        if (!::selectSport.isInitialized) {
            selectSport = intent.getStringExtra(SELECT_SPORTS_KEY)
            //  requestSport = intent.getStringExtra(SELECT_SPORTS_KEY)
            Timber.d("=========== select==== ${selectSport}")
            //  Timber.d("=========== request==== ${requestSport}")
        }

        viewModel =
            ViewModelProvider(
                this,
                factory
            ).get(SelectInterestedSportsViewModelActivity::class.java)
        viewModel.recyleListFetchListener = this
        recyclerViewInit()


        onUIHandle()
    }

    private fun recyclerViewInit() {
        val layoutManager = FlexboxLayoutManager(this)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = JustifyContent.CENTER
        sports_recycler_view.layoutManager = layoutManager
        if (!::sportsAdapter.isInitialized) {
            sportsAdapter =
                SportsListAdapter(
                    this,
                    this
                    // private val sel1 =sel1

                )
            sports_recycler_view.adapter = sportsAdapter
        }
    }

    //Lateint value is not recognized as the internal command Adapter is not get initialized
    private fun onUIHandle() {
        sports_list_layout.visibility = View.GONE
        selected_item.visibility = View.GONE
        sports_list_progress_bar.show()
        CoroutineScope(Dispatchers.Main).launch {

            if (selectSport == Constant.SELECT_SPORTS) {
                viewModel.sports.await().observe(this@SelectInterestedSportActivity, Observer {
                    if (sportType == "")
                        sports(it)
                })
            } else if (selectSport == Constant.REQUEST_SPORTS) {
                viewModel.reqSportSelectList.await()
                    .observe(this@SelectInterestedSportActivity, Observer {
                        if (sportType == "")
                            sports(it)
                    })
            }
        }
        selected_item.setOnClickListener {
            sports_list_layout.visibility = View.GONE
            selected_item.visibility = View.GONE
            sports_list_progress_bar.visibility = View.VISIBLE
            Coroutines.io {
                viewModel.sendSelectedSportList()
            }
        }
        back_press.setOnClickListener {
            onBackPressed()
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
                            if (sportType.isNullOrEmpty())
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
        sportsAdapter.setSports(sportsList)
        sports_list_layout.visibility = View.VISIBLE
        selected_item.visibility = View.VISIBLE
        sports_list_progress_bar.hide()
    }

    override fun throwable(throwable: Throwable) {

        var message = ""
        throwable.let {
            message = it.message!!
        }
        Coroutines.main {
            sports_list_layout.visibility = View.VISIBLE
            selected_item.visibility = View.VISIBLE
            sports_list_progress_bar.hide()
            showToast(message)
        }

    }

    override fun filter(sportsList: List<Sport>) {
        sportsAdapter.updateList(sportsList)
    }

    override fun sportSelectedUpdate() {
        Coroutines.main {

            val intent = Intent()
            // intent.putExtra("SELECT_SPORT_CONSTANT", selectSport)
            setResult(Activity.RESULT_OK, intent)
            finish()

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


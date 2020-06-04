package com.binduinfo.sports.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.binduinfo.sports.R
import com.binduinfo.sports.base.BaseActivity
import com.binduinfo.sports.util.extension.hide
import com.binduinfo.sports.util.extension.show
import timber.log.Timber

class HomeActivity : BaseActivity() {
    override fun uiHandle() {

    }
    private lateinit var navController:NavController
    private  var fragment: Fragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideToolbar()
        setContentView(R.layout.activity_home)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        if(!::navController.isInitialized)
        navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        handleBottomNavigation(navView)

    }

    private fun handleBottomNavigation(navView: BottomNavigationView) {
        if(::navController.isInitialized){
            navController.addOnDestinationChangedListener{ controller, destination, arguments ->
                Timber.d("ccc label id=== $arguments ======== ${destination.id}")
                when(destination.id){
                    R.id.navigation_home ->{
                        if (navView.visibility == View.GONE) {
                            navView.show()
                            showAnim(navView)
                        }
                    }
                    R.id.navigation_dashboard -> {
                        if (navView.visibility == View.GONE) {
                            navView.show()
                            showAnim(navView)
                        }
                    }

                    R.id.navigation_profile -> {
                        if (navView.visibility == View.GONE) {
                            navView.show()
                            showAnim(navView)
                        }
                    }
                    else -> {
                        if (navView.visibility == View.VISIBLE) {
                            navView.hide()
                            hideAnim(navView)
                        }
                    }
                }
//                Timber.d("ccc class=== $arguments ======== ${destination.javaClass}")
//                Timber.d("ccc === controller ======== $controller")
            }
        }
    }

    private fun hideAnim(navView: BottomNavigationView) {
        navView.startAnimation(
            AnimationUtils.loadAnimation(
                this,
                R.anim.slide_down
            )
        )
        navView.visibility = View.GONE
    }

    private fun showAnim(navView: BottomNavigationView){
        navView.startAnimation(
            AnimationUtils.loadAnimation(
                this,
                R.anim.slide_up
            )
        )
        navView.visibility = View.VISIBLE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(fragment == null)
         fragment = this.supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
                ?.childFragmentManager?.fragments?.get(0)

        fragment?.onActivityResult(requestCode, resultCode, data)

    }

}

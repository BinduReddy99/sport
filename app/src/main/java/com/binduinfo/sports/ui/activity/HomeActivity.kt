package com.binduinfo.sports.ui.activity

import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.binduinfo.sports.R
import com.binduinfo.sports.base.BaseActivity

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
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(fragment == null)
         fragment = this.supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
                ?.childFragmentManager?.fragments?.get(0)

        fragment?.onActivityResult(requestCode, resultCode, data)

    }

}

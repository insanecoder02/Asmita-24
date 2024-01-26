package com.example.xenon.Activity

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.xenon.Fragment.AboutUs
import com.example.xenon.Fragment.Gallery
import com.example.xenon.Fragment.Home
import com.example.xenon.Fragment.LiveScore
import com.example.xenon.Fragment.Developer
import com.example.xenon.Fragment.Gallery2
import com.example.xenon.Fragment.Leaderboard_Fragment
import com.example.xenon.Fragment.Notification
import com.example.xenon.Fragment.Sponsors
import com.example.xenon.Fragment.Team
import com.example.xenon.Fragment.participating_iiits
import com.example.xenon.R
import com.example.xenon.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class Main : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        // Set status bar icons to dark
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val decor = window.decorView
            decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }


        window.statusBarColor = 0xFF000000.toInt()
        binding.navView.setNavigationItemSelectedListener(this)
        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.open_nav,
            R.string.close_nav
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.notify.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, Notification()).commit()
        }
        if (savedInstanceState == null) {
            //Toast.makeText(this, "opened", Toast.LENGTH_SHORT).show()
            setToolbarAndStatusBarColor(R.color.yellow,R.color.yellow)
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, Home()).commit()
            binding.navView.setCheckedItem(R.id.nav_home)
        }

        val openFragment = intent.getStringExtra("open")
        if (openFragment != null) {
            // Open the appropriate fragment based on the extra data
            when (openFragment) {
                "dev" -> {
                    val notificationContent = intent.getStringExtra("NotificationContent")
                    if (notificationContent != null && notificationContent.contains("DeveloperFragment")) {
                        loadFr(Developer())
                    }
                }
                "gal"->{
                    loadFr(Gallery())
                }
                "team"->{
                    loadFr(Team())
                }
                "abt"->{
                    loadFr(AboutUs())
                }
            }
        }
    }

    private fun loadFr(fragment:Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.nav_home ->{
                setToolbarAndStatusBarColor(R.color.yellow,R.color.yellow)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, Home()).commit()
            }

            R.id.nav_team -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, Team()).commit()

            R.id.nav_sponsor -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, Sponsors()).commit()

            R.id.nav_gallery -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, Gallery2()).commit()

            R.id.nav_developer -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, Developer()).commit()

            R.id.nav_contact -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, AboutUs()).commit()

            R.id.nav_score -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, LiveScore()).commit()

            R.id.nav_web -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, Leaderboard_Fragment()).commit()

            R.id.nav_iiits -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, participating_iiits()).commit()
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setToolbarAndStatusBarColor(toolbarColorResId: Int, statusBarColorResId: Int) {
        binding.toolbar.setBackgroundColor(ContextCompat.getColor(this, toolbarColorResId))
        window.statusBarColor = ContextCompat.getColor(this, statusBarColorResId)
    }

    private fun resetToolbarAndStatusBarColor() {
        setToolbarAndStatusBarColor(R.color.black, R.color.black)
    }



}
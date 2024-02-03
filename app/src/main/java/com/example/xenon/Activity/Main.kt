package com.example.xenon.Activity

import android.content.DialogInterface
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.xenon.Fragment.AboutUs
import com.example.xenon.Fragment.Gallery
import com.example.xenon.Fragment.Home
import com.example.xenon.Fragment.Developer
import com.example.xenon.Fragment.Gallery2
import com.example.xenon.Fragment.LeaderBoard
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

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            val decor = window.decorView
//            decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//        }

        window.statusBarColor = 0xFFE9BE3E.toInt()
        binding.navView.setNavigationItemSelectedListener(this)
        val toggle = ActionBarDrawerToggle(
            this, binding.drawerLayout, binding.toolbar, R.string.open_nav, R.string.close_nav
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.notify.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, Notification()).commit()
        }
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, Home())
                .commit()
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

                "gal" -> {
                    loadFr(Gallery())
                }

                "team" -> {
                    loadFr(Team())
                }

                "abt" -> {
                    loadFr(AboutUs())
                }
            }
        }
    }

    private fun loadFr(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        when (item.itemId) {
            R.id.nav_home -> {
                if (currentFragment !is Home) {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, Home())
                        .commit()
                }
            }
            R.id.nav_team -> {
                if (currentFragment !is Team) {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, Team()).commit()
                }
            }
            R.id.nav_sponsor -> {
                if (currentFragment !is Sponsors) {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, Sponsors()).commit()
                }
            }
            R.id.nav_gallery -> {
                if (currentFragment !is Gallery2) {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, Gallery2()).commit()
                }
            }
            R.id.nav_developer -> {
                if (currentFragment !is Developer) {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, Developer()).commit()
                }
            }
            R.id.nav_contact -> {
                if (currentFragment !is AboutUs) {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, AboutUs()).commit()
                }
            }
            R.id.nav_iiits -> {
                if (currentFragment !is participating_iiits) {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, participating_iiits()).commit()
                }
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (fragment !is Home) {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, Home()).commit()
            binding.navView.setCheckedItem(R.id.nav_home)
        } else if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes") { _, _ -> finish() }
                .setNegativeButton("No", null)
                .show()
        }
    }


//    private fun setToolbarAndStatusBarColor(toolbarColorResId: Int, statusBarColorResId: Int) {
//        binding.toolbar.setBackgroundColor(ContextCompat.getColor(this, toolbarColorResId))
//        window.statusBarColor = ContextCompat.getColor(this, statusBarColorResId)
//    }

//    private fun resetToolbarAndStatusBarColor() {
//        setToolbarAndStatusBarColor(R.color.black, R.color.black)
//    }
}
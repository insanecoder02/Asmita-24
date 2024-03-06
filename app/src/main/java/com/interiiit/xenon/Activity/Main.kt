package com.interiiit.xenon.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import com.interiiit.xenon.Fragment.Home
import com.interiiit.xenon.Fragment.Developer
import com.interiiit.xenon.Fragment.Gallery2
import com.interiiit.xenon.Fragment.Sponsors
import com.interiiit.xenon.Fragment.Team
import com.interiiit.xenon.Fragment.participating_iiits
import com.interiiit.xenon.R
import com.interiiit.xenon.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.messaging.FirebaseMessaging
import com.interiiit.xenon.Fragment.Event
import com.interiiit.xenon.Fragment.Fixture_Sport_Wise
import com.interiiit.xenon.Fragment.LeaderBoard
import com.interiiit.xenon.Fragment.Result_Sport

class Main : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = 0xFF000000.toInt()
        binding.navView.setNavigationItemSelectedListener(this)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, Home())
                .commit()
            binding.navView.setCheckedItem(R.id.nav_home)
        }
        FirebaseMessaging.getInstance().subscribeToTopic("all")
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        when (item.itemId) {
            R.id.nav_home -> {
                if (currentFragment !is Home) {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, Home())
                        .commit()
                    window.statusBarColor = 0xFF000000.toInt()
                }
            }
            R.id.nav_team -> {
                if (currentFragment !is Team) {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, Team()).commit()
                    window.statusBarColor = 0xFF000000.toInt()
                }
            }
            R.id.nav_sponsor -> {
                if (currentFragment !is Sponsors) {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, Sponsors()).commit()
                    window.statusBarColor = 0xFF000000.toInt()
                }
            }
            R.id.nav_gallery -> {
                if (currentFragment !is Gallery2) {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, Gallery2()).commit()
                    window.statusBarColor = 0xFF000000.toInt()
                }
            }
            R.id.nav_developer -> {
                if (currentFragment !is Developer) {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, Developer()).commit()
                    window.statusBarColor = 0xFF000000.toInt()

                }
            }
            R.id.nav_iiits -> {
                if (currentFragment !is participating_iiits) {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, participating_iiits()).commit()
                    window.statusBarColor = 0xFF000000.toInt()
                }
            }
            R.id.nav_lead -> {
                if (currentFragment !is LeaderBoard) {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, LeaderBoard()).commit()
                    window.statusBarColor = 0xFF000000.toInt()
                }
            }
            R.id.nav_fix -> {
                if (currentFragment !is Fixture_Sport_Wise) {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, Fixture_Sport_Wise()).commit()
                    window.statusBarColor = 0xFF000000.toInt()
                }
            }
            R.id.nav_res -> {
                if (currentFragment !is Result_Sport) {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, Result_Sport()).commit()
                    window.statusBarColor = 0xFF000000.toInt()
                }
            }
            R.id.nav_eve -> {
                if (currentFragment !is Event) {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, Event()).commit()
                    window.statusBarColor = 0xFF000000.toInt()
                }
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    fun openDrawer() {
        binding.drawerLayout.openDrawer(GravityCompat.START)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            when (fragment) {
                is Team, is Sponsors, is Gallery2, is Developer, is participating_iiits, is Event, is Result_Sport, is Fixture_Sport_Wise, is LeaderBoard -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, Home())
                        .commit()
                    window.statusBarColor = 0xFF000000.toInt()
                }
                is Home -> {
                    AlertDialog.Builder(this)
                        .setMessage("Are you sure you want to exit?")
                        .setCancelable(false)
                        .setPositiveButton("Yes") { _, _ -> finish() }
                        .setNegativeButton("No", null)
                        .show()
                }
                else -> {
                    supportFragmentManager.popBackStack()
                }
            }
        }
    }
}
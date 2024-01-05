package com.example.xenon.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.example.xenon.Fragment.AboutUs
import com.example.xenon.Fragment.Events
import com.example.xenon.Fragment.Gallery
import com.example.xenon.Fragment.Home
import com.example.xenon.Fragment.LiveScore
import com.example.xenon.Fragment.Register
import com.example.xenon.Fragment.Sponsors
import com.example.xenon.Fragment.Team
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
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, Home()).commit()
            binding.navView.setCheckedItem(R.id.nav_home)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, Home()).commit()

            R.id.nav_event -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, Events()).commit()

            R.id.nav_team -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, Team()).commit()

            R.id.nav_sponsor -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, Sponsors()).commit()

            R.id.nav_gallery -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, Gallery()).commit()

            R.id.nav_register -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, Register()).commit()

            R.id.nav_contact -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, AboutUs()).commit()

            R.id.nav_score -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, LiveScore()).commit()
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
}
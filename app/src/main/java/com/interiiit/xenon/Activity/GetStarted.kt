package com.interiiit.xenon.Activity

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.interiiit.xenon.databinding.ActivityGetStrtedBinding

class GetStarted : AppCompatActivity() {
    private lateinit var binding: ActivityGetStrtedBinding
    var permissionGranted = false
    val notificationPermissionCode = 100
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGetStrtedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkPermissions()
        window.statusBarColor = 0xFF000000.toInt()

        binding.getStartedBut.setOnClickListener {
           startActivity(Intent(this,Main::class.java))
            finish()
        }
    }
    private fun checkPermissions() {
        val notification = ContextCompat.checkSelfPermission(
            this, android.Manifest.permission.POST_NOTIFICATIONS
        )
        if (notification == PackageManager.PERMISSION_GRANTED) {
            permissionGranted = true
        } else {
            permissionGranted = false
            makeRequest()
        }
    }

    private fun makeRequest() {
        val notification = android.Manifest.permission.POST_NOTIFICATIONS
        ActivityCompat.requestPermissions(this, arrayOf(notification), notificationPermissionCode)
    }
}
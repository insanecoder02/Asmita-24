package com.example.xenon.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.xenon.databinding.ActivitySplashBinding

class Splash : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = 0xFF000000.toInt()

        sharedPreferences = getPreferences(Context.MODE_PRIVATE)

        // Check if the app is opened for the first time
        if (isFirstTime()) {
            // If it's the first time, start the GetStarted activity
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this, GetStarted::class.java))
                finish()
            }, 3000)
        } else {
            // If it's not the first time, start the main activity or any other activity
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this, Main::class.java))
                finish()
            }, 3000)
        }
    }

    private fun isFirstTime(): Boolean {
        // Retrieve the value of the "firstTime" key from SharedPreferences
        val firstTime = sharedPreferences.getBoolean("firstTime", true)

        // Update the "firstTime" key to false, as the app has been opened now
        sharedPreferences.edit().putBoolean("firstTime", false).apply()

        // Return the original value of "firstTime"
        return firstTime
    }
}

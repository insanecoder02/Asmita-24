package com.example.xenon.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.xenon.databinding.ActivityGetStrtedBinding
import com.example.xenon.databinding.ActivityMainBinding

class GetStarted : AppCompatActivity() {
    private lateinit var binding: ActivityGetStrtedBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGetStrtedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = 0xFF000000.toInt()

        binding.getStartedBut.setOnClickListener {
           startActivity(Intent(this,Main::class.java))
            finish()
        }
    }
}
package com.example.xenon.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.xenon.R
import com.example.xenon.databinding.ActivityHomeBinding

class Home : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}
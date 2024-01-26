package com.example.xenon.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.example.xenon.DataClass.AboutUs
import com.example.xenon.DataClass.users
import com.example.xenon.R
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore

class Leaderboard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)
    }

}
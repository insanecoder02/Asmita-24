package com.example.xenon.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.xenon.R
import com.example.xenon.databinding.FragmentWebViewBinding

class WebView : Fragment() {
    private lateinit var binding: FragmentWebViewBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWebViewBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val imageUrl =
            "https://firebasestorage.googleapis.com/v0/b/argon-c1d67.appspot.com/o/WhatsApp%20Image%202024-01-26%20at%2016.35.30_9f24253d.jpg?alt=media&token=d0ef5c3d-0768-4e5e-94e3-8159d5f16400"

        // Load image using Glide
        Glide.with(this)
            .load(imageUrl)
            .placeholder(R.drawable.ic_launcher_background) // Placeholder image while loading
            .error(R.drawable.ic_launcher_background) // Image to show in case of error
            .into(binding.webView)

    }
}
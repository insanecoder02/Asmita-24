package com.example.xenon.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        val htmlContent = "<p>&nbsp;</p>\n" +
                "<table style=\"border-collapse: collapse; width: 100%;\" border=\"1\">" +
                "<colgroup><col style=\"width: 40%;\"><col style=\"width: 20%;\"><col style=\"width: 20%;\"><col style=\"width: 20%;\"></colgroup>" +
                "<tbody><tr><td>College Name</td><td>Total</td><td>Win</td><td>Lose</td></tr>" +
                "<tr><td>IIITA</td><td>5</td><td><p>3</p></td><td><p>2</p></td></tr>" +
                "<tr><td>&nbsp;</td><td>&nbsp;</td><td><p>badkjfnsd</p></td></tr></tbody></table>"

        // Load HTML content into the WebView
        binding.webView.loadData(htmlContent, "text/html", "utf-8")
    }
}
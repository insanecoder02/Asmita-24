package com.interiiit.xenon.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.fragment.app.Fragment
import com.interiiit.xenon.R

class DayClassFragment: Fragment() {
    private var htmlString: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_fixture, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val webView: WebView = view.findViewById(R.id.web)
        webView.settings.javaScriptEnabled = true
        htmlString?.let { webView.loadData(it, "text/html", "UTF-8") }
    }

    companion object {
        fun newInstance(htmlString: String): DayClassFragment {
            val fragment = DayClassFragment()
            val args = Bundle()
            args.putString("htmlString", htmlString)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            htmlString = it.getString("htmlString")
        }
    }
}
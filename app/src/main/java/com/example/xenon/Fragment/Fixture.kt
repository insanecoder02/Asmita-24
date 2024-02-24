package com.example.xenon.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.xenon.databinding.FragmentFixtureBinding

class Fixture : Fragment() {
    private lateinit var binding: FragmentFixtureBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentFixtureBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val name = arguments?.getString("name")
        binding.text1.text = name
        val htmlContent = """
        <table border ='1' cellpadding = '1' cellspacing = '1' style = 'width:500px'><tbody><tr><td style = 'text-align:center'><span style = 'color:#e74c3c'><strong>Team A</strong></span></td><td style = 'text-align:center'><span style = 'color:#e74c3c'><strong>Team B</strong></span></td><td style = 'text-align:center'><span style = 'color:#e74c3'><strong>Time</strong></span></td></tr><tr><td style = 'text-align:center'>IIITA</td><td style = 'text-align:center'>IIITB</td><td style = 'text-align:center'>09/03/2024, 3pm</td></tr><tr><td style = 'text-align:center'>IIITC</td><td style = 'text-align:center'>IIITD</td><td style = 'text-align:center'>09/03/2024, 5pm</td></tr><tr><td style = 'text-align:center'>IIITE</td><td style = 'text-align:center'>IIITF</td><td style = 'text-align:center'>10/03/2024, 9pm</td></tr><tr><td style = 'text-align:center'>IIITG</td><td style = 'text-align:center'>IIITH</td><td style = 'text-align:center'>09/03/2024, 10pm</td></tr></tbody</table><p>&nbsp;</p>
        """.trimIndent()

        binding.web.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
        binding.back.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

    }
}
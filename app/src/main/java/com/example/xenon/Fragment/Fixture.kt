package com.example.xenon.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.xenon.databinding.FragmentFixtureBinding

class Fixture : Fragment() {
    private lateinit var binding:FragmentFixtureBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFixtureBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val name = arguments?.getString("name")
        binding.text1.text = name
        val htmlContent = """
            <p>&nbsp;</p>
            <table style="border-collapse: collapse; width: 100%; color: white;" border="1">
                <colgroup>
                    <col style="width: 33.33%;">
                    <col style="width: 33.33%;">
                    <col style="width: 33.33%;">
                </colgroup>
                <tbody>
                    <tr>
                        <td colspan="3" style="text-align: center;"><strong>Match Details</strong></td>
                    </tr>
                    <tr>
                        <td><strong>Team A</strong></td>
                        <td><strong>Match Date</strong></td>
                        <td><strong>Match Time</strong></td>
                    </tr>
                    <tr>
                        <td>IOIIT</td>
                        <td>2024-02-20</td>
                        <td>10:00 AM</td>
                    </tr>
                    <tr>
                        <td>XYZ College</td>
                        <td>2024-02-21</td>
                        <td>3:30 PM</td>
                    </tr>
                    <tr>
                        <td>ABC University</td>
                        <td>2024-02-22</td>
                        <td>6:00 PM</td>
                    </tr>
                </tbody>
            </table>
        """.trimIndent()

        binding.web.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
        binding.back.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

    }
}
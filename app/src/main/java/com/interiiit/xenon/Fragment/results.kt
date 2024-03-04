package com.interiiit.xenon.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.interiiit.xenon.Adapter.ResultAdapter.ResultAdapter
import com.interiiit.xenon.DataClass.Score.MatchDetails
import com.interiiit.xenon.databinding.FragmentResultsBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.interiiit.xenon.Adapter.ResButAdapter
import com.interiiit.xenon.other.IIITSlogo

class results : Fragment() {
    private lateinit var binding:FragmentResultsBinding
    private lateinit var resultAdapter: ResultAdapter
    private var logo = IIITSlogo.logo
    private lateinit var dayList: MutableList<MatchDetails>
    private lateinit var resButAdapter: ResButAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResultsBinding.inflate(layoutInflater, container, false)
        requireActivity().window.statusBarColor = 0xFF000000.toInt()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val name = arguments?.getString("name")
        binding.text1.text = name
        val dayListJson = arguments?.getString("dayListJson")
        val type = object : TypeToken<List<MatchDetails>>() {}.type
        dayList = Gson().fromJson(dayListJson, type)

        dayList.forEachIndexed { index, matchDetails ->
            Log.d("Ma", matchDetails.toString())
        }

        resultAdapter = ResultAdapter(dayList, logo,Home(),false)
        binding.resultRv.adapter = resultAdapter
        binding.resultRv.layoutManager = LinearLayoutManager(requireContext())
        binding.back.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }



        resButAdapter = ResButAdapter(dayList,this)
        binding.hori.adapter = resButAdapter
        binding.hori.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)





//        binding.alli.setOnClickListener{
//            Toast.makeText(requireContext(), "hhhhhh", Toast.LENGTH_SHORT).show()
//            loadAllData()
//        }
    }

    fun onResClick(item : MatchDetails){
        val filteredList = dayList.filter { it.date == item.date }
        resultAdapter.updateData(filteredList)
        binding.resultRv.scrollToPosition(0)

    }

//    private fun loadAllData(){
//        // Simply update the RecyclerView with the original data list
//        resultAdapter.updateData(dayList)
//        Toast.makeText(requireContext(), "hello", Toast.LENGTH_SHORT).show()
//        binding.resultRv.scrollToPosition(0)
//    }
}
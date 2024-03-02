package com.interiiit.xenon.Fragment


import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.interiiit.xenon.R
import com.interiiit.xenon.databinding.FragmentDevelopersImageBinding

class DevelopersImage : Fragment() {
    private lateinit var binding: FragmentDevelopersImageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentDevelopersImageBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler(Looper.getMainLooper()).postDelayed({
            val nextFragment = Developer()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(
                R.id.fragment_container, nextFragment
            )
            transaction.addToBackStack(null)
            transaction.commit()
        },5000)
    }

}
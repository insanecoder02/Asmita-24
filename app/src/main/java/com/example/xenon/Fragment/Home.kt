package com.example.xenon.Fragment

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.xenon.Activity.Leaderboard
import com.example.xenon.R
import com.example.xenon.databinding.FragmentHomeBinding
import com.jackandphantom.carouselrecyclerview.CarouselRecyclerview


class Home : Fragment() {
    private lateinit var binding:FragmentHomeBinding

    private lateinit var countDownTimer: CountDownTimer
//    private lateinit var commingsoon: TextView
    private var isTimerRunning = false
    private val bringmeDateboy = "2024-03-09T00:00:00"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.liveMatch.layoutManager = com.jackandphantom.carouselrecyclerview.CarouselLayoutManager(
                true,
                true,
                0.5F,
                true,
                true,
                true,
                LinearLayoutManager.HORIZONTAL
            )
        (binding.liveMatch as CarouselRecyclerview).setInfinite(true)
        binding.upcomingMatches.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        binding.events.setOnClickListener {
            val fragment = Event()
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        binding.leaderboard.setOnClickListener {
            startActivity(Intent(requireContext(), Leaderboard::class.java))
        }
        //fetchSystemDateTime()
    }
   /* private fun fetchSystemDateTime() {
        try {
            val currentTimeMillis = System.currentTimeMillis()
            val targetDateString = bringmeDateboy
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val targetDate = sdf.parse(targetDateString)
            val timeDifferenceMillis = targetDate.time - currentTimeMillis

            startCountdown(timeDifferenceMillis)
        } catch (e: ParseException) {
            Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private fun startCountdown(timeInMillis: Long) {
        countDownTimer = object : CountDownTimer(timeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val days = TimeUnit.MILLISECONDS.toDays(millisUntilFinished)
                val hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished) % 24
                val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60
                val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60

                val countdownText = String.format(
                    "%02d:%02d:%02d:%02d", days, hours, minutes, seconds
                )
                updateUI(countdownText)
            }

            override fun onFinish() {
//                linearLayout.visibility = ViewGroup.GONE
//                dayLinearLayout.visibility = ViewGroup.GONE
                binding.comingSoon.text = "The Wait is Over..."
                binding.comingSoon.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f)
                binding.comingSoon.letterSpacing = 0f
                resetUI()
                isTimerRunning = false
            }
        }
        countDownTimer.start()
        isTimerRunning = true
    }*/
   /* private fun updateUI(countdownText: String) {
        val hourMinSec: List<String> = countdownText.split(":")
        binding.hours1.text = (hourMinSec[1].toInt() / 10).toString()
        binding.minutes1.text = (hourMinSec[2].toInt() / 10).toString()
        binding.days1.text = (hourMinSec[0].toInt() / 10).toString()
        binding.hours2.text = (hourMinSec[1].toInt() % 10).toString()
        binding.minutes2.text = (hourMinSec[2].toInt() % 10).toString()
        binding.days2.text = (hourMinSec[0].toInt() % 10).toString()
    }
    private fun resetUI() {
        binding.days1.text = "0"
        binding.hours1.text = "0"
        binding.minutes1.text = "0"
        binding.days2.text = "0"
        binding.hours2.text = "0"
        binding.minutes2.text = "0"
        isTimerRunning = false

    }*/
}
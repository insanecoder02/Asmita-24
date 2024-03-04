package com.interiiit.xenon.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.TranslateAnimation
import com.interiiit.xenon.databinding.ActivitySplashBinding

class Splash : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = 0xFF000000.toInt()
        sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        val middleElement = binding.middleElement
        val animationSetFadeIn = createTranslateAlphaAnimationSet(0f, 0f, 0f, 1f)
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            middleElement.startAnimation(animationSetFadeIn)
        }, 90)
        val commonAnimationListener = object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                if (isFirstTime()) {
                    startActivity(Intent(this@Splash, GetStarted::class.java))
                } else {
                    startActivity(Intent(this@Splash, Main::class.java))
                }
                finish()
            }
            override fun onAnimationRepeat(animation: Animation?) {}
        }
        animationSetFadeIn.setAnimationListener(commonAnimationListener)
    }
    private fun createTranslateAlphaAnimationSet(
        fromXDelta: Float,
        toXDelta: Float,
        fromYDelta: Float,
        toYDelta: Float
    ): AnimationSet {
        val animationSet = AnimationSet(true)
        val translateAnimation = TranslateAnimation(fromXDelta, toXDelta, fromYDelta, toYDelta)
        translateAnimation.duration = 2500
        val fadeInAnimation = AlphaAnimation(0f, 1f)
        fadeInAnimation.duration = 2500
        animationSet.addAnimation(translateAnimation)
        animationSet.addAnimation(fadeInAnimation)
        return animationSet
    }
    private fun isFirstTime(): Boolean {
        val firstTime = sharedPreferences.getBoolean("firstTime", true)
        sharedPreferences.edit().putBoolean("firstTime", false).apply()
        return firstTime
    }
}


package com.example.xenon.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationSet
import android.view.animation.TranslateAnimation
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.xenon.databinding.ActivitySplashBinding

class Splash : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = 0xFF000000.toInt()

        sharedPreferences = getPreferences(Context.MODE_PRIVATE)

        val imageViewLeftToRight = binding.imageViewLeftToRight
        val imageViewRightToLeft = binding.imageViewRightToLeft
        val middleElement = binding.middleElement

        val animationSetLeftToRight = createTranslateAlphaAnimationSet(-resources.displayMetrics.widthPixels.toFloat(), 0f, 0f, 0f)
        val animationSetRightToLeft = createTranslateAlphaAnimationSet(resources.displayMetrics.widthPixels.toFloat(), 0f, 0f, 0f)
        val animationSetFadeIn = createTranslateAlphaAnimationSet(0f, 0f, 0f, 1f)

        val handler = Handler(Looper.getMainLooper())

        handler.postDelayed({
            imageViewLeftToRight.startAnimation(animationSetLeftToRight)
            imageViewRightToLeft.startAnimation(animationSetRightToLeft)
            middleElement.startAnimation(animationSetFadeIn)
        }, 100) // Delayed to make sure all animations start together

        val commonAnimationListener = object : android.view.animation.Animation.AnimationListener {
            override fun onAnimationStart(animation: android.view.animation.Animation?) {}

            override fun onAnimationEnd(animation: android.view.animation.Animation?) {
                if (isFirstTime()) {
                    startActivity(Intent(this@Splash, GetStarted::class.java))
                } else {
                    startActivity(Intent(this@Splash, Main::class.java))
                }
                finish()
            }

            override fun onAnimationRepeat(animation: android.view.animation.Animation?) {}
        }

        animationSetLeftToRight.setAnimationListener(commonAnimationListener)
    }

    private fun createTranslateAlphaAnimationSet(
        fromXDelta: Float,
        toXDelta: Float,
        fromYDelta: Float,
        toYDelta: Float
    ): AnimationSet {
        val animationSet = AnimationSet(true)

        // Translate animation
        val translateAnimation = TranslateAnimation(fromXDelta, toXDelta, fromYDelta, toYDelta)
        translateAnimation.duration = 3000

        // Fade-in animation
        val fadeInAnimation = AlphaAnimation(0f, 1f)
        fadeInAnimation.duration = 3000

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

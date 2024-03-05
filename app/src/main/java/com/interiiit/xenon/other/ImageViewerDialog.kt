package com.interiiit.xenon.other

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.Window
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.interiiit.xenon.R

class ImageViewerDialog(context: Context, private val imageUrl: String) : Dialog(context) {
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.layout_image_view)
        val rootView = findViewById<RelativeLayout>(R.id.rootView)
        val imageView = findViewById<ImageView>(R.id.enlargedImageView)
        window?.setBackgroundDrawable(
            ContextCompat.getDrawable(
                context,
                android.R.color.transparent
            )
        )
        Glide.with(context)
            .load(imageUrl)
            .error(R.drawable.group)
            .fitCenter()
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .into(imageView)
        rootView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                dismiss()
                return@setOnTouchListener true
            }
            return@setOnTouchListener false
        }
    }
}
package com.interiiit.xenon.other

import android.os.Handler
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Timer
import java.util.TimerTask

class AutoScroll(private val recyclerView: RecyclerView) {
    private var timer: Timer? = null
    private var isAutoScrolling = false

    fun startAutoScroll() {
        if (!isAutoScrolling) {
            timer = Timer()
            timer?.schedule(object : TimerTask() {
                override fun run() {
                    val handler = Handler(recyclerView.context.mainLooper)
                    handler.post {
                        val layoutManager = recyclerView.layoutManager as? LinearLayoutManager
                        val itemCount = recyclerView.adapter?.itemCount ?: 0

                        if (itemCount > 0) {
                            val firstVisibleItemPosition = layoutManager?.findFirstVisibleItemPosition() ?: 0
                            val child = recyclerView.getChildAt(0)

                            if (child != null) {
                                val itemWidth = child.width
                                val currentPosition = firstVisibleItemPosition + 1

                                val scrollAmount = itemWidth

//                                val isAtEnd = currentPosition == itemCount

                                if (currentPosition==recyclerView.adapter?.itemCount) {
                                    recyclerView.scrollToPosition(0)
                                } else {
                                    recyclerView.smoothScrollBy(scrollAmount, 0)
                                }
                            }
                        }
                    }
                }
            }, 0, AUTO_SCROLL_INTERVAL_MS)
            isAutoScrolling = true
        }
    }

    fun stopAutoScroll() {
        timer?.cancel()
        isAutoScrolling = false
    }
    companion object {
        private const val AUTO_SCROLL_INTERVAL_MS = 6000L
    }
}

package com.example.xenon.other

import android.os.Handler
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class AutoScroll(private val recyclerView: RecyclerView) {
    private var timer: Timer? = null
    private var isAutoScrolling = false

    fun startAutoScroll(intervalMs: Long) {
        if (!isAutoScrolling) {
            timer = Timer()
            timer?.schedule(object : TimerTask() {
                override fun run() {
                    val handler = Handler(recyclerView.context.mainLooper)
                    handler.post {
                        val itemWidth = recyclerView.getChildAt(0)?.width ?: 0
                        val currentPosition = (recyclerView.layoutManager as? LinearLayoutManager)?.findFirstVisibleItemPosition() ?: 0

                        if (currentPosition == recyclerView.adapter?.itemCount?.minus(1)) {
                            // If at the last item, scroll back to the start
                            recyclerView.scrollToPosition(0)
                        } else {
                            recyclerView.smoothScrollBy(itemWidth, 0)
                        }
                    }
                }
            }, 0, intervalMs)
            isAutoScrolling = true
        }
    }

    fun stopAutoScroll() {
        timer?.cancel()
        isAutoScrolling = false
    }
}
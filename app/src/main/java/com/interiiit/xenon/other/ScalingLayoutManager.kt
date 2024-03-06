package com.interiiit.xenon.other

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

class ScalingLayoutManager(context: Context, orientation: Int, reverseLayout: Boolean) :
    LinearLayoutManager(context, orientation, reverseLayout) {

    override fun scrollHorizontallyBy(dx: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
        val scrolled = super.scrollHorizontallyBy(dx, recycler, state)
        applyTransformation()
        return scrolled
    }

    private fun applyTransformation() {
        val midpoint = width / 2.0f
        val scaleFactor = 1.0f // Initial scale factor for items
        val maxScaleFactor = 1.0f // Maximum scale factor for the item at the center

        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val childMid = (getDecoratedRight(child!!) + getDecoratedLeft(child)) / 2f
            val position = (childMid - midpoint) / midpoint // Calculate position relative to center
            val scaleFactorForItem = scaleFactor + (1 - abs(position)) * (maxScaleFactor - scaleFactor)
            child.scaleY = scaleFactorForItem
            child.scaleX = scaleFactorForItem
        }
    }
}

package com.lemust.ui.screens.main.map.adapter

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.ViewGroup


class HourlyLayoutManager(context: Context, orientation: Int, reverseLayout: Boolean, var r: RecyclerView) : LinearLayoutManager(context, orientation, reverseLayout) {
    private var scaleDownBy = 0.2f
    private var scaleDownDistance = 0.1f
    private var changeAlpha = true

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {

        super.onLayoutChildren(recycler, state)

    }


    override fun scrollHorizontallyBy(dx: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
        val orientation = orientation
        if (orientation == LinearLayoutManager.HORIZONTAL) {
            val scrolled = super.scrollHorizontallyBy(dx, recycler, state)
            scaleDownView(dx)
            return scrolled
        } else
            return 0
    }

    private fun scaleDownView(dx: Int) {
        val mid = width / 2.0f
        val unitScaleDownDist = scaleDownDistance * mid
        for (i in 0 until childCount) {


            val child = getChildAt(i)

            child.rotationY = 0f

            val childMid = (getDecoratedLeft(child) + getDecoratedRight(child)) / 2.0f
            val scale = 1.0f + -1 * scaleDownBy * Math.min(unitScaleDownDist, Math.abs(mid - childMid)) / unitScaleDownDist

            var rotate = ((mid - childMid) / mid) * 60
            child.scaleX = scale
            child.scaleY = scale
            if (childMid < mid) {
                child.rotationY = rotate
                child.alpha = childMid / mid
                Log.d("alpha_test", "" + i + " intex " + "alp: " + childMid / mid)


            } else {
                Log.d("alpha_test", "" + i + " intex " + "alp: " + childMid / mid)
                child.rotationY = rotate * -1
                var alpha = width/mid-(childMid / mid)
                 child.alpha = alpha


            }




        }


    }

    override fun getPaddingLeft(): Int {
        return (width / 2) - (width / 5) / 5
    }

    override fun getPaddingRight(): Int {
        return width / 2 - (width / 5) / 5
    }

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return setProperItemSize(super.generateDefaultLayoutParams())
    }

    override fun generateLayoutParams(lp: ViewGroup.LayoutParams?): RecyclerView.LayoutParams {
        return setProperItemSize(super.generateLayoutParams(lp))
    }

    override fun checkLayoutParams(lp: RecyclerView.LayoutParams?): Boolean {
        return super.checkLayoutParams(lp) && lp!!.width == getItemSize()
    }

    private fun setProperItemSize(layoutParams: RecyclerView.LayoutParams): RecyclerView.LayoutParams {
        val itemSize = getItemSize()
        if (orientation == LinearLayoutManager.HORIZONTAL) {
            layoutParams.width = itemSize
        } else {
            layoutParams.height = itemSize
        }
        return layoutParams
    }


    private fun getItemSize(): Int {
        val pageSize = if (orientation == LinearLayoutManager.HORIZONTAL) width else height
        return Math.round(pageSize.toFloat() / 5)
    }


}
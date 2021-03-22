package com.lemust.ui.screens.main.map.adapter

import android.support.v7.widget.RecyclerView
import android.view.MotionEvent

class RecyclerViewDisabler : RecyclerView.OnItemTouchListener {

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        return true
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {

    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

    }
}
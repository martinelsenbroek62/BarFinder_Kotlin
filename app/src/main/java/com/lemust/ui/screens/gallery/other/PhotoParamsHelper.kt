package com.lemust.ui.screens.gallery.other

import android.app.Activity
import android.util.DisplayMetrics
import android.view.ViewGroup
import android.widget.LinearLayout
import com.lemust.utils.Tools

class PhotoParamsHelper {
    var heightB = 0
    var heightM = 0
    var activity: Activity
    var paramsM: LinearLayout.LayoutParams? = null
    var paramsB: LinearLayout.LayoutParams? = null
    var paramsW: LinearLayout.LayoutParams? = null
    var paramsH: LinearLayout.LayoutParams? = null

    constructor(activity: Activity) {
        this.activity = activity
        initSize()
    }

    private fun initSize() {
        val displayMetrics = DisplayMetrics()

        activity!!.windowManager
                .defaultDisplay
                .getMetrics(displayMetrics)


        var height = displayMetrics.heightPixels / (displayMetrics.heightPixels / (displayMetrics.widthPixels / 1.9)).toInt()
        heightM = (height / 2.4).toInt()
        heightB = heightM + heightM / 4

        var margin = Tools.convertDpToPixel(4f, activity).toInt()

        paramsM = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, heightM)
        paramsM!!.setMargins(margin, margin, margin, margin)

        paramsH = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,height )
        paramsH!!.setMargins(margin, margin, margin, margin)

        paramsW = LinearLayout.LayoutParams(displayMetrics.widthPixels,ViewGroup.LayoutParams.MATCH_PARENT )
        paramsW!!.setMargins(margin, margin, margin, margin)

        paramsB = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, heightB)
        paramsB!!.setMargins(margin, margin, margin, margin)
    }
}

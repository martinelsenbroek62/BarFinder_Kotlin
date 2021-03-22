package com.lemust.utils

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.os.Build
import android.support.v7.app.AppCompatDelegate
import android.view.View
import android.view.WindowManager
import com.lemust.LeMustApp


object SystemUtils {
    internal var sDisplayWidth = -1
    internal var sDisplayHeight = -1


    val displayWidth: Int
        get() {
            if (sDisplayWidth == -1) {
                initDisplayDimensions()
            }
            return sDisplayWidth
        }

    @TargetApi(value = Build.VERSION_CODES.HONEYCOMB_MR2)
    private fun initDisplayDimensions() {
        val ctx = LeMustApp.instance
        val wm = ctx.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            val size = Point()
            display.getSize(size)
            sDisplayWidth = size.x
            sDisplayHeight = size.y
        } else {
            sDisplayWidth = display.width
            sDisplayHeight = display.height
        }
    }

    fun setTransparentForStatusBar(activity: Activity) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            Tools.setWindowFlag(activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
        }
        if (Build.VERSION.SDK_INT >= 19) {
            activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        if (Build.VERSION.SDK_INT >= 21) {
            Tools.setWindowFlag(activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            activity.window.statusBarColor = Color.TRANSPARENT
        }
    }





}

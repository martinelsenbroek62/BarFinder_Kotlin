package com.lemust.ui.base.views.base

import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import com.lemust.LeMustApp
import com.lemust.utils.Tools

object GraphicsTools {
    fun getWithText(number: Int, textSize: Int): Int {
        var bounds = Rect()
        var textPaint = Paint()
        textPaint!!.textSize = Tools.convertSpToPixel(textSize.toFloat(), LeMustApp.instance)
        textPaint.getTextBounds(number.toString(), 0, number.toString().length, bounds)
        return bounds.width()
    }

    fun getTextSizeBySizeView(){

    }
    fun getCustomTrumbDrawable(firstColor: Int, secondColor: Int): Drawable {
        val gd = GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                intArrayOf(firstColor, secondColor))
        gd.setSize(Tools.convertDpToPixel(16f, LeMustApp.instance).toInt(), Tools.convertDpToPixel(16f, LeMustApp.instance).toInt())
        gd.cornerRadius = 270f
        return gd
    }



}
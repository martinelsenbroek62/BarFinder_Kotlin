package com.example.dmitro.gameapp.engine.render.models

import android.graphics.Canvas
import android.graphics.Paint
import com.lemust.LeMustApp
import com.lemust.R
import com.lemust.ui.base.views.base.models.GameObject


class Line(var x1: Int, var y1: Int,var x2: Int,var  y2: Int,var width:Int) : GameObject( ) {

    private val paint = Paint()

    init {
        paint.color =LeMustApp.instance.resources.getColor(R.color.colorTextWhiteGrey)
        paint.style = Paint.Style.STROKE

    }


    override fun render(canvas: Canvas) {
        canvas.drawLine(x1.toFloat(), y1.toFloat(), x2.toFloat(), y2.toFloat(), paint)
    }

    override fun tick(objects: ArrayList<GameObject>) {

    }
}

package com.example.dmitro.gameapp.engine.render.models

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import com.lemust.ui.base.views.base.models.GameObject


class Rectangle(var x1: Int, var y1: Int, var x2: Int, var y2: Int) : GameObject() {

    private var paint = Paint()
    var color = Color.GREEN

    fun initColor(color: Int, alpha: Boolean) {

        this.color = color
        paint.color = color
        if (alpha)
            paint.alpha = 15
        else
            paint.alpha = 255



    }

    init {
        paint = Paint()
        paint.color = color
        paint.strokeWidth = 1F
        paint.flags = Paint.ANTI_ALIAS_FLAG
        paint.style = Paint.Style.FILL

    }


    override fun render(canvas: Canvas) {
        canvas.drawPath(roundedRect(x1.toFloat(), y1.toFloat(), x2.toFloat(), y2.toFloat(), 10F, 10F, true), paint)

    }

    override fun tick(objects: ArrayList<GameObject>) {

    }

    fun roundedRect(left: Float, top: Float, right: Float, bottom: Float, rx: Float, ry: Float, conformToOriginalPost: Boolean): Path {
        var rx = rx
        var ry = ry
        val path = Path()
        //path.fillType = Path.FillType.EVEN_ODD;

        if (rx < 0) rx = 0f
        if (ry < 0) ry = 0f
        val width = right - left
        val height = bottom - top
        if (rx > width / 2) rx = width / 2
        if (ry > height / 2) ry = height / 2
        val widthMinusCorners = width - 2 * rx
        val heightMinusCorners = height - 2 * ry

        path.moveTo(right, top + ry)
        path.arcTo(right - 2 * rx, top, right, top + 2 * ry, 0F, (-90).toFloat(), false) //top-right-corner
        path.rLineTo(-widthMinusCorners, 0F)
        path.arcTo(left, top, left + 2 * rx, top + 2 * ry, 270F, (-90).toFloat(), false)//top-left corner.
        path.rLineTo(0F, heightMinusCorners)
        if (conformToOriginalPost) {
            path.rLineTo(0F, ry)
            path.rLineTo(width, 0F)
            path.rLineTo(0F, -ry)
        } else {
            path.arcTo(left, bottom - 2 * ry, left + 2 * rx, bottom, 180F, (-90).toFloat(), false) //bottom-left corner
            path.rLineTo(widthMinusCorners, 0F)
            path.arcTo(right - 2 * rx, bottom - 2 * ry, right, bottom, 90F, (-90).toFloat(), false) //bottom-right corner
        }

        path.rLineTo(0F, -heightMinusCorners)

        path.close()//Given close, last lineto can be removed.
        return path
    }

}

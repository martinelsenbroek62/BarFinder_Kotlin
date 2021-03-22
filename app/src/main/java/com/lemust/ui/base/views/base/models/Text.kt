package com.example.dmitro.gameapp.engine.render.models

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.lemust.LeMustApp
import com.lemust.ui.base.views.base.models.GameObject
import com.lemust.utils.Tools



class Text(var x1: Int, var y1: Int, var text:String) : GameObject() {

    private val paint = Paint()

    init {
        paint.color = Color.WHITE
        paint.style = Paint.Style.FILL
        paint.textSize=Tools.convertSpToPixel(10F,LeMustApp.instance)

    }


    override fun render(canvas: Canvas) {
        canvas.drawText(text, x1.toFloat(), y1.toFloat(),paint)
    }

    override fun tick(objects: ArrayList<GameObject>) {

    }
}

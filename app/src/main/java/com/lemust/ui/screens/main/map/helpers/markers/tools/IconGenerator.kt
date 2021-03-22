package com.lemust.ui.screens.main.map.helpers.markers.tools

import android.graphics.*
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import com.lemust.LeMustApp
import com.lemust.utils.TextHelper
import com.lemust.utils.Tools
import org.apache.commons.lang.builder.HashCodeBuilder


object IconGenerator {
    var width = Tools.convertDpToPixel(95f, LeMustApp.instance).toInt()
    var height = Tools.convertDpToPixel(100f, LeMustApp.instance).toInt()

    fun getRoundedCroppedBitmap(icon: Int, size: Float, color: Int): Bitmap {
        var maxRadius = Tools.convertDpToPixel(40f, LeMustApp.instance).toInt() * size

        var hash = HashCodeBuilder().append(icon).append(size).append(color).toHashCode()

        if (IconGeneratorBuffer.map.containsKey(hash)) {
            return IconGeneratorBuffer.map[hash]!!
        } else {
            var markerIon = BitmapFactory.decodeResource(LeMustApp.instance.resources, icon)
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            canvas.drawColor(Color.TRANSPARENT)
            val paint = Paint()
            paint.style = Paint.Style.FILL
            paint.color = color
            paint.isAntiAlias = true
            var xCirclePosition = width / 2f
            var yCirclePosition = (markerIon.height.toFloat() + 1) - Tools.convertDpToPixel(4f, LeMustApp.instance)
            canvas.drawCircle(xCirclePosition, yCirclePosition, maxRadius, paint)
            var xIconPosition = width / 2 - markerIon.width / 2f
            var yIconPosition = 0f
            canvas.drawBitmap(markerIon, xIconPosition, yIconPosition, null)
            val paintText = Paint()
            paintText.style = Paint.Style.FILL
            paintText.color = Color.WHITE;
            paintText.textSize = Tools.convertSpToPixel(11f, LeMustApp.instance.baseContext);
            IconGeneratorBuffer.map.put(hash, bitmap)
            return bitmap
        }

//          drawCenter(canvas, paintText, name, markerIon.height, width)


    }

    private val r = Rect()

//    private fun drawCenter(canvas: Canvas, paint: Paint, text: String, y: Int, width: Int) {
//        canvas.getClipBounds(r)
//
//
//        val textPaint = TextPaint()
//        textPaint.color = Color.WHITE
//        textPaint.textSize = Tools.convertSpToPixel(11f, LeMustApp.instance.baseContext).toFloat()
//        val sl = StaticLayout(text, textPaint, width, Layout.Alignment.ALIGN_CENTER, 1f, 1f, true)
//
//        canvas.save()
//
//        val textHeight = TextHelper.getTextHeight(text, textPaint)
//        val numberOfTextLines = sl.lineCount
//        val textYCoordinate = y - numberOfTextLines * textHeight / 3
//
//        val textXCoordinate = 0
//
//        canvas.translate(textXCoordinate.toFloat(), textYCoordinate)
//
//        //draws static layout on canvas
//        sl.draw(canvas)
//        canvas.restore()
//
//
//    }

    fun drawCenter(bitmap: Bitmap, text: String): Bitmap {
        var bm = bitmap.copy(bitmap.getConfig(), true);
        var tf = Typeface.create(text, Typeface.BOLD);

        var paint = Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setTypeface(tf);
        paint.setTextAlign(Paint.Align.CENTER);

        var canvas = Canvas(bm);


        val textPaint = TextPaint()
        textPaint.color = Color.WHITE
        textPaint.textSize = Tools.convertSpToPixel(11f, LeMustApp.instance.baseContext).toFloat()
        val sl = StaticLayout(text, textPaint, bm.width, Layout.Alignment.ALIGN_CENTER, 1f, 1f, true)


        val textHeight = TextHelper.getTextHeight(text, textPaint)
        val numberOfTextLines = sl.lineCount
        val textYCoordinate = bitmap.height / 1.8 - numberOfTextLines * textHeight / 3

        val textXCoordinate = 0

        canvas.translate(textXCoordinate.toFloat(), textYCoordinate.toFloat())
        sl.draw(canvas)



        return bm;


    }


}
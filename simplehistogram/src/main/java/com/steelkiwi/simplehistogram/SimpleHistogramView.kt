package com.steelkiwi.simplehistogram

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.CornerPathEffect
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View


class SimpleHistogramView : View {

    private var itemsList: List<TimeData> = listOf()
    private val paintLines = Paint()
    private val paintColumns = Paint()
    private val paintText = Paint()

    companion object {
        const val DEFAULT_DIVIDERS_COUNT = 25
        const val DEFAULT_HOURS_COUNT = 24
        const val DEFAULT_STEP_COLUMN = 3
        const val DEFAULT_POSITION_Y = 0.0f
    }

    val DEFAULT_POSITION_X = convertDpToPixel(10f, context)

    val DEFAULT_TEXT_SIZE = convertDpToPixel(16f, context)


    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }


    private fun init(context: Context) {
        paintText.textSize = convertSpToPixel(12f, context)
        paintText.isAntiAlias = true

        paintText.color = context.resources.getColor(R.color.colorTextWhiteGrey)
        paintLines.isAntiAlias = true
        paintLines.isDither = true
        paintLines.alpha = 50
        paintLines.style = Paint.Style.STROKE
        paintLines.strokeJoin = Paint.Join.ROUND
        paintLines.strokeCap = Paint.Cap.ROUND



        paintColumns.isAntiAlias = true
        paintColumns.isDither = true
        paintColumns.alpha = 50
        paintColumns.setStrokeCap(Paint.Cap.ROUND);


    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        /**
         * Dividers drawing
         */
        canvas?.let { itCanvas ->
            var currentColumnIndex = 0
            var canvasHeight: Float = ((height) - (paddingTop + paddingBottom)).toFloat()
            var canvasWidth: Float = (width - (paddingEnd + paddingStart)).toFloat()
            var x = DEFAULT_POSITION_X
            while (currentColumnIndex < DEFAULT_DIVIDERS_COUNT) {
                if (currentColumnIndex.rem(DEFAULT_STEP_COLUMN) == 0) {
                    paintLines.strokeWidth = 5.0f
                    itCanvas.drawLine(x, DEFAULT_POSITION_Y, x, canvasHeight - DEFAULT_TEXT_SIZE, paintLines)
                    var time = calculateTime(currentColumnIndex).toString()
                    if (time.length == 1) {
                        time = "0$time"
                    }

                    itCanvas.drawText(time, x-paintText.measureText(time)/2, canvasHeight, paintText)
                } else {
                    paintLines.strokeWidth = 1.0f
                    itCanvas.drawLine(x, DEFAULT_POSITION_Y, x, canvasHeight - DEFAULT_TEXT_SIZE, paintLines)
                }
                x += canvasWidth / DEFAULT_HOURS_COUNT
                currentColumnIndex++
            }
        }

        /**
         *  Hours columns
         */
        canvas?.let { itCanvas ->
            var currentColumnIndex = 0
            var canvasHeight: Float = ((height) - (paddingTop + paddingBottom)).toFloat()
            var canvasWidth: Float = (width - (paddingEnd + paddingStart)).toFloat()
            var x = ((canvasWidth / DEFAULT_HOURS_COUNT) * 0.50f)+DEFAULT_POSITION_X
            while (currentColumnIndex < itemsList.size) {
                val timeData = itemsList[currentColumnIndex]
                paintColumns.color = timeData.color
                var heightWithText = canvasHeight - DEFAULT_TEXT_SIZE
                var scale = ((heightWithText / 100) * timeData.loadPercentage)
                if (timeData.loadPercentage > 1) scale -= convertDpToPixel(4f, context)
                var calculatedHeight = heightWithText - scale

                calculatedHeight = if (calculatedHeight > 5) calculatedHeight else 5.0f
                paintColumns.strokeWidth = (canvasWidth / DEFAULT_HOURS_COUNT) * 0.5f
                itCanvas.drawLine(x, calculatedHeight, x, heightWithText, paintColumns)
                x += canvasWidth / DEFAULT_HOURS_COUNT
                currentColumnIndex++
            }
        }
    }

    private fun calculateTime(currentColumnIndex: Int): Int =
            if (currentColumnIndex < 18) {
                currentColumnIndex + 6
            } else {
                currentColumnIndex - 18
            }


    fun setItems(items: List<TimeData>?) {
        this.itemsList = items ?: listOf()
        invalidate()
        requestLayout()
    }

    fun convertPixelsToDp(px: Float, context: Context): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, px, context.resources.displayMetrics)

    }

    fun convertDpToPixel(dp: Float, context: Context): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics)

    }

    fun convertSpToPixel(sp: Float, context: Context): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().displayMetrics)

    }

}
package com.example.dmitro.game

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import com.example.dmitro.gameapp.engine.render.models.Line
import com.example.dmitro.gameapp.engine.render.models.Rectangle
import com.example.dmitro.statistichistogram.base.render.base.HandlerObjects
import com.lemust.LeMustApp
import com.lemust.R
import com.lemust.utils.Tools

class MainView : View {
    private val handler = HandlerObjects()
    private var rectangleList = ArrayList<Rectangle>()
    var colors = ArrayList<Int>()
    var currentPercent = 0


    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    private fun initLevel(width: Float, height: Float) {
        rectangleList.clear()
        handler.clear()
        colors.add(LeMustApp.instance.resources.getColor(R.color.colorPinCircle10))
        colors.add(LeMustApp.instance.resources.getColor(R.color.colorPinCircle20))
        colors.add(LeMustApp.instance.resources.getColor(R.color.colorPinCircle30))
        colors.add(LeMustApp.instance.resources.getColor(R.color.colorPinCircle40))
        colors.add(LeMustApp.instance.resources.getColor(R.color.colorPinCircle50))
        colors.add(LeMustApp.instance.resources.getColor(R.color.colorPinCircle60))
        colors.add(LeMustApp.instance.resources.getColor(R.color.colorPinCircle70))
        colors.add(LeMustApp.instance.resources.getColor(R.color.colorPinCircle80))
        colors.add(LeMustApp.instance.resources.getColor(R.color.colorPinCircle90))
        colors.add(LeMustApp.instance.resources.getColor(R.color.colorPinCircle100))


//        var heightViewGraphic = height - height / 8
        var heightViewGraphic = height


        var startX = 0
        var countPart = 10
        var partX = (width / countPart)
        var partY = ((heightViewGraphic - heightViewGraphic / 10) / 10)
        var startY = partY
        var widthRectangle = partX - partX / 2
        var marginGraphicBottom = heightViewGraphic / 6

      //  var textNumber = 10

        handler.addObject(Line(startX, 0, startX, heightViewGraphic.toInt(), 1))
      //  var widthText = GraphicsTools.getWithText(textNumber, 10)
       // handler.addObject(Text(0, height.toInt(), "%"))
//        handler.addObject(Text((((startX + (partX / 2 - widthText / 2))).toInt()), height.toInt(), textNumber.toString()))
        addRectangle((((startX + (partX / 2 - widthRectangle / 2))).toInt()), startY.toInt(), widthRectangle.toInt(), marginGraphicBottom, heightViewGraphic)

        for (i in 1 until countPart) {
            startX = (startX + partX).toInt()
            startY = (startY + partY)
          //  textNumber += 10

            //widthText = GraphicsTools.getWithText(textNumber, 11)

            //handler.addObject(Text((((startX + (partX / 2 - widthText / 2))).toInt()), height.toInt(), textNumber.toString()))
            handler.addObject(Line(startX, 0, startX, heightViewGraphic.toInt(), 1))
            addRectangle((((startX + (partX / 2 - widthRectangle / 2))).toInt()), startY.toInt(), widthRectangle.toInt(), marginGraphicBottom, heightViewGraphic)
        }
        var rightLinePadding = Tools.convertDpToPixel(1f, context)
        handler.addObject(Line((startX + partX - rightLinePadding).toInt(), 0, (startX + partX - rightLinePadding).toInt(), heightViewGraphic.toInt(), 1))

        skipProgress()
        setProgress(currentPercent)
        handler.redraw()
    }


    private fun addRectangle(x: Int, heightRectangle: Int, width: Int, marginGraphicBottom: Float, height: Float) {
        var rec = Rectangle(x, (height - heightRectangle).toInt(), x + width, height.toInt())
        rectangleList.add(rec)
        handler.addObject(rec)

    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        handler!!.render(canvas!!)


    }

    constructor(context: Context) : super(context)


    fun initView() {
    }

    fun setColumnColors(colors: ArrayList<Int>) {
        colors.addAll(colors)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val widthPixels = View.MeasureSpec.getSize(widthMeasureSpec)
        val heightPixels = View.MeasureSpec.getSize(heightMeasureSpec)
        initLevel(widthPixels.toFloat(), (heightPixels).toFloat())


    }


    public fun setProgress(progress: Int) {
        currentPercent = progress

        skipProgress()


        if (rectangleList.isNotEmpty())
            when (progress) {
                in 0..0 -> {
                    skipProgress()
                   // rectangleList[0].initColor(colors[0], false)

                }
                in 1..10 -> {
                    rectangleList[0].initColor(colors[0], false)


                }
                in 11..20 -> {
                    rectangleList[1].initColor(colors[1], false)


                }
                in 21..30 -> {
                    rectangleList[2].initColor(colors[2], false)

                }
                in 31..40 -> {
                    rectangleList[3].initColor(colors[3], false)

                }
                in 41..50 -> {
                    rectangleList[4].initColor(colors[4], false)


                }
                in 51..60 -> {

                    rectangleList[5].initColor(colors[5], false)

                }
                in 61..70 -> {
                    rectangleList[6].initColor(colors[6], false)


                }
                in 71..80 -> {
                    rectangleList[7].initColor(colors[7], false)

                }
                in 81..90 -> {
                    rectangleList[8].initColor(colors[8], false)


                }
                in 91..100 -> {
                    rectangleList[9].initColor(colors[9], false)


                }
            }

        invalidate()
    }

    private fun skipProgress() {
        rectangleList.forEachIndexed { index, rectangle ->
            run {
                if (colors[index] != null)
                    rectangle.initColor(colors[index], true)
            }

        }
    }
}

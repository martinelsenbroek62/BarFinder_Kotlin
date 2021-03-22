package com.lemust.ui.screens.main.map.helpers.hours

import android.content.Context
import com.lemust.ui.screens.main.map.adapter.HourItem
import com.lemust.utils.TextHelper
import com.lemust.utils.TimeHelper
import com.lemust.utils.onBackgroundThread
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HoursHelper {
    var time: TimeScrollData? = null
    var startPopularTime = 0
    val timeThread = TimeChangeCheckThread()
    var callback: Callback? = null
    var isWatchingForHourChanges = true

    var currrentHourItem: HourItem? = null

    val WEEK = 6
    val HOURS = 23


    fun onDestroy() {
        if (timeThread != null) {
            timeThread.isRunning = false
            timeThread.onDestroy()
        }


    }

    val hours = ArrayList<HourItem>()


    fun updateLanguageForItems(context: Context) {

        if (hours.isNotEmpty()) {
            var index = -1;
            for (i in 0..WEEK) {
                var weekName = TimeHelper.getDayWeekNameSupport(i, context)
                for (j in 0..HOURS) {
                    index++
                    hours[index].day = weekName
                }

            }
        }
    }

    fun getTimewheelHours(context: Context): TimeScrollData {
        var currentTime = Calendar.getInstance()
        currentTime.set(Calendar.MINUTE, 0)
        var currentDate = SimpleDateFormat("HH:mm", Locale.US).format(currentTime.time)
        var currentPosition = 0
        var currentHourItem: HourItem? = null




        for (i in 0..WEEK) {
            var weekName = TimeHelper.getDayWeekNameSupport(i, context)
            var weekDay = Calendar.getInstance()
            weekDay.set(Calendar.HOUR_OF_DAY, i)

            for (j in 0..HOURS) {
                weekDay.set(Calendar.HOUR_OF_DAY, j)
                weekDay.set(Calendar.MINUTE, 0)
                val hour = SimpleDateFormat("HH:mm", Locale.US).format(weekDay.time)

                var hourItem = HourItem(hour, j, i, TextHelper.cap1stChar(weekName), false)


                var dayWeek = TimeHelper.getDayWeekSupport(currentTime.get(Calendar.DAY_OF_WEEK))
                if (dayWeek == i) {
                    if (currentDate == hour) {
                        currentHourItem = hourItem
                        currentPosition = i * HOURS + j + i
                        hourItem.isSelected = true

                    }

                }
                hours.add(hourItem)

            }
        }

        if (currentHourItem == null) {
            currentHourItem = hours.first()
        }


        return TimeScrollData(hours, currentPosition, currentPosition, currentHourItem!!)

    }


    class TimeScrollData(var hours: ArrayList<HourItem>, var currentHourPosition: Int, var currentTimePosition: Int, var hour: HourItem)

//    public fun startStateForTimewheel(callback: Callback? = null) {
//        this.callback = callback
//        var hours = HoursHelper().getTimewheelHours(LeMustApp.instance)
////        time = TimeScrollData(hours.hours, hours.currentHourPosition, hours.currentTimePosition)
//        if (callback != null) callback.update(time!!, false)
//
//
//    }


    fun setCurrentHourItem(item: HourItem) {
        this.currrentHourItem = item
    }

    //    public fun updateStateForTimewheel(callback: Callback? = null) {
//        this.callback = callback
//        var hours = HoursHelper().getTimewheelHours(LeMustApp.instance)
//
////        if(TimeHelper.isNowNight()){
////        time = TimeScrollData(hours.hours, time!!.currentHourPosition, hours.currentHourPosition)
////        }else{
////        time = TimeScrollData(hours.hours, hours!!.currentHourPosition)
//
//        // }
//
//        callback?.update(time!!, false)
//
//
//    }
//
    fun startWatchingForHourChanges() {
        Thread(timeThread).start()
        onBackgroundThread(timeThread.changeHourAction).subscribe {
            if (callback != null) {
                if (isWatchingForHourChanges) {
                    callback!!.update()
                }
            }


        }

    }

    fun pauseWatchingForHourChanges() {
        timeThread.isPause = true
    }

    fun resumeWatchingForHourChanges() {
        timeThread.isPause = false

    }

    interface Callback {
        fun update()
    }

//    public fun setCurrentPositionTimeWheel(hour: HourItem) {
//        startPopularTime = hour.hourInt
//        time!!.currentHourPosition = hour.hourInt - 6
//    }


}
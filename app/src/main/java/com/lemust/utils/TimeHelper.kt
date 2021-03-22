package com.lemust.utils

import android.content.Context
import com.lemust.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


object TimeHelper {
    //00:00-06:00
    fun isNowNight(): Boolean {
        var currentTime = SimpleDateFormat("HH", Locale.US).format(Calendar.getInstance().time).toInt()
        return currentTime in 0..5
    }


    fun getDayWeekName(position: Int, context: Context): String {
        var dayOfWeek = position + 1
        if (dayOfWeek >= 7) {
            dayOfWeek = 0
        }
        val c1 = Calendar.getInstance()
        c1.set(Calendar.DAY_OF_WEEK, dayOfWeek)
        var name = c1.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale(AppHelper.locale.getLanguage(context!!)))
        var dayName = TextHelper.cap1stChar(name)
        return dayName
    }

    fun getDayWeekNameSupport(position: Int, context: Context): String {
        var dayOfWeek = position + 2
        if (position >= 7) {
            dayOfWeek = 0
        }
        val c1 = Calendar.getInstance()
        c1.set(Calendar.DAY_OF_WEEK, dayOfWeek)
        var name = c1.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale(AppHelper.locale.getLanguage(context!!)))
        return TextHelper.cap1stChar(name)
    }

    fun getDayWeekSupport(position: Int): Int {
        var dayOfWeek = position - 2
        if (dayOfWeek < 0) {
            dayOfWeek = 6
        }
     return dayOfWeek
    }

    fun getWeekDayNames(context: Context): ArrayList<String> {

        var days = ArrayList<String>()
        for (i in 0..6) {
            days.add(getDayWeekName(i, context))
        }

        return days
    }

    fun getResourcesPluralWeekDays(context: Context): ArrayList<String> {
        val res = context.getResources()
        val planets = res.getStringArray(R.array.planets_array)
        var days = planets.toCollection(ArrayList<String>())
        return days
    }

    fun getCurrentDayOfWeek(): Int {
        val c = Calendar.getInstance()
        var day = c.get(Calendar.DAY_OF_WEEK)

        var yesterday = day

        if(yesterday<0){
            yesterday=5
        }
        return yesterday
    }
    fun getCurrentHour(): Int {
        val c = Calendar.getInstance()
        return c.get(Calendar.HOUR_OF_DAY)
    }

    fun getYesterdayDayOfWeek(): Int {
        val c = Calendar.getInstance()
        var day = c.get(Calendar.DAY_OF_WEEK)

        var yesterday = day - 1

        if(yesterday<0){
            yesterday=5
        }
        return yesterday
    }

}
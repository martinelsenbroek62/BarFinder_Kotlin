package com.lemust.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.TypedValue
import android.view.View
import com.lemust.LeMustApp
import com.lemust.R
import com.lemust.ui.screens.filters.base.adapter.type_place.NestedImageResourcesItem
import com.lemust.ui.screens.filters.base.barKey
import com.lemust.ui.screens.filters.base.karaokeKey
import com.lemust.ui.screens.filters.base.nightClubKey
import com.lemust.ui.screens.filters.base.restaurantKey
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.atomic.AtomicInteger


object Tools {


    public fun loadBitmapFromView(v: View, it: Bitmap): Bitmap {
        val paint = Paint()
        var margin = Tools.convertDpToPixel(0f, LeMustApp.instance).toInt()
        var newBitmap = Bitmap.createBitmap(it, margin, 0, it.width - margin, v.height)
        var c = Canvas(newBitmap);

        c.drawColor(Color.TRANSPARENT);
        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        v.draw(c);
        return newBitmap;
    }


    fun setWindowFlag(activity: Activity, bits: Int, on: Boolean) {
        val win = activity.getWindow()
        val winParams = win.getAttributes()
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.setAttributes(winParams)
    }

    fun getIconForTypePlaces(slug: String): NestedImageResourcesItem {
        when (slug) {
            nightClubKey -> {
                return NestedImageResourcesItem(R.mipmap.icn_place_main_main_nightclub_inactive, R.mipmap.icn_place_main_nightclub)
            }
            barKey -> {
                return NestedImageResourcesItem(R.mipmap.icn_place_main_main_bars_inactive, R.mipmap.icn_place_main_bars)

            }
            restaurantKey -> {
                return NestedImageResourcesItem(R.mipmap.icn_place_main_main_restaurants_inactive, R.mipmap.icn_place_main_restaurants)

            }
            karaokeKey -> {
                return NestedImageResourcesItem(R.mipmap.icn_place_main_karaoke_inactive, R.mipmap.icn_place_main_karaoke)

            }
            else -> {
                return NestedImageResourcesItem(R.mipmap.icn_place_main_dafault_inactive, R.mipmap.icn_place_main_default)
            }
        }
    }


    fun getIconForMarker(idPlaceType: Int): Int {
        when (idPlaceType) {
            1 -> {
                return R.mipmap.map_pin_place_nightclub
            }
            2 -> {
                return R.mipmap.map_pin_place_bars

            }
            3 -> {
                return R.mipmap.map_pin_place_restaurant

            }
            4 -> {
                return R.mipmap.map_pin_place_karaoke

            }
            else -> {
                return R.mipmap.map_pin_place_default
            }
        }
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

    fun appendStrings(names: List<String>): String {
        var servicesText = StringBuilder()

        for (i in 0 until names.size - 1) {
            servicesText.append(names[i] + ", ")
        }
        servicesText.append(names[names.lastIndex])
        return servicesText.toString()
    }

    fun appendStrings(names: List<String>, delimiter: String): String {
        if (names.isEmpty()) return ""

        var servicesText = StringBuilder()
        if (names.isEmpty()) {
            return ""
        }
        for (i in 0 until names.size - 1) {
            servicesText.append(names[i] + " " + delimiter + " ")
        }
        servicesText.append(names[names.lastIndex])
        return servicesText.toString()
    }


    fun getTodayDayOfWeek(): Int {
        return getCurrentDayOfWeek()

    }

    fun getTomorrowDayOfWeek(): Int {
        var tomorrowDay = getCurrentDayOfWeek() + 1
        if (tomorrowDay >= 7) {
            tomorrowDay = 0
        }
        return tomorrowDay
    }


    fun getCurrentDayOfWeek(): Int {
        val c = Calendar.getInstance()

        var dayOfWeek = 0
        when (c.get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> dayOfWeek = 0
            Calendar.TUESDAY -> dayOfWeek = 1
            Calendar.WEDNESDAY -> dayOfWeek = 2
            Calendar.THURSDAY -> dayOfWeek = 3
            Calendar.FRIDAY -> dayOfWeek = 4
            Calendar.SATURDAY -> dayOfWeek = 5
            Calendar.SUNDAY -> dayOfWeek = 6
        }
        return dayOfWeek
    }

    fun getYesterdayDayOfWeek(): Int {
        val c = Calendar.getInstance()

        var dayOfWeek = 0
        when (c.get(Calendar.DAY_OF_WEEK) - 1) {
            Calendar.MONDAY -> dayOfWeek = 0
            Calendar.TUESDAY -> dayOfWeek = 1
            Calendar.WEDNESDAY -> dayOfWeek = 2
            Calendar.THURSDAY -> dayOfWeek = 3
            Calendar.FRIDAY -> dayOfWeek = 4
            Calendar.SATURDAY -> dayOfWeek = 5
            Calendar.SUNDAY -> dayOfWeek = 6
        }
        return dayOfWeek
    }

    fun getCoverBackground(placeId: Int): Int {
        when (placeId) {
            1 -> {
                return R.drawable.background_gradient_night_club_view
            }
            2 -> {
                return R.drawable.background_gradient_bar_view
            }
            3 -> {
                return R.drawable.background_gradient_restaurant_view
            }
            4 -> {
                return R.drawable.background_gradient_karaoke_view
            }
            else -> {
                return R.drawable.background_gradient_other_view

            }
        }
    }


    fun getPlaceTypeKeyById(id: Int): String {
        when (id) {
            1 -> {
                return nightClubKey
            }
            2 -> {
                return barKey
            }
            3 -> {
                return restaurantKey
            }
            4 -> {
                return karaokeKey
            }

            else -> {
                return ""

            }
        }
    }

    fun getPlaceTypeNameById(id: Int, context: Context): String {
        when (id) {
            1 -> {
                return context.getString(R.string.title_night_club)
            }
            2 -> {
                return context.getString(R.string.title_bar)
            }
            3 -> {
                return context.getString(R.string.title_restaurant)
            }
            4 -> {
                return context.getString(R.string.title_karaoke)
            }

            else -> {
                return ""

            }
        }
    }


    fun getPlaceTypeKeySlugId(slug: String): Int {
        when (slug) {
            nightClubKey -> {
                return 1
            }
            barKey -> {
                return 2
            }
            restaurantKey -> {
                return 3
            }
            karaokeKey -> {
                return 4
            }
            else -> {
                return 0

            }
        }
    }


    fun getTitleByPlaceTypeId(placeId: Int): Int {
        when (placeId) {
            1 -> {
                return R.drawable.background_gradient_night_club_view
            }
            2 -> {
                return R.drawable.background_gradient_bar_view
            }
            3 -> {
                return R.drawable.background_gradient_restaurant_view
            }
            3 -> {
                return R.mipmap.icn_filter_small_karaoke
            }
            4 -> {
                return R.drawable.background_gradient_karaoke_view
            }
            else -> {
                return R.drawable.background_gradient_other_view

            }
        }
    }

    fun getIconForFilter(placeId: Int): Int {
        when (placeId) {
            1 -> {
                return R.mipmap.icn_filter_small_nightclub
            }
            2 -> {
                return R.mipmap.icn_filter_small_bars
            }
            3 -> {
                return R.mipmap.icn_filter_small_restaurants
            }
            4 -> {
                return R.mipmap.icn_filter_small_karaoke
            }

            else -> {
                return R.mipmap.icn_filter_small_default

            }
        }
    }

    fun getSearchIconById(placeId: Int): Int {
        when (placeId) {
            1 -> {
                return R.mipmap.icn_map_nightclub_small
            }
            2 -> {
                return R.mipmap.icn_map_bar_small
            }
            3 -> {
                return R.mipmap.icn_map_restaurant_small
            }
            4 -> {
                return R.mipmap.icn_map_karaoke_small
            }

            else -> {
                return R.mipmap.icn_map_default_small

            }
        }
    }


    fun generateViewId(int: Int): Int {
        var sNextGeneratedId = AtomicInteger(int);
        while (true) {
            val result = sNextGeneratedId.get()
            var newValue = result + 1
            if (newValue > 0x00FFFFFF) newValue = 1 // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result
            }
        }
    }


    fun getColorByPercentage(popularTimes: Int): Int {
        var color = LeMustApp.instance.resources.getColor(R.color.colorPinCircle10)
        when (popularTimes) {
            in 0..10 -> {
                color = LeMustApp.instance.resources.getColor(R.color.colorPinCircle10)

            }
            in 11..20 -> {
                color = LeMustApp.instance.resources.getColor(R.color.colorPinCircle20)

            }
            in 21..30 -> {
                color = LeMustApp.instance.resources.getColor(R.color.colorPinCircle30)
            }
            in 31..40 -> {
                color = LeMustApp.instance.resources.getColor(R.color.colorPinCircle40)

            }
            in 41..50 -> {
                color = LeMustApp.instance.resources.getColor(R.color.colorPinCircle50)

            }
            in 51..60 -> {
                color = LeMustApp.instance.resources.getColor(R.color.colorPinCircle60)

            }
            in 61..70 -> {
                color = LeMustApp.instance.resources.getColor(R.color.colorPinCircle70)

            }
            in 71..80 -> {
                color = LeMustApp.instance.resources.getColor(R.color.colorPinCircle80)

            }
            in 81..90 -> {
                color = LeMustApp.instance.resources.getColor(R.color.colorPinCircle90)

            }
            in 91..1000 -> {
                color = LeMustApp.instance.resources.getColor(R.color.colorPinCircle100)
            }
        }
        return color
    }


    fun getDayWeek(startDay: Int?, endDay: Int?, timeStartDay: String?, timeEndDay: String?, isUseTimePrefixFormat: Boolean): Day {
        var day = "Monday"

        val newTime = StringBuilder("")
        val strStartDay = StringBuilder("")
        val strEndDay = StringBuilder()

        if (startDay != null) {
            strStartDay.append(timeStartDay)
            strStartDay.insert(2, ':')

//            if (isUseTimePrefixFormat) {
//                strStartDay.append(getTime(strStartDay.toString()))
//            }
            when (startDay - 1) {
                0 -> {
                    day = "Monday"
                }
                1 -> {
                    day = "Tuesday"

                }
                2 -> {
                    day = "Wednesday"

                }
                3 -> {
                    day = "Thursday"

                }
                4 -> {
                    day = "Friday"

                }
                5 -> {
                    day = "Saturday"

                }
                6 -> {
                    day = "Sunday"

                }
            }
            newTime.append(" ").append(strStartDay).append(" ")
        }

        if (endDay != null) {
            strEndDay.append(timeEndDay)
            strEndDay.insert(2, ':')
//            if (isUseTimePrefixFormat) {
//                strEndDay.append(getTime(strEndDay.toString().toString()))
//            }
            newTime.append(" - ").append(strEndDay)

        }


        var dayPeriod = Day()
        dayPeriod.dayPosition = startDay!!
        dayPeriod.time = newTime.toString()
        dayPeriod.dayName = day
        return dayPeriod
    }

    fun getTime(milliseconds: String): String {
        var time = 0L
        val formatter = SimpleDateFormat("HH:mm")
        try {
            var date = formatter.parse(milliseconds) as Date
            time = date.time
        } catch (ex: ParseException) {
        }
        var cal = Calendar.getInstance()
        cal.setTimeInMillis(time);

        if (cal.get(Calendar.AM_PM) == 0)
            return " AM";
        else
            return " PM";
    }


    fun getPlacePluralTitle(title: String, ctx: Context): String {
        when (title.toUpperCase()) {
            ctx.resources.getString(R.string.title_night_club).toUpperCase() -> {
                return ctx.getString(R.string.title_plural_place_type_night_club)
            }
            ctx.resources.getString(R.string.title_bar).toUpperCase() -> {
                return ctx.getString(R.string.title_plural_place_type_bar)
            }
            ctx.resources.getString(R.string.title_karaoke).toUpperCase() -> {
                return ctx.getString(R.string.title_karaoke)
            }
            ctx.resources.getString(R.string.title_restaurant).toUpperCase() -> {
                return ctx.getString(R.string.title_plural_place_type_restaurant)
            }

            else -> {
                return title

            }
        }
    }


    class Day() {
        var dayPosition = 0
        var dayName = ""
        var time = ""
    }

}
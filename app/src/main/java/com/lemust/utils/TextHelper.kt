package com.lemust.utils

import android.graphics.Paint
import android.graphics.Rect
import com.lemust.LeMustApp
import com.lemust.R

object TextHelper {
    fun getPlaceSingularTitle(title: String): String {
        when (title) {
            "Night Club" -> {
                return LeMustApp.instance.getString(R.string.title_plural_place_type_night_club)
            }
            "Bar" -> {
                return LeMustApp.instance.getString(R.string.title_plural_place_type_bar)
            }
            "Restaurant" -> {
                return LeMustApp.instance.getString(R.string.title_plural_place_type_restaurant)
            }

            else -> {
                return title

            }
        }
    }

    fun cap1stChar(userIdea: String): String {
        return if (userIdea.isNotEmpty()) {
            var userIdea = userIdea
            val stringArray = userIdea.toCharArray()
            stringArray[0] = Character.toUpperCase(stringArray[0])
            String(stringArray)

        } else
            ""
    }

    fun getTextHeight(text: String, paint: Paint): Float {
        val rect = Rect()
        paint.getTextBounds(text, 0, text.length, rect)
        return rect.height().toFloat()
    }

}
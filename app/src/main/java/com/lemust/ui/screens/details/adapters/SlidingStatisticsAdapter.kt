package com.lemust.ui.screens.details.adapters

import android.content.Context
import android.os.Parcelable
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lemust.R
import com.steelkiwi.simplehistogram.TimeData
import kotlinx.android.synthetic.main.slidingimages_layout.view.*
import java.util.*


class SlidingStatisticsAdapter(private val context: Context, private val statics: ArrayList<ArrayList<TimeData>>) : PagerAdapter() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }


    override fun getCount(): Int {
        /// return statics.size
        return Int.MAX_VALUE
    }

    fun getActualListCount(): Int {
        return statics.size
    }
    override fun instantiateItem(view: ViewGroup, fakePosition: Int): Any {
        val imageLayout = inflater.inflate(R.layout.slidingimages_layout, view, false)
        var positionInList = fakePosition % getActualListCount();

        var item = statics[positionInList]

        imageLayout.histoView.setItems(item)
        view.addView(imageLayout, 0)


        return imageLayout
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {}

    override fun saveState(): Parcelable? {
        return null
    }


}

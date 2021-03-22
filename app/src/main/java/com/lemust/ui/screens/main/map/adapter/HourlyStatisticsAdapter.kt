package com.hairdresser.ui.main.customer.reward_points.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lemust.LeMustApp
import com.lemust.R
import com.lemust.ui.screens.main.map.adapter.HourItem
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_hour.view.*


class HourlyStatisticsAdapter : RecyclerView.Adapter<HourlyStatisticsAdapter.ContentViewHolder> {

    var itemsData: ArrayList<HourItem> = ArrayList()
    var onClickObservable = PublishSubject.create<String>()

    constructor(itemsData: ArrayList<HourItem>) : super() {
        this.itemsData = itemsData
    }


    fun getAdapterItemPosition(id: Int): Int {
        for (position in 0 until itemsData.size)
            if (itemsData.get(position).hourInt == id)
                return position
        return 0
    }

//    override fun getItemCount(): Int = itemsData.size


    override fun onBindViewHolder(holder: ContentViewHolder, position: Int) {
        if (itemsData.isNotEmpty()) {
            var actualPosition = position % itemsData.size
            val content = itemsData[actualPosition]
            holder!!.bindView(content)
        }

    }

    override fun getItemCount(): Int {
        return Integer.MAX_VALUE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentViewHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.item_hour, parent, false)
        return ContentViewHolder(view)
    }

    inner class ContentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(item: HourItem) {
            itemView.tv_hour.text = item.hour
            if (item.isSelected) {
                itemView.tv_hour.setTextColor(LeMustApp.instance.resources.getColor(R.color.colorDatePickerCurrentHour))
            } else {
                itemView.tv_hour.setTextColor(LeMustApp.instance.resources.getColor(android.R.color.white))

            }
        }


    }


}
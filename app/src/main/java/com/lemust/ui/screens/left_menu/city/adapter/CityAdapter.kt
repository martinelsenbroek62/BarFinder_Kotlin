package com.lemust.ui.screens.left_menu.city.adapter

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lemust.LeMustApp
import com.lemust.R
import com.lemust.repository.models.rest.City
import com.lemust.ui.screens.left_menu.city.adapter.item.BaseItem
import com.lemust.ui.screens.left_menu.city.adapter.item.CityItem
import com.lemust.ui.screens.left_menu.city.adapter.item.CountyItem
import com.lemust.ui.screens.left_menu.city.adapter.item.TypeItem
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_city.view.*
import kotlinx.android.synthetic.main.item_country.view.*


class CityAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {

//    var itemsData: ArrayList<CityItem> = ArrayList()

    var itemsData: ArrayList<BaseItem> = ArrayList()

    var clickListener = PublishSubject.create<City>()

    constructor(itemsData: ArrayList<BaseItem>) : super() {
        this.itemsData = itemsData
    }


    override fun getItemCount(): Int = itemsData.size


    override fun getItemViewType(position: Int): Int {
        when (itemsData[position].typeItem) {
            TypeItem.COUNRTY -> {
                return TypeItem.COUNRTY.ordinal
            }
            TypeItem.CITY_NAME -> {
                return TypeItem.CITY_NAME.ordinal
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            TypeItem.COUNRTY.ordinal -> {
                val view = LayoutInflater.from(parent!!.context).inflate(R.layout.item_country, parent, false)
                return CountryViewHolder(view)

            }
            else -> {
                val view = LayoutInflater.from(parent!!.context).inflate(R.layout.item_city, parent, false)
                return CityViewHolder(view)
            }

        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            TypeItem.COUNRTY.ordinal ->
                (holder as CountryViewHolder).bindView(itemsData[position] as CountyItem)

            TypeItem.CITY_NAME.ordinal ->
                (holder as CityViewHolder).bindView(itemsData[position] as CityItem)

        }
    }

    inner class CountryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(item: CountyItem) {
            itemView.tv_country.text = item.name
//            setItemStyle(item, itemView)
//            itemView.setOnClickListener {
//                //   itemView.setBackgroundColor(Color.RED)
//                handleChoice(item, itemView)
//
//                clickListener.onNext(item.city)
        }
    }


    inner class CityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(item: CityItem) {
            itemView.tv_city.text = item.city.name
            setItemStyle(item, itemView)
            itemView.setOnClickListener {
                handleChoice(item, itemView)

                clickListener.onNext(item.city)
            }
        }

    }


    private fun handleChoice(item: CityItem, itemView: View) {
        itemsData.filter { it.typeItem == TypeItem.CITY_NAME }.forEach { (it as CityItem).isSelected = false }
        notifyDataSetChanged()
        item.isSelected = true


    }


    private fun setItemStyle(item: CityItem, itemView: View) {
        if (item.isSelected) {
            itemView.tv_city.setTextColor(LeMustApp.instance.resources.getColor(R.color.colorTextSubFilterSelected))
            itemView.iv_check.visibility = View.VISIBLE
        } else {
            itemView.tv_city.setTextColor(Color.WHITE)
            itemView.iv_check.visibility = View.INVISIBLE

        }


    }
}

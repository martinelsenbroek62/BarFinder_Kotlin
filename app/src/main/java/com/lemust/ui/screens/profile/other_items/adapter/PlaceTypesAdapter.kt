package com.lemust.ui.screens.profile.other_items.adapter

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lemust.LeMustApp
import com.lemust.R
import kotlinx.android.synthetic.main.item_multi_selected.view.*


class PlaceTypesAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {


    var itemsData: ArrayList<FavoriteMusicItem> = ArrayList()

    constructor(itemsData: ArrayList<FavoriteMusicItem>) : super() {
        this.itemsData = itemsData
    }


    override fun getItemCount(): Int = itemsData.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.item_day_out, parent, false)
        return ContentViewHolderMulti(view)


    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ContentViewHolderMulti).bindView(itemsData[position])
    }


    inner class ContentViewHolderMulti(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(item: FavoriteMusicItem) {
            itemView.tv_name.text = item.option.option
            if (item.isSelected) {
                itemView.tv_name.setTextColor(LeMustApp.instance.resources.getColor(R.color.colorTextSubFilterSelected))
                itemView.iv_check.visibility = View.VISIBLE
            } else {
                itemView.tv_name.setTextColor(Color.WHITE)
                itemView.iv_check.visibility = View.INVISIBLE


            }
            itemView.setOnClickListener {
                handleChoice(item, itemView)

            }


        }
    }


    private fun handleChoice(item: FavoriteMusicItem, itemView: View) {
        item.isSelected = !item.isSelected
        setItemStyle(item, itemView)

    }

    private fun setItemStyle(item: FavoriteMusicItem, itemView: View) {
        if (item.isSelected) {
            itemView.tv_name.setTextColor(LeMustApp.instance.resources.getColor(R.color.colorTextSubFilterSelected))
            itemView.iv_check.visibility = View.VISIBLE
        } else {
            itemView.tv_name.setTextColor(Color.WHITE)
            itemView.iv_check.visibility = View.INVISIBLE

        }
    }

}
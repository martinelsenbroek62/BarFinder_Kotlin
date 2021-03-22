package com.lemust.ui.screens.details.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lemust.R
import com.lemust.repository.models.rest.City
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_place_details_sub.view.*


class PlaceDetailsSubCategoryAdapter : RecyclerView.Adapter<PlaceDetailsSubCategoryAdapter.ContentViewHolder> {
    var itemsData: ArrayList<PlaceDetailsSubCategoryItem> = ArrayList()
    var clickListener = PublishSubject.create<City>()

    constructor(itemsData: ArrayList<PlaceDetailsSubCategoryItem>) : super() {
        this.itemsData = itemsData
    }


    override fun getItemCount(): Int = itemsData.size


    override fun onBindViewHolder(holder: ContentViewHolder, position: Int) {
        val content = itemsData[position]
        holder!!.bindView(content)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentViewHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.item_place_details_sub, parent, false)
        return ContentViewHolder(view)
    }

    inner class ContentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(item: PlaceDetailsSubCategoryItem) {
            itemView.tv_title_sub.text = item.title
            itemView.tv_value_sub.text = item.value

            if (item.value.isEmpty()) {
                itemView.tv_value_sub.visibility = View.GONE
            }
        }


    }
}
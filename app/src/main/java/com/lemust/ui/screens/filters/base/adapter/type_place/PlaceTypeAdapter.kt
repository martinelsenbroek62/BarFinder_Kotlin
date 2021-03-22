package com.lemust.ui.screens.filters.base.adapter.type_place

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lemust.R
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_place_type.view.*


class PlaceTypeAdapter : RecyclerView.Adapter<PlaceTypeAdapter.ContentViewHolder> {

    private var itemsData: ArrayList<PlaceTypeItem> = ArrayList()
    var onClickObservable = PublishSubject.create<PlaceTypeItem>()

    constructor(itemsData: ArrayList<PlaceTypeItem>) : super() {
        this.itemsData = itemsData
    }


    override fun getItemCount(): Int = itemsData.size


    override fun onBindViewHolder(holder: ContentViewHolder, position: Int) {
        val content = itemsData[position]
        holder!!.bindView(content)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentViewHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.item_place_type, parent, false)
        return ContentViewHolder(view)
    }

    inner class ContentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(item: PlaceTypeItem) {
            itemView.tv_place_name.text = item.name
            if (item.isSelected) {
                itemView.img_place_type.setImageResource(item.icon.selectedStateImage)

            } else {
                itemView.img_place_type.setImageResource(item.icon.unSelectedStateImage)
            }
            itemView.setOnClickListener {
                itemsData.forEach { it.isSelected = false }
                item.isSelected = true
                onClickObservable.onNext(item)
                notifyDataSetChanged()
            }

        }


    }


}
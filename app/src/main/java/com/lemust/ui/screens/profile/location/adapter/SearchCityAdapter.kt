package com.lemust.ui.screens.profile.location.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lemust.R
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_search_city.view.*


class SearchCityAdapter : RecyclerView.Adapter<SearchCityAdapter.ContentViewHolder> {
    var itemsData: ArrayList<SearchCitytem> = ArrayList()
    var clickListener = PublishSubject.create<SearchCitytem>()

    constructor(itemsData: ArrayList<SearchCitytem>) : super() {
        this.itemsData = itemsData
    }

    public fun clear() {
        itemsData.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = itemsData.size


    override fun onBindViewHolder(holder: ContentViewHolder, position: Int) {
        val content = itemsData[position]
        holder!!.bindView(content)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentViewHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.item_search_city, parent, false)
        return ContentViewHolder(view)
    }

    inner class ContentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(item: SearchCitytem) {
            itemView.tv_city.text = item.city.name
            itemView.setOnClickListener {
                clickListener.onNext(item)
            }

        }

        //TODO move to presenter


    }
}

package com.lemust.ui.screens.search.adapter

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lemust.R
import com.lemust.repository.models.rest.search.SearchItemDTO
import com.lemust.utils.Tools
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_search_place_type.view.*


class SearchAdapter : RecyclerView.Adapter<SearchAdapter.ContentViewHolder> {
    var itemsData: ArrayList<SearchItemDTO> = ArrayList()
    var clickListener = PublishSubject.create<SearchItemDTO>()

    constructor(itemsData: ArrayList<SearchItemDTO>) : super() {
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
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.item_search_place_type, parent, false)
        return ContentViewHolder(view)
    }

    inner class ContentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(item: SearchItemDTO) {
            itemView.iv_icon.setImageResource(Tools.getSearchIconById(item.placeType!!.id!!))
            var name=item.place!!.name!!.trim()
            itemView.tv_place_name_item.text = name
            itemView.setOnClickListener {
                clickListener.onNext(item)
            }

        }

        //TODO move to presenter


    }
}

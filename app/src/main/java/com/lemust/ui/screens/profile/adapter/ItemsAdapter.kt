package com.lemust.ui.screens.profile.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lemust.LeMustApp
import com.lemust.R
import com.lemust.utils.Tools
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_profile_lauout.view.*


class ItemsAdapter : RecyclerView.Adapter<ItemsAdapter.ContentViewHolder> {
    var itemsData: ArrayList<Item> = ArrayList()
    var clickListener = PublishSubject.create<Item>()


    constructor(itemsData: ArrayList<Item>) : super() {
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
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.item_profile_lauout, parent, false)
        return ContentViewHolder(view)
    }

    inner class ContentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(item: Item) {
            itemView.tv_place_type_item.text = item.name

            var msg = itemView.context.resources.getString(R.string.title_not_set)
            var selectedoption = item.option.filter { it.isSelected == true }.map { it.option } as List<String>
            if (selectedoption.isNotEmpty()) {
                msg = Tools.appendStrings(selectedoption)
            }
            itemView.tv_place_types.text = msg



            itemView.setOnClickListener {
                clickListener.onNext(item)
            }

        }



    }
}

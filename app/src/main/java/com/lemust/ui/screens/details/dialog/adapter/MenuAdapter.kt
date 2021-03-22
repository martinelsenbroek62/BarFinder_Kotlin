package com.lemust.ui.screens.details.dialog.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lemust.R
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.menu_item.view.*


class MenuAdapter : RecyclerView.Adapter<MenuAdapter.ContentViewHolder> {
    var itemsData: ArrayList<MenuItem> = ArrayList()
    var clickListener = PublishSubject.create<MenuItem>()

    constructor(itemsData: ArrayList<MenuItem>) : super() {
        this.itemsData = itemsData
    }


    override fun getItemCount(): Int = itemsData.size


    override fun onBindViewHolder(holder: ContentViewHolder, position: Int) {
        val content = itemsData[position]
        holder!!.bindView(content)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentViewHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.menu_item, parent, false)
        return ContentViewHolder(view)
    }

    inner class ContentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(item: MenuItem) {
            itemView.btn_item_txt.text=item.name
            itemView.setOnClickListener {
                clickListener.onNext(item)
            }
        }

    }




}

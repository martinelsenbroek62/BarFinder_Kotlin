package com.lemust.ui.screens.left_menu.localization.adapter

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lemust.LeMustApp
import com.lemust.R
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_locale.view.*


class LanguageAdapter : RecyclerView.Adapter<LanguageAdapter.ContentViewHolder> {
    var itemsData: ArrayList<LanguageItem> = ArrayList()
    var clickListener = PublishSubject.create<LanguageItem>()

    constructor(itemsData: ArrayList<LanguageItem>) : super() {
        this.itemsData = itemsData
    }


    override fun getItemCount(): Int = itemsData.size


    override fun onBindViewHolder(holder: ContentViewHolder, position: Int) {
        val content = itemsData[position]
        holder!!.bindView(content)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentViewHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.item_locale, parent, false)
        return ContentViewHolder(view)
    }

    inner class ContentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(item: LanguageItem) {
            itemView.tv_locale.text = item.language
            setItemStyle(item, itemView)
            itemView.setOnClickListener {
                //   itemView.setBackgroundColor(Color.RED)
                handleChoice(item, itemView)

                clickListener.onNext(item)
            }
        }

    }


    private fun handleChoice(item: LanguageItem, itemView: View) {
        itemsData.forEach { it.isSelected = false }
        item.isSelected = true
        notifyDataSetChanged()


    }


    private fun setItemStyle(item: LanguageItem, itemView: View) {
        if (item.isSelected) {
            itemView.tv_locale.setTextColor(LeMustApp.instance.resources.getColor(R.color.colorTextSubFilterSelected))
            itemView.iv_check.visibility = View.VISIBLE
        } else {
            itemView.tv_locale.setTextColor(Color.WHITE)
            itemView.iv_check.visibility = View.INVISIBLE

        }


    }
}

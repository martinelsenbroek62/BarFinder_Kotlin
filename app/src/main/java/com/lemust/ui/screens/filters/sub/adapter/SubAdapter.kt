package com.lemust.ui.screens.filters.sub.adapter

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lemust.LeMustApp
import com.lemust.R
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_subfilter.view.*



class SubAdapter : RecyclerView.Adapter<SubAdapter.ContentViewHolder> {
    public var itemsData: ArrayList<SubItem> = ArrayList()
    private var isMultiChoice = false

    var test = PublishSubject.create<SubItem>()

    constructor(itemsData: ArrayList<SubItem>, isMultiChoice: Boolean) : super() {
        this.itemsData = itemsData
        this.isMultiChoice = isMultiChoice
    }


    override fun getItemCount(): Int = itemsData.size


    override fun onBindViewHolder(holder: ContentViewHolder, position: Int) {
        val content = itemsData[position]
        holder!!.bindView(content)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentViewHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.item_subfilter, parent, false)
        return ContentViewHolder(view)
    }

    inner class ContentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(item: SubItem) {
            itemView.tv_bool_name.text = item.name.option
            setItemStyle(item, itemView)
            itemView.setOnClickListener {
                //   itemView.setBackgroundColor(Color.RED)
                handleChoice(item, itemView)

                test.onNext(item)
            }
        }


    }

    private fun handleChoice(item: SubItem, itemView: View) {
        if (isMultiChoice) {
            item.isSelected = !item.isSelected
            setItemStyle(item, itemView)
        } else {
            if (item.isSelected) {
                item.isSelected = !item.isSelected
            } else {
                itemsData.forEach {
                    it.isSelected = false
                }
                item.isSelected = true
            }
            notifyDataSetChanged()


        }
    }

    private fun setItemStyle(item: SubItem, itemView: View) {
        if (item.isSelected) {
            itemView.tv_bool_name.setTextColor(LeMustApp.instance.resources.getColor(R.color.colorTextSubFilterSelected))
            itemView.iv_check.visibility = View.VISIBLE
        } else {
            itemView.tv_bool_name.setTextColor(Color.WHITE)
            itemView.iv_check.visibility = View.INVISIBLE

        }
    }


}
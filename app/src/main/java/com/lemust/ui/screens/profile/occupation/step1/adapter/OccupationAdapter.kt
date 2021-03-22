package com.lemust.ui.screens.profile.occupation.step1.adapter

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lemust.LeMustApp
import com.lemust.R
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_multi_selected.view.*


class OccupationAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {


    var itemsData: ArrayList<OccupationItem> = ArrayList()
    var clickAction = PublishSubject.create<Any>()

    constructor(itemsData: ArrayList<OccupationItem>) : super() {
        this.itemsData = itemsData
    }


    override fun getItemCount(): Int = itemsData.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.item_multi_selected, parent, false)
        return ContentViewHolderMulti(view)


    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ContentViewHolderMulti).bindView(itemsData[position])
    }


    inner class ContentViewHolderMulti(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(item: OccupationItem) {
            itemView.tv_name.text = item.name
            if (item.isSelected) {
                itemView.tv_name.setTextColor(LeMustApp.instance.resources.getColor(R.color.colorTextSubFilterSelected))
                itemView.iv_check.visibility = View.VISIBLE
            } else {
                itemView.tv_name.setTextColor(Color.WHITE)
                itemView.iv_check.visibility = View.INVISIBLE


            }
            itemView.setOnClickListener {
                handleChoice(item, itemView)
                clickAction.onNext(Any())


            }


        }
    }


    private fun handleChoice(item: OccupationItem, itemView: View) {

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


//    private fun setItemStyle(item: OccupationItem, itemView: View) {
//        if (item.isSelected) {
//            itemView.tv_name.setTextColor(LeMustApp.instance.resources.getColor(R.color.colorTextSubFilterSelected))
//            itemView.iv_check.visibility = View.VISIBLE
//        } else {
//            itemView.tv_name.setTextColor(Color.WHITE)
//            itemView.iv_check.visibility = View.INVISIBLE
//
//        }
//    }

}

package com.lemust.ui.screens.filters.base.adapter.sub_filters

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lemust.LeMustApp
import com.lemust.R
import com.lemust.utils.Tools
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_button.view.*
import kotlinx.android.synthetic.main.item_subfilter_bool.view.*
import kotlinx.android.synthetic.main.item_subfilter_choice.view.*
import kotlinx.android.synthetic.main.item_subfilter_multi.view.*


class SubFiltersAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {
    var keyButton = -1

    var itemsData: ArrayList<SubFiltersItem> = ArrayList()
    var openSubFiltersObservable = PublishSubject.create<SubFiltersItem>()
    var onApplyAction = PublishSubject.create<Any>()

    constructor(itemsData: ArrayList<SubFiltersItem>) : super() {
        this.itemsData = itemsData
    }


    override fun getItemCount(): Int = itemsData.size + 1

    override fun getItemViewType(position: Int): Int {
        if (position == itemCount - 1) {
            return keyButton
        } else {
            when (itemsData[position].filter.fieldType) {
                fieldTypeBoolKey -> {
                    return viewTypeBool
                }
                fieldTypeChoiceKey -> {
                    return viewTypeChoice
                }
                fieldTypeMultiKey -> {
                    return viewTypeMulti

                }
            }
        }
        return position - 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            viewTypeChoice -> {
                val view = LayoutInflater.from(parent!!.context).inflate(R.layout.item_subfilter_choice, parent, false)
                return ContentViewHolderChoice(view)

            }
            viewTypeMulti -> {
                val view = LayoutInflater.from(parent!!.context).inflate(R.layout.item_subfilter_multi, parent, false)
                return ContentViewHolderMulti(view)

            }
            keyButton -> {
                val view = LayoutInflater.from(parent!!.context).inflate(R.layout.item_button, parent, false)
                return ContentViewHolderButton(view)

            }
            else -> {
                val view = LayoutInflater.from(parent!!.context).inflate(R.layout.item_subfilter_bool, parent, false)
                return ContentViewHolderBool(view)
            }

        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            viewTypeBool ->
                (holder as ContentViewHolderBool).bindView(itemsData[position])


            viewTypeChoice -> {
                (holder as ContentViewHolderChoice).bindView(itemsData[position])

            }
            viewTypeMulti -> {
                (holder as ContentViewHolderMulti).bindView(itemsData[position])

            }

            keyButton -> {
                (holder as ContentViewHolderButton).bindView()

            }
        }
    }


    inner class ContentViewHolderButton(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView() {
            if (itemsData.isEmpty()) {
                itemView.tv_no_filters.visibility = View.VISIBLE
            } else {
                itemView.tv_no_filters.visibility = View.GONE
            }

            itemView.tv_apply.setOnClickListener {
                onApplyAction.onNext(Any())

            }


        }

    }


    inner class ContentViewHolderBool(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(item: SubFiltersItem) {
            itemView.tv_bool_name.text = item.filter.name
            checkItemSelected(item)
            itemView.setOnClickListener {
                item.isSelected = !item.isSelected
                checkItemSelected(item)
                openSubFiltersObservable.onNext(item)

            }

        }

        private fun checkItemSelected(item: SubFiltersItem) {
            if (item.isSelected) {
                itemView.tv_bool_name.setTextColor(LeMustApp.instance.resources.getColor(R.color.colorTextSubFilterSelected))
                itemView.iv_check.visibility = View.VISIBLE
            } else {
                itemView.tv_bool_name.setTextColor(Color.WHITE)
                itemView.iv_check.visibility = View.INVISIBLE

            }
        }
    }

    inner class ContentViewHolderChoice(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(item: SubFiltersItem) {
            itemView.tv_choice_name.text = item.filter.name
            if (item.selectedSubFilters.size > 0) {
                itemView.tv_sub_filter.text = item.selectedSubFilters[0].option
            } else {
                itemView.tv_sub_filter.text = itemView.context.resources.getString(R.string.title_no_filters)
            }

            itemView.setOnClickListener {
                openSubFiltersObservable.onNext(item) }

        }
    }

    inner class ContentViewHolderMulti(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(item: SubFiltersItem) {
            itemView.tv_multi_name.text = item.filter.name
            if (item.selectedSubFilters.size > 0) {
                itemView.tv_sub_filter_multi.text = Tools.appendStrings(item.selectedSubFilters.map { it.option.toString() })
            } else {
                itemView.tv_sub_filter_multi.text = itemView.context.resources.getString(R.string.title_no_filters)
            }

            itemView.setOnClickListener {
                openSubFiltersObservable.onNext(item)
            }


        }
    }


}
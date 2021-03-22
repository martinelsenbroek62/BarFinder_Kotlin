package com.lemust.ui.screens.reports.report_details.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.lemust.LeMustApp
import com.lemust.R
import com.lemust.repository.models.rest.report_details.Option
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_report_change_place_name.view.*
import kotlinx.android.synthetic.main.item_report_place_details_button.view.*
import kotlinx.android.synthetic.main.item_report_place_details_header.view.*
import kotlinx.android.synthetic.main.item_report_place_details_icon_price_item.view.*
import kotlinx.android.synthetic.main.item_report_place_details_text_item.view.*
import java.io.Serializable


class PlaceDetailsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {


    var itemsData: ArrayList<Base> = ArrayList()
    var onEditPlaceNameScreen = PublishSubject.create<Any>()
    var onAddNewItem = PublishSubject.create<NewItem>()

    constructor(itemsData: ArrayList<Base>) : super() {
        this.itemsData = itemsData
    }

    fun getHeaders(): List<Header> {
        return itemsData.filter { it.typeItem == TypeItem.TITLE }.map { it as Header }
    }

    fun getTextItems(id: Int): List<TextItem> {
        return itemsData.filter { it.typeItem == TypeItem.TEXT_ITEM }.filter { it.id == id }.map { it as TextItem }
    }


    override fun getItemCount(): Int = itemsData.size

    override fun getItemViewType(position: Int): Int {
        when (itemsData[position].typeItem) {
            TypeItem.BUTTON -> {
                return TypeItem.BUTTON.ordinal
            }
            TypeItem.PRICE_ITEM -> {
                return TypeItem.PRICE_ITEM.ordinal
            }
            TypeItem.TEXT_ITEM -> {
                return TypeItem.TEXT_ITEM.ordinal

            }
            TypeItem.EDIT_ITEM -> {
                return TypeItem.EDIT_ITEM.ordinal

            }
            TypeItem.TITLE -> {
                return TypeItem.TITLE.ordinal

            }
            TypeItem.EDIT_PLACE_NAME_ITEM -> {
                return TypeItem.EDIT_PLACE_NAME_ITEM.ordinal

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            TypeItem.PRICE_ITEM.ordinal -> {
                val view = LayoutInflater.from(parent!!.context).inflate(R.layout.item_report_place_details_icon_price_item, parent, false)
                return ContentViewPriceItem(view)

            }

            TypeItem.TEXT_ITEM.ordinal -> {
                val view = LayoutInflater.from(parent!!.context).inflate(R.layout.item_report_place_details_text_item, parent, false)
                return ContentViewTextItem(view)

            }
//            TypeItem.EDIT_ITEM.ordinal -> {
//                val view = LayoutInflater.from(parent!!.context).inflate(R.layout.item_report_place_details_add_item, parent, false)
//                return ContentViewEditItem(view)
//
//            }
            TypeItem.BUTTON.ordinal -> {
                val view = LayoutInflater.from(parent!!.context).inflate(R.layout.item_report_place_details_button, parent, false)
                return ContentViewButtonItem(view)

            }
            TypeItem.EDIT_PLACE_NAME_ITEM.ordinal -> {
                val view = LayoutInflater.from(parent!!.context).inflate(R.layout.item_report_change_place_name, parent, false)
                return EditPlaceNameItem(view)

            }

        //title
            else -> {
                val view = LayoutInflater.from(parent!!.context).inflate(R.layout.item_report_place_details_header, parent, false)
                return ContentViewHeaderItem(view)
            }

        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            TypeItem.PRICE_ITEM.ordinal ->
                (holder as ContentViewPriceItem).bindView(itemsData[position] as Pricetem)

            TypeItem.TEXT_ITEM.ordinal ->
                (holder as ContentViewTextItem).bindView(itemsData[position] as TextItem)

            TypeItem.BUTTON.ordinal ->
                (holder as ContentViewButtonItem).bindView(itemsData[position] as ButtonItem)

//            TypeItem.EDIT_ITEM.ordinal ->
//                (holder as ContentViewEditItem).bindView(itemsData[position] as EditItem)

            TypeItem.TITLE.ordinal ->
                (holder as ContentViewHeaderItem).bindView(itemsData[position] as Header)

            TypeItem.EDIT_PLACE_NAME_ITEM.ordinal ->
                (holder as EditPlaceNameItem).bindView(itemsData[position] as EditPlaceName)


        }
    }


    inner class EditPlaceNameItem(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(item: EditPlaceName) {
            itemView.tv_place_name.text = item.currentName
            itemView.setOnClickListener {
                onEditPlaceNameScreen.onNext(Any())
            }
        }
    }
    inner class ContentViewButtonItem(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(item: ButtonItem) {
            itemView.tv_button_title.text = itemView.context.resources.getString(R.string.title_add) + " " + item.title
            var isBackgroundFill = false
            itemView.setOnClickListener {


                onAddNewItem.onNext(NewItem(adapterPosition - 1, item.title, item.id, isBackgroundFill = isBackgroundFill))

            }
        }
    }


    inner class ContentViewHeaderItem(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(item: Header) {

            itemView.tv_header_title.text = item.title
        }
    }


    inner class ContentViewTextItem(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(item: TextItem) {
            var lastItemPosition = adapterPosition - 1
            if (lastItemPosition > 0) {
                if (itemsData.size > lastItemPosition) {
                    if (itemsData[lastItemPosition] is TextItem) {
                        var lastItemIsBackgroundFill = (itemsData[lastItemPosition] as TextItem).isBackgroundFill
                        item.isBackgroundFill = !lastItemIsBackgroundFill!!

                    }
                }
            }

            var nextItem = adapterPosition
            if (nextItem != itemsData.size) {
                item.isVisibleDivider = itemsData[nextItem] is Header
            }else{
                item.isVisibleDivider = true

            }







            setCheck(item.isChecked)
            if (item.isVisibleDivider!!) {
                itemView.divider.visibility = View.VISIBLE
            } else {
                itemView.divider.visibility = View.GONE

            }

            if (item.isBackgroundFill!!) {
                itemView.setBackgroundColor(itemView.context.resources.getColor(R.color.colorDivider))
            } else {
                itemView.setBackgroundColor(itemView.context.resources.getColor(R.color.colorBackground))

            }
            itemView.tv_item_title.text = item.title

            if (item.isCustomItem) {
                itemView.iv_check_text_item.setImageResource(R.drawable.ic_trash)
                itemView.setOnClickListener {
                    itemsData.remove(item)
                    notifyDataSetChanged()

                }

            } else
                itemView.setOnClickListener {
                    var check = !item.isChecked

                    if (item.isBool!!) {
                        item.isChecked = !item.isChecked
                        setCheck(check)

                    } else {
                        (item.obj as Option).isSelected = check
                        item.isChecked = check
                        setCheck(check)
                    }


                }


        }


        private fun setCheck(check: Boolean) {
            if (check) {
                itemView.iv_check_text_item.setImageResource(R.drawable.ic_checkbox_choose)
                itemView.tv_item_title.setTextColor(itemView.context.resources.getColor(R.color.colorTextBlue))

            } else {
                itemView.iv_check_text_item.setImageResource(R.drawable.ic_checkbox_empty)
                itemView.tv_item_title.setTextColor(itemView.context.resources.getColor(R.color.colorWhite))

            }
        }
    }

    inner class ContentViewPriceItem(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(item: Pricetem) {
            var imageViewIcons = ArrayList<ImageView>()
            imageViewIcons.add(itemView.iv_price_level1)
            imageViewIcons.add(itemView.iv_price_level2)
            imageViewIcons.add(itemView.iv_price_level3)
            imageViewIcons.add(itemView.iv_price_level4)
            var price = item.obj as Int


            if (item.isBackgroundFill!!) {
                itemView.setBackgroundColor(LeMustApp.instance.resources.getColor(R.color.colorDivider))
            } else {
                itemView.setBackgroundColor(LeMustApp.instance.resources.getColor(R.color.colorBackground))

            }

            setCheckedItem(imageViewIcons, item.isChecked, itemView)
            setVisibleItems(imageViewIcons, price)

            itemView.setOnClickListener {
                var check = !item.isChecked
                itemsData.filter { it.typeItem == TypeItem.PRICE_ITEM }.map {
                    (it as Pricetem).isChecked = false
                    item.isChecked = check

                    notifyDataSetChanged()
                }

            }
        }

        private fun setVisibleItems(imageViewIcons: ArrayList<ImageView>, price: Int) {

            imageViewIcons.map { it.visibility = View.INVISIBLE }
            for (i in 0 until price) {
                imageViewIcons[i].visibility = View.VISIBLE
            }
        }

        private fun setCheckedItem(imageViewIcons: ArrayList<ImageView>, isChecked: Boolean, itemView: View) {

            if (isChecked) {
                itemView.iv_check_price.setImageResource(R.drawable.ic_checkbox_choose)

                imageViewIcons.map { it.setImageResource(R.drawable.ic_icn_place_price_active_blue) }
            } else {
                itemView.iv_check_price.setImageResource(R.drawable.ic_checkbox_empty)

                imageViewIcons.map { it.setImageResource(R.mipmap.icn_place_price_active_white) }
            }

        }


    }

    class NewItem(var position: Int, var title: String, var itemId: Int, var newItemName: String = "", var isBackgroundFill: Boolean = false) : Serializable

}
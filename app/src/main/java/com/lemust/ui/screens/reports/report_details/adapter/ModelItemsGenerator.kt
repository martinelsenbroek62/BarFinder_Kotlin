package com.lemust.ui.screens.reports.report_details.adapter

import com.lemust.LeMustApp
import com.lemust.R
import com.lemust.ui.screens.reports.report_details.ReportDetailsPresenter
import java.util.*

class ModelItemsGenerator {
    var headerPrimaryKey = 0
    val list = ArrayList<Base>()


    fun addHeader(title: String, id: Int) {
        headerPrimaryKey = id
        list.add(Header(title, headerPrimaryKey, TypeItem.TITLE))
    }

    fun addEditPlaceNameItem(title: String, id: Int) {
        list.add(EditPlaceName(title, id, TypeItem.EDIT_PLACE_NAME_ITEM))
    }

    fun addTextItem(title: String, childId: Int, isChecked: Boolean = false, obj: Any? = null, isVisibleDivider: Boolean = false, isBackgroundFill: Boolean? = false,isBoolean: Boolean?=false) {
        list.add(TextItem(title, isChecked, headerPrimaryKey, childId, obj, TypeItem.TEXT_ITEM, isBackgroundFill = isBackgroundFill, isVisibleDivider = isVisibleDivider,isBool = isBoolean))
    }

    fun addIconItem(isChecked: Boolean = false, obj: Any? = null, isVisibleDivider: Boolean = false, isBackgroundFill: Boolean? = false) {
        list.add(Pricetem(isChecked, headerPrimaryKey, obj, TypeItem.PRICE_ITEM, isBackgroundFill = isBackgroundFill, isVisibleDivider = isVisibleDivider))
    }


    fun addButton(title: String) {
        list.add(ButtonItem(title, headerPrimaryKey, TypeItem.BUTTON))
    }

    fun addPriceCategory(checkedPosition: Int?, priceID: Int,name:String) {
        var priceItems = ArrayList<Pricetem>()

        addHeader(name, ReportDetailsPresenter.PRICE_ID)

        var isChecked = false
        for (i in 0..3) {
            if (checkedPosition != null)
                isChecked = i+1 == checkedPosition
            priceItems.add((Pricetem(isChecked, headerPrimaryKey, i + 1, TypeItem.PRICE_ITEM, isBackgroundFill = i % 2 == 0)))
        }
        priceItems.last().isVisibleDivider = true
        list.addAll(priceItems)

    }


    open class BaseItem(var isChecked: Boolean, var headerId: Int, obj: Any?, typeItem: TypeItem, var isFillBackground: Boolean? = false,
                        var isVisibleDivider: Boolean? = false) : Base(typeItem, headerId, obj)


}
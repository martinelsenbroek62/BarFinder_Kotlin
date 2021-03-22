package com.lemust.ui.screens.reports.report_details.adapter

open class TextItem(var title: String, isChecked: Boolean, headerId: Int, var childId: Int, obj: Any?, typeItem: TypeItem, var isBackgroundFill: Boolean? = false,  isVisibleDivider: Boolean? = false,var isBool:Boolean?=null) : ModelItemsGenerator.BaseItem(isChecked, headerId, obj, typeItem, isFillBackground = isBackgroundFill, isVisibleDivider = isVisibleDivider) {
    var isCustomItem = false

}

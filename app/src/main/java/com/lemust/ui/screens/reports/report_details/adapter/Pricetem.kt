package com.lemust.ui.screens.reports.report_details.adapter

open class Pricetem( isChecked: Boolean, headerId: Int, obj: Any?, typeItem: TypeItem, var isBackgroundFill:Boolean?=false, isVisibleDivider:Boolean?=false) : ModelItemsGenerator.BaseItem(isChecked, headerId, obj, typeItem,isFillBackground=isBackgroundFill,isVisibleDivider=isVisibleDivider)

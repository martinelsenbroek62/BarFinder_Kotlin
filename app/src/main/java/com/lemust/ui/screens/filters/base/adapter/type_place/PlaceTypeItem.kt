package com.lemust.ui.screens.filters.base.adapter.type_place

import com.lemust.repository.models.filters.FilterData


class PlaceTypeItem(var id: Int, var name: String, var slug: String, var icon: NestedImageResourcesItem, var filters: List<FilterData>, var isSelected: Boolean)

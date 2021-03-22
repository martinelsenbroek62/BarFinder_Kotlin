package com.lemust.ui.screens.filters.base.adapter.sub_filters

import com.lemust.repository.models.filters.FilterData
import com.lemust.repository.models.filters.OptionDTO


class SubFiltersItem(var filter: FilterData, var isSelected: Boolean, var selectedSubFilters:ArrayList<OptionDTO>){}
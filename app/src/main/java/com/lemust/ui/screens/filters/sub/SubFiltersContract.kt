package com.lemust.ui.screens.filters.sub

import com.lemust.ui.screens.filters.sub.adapter.SubItem
import io.reactivex.Observable


class SubFiltersContract {
    interface View {
        fun setData(data: List<SubItem>)
        fun onBackAction(): Observable<Any>
        fun onSelectAction(): Observable<SubItem>
        fun setTitleScreen(name: String)
        fun closeScreen()
        fun invalidate()
        fun getAdapterData(): ArrayList<SubItem>

    }

    interface Presenter {

    }
}
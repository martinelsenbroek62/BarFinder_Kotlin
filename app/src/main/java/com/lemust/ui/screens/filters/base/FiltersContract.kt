package com.lemust.ui.screens.filters.base

import android.arch.lifecycle.LifecycleObserver
import android.os.Bundle
import com.lemust.repository.models.filters.OptionDTO
import com.lemust.ui.screens.filters.base.adapter.sub_filters.SubFiltersItem
import com.lemust.ui.screens.filters.base.adapter.type_place.PlaceTypeItem
import io.reactivex.Observable


class FiltersContract {
    interface View : LifecycleObserver {
        fun setVisibleContent(isVisible: Boolean)
        fun setVisibleErrorMsg(isVisible: Boolean)
        fun setVisibleContentFilter(isVisible: Boolean)
        fun setVisibleLoader(isVisible: Boolean)
        fun setFilterPlaceTypeItems(filters: List<PlaceTypeItem>)
        fun setSubFilterItems(filters: List<SubFiltersItem>)
        fun onApplyFilters(): Observable<Any>
        fun onResetFilters(): Observable<Any>
        fun onChangedPlaceTypeId(): Observable<PlaceTypeItem>
        //        fun isVisibleApplyButton(isVisible: Boolean)
        fun onOpenSubFiltersAction(): Observable<SubFiltersItem>

        fun openSubScreen(isMultiChoice: Boolean, id: Int, data: List<OptionDTO>, selectedItems: ArrayList<OptionDTO>?, screenName: String)
        //  fun showSubFiltersEmptyMessage(isShow:Boolean)
        fun updateItemSubFilters(id: Int, data: ArrayList<OptionDTO>)

        fun getAdapterDate(): List<SubFiltersItem>
        fun selectFilterTypeById(id: Int): PlaceTypeItem?
        fun cancelSelectedItems()
        fun showContent()
        fun updateLocaleResources()
        fun hideScreens()
        fun onBackPressed(): Observable<Any>
        fun filterDetailsIsShowed(): Boolean
        fun notifyAdapter()

        fun hideFilters()
        fun hideAnimateLoader()
        fun showAnimateContent()
        fun onReloadFilter():Observable<Any>

        fun onDestroy()
        fun languageChanged():Observable<Any>

        fun showFilterException(e:Throwable)

    }

    interface Presenter {
        fun onDestroy()
        fun onSaveState(savedInstanceState: Bundle)
        fun onRestoreState(savedInstanceState: Bundle?)
        public fun initAction()
    }
}

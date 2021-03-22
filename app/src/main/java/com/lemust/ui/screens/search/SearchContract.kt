package com.lemust.ui.screens.search

import com.lemust.repository.models.rest.search.SearchItemDTO
import com.lemust.ui.base.BaseViewContract
import com.lemust.ui.base.dialog.DialogModel
import io.reactivex.Observable


interface SearchContract {
    interface View: BaseViewContract {
        fun setVisibleEmptyMessage(isVisible: Boolean)
        fun setVisibleLoader(isVisible: Boolean)
        fun setVisibleRightEditTextIcon(isVisible: Boolean)
        fun setVisibleContent(isVisible: Boolean)
        fun onTouchItemEvent(): Observable<SearchItemDTO>
        fun onClearAction(): Observable<Any>
        fun onScrollDown(): Observable<Any>
        fun getPlacesName(): Observable<CharSequence>
        fun setData(data: List<SearchItemDTO>)
        fun isEnabledEditText(isEnabled: Boolean)
        fun hideKeyboard()
        fun dismiss()
        fun unregister()
        fun showPlace(place:SearchItemDTO)
        fun getSearchText():String
        fun showDialogException(e:Throwable):Observable<DialogModel.OnDialogResult>
    }

    interface Presenter {
        fun onDestroy()
        fun onPause()
        fun onStart()
//        fun changePlaceType(placeId: Int)
//        fun initAction()
    }


}
package com.lemust.ui.screens.profile.edit_user

import android.content.Context
import com.lemust.ui.base.BaseViewContract
import io.reactivex.Observable


class EditUserContract {
    interface View : BaseViewContract {
        fun changeTextInProgressBar(text: String)
        fun isShowProgressLoader(isShow: Boolean)
        fun getFirstNameText(): String
        fun getLastText(): String
        fun onApplyAction(): Observable<Any>
        fun setFirstName(firstName: String)
        fun setLastName(lastName: String)
        fun finish()
        fun getAppContext(): Context

        fun showFirstNameError(error: String)
        fun showSeconNameError(error: String)
        fun disableFirstNameError()
        fun disableSecondNameError()
        fun hideKeyboard()
        fun onBackAction(): Observable<Any>

        fun setTitle(text: String)
        fun setDescription(text: String)

        fun isVisibleFirstName(isVisible: Boolean)
        fun isVisibleLastName(isVisible: Boolean)

    }

    interface Presenter {

    }
}

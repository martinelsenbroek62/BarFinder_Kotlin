package com.lemust.ui.screens.auth.policy

import com.lemust.ui.base.BaseViewContract
import io.reactivex.Observable


class PrivacyPolicyContract {
    interface View : BaseViewContract {
        fun onBackAction(): Observable<Any>
        fun back()
        fun isShowProgressLoader(isShow: Boolean)

        fun setTitle(title:String)
        fun setTypeDescription(title:String)
        fun setText(title:String)
        fun setShortDescription(title:String)
        fun initButtonBackArrow(isArrow:Boolean)

    }

    interface Presenter {

    }
}

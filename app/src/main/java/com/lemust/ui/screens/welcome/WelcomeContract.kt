package com.lemust.ui.screens.welcome

import com.lemust.ui.base.BaseViewContract
import com.lemust.ui.screens.welcome.adapter.WelcomePagerItem
import io.reactivex.Observable
import java.util.*


class WelcomeContract {
    interface View : BaseViewContract {
        fun initViewPager(statics: ArrayList<WelcomePagerItem>)
        fun onPositionAction(): Observable<Int>
        fun onButtonAction(): Observable<Any>
        fun onSkipAction(): Observable<Any>
        fun setPositionViewPager(position: Int)
        fun changeButtonTitle(title: String)
        fun setVisibilityButtonSkip(isVisible:Boolean)
        fun finishGotIt()


    }

    interface Presenter {

    }
}

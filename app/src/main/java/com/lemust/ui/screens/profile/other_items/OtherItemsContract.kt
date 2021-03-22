package com.lemust.ui.screens.profile.other_items

import android.content.Context
import com.lemust.ui.base.BaseViewContract
import com.lemust.ui.screens.profile.other_items.adapter.FavoriteMusicItem
import io.reactivex.Observable


class OtherItemsContract {
    interface View : BaseViewContract {
        fun setMusicItems(items:List<FavoriteMusicItem>)

        fun isShowProgressLoader(isShow: Boolean)
        fun getFavoriteMusicsItem(): ArrayList<FavoriteMusicItem>
        fun onApplyAction():Observable<Any>
        fun setMusicTypeTitle(title:String)
        fun changeTextInProgressBar(text: String)
    }

    interface Presenter {

    }


    interface Remainder {

        fun cancel()
        fun finish()
        fun getContext(): Context
        fun onResume()
        fun onDestroy()
        fun rewriteData(): Observable<Any>
        fun onBackPressedAction(): Observable<Any>
        fun generateBackPressed()


    }
}

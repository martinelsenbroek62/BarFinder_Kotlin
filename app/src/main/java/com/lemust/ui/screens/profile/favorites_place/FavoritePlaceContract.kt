package com.lemust.ui.screens.profile.favorites_place

import android.content.Context
import com.lemust.ui.base.BaseViewContract
import com.lemust.ui.screens.profile.favorites_place.adapter.FavoriteIPlacestem
import io.reactivex.Observable


class FavoritePlaceContract {
    interface View : BaseViewContract {
        //        fun onBackAction(): Observable<Any>
        fun changeTextInProgressBar(text: String)

        fun isShowProgressLoader(isShow: Boolean)
        fun getFavoriteMusicsItem(): ArrayList<FavoriteIPlacestem>
        fun onApplyAction(): Observable<Any>

        fun setMusicItems(items: List<FavoriteIPlacestem>)
    }

    interface Presenter

    interface Remainder {

        fun finish()
        fun getContext(): Context
        fun onResume()
        fun onDestroy()
        fun rewriteData(): Observable<Any>
        fun onBackPressedAction(): Observable<Any>
        fun generateBackPressed()


    }
}

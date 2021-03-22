package com.lemust.ui.screens.profile.change_password

import com.lemust.ui.base.BaseViewContract
import io.reactivex.Observable


class ChangePasswordContract {
    interface View : BaseViewContract {
        fun getCurrentPassword(): String
        fun getNewPassword(): String
        fun getRepeatPassword(): String
        fun onPasswordChangeAction(): Observable<Any>
        fun isVisibleOldPassword(isVisible: Boolean)
        fun finish()
        fun hideKeyboard()
        fun getNewPasswordAction(): Observable<CharSequence>
        fun getOldPasswordAction(): Observable<CharSequence>
        fun getRepeatPasswordAction(): Observable<CharSequence>
        fun isShowProgressLoader(isShow: Boolean)
//        fun isEnableButtonSent(isEnabled: Boolean)
        fun onDestroy()
        fun changeTextInProgressBar(text: String)

//        fun setUserSelectedTypeMusic(title: String)
//        fun setUserSelectedPlaceType(title: String)
//        fun onMusicTypeAction(): Observable<Any>
//        fun onPlaceTypeAction(): Observable<Any>
//        fun onDaysGoOutAction(): Observable<Any>
//        fun onUserBirthDateAction(): Observable<Any>
//        fun showDateDialog(): Observable<DatePickerFragment.Date>
//        fun openTypesMusicScreen(selectedMusicTypes: ArrayList<UserOptionDTO>, allMusicTypes: ArrayList<UserOptionDTO>)
//        fun openPlaceTypeScreen(selectedMusicTypes: ArrayList<UserFavoritePlaceTypeDTO>, allMusicTypes: ArrayList<UserFavoritePlaceTypeDTO>)
//        fun openDaysGoOutScreen()
//        fun setBirthDate( year: String,  month: String,  day: String)
    }

    interface Presenter {

    }
}

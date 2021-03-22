package com.lemust.ui.screens.profile

import android.content.Context
import android.graphics.Bitmap
import com.lemust.repository.models.rest.user.get.UserFavoritePlaceTypeDTO
import com.lemust.repository.models.rest.user.get.UserOptionDTO
import com.lemust.ui.base.BaseViewContract
import com.lemust.ui.screens.details.dialog.adapter.MenuItem
import com.lemust.ui.screens.profile.adapter.Item
import io.reactivex.Observable
import java.util.*


class ProfileContract {
    interface View : BaseViewContract {
        //        fun setUserSelectedTypeMusic(title: String)
        fun setUserSelectedPlaceType(title: String)

        fun isShowLoaderPhoto(isShow: Boolean)
        fun setItems(items: List<Item>)
        fun hideChangePasswordScreen()

        //        fun onMusicTypeAction(): Observable<Any>
        fun onPlaceTypeAction(): Observable<Any>

        fun openGallery()
        fun onOccupationAction(): Observable<Any>
        fun onDaysGoOutAction(): Observable<Any>
        fun onUserBirthDateAction(): Observable<Any>
        fun onChangeActionPassword(): Observable<Any>
        fun onLocationAction(): Observable<Any>
        fun onImageAvatarAction(): Observable<Any>
        fun onSettingsAction(): Observable<Any>
        fun onEditFirstName(): Observable<Any>
        fun onEditLastName(): Observable<Any>
        fun changeTextInProgressBar(text: String)



        fun onBottomSheetAction(): Observable<MenuItem>
        fun updateBottomSheetItems(items: ArrayList<MenuItem>)

        fun onOtherItemsAction(): Observable<Item>
        fun resetUserPhoto()
        fun setBirthDate(date: String)
        fun setDaysGoOut(days: String)
        fun setOccupation(days: String)
        fun setUserLocation(location: String)
        fun showAvatarDialog()
        fun setFirstName(name: String)
        fun setLastName(name: String)

        fun setUserAvatar(img: Bitmap)
        fun setUserAvatar(img: String)
        fun setDefaultAvatar()

        fun setPhotoMenuItems(items: ArrayList<MenuItem>)

        fun updateDialogResources()


        fun showDateDialog(date: String?): Observable<DatePickerFragment.Date>
        fun closePhotoDialog()

        fun isShowProgressLoader(isShow: Boolean)

        fun updateResources()
    }

    interface Presenter{
        fun onDestroy()

    }

    interface Remainder {
        fun getContext(): Context
        fun openTypesMusicScreen(selectedMusicTypes: ArrayList<UserOptionDTO>, allMusicTypes: ArrayList<UserOptionDTO>, id: Int)
        fun openPlaceTypeScreen(selectedMusicTypes: ArrayList<UserFavoritePlaceTypeDTO>, allMusicTypes: ArrayList<UserFavoritePlaceTypeDTO>)
        fun openDaysGoOutScreen()
        fun openChangePasswordScreen()
        fun openOccupation(map: HashMap<String, String>?)
        fun openLocation()
        fun openSettings()
        //fun openEditUserScreen()
        fun openEditFirstName()

        fun openEditLastName()

        fun closeScreen(isUpdateUserData: Boolean)

        fun onResume()
        fun onDestroy()

        fun showGallery()
        fun openCamera()

        fun onDaysGoOutRewriteAction(): Observable<Any>
        fun onFavoriteMusicRewriteAction(): Observable<Any>
        fun onFavoritePlacesRewriteAction(): Observable<Any>
        fun onOccupationRewriteAction(): Observable<Any>
        fun onSearchRewriteAction(): Observable<Any>
        fun onNewImageAction(): Observable<ProfileRemainder.ImageState>
        fun onUserEditRewriteAction(): Observable<Any>
        fun onSettingRewriteAction(): Observable<Boolean>
        fun onBackPressedAction(): Observable<Any>
        fun onBackPressed()
    }
}

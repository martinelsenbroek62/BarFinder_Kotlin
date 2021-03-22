package com.lemust.ui.screens.profile

import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.support.v7.app.AlertDialog
import android.util.Log
import com.lemust.R
import com.lemust.repository.api.RestManagerImplNull
import com.lemust.repository.models.rest.user.get.UserDTO
import com.lemust.repository.models.rest.user.get.UserFavoritePlaceTypeDTO
import com.lemust.repository.models.rest.user.get.UserOptionDTO
import com.lemust.repository.models.rest.user.path.PathUserDTO
import com.lemust.repository.models.rest.user.reset.ImageResetDTO
import com.lemust.ui.base.BaseView
import com.lemust.ui.base.dialog.DialogModel
import com.lemust.ui.screens.details.dialog.adapter.MenuItem
import com.lemust.ui.screens.profile.adapter.Item
import com.lemust.utils.*
import com.squareup.otto.Bus
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.io.File
import java.lang.Exception
import java.net.ProtocolException


class ProfilePresenter(var view: ProfileContract.View, var remainder: ProfileContract.Remainder, var eventBus: Bus) : ProfileContract.Presenter {
    private var compositeDisposable = CompositeDisposable()
    private var isLanguageChanged = false
    var user = AppHelper.preferences.getUser()!!
    var photoActionDisposable: Disposable? = null

    val SELECT_PHOTO_ID = 0
    val TAKE_PHOTO_ID = 1
    val DELETE_PHOTO_ID = -1
    var isActivityDestroyed = false


    init {
        initUiData()
        initAction()
        handleData()
    }

    private fun initUiData() {
        view.setPhotoMenuItems(initBottomSheetItems())
        if (user != null) {
            var isShowChangePasswordScreen = user.hasPassword!!
            if (!isShowChangePasswordScreen) {
                view.hideChangePasswordScreen()
            }
        }
    }

    private fun initBottomSheetItems(): ArrayList<MenuItem> {
        var items = ArrayList<MenuItem>()
        items.add(MenuItem(remainder.getContext().getString(R.string.title_select_photo), SELECT_PHOTO_ID))
        items.add(MenuItem(remainder.getContext().getString(R.string.title_take_new_photo), TAKE_PHOTO_ID))


        if (user.image != null) {
            if (user!!.image!!.isNotEmpty()) {
                items.add(MenuItem(remainder.getContext().getString(R.string.title_delete_photo), DELETE_PHOTO_ID))

            }
        }

        return items
    }

    override fun onDestroy() {
        isActivityDestroyed = true
    }

    private fun initAction() {

        view.onPlaceTypeAction().subscribe {
            remainder.openPlaceTypeScreen(user!!.favoritePlaceTypes!!.filter {
                it.isSelected == true
            } as java.util.ArrayList<UserFavoritePlaceTypeDTO>, AppHelper.preferences.getUser()!!.favoritePlaceTypes!!)
        }
        view.onDaysGoOutAction().subscribe {
            remainder.openDaysGoOutScreen()
        }
        view.onUserBirthDateAction().subscribe {
            view.showDateDialog(user.birthdate).subscribe {
                setUserBirthDate(it)

            }
        }
        view.onChangeActionPassword().subscribe {
            remainder.openChangePasswordScreen()
        }

        view.onOccupationAction().subscribe {
            remainder.openOccupation(user.occupationChoices!!.map)
        }

        remainder.onDaysGoOutRewriteAction().subscribe {
            user = AppHelper.preferences.getUser()!!
            initUsersDaysGoOut()
        }


        remainder.onFavoriteMusicRewriteAction().subscribe {
            user = AppHelper.preferences.getUser()!!

            if (user!!.favorites!!.isEmpty()) {
            } else {
                var items = user.favorites!!.map { Item(it.id, it.name, it.options) }
                view.setItems(items)
            }
        }
        remainder.onFavoritePlacesRewriteAction().subscribe {
            user = AppHelper.preferences.getUser()!!
            initUsersPlaceType()
        }

        view.onLocationAction().subscribe {
            remainder.openLocation()
        }
        remainder.onOccupationRewriteAction().subscribe {
            user = AppHelper.preferences.getUser()!!
            initUserOccupation()

        }
        remainder.onSearchRewriteAction().subscribe {
            user = AppHelper.preferences.getUser()!!
            initUserLocation()
        }
        view.onImageAvatarAction().subscribe {
            view.showAvatarDialog()
        }

        remainder.onNewImageAction().subscribe {
            user = AppHelper.preferences.getUser()!!

            if (it.errorLoad.isEmpty()) {
                if (it.isStartLoad) {
                    view.isShowLoaderPhoto(true)
                } else {
                    uploadPhoto(it.path)

                }
            } else {
                view.isShowLoaderPhoto(false)

                view.showDialog(DialogModel().build(view.getViewContext(), view.getViewContext().resources.getString(R.string.title_error)).showMessage(it.errorLoad).single(true)
                        .isAutoCloseFirstButton(true).showFirstButton("Ok"))
            }
        }

        view.onSettingsAction().subscribe {
            remainder.openSettings()
        }
        view.onEditFirstName().subscribe {
            remainder.openEditFirstName()
        }
        view.onEditLastName().subscribe {
            remainder.openEditLastName()
        }
        remainder.onUserEditRewriteAction().subscribe {
            user = AppHelper.preferences.getUser()!!
            initUserInformation()
        }
        remainder.onSettingRewriteAction().subscribe {
            user = AppHelper.preferences.getUser()!!
            isLanguageChanged = it
            handleChangeLanguage(it)
        }

        remainder.onBackPressedAction().subscribe {
            if (isLanguageChanged)
                remainder.closeScreen(true)
            else
                remainder.closeScreen(false)


        }




        view.onOtherItemsAction().subscribe {
            remainder.openTypesMusicScreen(it.option as java.util.ArrayList<UserOptionDTO>, it.option.filter { it.isSelected == true } as java.util.ArrayList<UserOptionDTO>, it.id)

        }

        bottomSheetAction()

    }

    private fun handleChangeLanguage(it: Boolean) {
        if (it) {
            view.changeTextInProgressBar(remainder.getContext().resources.getString(R.string.string_updating))
            view.isShowProgressLoader(true)
            AppHelper.api.getUser(AppHelper.preferences.getToken(), AppHelper.preferences.getUserId()).subscribe(object : Observer<UserDTO> {
                override fun onComplete() {

                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: UserDTO) {
                    AppHelper.preferences.saveUserObj(t)

                    user = t
                    view.isShowProgressLoader(false)
                    compositeDisposable.dispose()
                    view.updateDialogResources()
                    view.updateResources()
                    bottomSheetAction()
                    initUserInformation()
                    initUserBirth()
                    initUserLocation()
                    initAvatar()
                    initUserOccupation()
                    initUsersPlaceType()
                    initUsersDaysGoOut()
                    initVisited()
                    initFavourite()
                    view.updateBottomSheetItems(initBottomSheetItems())


                }

                override fun onError(e: Throwable) {

                    view.isShowProgressLoader(false)
                    Handler(Looper.getMainLooper()).post {
                        try {

                            var errorMessage = ErrorUtils(e, false)
                            errorMessage.parse()
                            view.showDialogWithOneButtons(errorMessage.titleError, errorMessage.bodyError, remainder.getContext().resources.getString(R.string.title_ok),
                                    object : BaseView.DialogController1 {
                                        override fun action1(dialog: AlertDialog) {
                                            dialog.dismiss()
                                        }
                                    })
                        } catch (e: Exception) {
                            System.out.print(e.localizedMessage)
                        }
                    }

                }
            })

        }
    }

    private fun bottomSheetAction() {
        if (photoActionDisposable != null) {
            if (!photoActionDisposable!!.isDisposed) {
                photoActionDisposable!!.dispose()
            }
        }

        photoActionDisposable = view.onBottomSheetAction().subscribe {


            when (it.id) {
                TAKE_PHOTO_ID -> {
                    view.closePhotoDialog()
                    remainder.openCamera()
                }
                SELECT_PHOTO_ID -> {
                    view.closePhotoDialog()
//                    view.openGallery()
                    remainder.showGallery()
                }
                DELETE_PHOTO_ID -> {
                    view.isShowProgressLoader(true)
                    view.closePhotoDialog()

                    RestManagerImplNull().api.changeUserImage(ImageResetDTO(null)).subscribe(object : Observer<UserDTO> {
                        override fun onComplete() {
                        }

                        override fun onSubscribe(d: Disposable) {

                        }

                        override fun onNext(t: UserDTO) {
                            user = t
                            AppHelper.preferences.saveUserObj(t)

                            view.updateBottomSheetItems(initBottomSheetItems())
                            view.isShowProgressLoader(false)
                            view.resetUserPhoto()
                            AppDataHolder.actualUserPhoto = ""

                        }

                        override fun onError(e: Throwable) {
                            if (e is ProtocolException) {
                                Log.d("is_prot_e", "protocol ex")
                            }


                            try {
                                Handler(Looper.getMainLooper()).post {
                                    var errorMessage = ErrorUtils(e, false)
                                    errorMessage.parse()
                                    view.showDialogWithOneButtons(errorMessage.titleError, errorMessage.bodyError, remainder.getContext().resources.getString(R.string.title_ok),
                                            object : BaseView.DialogController1 {
                                                override fun action1(dialog: AlertDialog) {
                                                    dialog.dismiss()
                                                }
                                            })
                                }
                                view.isShowLoaderPhoto(false)
                            } catch (e: Exception) {
                                System.out.print(e.localizedMessage)
                            }
                        }
                    })

                }
            }
        }

    }

    private fun uploadPhoto(it: String?) {
        Thread(Runnable {
            AppHelper.api.uploadUserAvatar(File(it)).subscribe(object : Observer<UserDTO> {
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: UserDTO) {
                    user = t

                    AppHelper.preferences.saveUserObj(t)
                    AppDataHolder.actualUserPhoto = it!!
                    if (!isActivityDestroyed) {
                        view.setUserAvatar(BitmapFactory.decodeFile(it))
                        view.isShowLoaderPhoto(false)
                        view.updateBottomSheetItems(initBottomSheetItems())
                    }

                }

                override fun onError(e: Throwable) {
                    Handler(Looper.getMainLooper()).post {
                        var errorMessage = ErrorUtils(e, false)
                        errorMessage.parse()
                        view.showDialogWithOneButtons(errorMessage.titleError, errorMessage.bodyError, remainder.getContext().resources.getString(R.string.title_ok),
                                object : BaseView.DialogController1 {
                                    override fun action1(dialog: AlertDialog) {
                                        dialog.dismiss()
                                    }
                                })
                    }
                    view.isShowLoaderPhoto(false)

                }


            })
        }).start()
    }

    private fun setUserBirthDate(it: DatePickerFragment.Date) {
        var day = it.day.toString()
        var month = it.month.toString()
        var year = it.year.toString()

        if (day.length == 1) {
            day = "0$day"
        }
        if (month.length == 1) {
            month = "0$month"
        }
        saveBirthDate("$year-$month-$day")
    }

    private fun saveBirthDate(s: String) {
        view.changeTextInProgressBar(remainder.getContext().resources.getString(R.string.string_updating))
        view.isShowProgressLoader(true)
        AppHelper.api.changeUserInformation(AppHelper.preferences.getToken(), AppHelper.preferences.getUserId(), PathUserDTO(birthdate = s)).subscribe(object : Observer<Any> {
            override fun onComplete() {
            }

            override fun onSubscribe(d: Disposable) {
            }

            override fun onNext(t: Any) {
                view.setBirthDate(s)
                view.isShowProgressLoader(false)

            }

            override fun onError(e: Throwable) {
                try {
                    view.isShowProgressLoader(false)
                    var errorMessage = ErrorUtils(e, false)
                    errorMessage.parse()
                    view.showDialogWithOneButtons(errorMessage.titleError, errorMessage.bodyError, remainder.getContext().resources.getString(R.string.title_ok),
                            object : BaseView.DialogController1 {
                                override fun action1(dialog: AlertDialog) {
                                    dialog.dismiss()
                                }
                            })

                } catch (e: Exception) {
                    System.out.print(e.localizedMessage)
                }
            }
        })
    }

    private fun handleData() {
        var user = AppHelper.preferences.getUser()


        var items = user!!.favorites!!.map { Item(it.id, it.name, it.options) }
        view.setItems(items)



        if (AppHelper.preferences.isLanguageChangedForProfile()) {
            AppHelper.preferences.setLanguageChangedForProfile(false)
            handleChangeLanguage(true)
        } else {
            initUserInformation()
            initUserBirth()
            initUserLocation()
            initAvatar()
            initUserOccupation()
            initUsersPlaceType()
            initUsersDaysGoOut()
            initVisited()
            initFavourite()
        }
    }

    private fun initUserInformation() {

        if (user!!.firstName != null)
            if (user.firstName!!.isNotEmpty()) {
                view.setFirstName(user.firstName!!)
            } else {
                view.setFirstName(remainder.getContext().resources.getString(R.string.title_not_set))

            }



        if (user.lastName != null)
            if (user.lastName!!.isNotEmpty()) {
                view.setLastName(user.lastName!!)
            } else {
                view.setLastName(remainder.getContext().resources.getString(R.string.title_not_set))

            }


    }

    private fun initAvatar() {
        var user = AppHelper.preferences.getUser()

        if (user!!.image != null)
            view.setUserAvatar(user.image!!)
        else view.setDefaultAvatar()
    }

    private fun initFavourite() {


    }

    private fun initVisited() {

    }

    private fun initUserOccupation() {
        var textOccupation = ""
        var textOccupationDetails = ""

        var user = AppHelper.preferences.getUser()
        if (user!!.occupation != null) {
            var id = user.occupation
            textOccupation = TextHelper.cap1stChar(user.occupationChoices!!.map!![id.toString()].toString())
        }

        if (textOccupation.isNotEmpty()) {
            view.setOccupation("$textOccupation $textOccupationDetails".trim())
        } else {
            view.setOccupation(remainder.getContext().resources.getString(R.string.title_not_set))

        }
        ///    }


    }

    private fun initUsersDaysGoOut() {
        var items = TimeHelper.getResourcesPluralWeekDays(remainder.getContext())

        var selectedDays = ArrayList<String>()
        if (user.goOutDays != null) {
            user.goOutDays!!.forEach {
                selectedDays.add(items[it])

            }
            if (!selectedDays.isEmpty())
                view.setDaysGoOut(Tools.appendStrings(selectedDays))
            else
                view.setDaysGoOut(remainder.getContext().resources.getString(R.string.title_not_set))

        }
    }

    private fun initUsersPlaceType() {
        if (user!!.favoritePlaceTypes != null) {
            var selectedType = user.favoritePlaceTypes!!.filter { it.isSelected == true }.map { it.name }
            if (!selectedType.isEmpty()) {
                var title = Tools.appendStrings(selectedType as List<String>)
                view.setUserSelectedPlaceType(title)
            } else
                view.setUserSelectedPlaceType(remainder.getContext().resources.getString(R.string.title_not_set))

        } else
            view.setUserSelectedPlaceType(remainder.getContext().resources.getString(R.string.title_not_set))


    }


    private fun initUserLocation() {
        var user = AppHelper.preferences.getUser()
        if (user!!.city != null) {
            view.setUserLocation(user!!.city!!.name!!)
        } else
            view.setUserLocation(remainder.getContext().resources.getString(R.string.title_not_set))

    }

    private fun initUserBirth() {

        var user = AppHelper.preferences.getUser()

        if (user!!.birthdate !== null) {
            if (!user!!.birthdate!!.isEmpty()) {
                view.setBirthDate(user!!.birthdate!!)

            } else
                view.setBirthDate(remainder.getContext().resources.getString(R.string.title_not_set))
        } else
            view.setBirthDate(remainder.getContext().resources.getString(R.string.title_not_set))

    }

}
package com.lemust.ui.screens.profile.favorites_place

import com.lemust.LeMustApp
import com.lemust.R
import com.lemust.repository.models.rest.user.get.UserDTO
import com.lemust.repository.models.rest.user.get.UserFavoritePlaceTypeDTO
import com.lemust.repository.models.rest.user.path.PathUserDTO
import com.lemust.ui.base.dialog.DialogModel
import com.lemust.ui.screens.profile.favorites_place.adapter.FavoriteIPlacestem
import com.lemust.utils.AppHelper
import com.lemust.utils.ErrorUtils
import com.squareup.otto.Bus
import io.reactivex.Observer
import io.reactivex.disposables.Disposable


class FavoritePlacePresenter(var view: FavoritePlaceContract.View, var eventBus: Bus, var remainder: FavoritePlaceContract.Remainder, var selectedMusicTypes: ArrayList<UserFavoritePlaceTypeDTO>, var allMusicTypes: ArrayList<UserFavoritePlaceTypeDTO>) : FavoritePlaceContract.Presenter {
    init {
        initDate()
        initAction()
    }

    private fun initAction() {

        remainder.onBackPressedAction().subscribe {
            remainder.finish()


        }
        view.onApplyAction().subscribe {
            saveFavoritePlaces()

        }
    }

    private fun saveFavoritePlaces() {
        var days = ArrayList<UserFavoritePlaceTypeDTO>()
        view.getFavoriteMusicsItem().forEach {
            if (it.isSelected!!) {
                it.option.isSelected = true
                days.add(it.option)
            }
        }


        var placesId = days.map { it.id }

        if (selectedMusicTypes.hashCode() != days.hashCode())
            AppHelper.api.changeUserInformation(AppHelper.preferences.getToken(), AppHelper.preferences.getUserId(), PathUserDTO(favoritePlaceTypes = placesId)).subscribe(object : Observer<UserDTO> {
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                    view.changeTextInProgressBar(remainder.getContext().resources.getString(R.string.string_updating))
                    view.isShowProgressLoader(true)
                }

                override fun onNext(t: UserDTO) {
                    view.isShowProgressLoader(false)
                    remainder.finish()


                }

                override fun onError(e: Throwable) {
                    try {
                        view.isShowProgressLoader(false)
                        var errorMessage = ErrorUtils(e, false)
                        errorMessage.parse()

                        view.showDialog(DialogModel().build(view.getViewContext(), errorMessage.titleError)
                                .showMessage(errorMessage.bodyError)
                                .showFirstButton(view.getViewContext().resources.getString(R.string.title_ok))
                                .single(true)).subscribe {
                            if (DialogModel.State.FIRST_BUTTON == it.clicked) {
                                it.dialog.dismiss()

                            }
                        }
                    } catch (e: Exception) {
                        System.out.print(e.localizedMessage)
                    }

                }
            })
        else
            remainder.finish()
    }

    private fun initDate() {
        var selectedCheck = allMusicTypes.map { FavoriteIPlacestem(it, false) }
        selectedCheck.forEach {
            if (selectedMusicTypes.contains(it.option)) {
                it.isSelected = true
            }
        }


        view.setMusicItems(selectedCheck)
    }


}
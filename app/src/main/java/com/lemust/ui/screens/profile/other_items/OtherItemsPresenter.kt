package com.lemust.ui.screens.profile.other_items

import com.lemust.LeMustApp
import com.lemust.R
import com.lemust.repository.models.rest.user.get.UserDTO
import com.lemust.repository.models.rest.user.get.UserOptionDTO
import com.lemust.repository.models.rest.user.path.PathFavoriteDTO
import com.lemust.repository.models.rest.user.path.PathUserDTO
import com.lemust.ui.base.dialog.DialogModel
import com.lemust.ui.screens.profile.other_items.adapter.FavoriteMusicItem
import com.lemust.utils.AppHelper
import com.lemust.utils.ErrorUtils
import com.squareup.otto.Bus
import io.reactivex.Observer
import io.reactivex.disposables.Disposable


class OtherItemsPresenter(var view: OtherItemsContract.View, var eventBus: Bus, var remainder: OtherItemsContract.Remainder, var selectedMusicTypes: ArrayList<UserOptionDTO>, var allMusicTypes: ArrayList<UserOptionDTO>, var id: Int) : OtherItemsContract.Presenter {
    private val CURRENT_ITEM_KEY = id
    var user = AppHelper.preferences.getUser()

    init {
        initDate()
        initAction()
        view.changeTextInProgressBar(remainder.getContext().resources.getString(R.string.string_updating))


    }

    private fun initAction() {

        remainder.onBackPressedAction().subscribe {
            remainder.finish()

        }
        view.onApplyAction().subscribe {
            saveFavoriteMusic()

        }
    }

    private fun saveFavoriteMusic() {

        var days = mutableListOf<UserOptionDTO>()
        view.getFavoriteMusicsItem().forEach {
            if (it.isSelected!!) {
                it.option.isSelected = true
                days.add(it.option)
            }
        }


        var newFavoriteMusic = user!!.favorites!!.filter { it.id == CURRENT_ITEM_KEY }
        var favoritesMusic = PathFavoriteDTO(newFavoriteMusic[0].id, days.map { it.id!! })

        if (selectedMusicTypes.hashCode() != days.hashCode())
            AppHelper.api.changeUserInformation(AppHelper.preferences.getToken(), AppHelper.preferences.getUserId(), PathUserDTO(favorites = mutableListOf(favoritesMusic))).subscribe(object : Observer<UserDTO> {
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                    view.isShowProgressLoader(true)
                }

                override fun onNext(t: UserDTO) {
                    view.isShowProgressLoader(false)
                    remainder.finish()


                }

                override fun onError(e: Throwable) {
                    try {


                        view.isShowProgressLoader(false)
                        var errorMessage = ErrorUtils(e, false, view.getViewContext())
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
        user!!.favorites!!.filter { it.id == CURRENT_ITEM_KEY }.map {
            view.setMusicTypeTitle(it.name)
        }
        var places = ArrayList<FavoriteMusicItem>()

        allMusicTypes!!.forEach {
            if (selectedMusicTypes!!.contains(it)) {
                places.add(FavoriteMusicItem(it, true))
            } else {
                places.add(FavoriteMusicItem(it, false))

            }
        }
        view.setMusicItems(places)

    }


}
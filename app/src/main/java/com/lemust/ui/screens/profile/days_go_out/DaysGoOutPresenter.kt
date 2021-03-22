package com.lemust.ui.screens.profile.days_go_out

import com.lemust.LeMustApp
import com.lemust.R
import com.lemust.repository.models.rest.user.path.PathUserDTO
import com.lemust.ui.base.dialog.DialogModel
import com.lemust.ui.screens.profile.days_go_out.adapter.WeekDayItem
import com.lemust.utils.AppHelper
import com.lemust.utils.ErrorUtils
import com.lemust.utils.TimeHelper
import com.squareup.otto.Bus
import io.reactivex.Observer
import io.reactivex.disposables.Disposable


class DaysGoOutPresenter(var view: DaysGoOutContract.View, var remainder: DaysGoOutContract.Remainder, var eventBus: Bus) : DaysGoOutContract.Presenter {
    private val TYPE_MUSICK_KEY = "Style of music"
    private val oldDays = ArrayList<Int>()
    var user = AppHelper.preferences.getUser()!!

    init {
        initDate()
        initAction()


    }

    private fun initAction() {
//        view.onBackAction().subscribe {
//            onBackAction()
//        }
        remainder.onBackPressedAction().subscribe {
            remainder.finish()
        }

//        view.onBackAction().subscribe {
//            // saveFavoriteMusic()
//            remainder.finish()
//
//        }
//        remainder.onBackPressedAction().subscribe {
//            // saveFavoriteMusic()
//            remainder.finish()
//
//
//        }
        view.onApplyAction().subscribe {
            saveDays()

        }
    }

//    private fun onBackAction() {
//        saveDays()
//    }

    private fun saveDays() {

        var days = mutableListOf<Int>()
        view.getDays().forEachIndexed { index, hourItem ->
            if (hourItem.isSelected) {
                days.add(index)
            }
        }

        if (oldDays.hashCode() != days.hashCode())
            AppHelper.api.changeUserInformation(AppHelper.preferences.getToken(), AppHelper.preferences.getUserId(), PathUserDTO(goOutDays = days)).subscribe(object : Observer<Any> {
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                    view.changeTextInProgressBar(remainder.getContext().resources.getString(R.string.string_updating))
                    view.isShowProgressLoader(true)
                }

                override fun onNext(t: Any) {
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
        var items = TimeHelper.getResourcesPluralWeekDays(remainder.getContext())
        var currentDays = ArrayList<WeekDayItem>()

        if (user.goOutDays != null) {
            oldDays.addAll(user.goOutDays!!)
        }

        items!!.forEachIndexed { index, s ->
            if (oldDays!!.contains(index)) {
                currentDays.add(WeekDayItem(s, true))
            } else {
                currentDays.add(WeekDayItem(s, false))

            }
        }
        view.setMusicItems(currentDays)
    }


}
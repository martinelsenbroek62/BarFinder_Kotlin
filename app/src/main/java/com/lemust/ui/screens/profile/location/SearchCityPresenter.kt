package com.lemust.ui.screens.profile.location

import android.support.v7.app.AlertDialog
import com.lemust.LeMustApp
import com.lemust.R
import com.lemust.repository.api.RestManagerImplNull
import com.lemust.repository.models.rest.search.SearchCityDTO
import com.lemust.repository.models.rest.user.get.UserDTO
import com.lemust.repository.models.rest.user.path.PathUserDTO
import com.lemust.repository.models.rest.user.reset.CityResetDTO
import com.lemust.ui.base.BaseActivity
import com.lemust.ui.base.BaseView
import com.lemust.ui.base.dialog.DialogModel
import com.lemust.ui.screens.profile.location.adapter.SearchCitytem
import com.lemust.utils.AppHelper
import com.lemust.utils.ErrorUtils
import com.squareup.otto.Bus
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import java.lang.Exception


class SearchCityPresenter(var view: SearchCityContract.View, var eventBus: Bus, var activity: BaseActivity) : SearchCityContract.Presenter {
    var searchDisposable: Disposable? = null
    var resetDisposable: Disposable? = null
    var user = AppHelper.preferences.getUser()!!
    var isLoadDataAtStartup = true

    init {
        initData()
        initAction()
    }

    private fun initData() {
        if (user!!.city != null) {
            view.setDefaultCirt(user.city!!.name!!)
            isLoadDataAtStartup = false
        }
    }


    private fun initAction() {
        view.onScrollDown().subscribe {
            view.hideKeyboard()
        }
        view.onClearAction().subscribe {
            cancelLoadCity()
            view.setVisibleContent(true)
            view.setVisibleLoader(false)
        }
        view.getPlacesName().subscribe { text ->
            handleSearch(text)
        }
        view.onTouchItemEvent().subscribe {
            view.setVisibleLoader(true)

            saveCity(it.city.id)
        }

        view.onResetAction().subscribe {
            view.setVisibleLoader(true)
            RestManagerImplNull().api.resetUserCity(CityResetDTO(null)).subscribe(object : Observer<UserDTO> {
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                    resetDisposable?.let {
                        if (!resetDisposable!!.isDisposed) {
                            resetDisposable!!.dispose()
                        }
                    }
                    resetDisposable = d

                }

                override fun onNext(t: UserDTO) {
                    view.setVisibleLoader(false)
                    view.setDefaultCirt("")
                }

                override fun onError(e: Throwable) {
                    try {
                        var errorMessage = ErrorUtils(e, true, view.getViewContext())
                        errorMessage.parse()
                        view.showDialogWithOneButtons(errorMessage.titleError, errorMessage.bodyError, view.getViewContext().resources.getString(R.string.title_ok),
                                object : BaseView.DialogController1 {
                                    override fun action1(dialog: AlertDialog) {
                                        if (errorMessage.isInternetError)
                                            dialog.dismiss()
                                    }
                                })
                    } catch (e: Exception) {
                        System.out.print(e.localizedMessage)
                    }
                }
            })
        }

    }

    private fun handleSearch(text: CharSequence) {
        if (isLoadDataAtStartup) {
            if (!text.trim().isEmpty()) {
                loadPlaces(text.trim().toString())
            } else {
                view.setVisibleContent(true)
                view.setVisibleLoader(false)
                cancelLoadCity()
                view.clearCity()
            }
        } else {
            isLoadDataAtStartup = true
        }
    }

    private fun cancelLoadCity() {
        if (searchDisposable != null)
            if (!searchDisposable!!.isDisposed) {
                searchDisposable!!.dispose()
                //   isLoadDataAtStartup = true
            }
    }

    private fun saveCity(id: Int?) {
        view.changeTextInProgressBar(activity.resources.getString(R.string.string_updating))
        // view.setVisibleLoader(true)

        AppHelper.api.changeUserInformation(AppHelper.preferences.getToken(), AppHelper.preferences.getUserId(), PathUserDTO(city = id)).subscribe(object : Observer<UserDTO> {
            override fun onComplete() = Unit
            override fun onSubscribe(d: Disposable) = Unit
            override fun onNext(t: UserDTO) {
                view.setVisibleLoader(false)
                activity.finish()

            }

            override fun onError(e: Throwable) {
                view.setVisibleLoader(false)
                var errorMessage = ErrorUtils(e, false, view.getViewContext())
                errorMessage.parse()

                view.showDialog(DialogModel().build(view.getViewContext(), errorMessage.titleError)
                        .showMessage(errorMessage.bodyError).single(true)
                        .showFirstButton(view.getViewContext().resources.getString(R.string.title_ok))
                        .isAutoCloseFirstButton(true))

            }
        })
    }

    public fun loadPlaces(name: String) {
        view.setVisibleLoader(true)
        AppHelper.api.searchCity(AppHelper.preferences.getToken(), name).subscribe(placesObservable)

    }

    var placesObservable = object : Observer<List<SearchCityDTO>> {
        override fun onComplete() = Unit
        override fun onSubscribe(d: Disposable) {
            cancelLoadCity()
            searchDisposable = d
            view.setVisibleContent(false)


        }

        override fun onNext(t: List<SearchCityDTO>) {
            view.setData(t.map { SearchCitytem(it, false) })
            view.setVisibleContent(true)
            view.setVisibleLoader(false)


        }

        override fun onError(e: Throwable) {
            try {
                view.setVisibleContent(true)
                view.setVisibleLoader(false)
                view.hideKeyboard()
                var errorMessage = ErrorUtils(e, false)
                errorMessage.parse()
                view.showDialogWithOneButtons(errorMessage.titleError, errorMessage.bodyError, view.getViewContext().resources.getString(R.string.title_ok),
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

}
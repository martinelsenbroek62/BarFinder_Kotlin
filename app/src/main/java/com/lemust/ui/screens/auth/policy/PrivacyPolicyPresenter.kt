package com.lemust.ui.screens.auth.policy

import android.support.v7.app.AlertDialog
import com.lemust.LeMustApp
import com.lemust.R
import com.lemust.repository.models.rest.StoringPageDTO
import com.lemust.ui.base.BaseView
import com.lemust.utils.AppHelper
import com.lemust.utils.ErrorUtils
import com.squareup.otto.Bus
import io.reactivex.Observer
import io.reactivex.disposables.Disposable


class PrivacyPolicyPresenter(var view: PrivacyPolicyContract.View, var eventBus: Bus, var typePage: String, var typeIconBackArrow: Boolean) : PrivacyPolicyContract.Presenter {

    init {
        initView()
        loadData()
        initAction()

    }

    private fun initView() {
        view.initButtonBackArrow(typeIconBackArrow)

    }

    private fun loadData() {
        view.isShowProgressLoader(true)
        AppHelper.api.getStoringPage(typePage).subscribe(object : Observer<StoringPageDTO> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {
            }

            override fun onNext(t: StoringPageDTO) {
                if (t.title != null)
                    view.setTitle(t.title!!)

                if (t.shortDescription != null)
                    view.setShortDescription(t.shortDescription!!)

                if (t.text != null)
                    view.setText(t.text!!)

               // setTypeDescription()
                view.isShowProgressLoader(false)

            }

            override fun onError(e: Throwable) {
                view.isShowProgressLoader(false)
                view.isShowProgressLoader(false)
                var errorMessage = ErrorUtils(e, false)
                errorMessage.parse()
                view.showDialogWithOneButtons(errorMessage.titleError, errorMessage.bodyError, view.getViewContext().resources.getString(R.string.title_ok),
                        object : BaseView.DialogController1 {
                            override fun action1(dialog: AlertDialog) {
                                dialog.dismiss()
                            }
                        })
            }
        })

    }

    private fun setTypeDescription() {
        when (typePage) {
            PrivacyPolicyActivity.SLUG_TERMS -> {
                view.setTypeDescription("Terms and Conditions  short description")

            }
            PrivacyPolicyActivity.SLUG_POLICY -> {
                view.setTypeDescription("Privacy Policy  short description")


            }
        }
    }

    private fun initAction() {
        view.onBackAction().subscribe { view.back() }
    }

}
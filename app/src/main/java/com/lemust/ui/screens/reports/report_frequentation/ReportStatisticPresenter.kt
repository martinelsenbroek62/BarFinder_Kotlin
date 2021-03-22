package com.lemust.ui.screens.reports.report_frequentation

import android.os.Handler
import android.os.Looper
import android.support.v7.app.AlertDialog
import com.lemust.R
import com.lemust.repository.models.rest.report.ReportFrequentationDTO
import com.lemust.ui.base.BaseView
import com.lemust.ui.base.dialog.DialogModel
import com.lemust.utils.AppHelper
import com.squareup.otto.Bus
import io.reactivex.Observer
import io.reactivex.disposables.Disposable


class ReportStatisticPresenter(var view: ReportStatisticContract.View, var eventBus: Bus, var remainder: ReportStatisticContract.Remainder, var placeId: Int) : ReportStatisticContract.Presenter {
    var currentProgress: Int = 0

    init {
        initAction()
    }

    private fun initAction() {
        view.onChangeProgress().subscribe {
            currentProgress = it
            handleSeekColor(it)
            view.setProgress(it)
            view.onChangeStatistic(it)
        }
        view.onSendReportAction().subscribe {

            sendReport()
        }
    }

    private fun sendReport() {
        view.isShowProgressLoader(true)
        AppHelper.api.reportFrequentation(ReportFrequentationDTO(placeId.toString(), currentProgress)).subscribe(object : Observer<ReportFrequentationDTO> {
            override fun onComplete() =Unit
            override fun onSubscribe(d: Disposable)=Unit
            override fun onNext(t: ReportFrequentationDTO) {
                view.isShowProgressLoader(false)
                view.showPositiveDialogOkCallback(remainder.getContext().getString(R.string.thanks_for_you_update), remainder.getContext().getString(R.string.thanks_update)).subscribe {
                    remainder.dismiss()

                }
            }

            override fun onError(e: Throwable) {
                view.isShowProgressLoader(false)
                Handler(Looper.getMainLooper()).post {
                    view.showDialog(DialogModel().build(remainder.getContext(), remainder.getContext().resources.getString(R.string.no_internet_connection))
                            .showFirstButton(remainder.getContext().resources.getString(R.string.title_ok))
                            .single(true)).subscribe {
                        if (DialogModel.State.FIRST_BUTTON == it.clicked) {
                            it.dialog.dismiss()
                        }
                    }
                }




            }
        })


    }

    private fun handleSeekColor(it: Int?) {
        when (it) {
            in 0..0 -> {
                view.setSeekBarColor(remainder.getContext().resources.getColor(R.color.colorPinCircle10))
            }
            in 1..10 -> {
                view.setSeekBarColor(remainder.getContext().resources.getColor(R.color.colorPinCircle10))


            }
            in 11..20 -> {
                view.setSeekBarColor(remainder.getContext().resources.getColor(R.color.colorPinCircle20))


            }
            in 21..30 -> {
                view.setSeekBarColor(remainder.getContext().resources.getColor(R.color.colorPinCircle30))

            }
            in 31..40 -> {
                view.setSeekBarColor(remainder.getContext().resources.getColor(R.color.colorPinCircle40))

            }
            in 41..50 -> {
                view.setSeekBarColor(remainder.getContext().resources.getColor(R.color.colorPinCircle50))


            }
            in 51..60 -> {

                view.setSeekBarColor(remainder.getContext().resources.getColor(R.color.colorPinCircle60))

            }
            in 61..70 -> {
                view.setSeekBarColor(remainder.getContext().resources.getColor(R.color.colorPinCircle70))


            }
            in 71..80 -> {
                view.setSeekBarColor(remainder.getContext().resources.getColor(R.color.colorPinCircle80))

            }
            in 81..90 -> {
                view.setSeekBarColor(remainder.getContext().resources.getColor(R.color.colorPinCircle90))


            }
            in 91..100 -> {
                view.setSeekBarColor(remainder.getContext().resources.getColor(R.color.colorPinCircle100))


            }
        }
    }
}
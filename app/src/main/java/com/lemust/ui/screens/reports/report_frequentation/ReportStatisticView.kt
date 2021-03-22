package com.lemust.ui.screens.reports.report_frequentation

//import com.stfalcon.frescoimageviewer.ImageViewer
import android.graphics.PorterDuff
import android.view.View
import android.widget.SeekBar
import com.jakewharton.rxbinding2.view.RxView
import com.lemust.R
import com.lemust.ui.base.BaseActivity
import com.lemust.ui.base.BaseView
import com.lemust.ui.base.views.base.GraphicsTools
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_report_statistic.view.*


class ReportStatisticView(var ctx: BaseActivity, var root: View) : ReportStatisticContract.View, BaseView(ctx) {
    override fun setProgress(progress: Int) {
        root.tv_progress.text = progress.toString() + " %"
    }

    override fun onSendReportAction(): Observable<Any> {
        return RxView.clicks(root.tv_send)
    }

    override fun setSeekBarColor(color: Int) {
        var fColor = color

        root.test_seek_bar.progressDrawable.setColorFilter(fColor, PorterDuff.Mode.MULTIPLY);
        root.test_seek_bar.thumb = GraphicsTools.getCustomTrumbDrawable(color, color)
    }


    public var onChangeProgressAtion = PublishSubject.create<Int>()

    init {
        root.test_view.initView()
        initAction()
        root.iv_close.setOnClickListener {
            ctx.finish()
        }
        setSeekBarColor(ctx.resources.getColor(R.color.colorPinCircle10))


    }

    override fun isShowProgressLoader(isShow: Boolean) {
        ctx.showDefaultProgressLoader(isShow)

    }


    override fun onChangeProgress(): Observable<Int> {
        return onChangeProgressAtion
    }

    private fun initAction() {
        root.test_seek_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                onChangeProgressAtion.onNext(p1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })
    }


    override fun onChangeStatistic(pogress: Int) {
        root.test_view.setProgress(pogress)

    }
}
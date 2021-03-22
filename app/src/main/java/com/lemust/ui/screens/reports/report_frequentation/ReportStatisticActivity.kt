package com.lemust.ui.screens.reports.report_frequentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lemust.R
import com.lemust.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_report_statistic.view.*

class ReportStatisticActivity : BaseActivity() {
    companion object {
        val placeIdKey = "place_id_key"
        fun start(context: Context, placeId: Int) {
            var intent = Intent(context, ReportStatisticActivity::class.java)
            intent.putExtra(placeIdKey, placeId)
            context.startActivity(intent)
        }
    }

    override fun getDefaultFragmentsContainer(): Int? {
        return android.R.id.content
    }

    var view: ReportStatisticContract.View? = null
    var presenter: ReportStatisticContract.Presenter? = null
    var remainder: ReportStatisticContract.Remainder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_statistic)
        var root = findViewById<View>(android.R.id.content)

        var placeId=intent.getIntExtra(placeIdKey,-1)
        setDefaultProgressLoader(root.pb_map)

        view = ReportStatisticView(this, root)
        remainder = ReportStatisticRemainder(this)

        presenter = ReportStatisticPresenter(view as ReportStatisticView, activityEventBus, remainder as ReportStatisticRemainder,placeId)


    }
}

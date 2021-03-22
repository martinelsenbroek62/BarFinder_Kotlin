package com.lemust.ui.screens.reports.report_details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lemust.R
import com.lemust.ui.base.BaseActivity
import com.lemust.ui.screens.rports.report_details.ReportDetailsRemainder
import kotlinx.android.synthetic.main.activity_report_place_details.*

class ReportDetailsActivity : BaseActivity() {
    companion object {
        val placeIdKey = "place_id_key"
        val placeTypeIdKey = "place_type_id_key"
        fun start(context: Context, placeId: Int, placeTypeId: String) {
            var intent = Intent(context, ReportDetailsActivity::class.java)
            intent.putExtra(placeIdKey, placeId)
            intent.putExtra(placeTypeIdKey, placeTypeId)
            context.startActivity(intent)
        }
    }

    override fun getDefaultFragmentsContainer(): Int? {
        return android.R.id.content
    }

    var view: ReportDetailsContract.View? = null
    var presenter: ReportDetailsContract.Presenter? = null
    var remainder: ReportDetailsContract.Remainder? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_place_details)

        var placeId = intent.getIntExtra(placeIdKey, -1)
        var placeTypeId = intent.getStringExtra(placeTypeIdKey)

        var root = findViewById<View>(android.R.id.content)
        setDefaultProgressLoader(pb_map_details)

        view = ReportDetailsView(this, root)
        remainder = ReportDetailsRemainder(this)

        presenter = ReportDetailsPresenter(view as ReportDetailsView, activityEventBus, remainder as ReportDetailsRemainder, placeId, placeTypeId)
    }

    override fun onDestroy() {
        presenter!!.onDestroy()
        view!!.onDestroy()
        super.onDestroy()
    }
}

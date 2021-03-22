package com.lemust.ui.screens.left_menu.report

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.lemust.R
import com.lemust.ui.base.BaseActivity
import com.lemust.utils.AppHelper
import com.lemust.utils.SystemUtils

class ReportActivity : BaseActivity() {
    override fun getDefaultFragmentsContainer(): Int? {
        return android.R.id.content
    }

    var view: ReportContract.View? = null
    var presenter: ReportContract.Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)
        SystemUtils.setTransparentForStatusBar(this)

        var root = findViewById<View>(android.R.id.content)
        view = ReportView(this, root)
        presenter = ReportPresenter(view as ReportView, activityEventBus, this)
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        hideKeyboard()
        super.onDestroy()
    }
}

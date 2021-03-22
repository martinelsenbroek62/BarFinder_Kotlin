package com.lemust.ui.screens.profile.days_go_out

import android.os.Bundle
import android.view.View
import com.lemust.R
import com.lemust.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_days_go_out.view.*

class DaysGoOutActivity : BaseActivity() {
    override fun getDefaultFragmentsContainer(): Int? {
        return android.R.id.content
    }

    var view: DaysGoOutContract.View? = null
    var presenter: DaysGoOutContract.Presenter? = null
    var remainder: DaysGoOutContract.Remainder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_days_go_out)


        var root = findViewById<View>(android.R.id.content)
        setDefaultProgressLoader(root.pb_map)
        view = DaysGoOutView(this, root)
        remainder = DaysGoOutRemainder(this)
        presenter = DaysGoOutPresenter(view as DaysGoOutView, remainder as DaysGoOutRemainder, activityEventBus)

    }

    override fun onBackPressed() {
        remainder!!.generateBackPressed()
    }
}

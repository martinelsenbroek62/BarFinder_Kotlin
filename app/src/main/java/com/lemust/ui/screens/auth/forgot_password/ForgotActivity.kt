package com.lemust.ui.screens.auth.forgot_password

import android.os.Bundle
import android.view.View
import com.lemust.R
import com.lemust.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_auth.view.*

class ForgotActivity : BaseActivity() {
    override fun getDefaultFragmentsContainer(): Int? {
        return container
    }

    var view: ForgotContract.View? = null
    var presenter: ForgotContract.Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        var root = findViewById<View>(android.R.id.content)
        setDefaultProgressLoader(root.pb_map)

        //  setDefaultProgressLoader(root.pb_map)
        view = ForgotView(this, root)
        presenter = ForgotPresenter(view as ForgotView, activityEventBus)
    }
}

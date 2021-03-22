package com.lemust.ui.screens.profile.change_password

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.lemust.R
import com.lemust.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_change_password.view.*


class ChangePasswordActivity : BaseActivity() {
    override fun getDefaultFragmentsContainer(): Int? {
        return android.R.id.content
    }

    var view: ChangePasswordContract.View? = null
    var presenter: ChangePasswordContract.Presenter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        var root = findViewById<View>(android.R.id.content)
        setDefaultProgressLoader(root.pb_map_change_pass)
        view = ChangePasswordView(this, root)
        presenter = ChangePasswordPresenter(view as ChangePasswordView, this, activityEventBus)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }

    override fun onDestroy() {
        view!!.onDestroy()
        super.onDestroy()
    }
}

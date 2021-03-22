package com.lemust.ui.screens.profile

import android.arch.lifecycle.LifecycleObserver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lemust.R
import com.lemust.ui.base.BaseActivity
import com.lemust.ui.screens.auth.AuthActivity
import com.lemust.utils.SystemUtils
import kotlinx.android.synthetic.main.activity_profile.view.*


class ProfileActivity : BaseActivity() {

    override fun getDefaultFragmentsContainer(): Int? {
        return R.id.container
    }

    var view: ProfileContract.View? = null
    var presenter: ProfileContract.Presenter? = null
    var remainder: ProfileContract.Remainder? = null


    companion object {
        fun start(context: Context) {

            var intent = Intent(context, AuthActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        SystemUtils.setTransparentForStatusBar(this)
        try {


            var root = findViewById<View>(android.R.id.content)
            setDefaultProgressLoader(root.pb_map)
            view = ProfileView(this, root)

            remainder = ProfileRemainder(this)

            presenter = ProfilePresenter(view as ProfileView, remainder as ProfileRemainder, activityEventBus)
            lifecycle.addObserver(remainder as LifecycleObserver)

        } catch (e: Throwable) {
            System.err.print(e.localizedMessage)
        }
    }

    override fun onBackPressed() {
        remainder!!.onBackPressed()

    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.onDestroy()
        lifecycle.removeObserver(remainder as LifecycleObserver)

    }
}

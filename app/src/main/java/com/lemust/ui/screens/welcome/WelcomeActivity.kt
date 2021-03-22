package com.lemust.ui.screens.welcome

import android.os.Bundle
import com.lemust.R
import com.lemust.ui.base.BaseActivity
import com.orhanobut.hawk.Hawk

class WelcomeActivity : BaseActivity() {
    override fun getDefaultFragmentsContainer(): Int? {
        return R.layout.activity_welcome
    }

    var view: WelcomeContract.View? = null
    var presenter: WelcomeContract.Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        view = WelcomeView(this, findViewById(android.R.id.content))
        presenter = WelcomePresenter(view as WelcomeView, activityEventBus,this)
        Hawk.init(this).build();

    }
}

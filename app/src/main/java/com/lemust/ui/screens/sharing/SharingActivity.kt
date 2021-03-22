package com.lemust.ui.screens.sharing

import android.arch.lifecycle.LifecycleObserver
import android.os.Bundle
import android.view.View
import com.lemust.R
import com.lemust.ui.base.BaseActivity

class SharingActivity : BaseActivity() {
    override fun getDefaultFragmentsContainer(): Int? {
        return R.layout.activity_sharing
    }

    var view: SharingContract.View? = null
    var presenter: SharingContract.Presenter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sharing)

        var root = findViewById<View>(android.R.id.content)


        view = SharingView(this, root)
//        setDefaultProgressLoader(root.pb_map)

        presenter = SharingPresenter(view as SharingContract.View, this, activityEventBus)
        lifecycle.addObserver(presenter as LifecycleObserver);

    }


}

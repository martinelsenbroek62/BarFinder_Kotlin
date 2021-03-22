package com.lemust.ui.screens.profile.settings

import android.os.Bundle
import android.view.View
import com.lemust.R
import com.lemust.ui.base.BaseActivity
import com.lemust.ui.screens.filters.base.FiltersView
import com.lemust.utils.SystemUtils
import kotlinx.android.synthetic.main.activity_settings.view.*

class SettingsActivity : BaseActivity() {
    override fun getDefaultFragmentsContainer(): Int? {
        return android.R.id.content
    }

    var view: SettingContract.View? = null
    var presenter: SettingContract.Presenter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        var root = findViewById<View>(android.R.id.content)
        setDefaultProgressLoader(root.pb_map_setting)
        view = com.lemust.ui.screens.profile.settings.SettingView(this, root)
        presenter = com.lemust.ui.screens.profile.settings.SettingPresenter(view as SettingView, this, activityEventBus)
        lifecycle.addObserver(view as SettingView)

    }

    override fun onBackPressed() {
        presenter!!.onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(view as SettingView)

    }
}

package com.lemust.ui.screens.left_menu.city

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.lemust.R
import com.lemust.ui.base.BaseActivity
import com.lemust.ui.base.BaseFragment
import com.lemust.utils.SystemUtils

class AvailableCityActivity : BaseActivity() {
    var view: CityContract.View? = null
    var presenter: CityContract.Presenter? = null
    override fun getDefaultFragmentsContainer(): Int? {
        return android.R.id.content
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_available_city)
        var root = findViewById<View>(android.R.id.content)
        SystemUtils.setTransparentForStatusBar(this)

        view = CityView(this, root)
        presenter = CityPresenter(view as CityView, this)


    }
}

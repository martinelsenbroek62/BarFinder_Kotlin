package com.lemust.ui.screens.profile.location

import android.os.Bundle
import android.view.View
import com.lemust.R
import com.lemust.ui.base.BaseActivity

class SearchCityActivity : BaseActivity() {
    override fun getDefaultFragmentsContainer(): Int? {
        return android.R.id.content
    }

    var view: SearchCityContract.View? = null
    var presenter: SearchCityContract.Presenter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_city)


        var root = findViewById<View>(android.R.id.content)
        view = SearchCityView(this, root)
//        remainder = DaysGoOutRemainder(this)
        presenter = SearchCityPresenter(view as SearchCityView, activityEventBus,this)

    }
}

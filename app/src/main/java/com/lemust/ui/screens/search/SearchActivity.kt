package com.lemust.ui.screens.search

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.lemust.R
import com.lemust.ui.AppConst
import com.lemust.ui.base.BaseActivity
import com.lemust.ui.screens.main.map.MainFragment
import com.lemust.utils.SystemUtils

class SearchActivity : BaseActivity() {
    override fun getDefaultFragmentsContainer(): Int? {
        return android.R.id.content
    }

    companion object {
        var ACTIVITY_IS_STARTED = false
        var CITY_ID_KEY = "cityIdKey"

        public fun start(activity: BaseActivity, id: Int) {
            ACTIVITY_IS_STARTED = true
            var intent = Intent(activity, SearchActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(CITY_ID_KEY, id)
            activity.startActivityForResult(intent, AppConst.ACTIVITY_SEARCH_RESULT)

        }

    }




    var view: SearchContract.View? = null
    var presenter: SearchContract.Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        if(!MainFragment.isScreenReady)finish()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        SystemUtils.setTransparentForStatusBar(this)

        var root = findViewById<View>(android.R.id.content)
        view = SearchView(this, root)

        var cityId = intent.getIntExtra(CITY_ID_KEY, -1)
        presenter = SearchPresenter(view as SearchView, cityId)


    }

    override fun onStart() {
        if(!MainFragment.isScreenReady)finish()

        super.onStart()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        ACTIVITY_IS_STARTED = false

    }

    override fun onDestroy() {
        super.onDestroy()
        ACTIVITY_IS_STARTED = false

        if (presenter != null)
            presenter!!.onDestroy()

    }
}

package com.lemust.ui.screens.reports.place_name

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lemust.R
import com.lemust.ui.base.BaseActivity

class PlaceNameActivity : BaseActivity() {
    override fun getDefaultFragmentsContainer(): Int? {
        return android.R.id.content
    }


    companion object {
        val PLACE_NAME_KEY = "place_name_key"
        fun start(context: Activity, resultCode:Int,name: String) {
            var intent = Intent(context, PlaceNameActivity::class.java)
            intent.putExtra(PLACE_NAME_KEY, name)
            context.startActivityForResult(intent,resultCode)
        }
    }

    var view: PlaceNameContract.View? = null
    var presenter: PlaceNameContract.Presenter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_name_creator)
        var name = intent.getStringExtra(PLACE_NAME_KEY)

        var root = findViewById<View>(android.R.id.content)
        view = PlaceNameView(this, root)
        presenter = PlaceNamePresenter(view as PlaceNameView, activityEventBus, this,name)

    }
}

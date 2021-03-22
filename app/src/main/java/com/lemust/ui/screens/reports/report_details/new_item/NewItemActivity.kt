package com.lemust.ui.screens.reports.report_details.new_item

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lemust.R
import com.lemust.ui.base.BaseActivity
import com.lemust.ui.screens.reports.report_details.adapter.PlaceDetailsAdapter

class NewItemActivity : BaseActivity() {
    override fun getDefaultFragmentsContainer(): Int? {
        return android.R.id.content
    }

    companion object {
        val NEW_ITEM_KEY = "new_item_key"
        fun start(context: Activity, resultCode: Int, newItem: PlaceDetailsAdapter.NewItem) {
            var intent = Intent(context, NewItemActivity::class.java)
            intent.putExtra(NEW_ITEM_KEY, newItem)
            context.startActivityForResult(intent, resultCode)
        }
    }

    var view: NewItemContract.View? = null
    var presenter: NewItemContract.Presenter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_item)

        var item = intent.getSerializableExtra(NEW_ITEM_KEY) as PlaceDetailsAdapter.NewItem
        var root = findViewById<View>(android.R.id.content)
        view = NewItemView(this, root)
        presenter = NewItemPresenter(view as NewItemView, activityEventBus, this,item)

    }
}

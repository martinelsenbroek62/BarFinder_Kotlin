package com.lemust.ui.screens.profile.edit_user

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lemust.R
import com.lemust.ui.base.BaseActivity
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_edit_user.view.*

class EditUserActivity : BaseActivity() {
    var onBackAction=PublishSubject.create<Any>()


    companion object {
        const val IS_FIRST_NAME_KEY="is_first_name"

        fun start(context: Activity, isFirstName: Boolean, resultKey: Int) {
            var intent = Intent(context, EditUserActivity::class.java)
            intent.putExtra(IS_FIRST_NAME_KEY,isFirstName)
            context.startActivityForResult(intent,resultKey)
        }
    }
    override fun getDefaultFragmentsContainer(): Int? {
        return android.R.id.content
    }

    var view: EditUserContract.View? = null
    var presenter: EditUserContract.Presenter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_user)

        var root = findViewById<View>(android.R.id.content)

        val isFirstName=intent.getBooleanExtra(IS_FIRST_NAME_KEY,true)

        view = EditUserView(this, root)
        setDefaultProgressLoader(root.pb_map)

        presenter = EditUserPresenter(view as EditUserContract.View, this, activityEventBus,isFirstName)

    }

    override fun onBackPressed() {
        onBackAction.onNext(Any())
    }

    override fun onDestroy() {
        onBackAction.onComplete()
        super.onDestroy()
    }
}

package com.lemust.ui.screens.profile.occupation

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lemust.R
import com.lemust.ui.base.BaseActivity
import io.reactivex.subjects.PublishSubject
import java.util.*

class OccupationActivity : BaseActivity() {

    public var onBackPressedListener = PublishSubject.create<Any>()


    override fun getDefaultFragmentsContainer(): Int? {
        return R.id.container
    }

    var view: Step1OccupationContract.View? = null
    var presenter: Step1OccupationContract.Presenter? = null


    companion object {

        const val OCCUPATION_TYPES_KEY = "occupation_key"

        fun start(context: BaseActivity, occupation: HashMap<String, String>, occupatioN_RESULT: Int) {
            var intent = Intent(context, OccupationActivity::class.java)
            intent.putExtra(OCCUPATION_TYPES_KEY, occupation)
            context.startActivityForResult(intent, occupatioN_RESULT)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_occupation)


        var root = findViewById<View>(android.R.id.content)
        var occupation = intent.getSerializableExtra(OCCUPATION_TYPES_KEY) as HashMap<String, String>

        view = Step1OccupationView(this, root)
        presenter = Step1OccupationPresenter(view as Step1OccupationView, this, activityEventBus, occupation!!)
    }



    override fun onBackPressed() {
        onBackPressedListener.onNext(Any())
    }

    override fun onDestroy() {
        onBackPressedListener.onComplete()

        super.onDestroy()
    }


}

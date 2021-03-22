package com.lemust.ui.screens.auth.policy

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lemust.R
import com.lemust.ui.base.BaseActivity
import com.lemust.utils.SystemUtils
import kotlinx.android.synthetic.main.activity_privacy_policy.view.*

class PrivacyPolicyActivity : BaseActivity() {
    override fun getDefaultFragmentsContainer(): Int? {
        return R.layout.activity_privacy_policy
    }


    companion object {
        val TYPE_DESCRIPTION_KEY = "description_key"
        val TYPE_ICON_BACK_ARROW_KEY = "type_icon_back"
        const val SLUG_TERMS = "terms"
        const val SLUG_POLICY = "policy"
        const val SLUG_FAQ = "faq"
        fun start(context: Context, type: String,iconBackArrow:Boolean=true) {
            var intent = Intent(context, PrivacyPolicyActivity::class.java)
            intent.putExtra(TYPE_ICON_BACK_ARROW_KEY,iconBackArrow)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            intent.putExtra(TYPE_DESCRIPTION_KEY, type)
            context.startActivity(intent)
        }
    }

    var view: PrivacyPolicyContract.View? = null
    var presenter: PrivacyPolicyContract.Presenter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_policy)
        var root = findViewById<View>(android.R.id.content)
        setDefaultProgressLoader(root.pb_map_page)
        SystemUtils.setTransparentForStatusBar(this)

        var typeIconBack = intent.getBooleanExtra(TYPE_ICON_BACK_ARROW_KEY, true)
        view = PrivacyPolicyView(this, root)
        presenter = PrivacyPolicyPresenter(view as PrivacyPolicyView, activityEventBus, intent.getStringExtra(TYPE_DESCRIPTION_KEY),typeIconBack)
    }
}

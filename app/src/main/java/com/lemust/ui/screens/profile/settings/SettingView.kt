package com.lemust.ui.screens.profile.settings

//import com.stfalcon.frescoimageviewer.ImageViewer
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.view.View
import com.jakewharton.rxbinding2.view.RxView
import com.lemust.R
import com.lemust.ui.AppConst
import com.lemust.ui.base.BaseActivity
import com.lemust.ui.base.BaseView
import com.lemust.ui.screens.auth.AuthActivity
import com.lemust.ui.screens.auth.policy.PrivacyPolicyActivity
import com.lemust.ui.screens.left_menu.localization.ApplicationLanguageActivity
import com.lemust.ui.screens.left_menu.report.ReportActivity
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_settings.view.*


class SettingView(var fragment: BaseActivity, var root: View) : SettingContract.View, BaseView(fragment!!) {
    override fun getActivityContext(): Context {
        return fragment.baseContext
    }


    override fun showRemoveAccountDialog(title: String, message: String, positiveTitle: String, negativeString: String): Observable<Any> {
        return showSettingDialog(title, message, positiveTitle, negativeString)
    }

    override fun isShowProgressLoader(isShow: Boolean) {
        (fragment).showDefaultProgressLoader(isShow)

    }


    override fun onBackAction(): Observable<Any> {
        return RxView.clicks(root.iv_back)
    }

    init {
        // initAction()
    }

    private fun initAction() {
        root.iv_back.setOnClickListener { fragment.finish() }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    override fun updateResources() {
        root.post {
        root.tv_setting.text = fragment.resources.getString(R.string.title_setting)

        root.tv_language_title.text = fragment.resources.getString(R.string.title_language)

        root.tv_send_feedback_title.text = fragment.resources.getString(R.string.send_feedback)

        root.tv_information.text = fragment.resources.getString(R.string.title_information)

        root.tv_privacy_policy.text = fragment.resources.getString(R.string.title_privacy_policy)

        root.tv_terms.text = fragment.resources.getString(R.string.title_terms_and_condition)

        root.tv_logout.text = fragment.resources.getString(R.string.title_logout)
        root.tv_remove_account.text = fragment.resources.getString(R.string.title_delete_account)

        }
    }


    override fun openPrivacyPolicy() {
        PrivacyPolicyActivity.start(fragment, PrivacyPolicyActivity.SLUG_POLICY)

    }

    override fun openTermsConditions() {
        PrivacyPolicyActivity.start(fragment, PrivacyPolicyActivity.SLUG_TERMS)
    }

    override fun openAuthScreen() {

        var intent = Intent(fragment, AuthActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        fragment.startActivity(intent)
    }

    override fun openFeedbackScreen() {
        fragment.startActivity(Intent(fragment, ReportActivity::class.java))

    }

    override fun openLanguageScreen() {
        Handler().post {
            fragment!!.startActivityForResult(Intent(fragment, ApplicationLanguageActivity::class.java), AppConst.ACTIVITY_AVAILABLE_LANGUAGE_RESULT)
        }
    }

    override fun onLanguageAction(): Observable<Any> {
        return RxView.clicks(root.item_language)

    }

    override fun onFeedbackAction(): Observable<Any> {
        return RxView.clicks(root.item_send_feedback)
    }

    override fun onPrivacyPolicyAction(): Observable<Any> {
        return RxView.clicks(root.item_privacy_policy)
    }

    override fun onTermsConditionsAction(): Observable<Any> {
        return RxView.clicks(root.item_terms_and_conditions)
    }

    override fun onLogoutAction(): Observable<Any> {
        return RxView.clicks(root.item_logout)
    }

    override fun onRemoveAccount(): Observable<Any> {
        return RxView.clicks(root.item_remove_account)
    }


}
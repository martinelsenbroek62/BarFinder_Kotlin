package com.lemust.ui.screens.auth

//import com.stfalcon.frescoimageviewer.ImageViewer
import android.support.v7.app.AlertDialog
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import com.lemust.R
import com.lemust.ui.base.BaseActivity
import com.lemust.ui.base.BaseView
import com.lemust.ui.screens.auth.policy.PrivacyPolicyActivity
import com.lemust.ui.screens.main.MainActivity
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_auth.view.*


class AuthView(var fragment: BaseActivity, var root: View) : AuthContract.View, BaseView(fragment!!) {
    override fun closeDialogs() {
        dialog?.let {
            if (it.isShowing) {
                it.dismiss()
            }
        }
    }


    private var privacyAction = PublishSubject.create<Any>()!!
    private var termsAction = PublishSubject.create<Any>()!!
    private var reloadCityAction = PublishSubject.create<Any>()!!


    init {
        initHyperText()

    }

    override fun onDestroy() {
        privacyAction.onComplete()
        termsAction.onComplete()
        reloadCityAction.onComplete()
    }

    override fun showRequestError(title: String, message: String) {
        BaseView(fragment).showDialogWithOneButtons(title, message, fragment.getString(R.string.title_reload), object : BaseView.DialogController1 {
            override fun action1(dialog: AlertDialog) {
                reloadCityAction.onNext(Any())
            }
        })
    }


    private fun initHyperText() {
        var clickableSpanPrivacy = object : ClickableSpan() {
            override fun onClick(p0: View?) {
                privacyAction.onNext(Any())
            }

            override fun updateDrawState(ds: TextPaint) {// override updateDrawState
                ds.isUnderlineText = false // set to false to remove underline
            }
        }

        var clickableSpanTerms = object : ClickableSpan() {
            override fun onClick(p0: View?) {
                termsAction.onNext(Any())
            }

            override fun updateDrawState(ds: TextPaint) {// override updateDrawState
                ds.isUnderlineText = false // set to false to remove underline
            }
        }

        var firstHyperWord = fragment.resources.getString(R.string.title_terms_conditions)
        var secondHyperWord = fragment.resources.getString(R.string.title_privacy_policy)
        var textAnd = fragment.resources.getString(R.string.title_and)

        var text = fragment.resources.getString(R.string.title_privacy_term_prefix) + " " + firstHyperWord + " " + textAnd + " " + secondHyperWord


        val s1 = SpannableString(text)

        var startFirstWord = text.indexOf(firstHyperWord)
        var endFirstWord = startFirstWord + firstHyperWord.length

        var startSecondWord = text.indexOf(secondHyperWord)
        var endSecondWord = startSecondWord + secondHyperWord.length



        s1.setSpan(clickableSpanTerms, startFirstWord, endFirstWord, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        s1.setSpan(clickableSpanPrivacy, startSecondWord, endSecondWord, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)




        root.tv_terms.text = s1;
        root.tv_terms.movementMethod = LinkMovementMethod.getInstance();
    }

    override fun showTerms() {
        PrivacyPolicyActivity.start(fragment, PrivacyPolicyActivity.SLUG_TERMS)
    }


    override fun showPrivacyPolicy() {
        PrivacyPolicyActivity.start(fragment, PrivacyPolicyActivity.SLUG_POLICY)
    }

    override fun openNextActivity() {

        MainActivity.start(fragment)
        fragment.finish()

    }

    override fun onPrivacyPolicyAction(): Observable<Any> {
        return privacyAction
    }

    override fun onPrivacyTermsAction(): Observable<Any> {
        return termsAction
    }

    override fun showNoInternetConnection() {
        BaseView(fragment).showDialogWithOneButtons(fragment.resources.getString(R.string.no_internet_connection), "", fragment.resources.getString(R.string.title_ok),
                object : BaseView.DialogController1 {
                    override fun action1(dialog: AlertDialog) {
                        dialog.dismiss()
                    }


                }, false)
    }


    override fun onReloadCityAction(): Observable<Any> {
        return reloadCityAction
    }

}
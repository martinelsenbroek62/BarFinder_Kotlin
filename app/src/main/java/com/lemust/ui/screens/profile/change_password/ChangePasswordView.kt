package com.lemust.ui.screens.profile.change_password

//import com.stfalcon.frescoimageviewer.ImageViewer
import android.view.View

import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.lemust.R
import com.lemust.ui.base.BaseActivity
import com.lemust.ui.base.BaseView
import com.lemust.utils.Tools
import com.lemust.utils.onMainThread
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_change_password.view.*
import kotlinx.android.synthetic.main.loader.view.*
import java.util.concurrent.TimeUnit


class ChangePasswordView(var fragment: BaseActivity, var root: View) : ChangePasswordContract.View, BaseView(fragment!!) {


    override fun hideKeyboard() {
        fragment.hideKeyboard()
    }

    override fun onDestroy() {
//        if (unregister != null) {
//            unregister!!.unregister()
//        }
    }
    override fun getOldPasswordAction(): Observable<CharSequence> {
        return onMainThread(RxTextView.textChanges(root.findViewById(R.id.et_current_password)).skip(0).debounce(300, TimeUnit.MILLISECONDS))
    }

    override fun getRepeatPasswordAction(): Observable<CharSequence> {
        return onMainThread(RxTextView.textChanges(root.findViewById(R.id.et_repeat_password)).skip(0).debounce(300, TimeUnit.MILLISECONDS))
    }

    override fun getNewPasswordAction(): Observable<CharSequence> {
        return onMainThread(RxTextView.textChanges(root.findViewById(R.id.et_new_password)).skip(0).debounce(300, TimeUnit.MILLISECONDS))

    }

    override fun finish() {
        fragment.finish()
    }

    override fun changeTextInProgressBar(text: String) {
        root.title_loading.text = text
    }
//    override fun isEnableButtonSent(isEnabled: Boolean) {
//        if (isEnabled) {
//            root.btn_change_password.isEnabled = true
//            root.btn_change_password.setBackgroundResource(R.drawable.button_filter_selector)
//        } else {
//            root.btn_change_password.isEnabled = false
//            root.btn_change_password.setBackgroundResource(R.drawable.background_preview_pressed)
//
//
//        }
//    }

    override fun isShowProgressLoader(isShow: Boolean) {
        (fragment).showDefaultProgressLoader(isShow)

    }

    override fun isVisibleOldPassword(isVisible: Boolean) {
        if (isVisible) {
            root.et_current_password.visibility = View.VISIBLE
        } else {
            root.et_current_password.visibility = View.GONE

        }
    }

    init {
        root.iv_close.setOnClickListener {
                fragment.onBackPressed()
        }


        root.post {

            val location = IntArray(2)
            root.et_repeat_password.getLocationOnScreen(location)
            val y = location[1] + root.et_repeat_password.height + Tools.convertDpToPixel(32f, fragment)
            var new_space = root.height - y - root.btn_change_password.height




        }

    }

    override fun getCurrentPassword(): String {
        return root.et_current_password.text.toString()
    }

    override fun getNewPassword(): String {
        return root.et_new_password.text.toString()

    }

    override fun getRepeatPassword(): String {
        return root.et_repeat_password.text.toString()

    }

    override fun onPasswordChangeAction(): Observable<Any> {
        return RxView.clicks(root.btn_change_password)
    }


}
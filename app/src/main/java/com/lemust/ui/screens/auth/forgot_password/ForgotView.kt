package com.lemust.ui.screens.auth.forgot_password

//import com.stfalcon.frescoimageviewer.ImageViewer
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.lemust.ui.base.BaseActivity
import com.lemust.ui.base.BaseView
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_forgot_password.view.*


class ForgotView(var fragment: BaseActivity, var root: View) : ForgotContract.View, BaseView(fragment!!) {


    init {
        root.email_input.post {
            root.email_input.visibility = View.VISIBLE

            val imm = fragment.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(root.email_input, InputMethodManager.SHOW_IMPLICIT)
        }

    }

    override fun hideKeyboard() {
        fragment.hideKeyboard()
        root.email_input.clearFocus()

    }

    override fun showKeyboard() {
        root.email_input.post {
            root.email_input.requestFocus()
            val imm = fragment.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(root.email_input, InputMethodManager.SHOW_IMPLICIT)
        }

    }

    override fun finish() {
        fragment.finish()
    }

    override fun onBackAction(): Observable<Any> {
        return RxView.clicks(root.iv_close)
    }

    override fun back() {
        fragment.finish()
    }

    override fun isShowProgressLoader(isShow: Boolean) {
        (fragment).showDefaultProgressLoader(isShow)

    }

    override fun onSendAction(): Observable<Any> {
        return RxView.clicks(root.tv_send)
    }

    override fun getEmail(): String {
        return root.email_input.text.toString()
    }

    init {
        RxTextView.textChanges(root.email_input).subscribe {
            disableEmailError()
        }
    }

    private fun disableEmailError() {
        root.email_view.isErrorEnabled = false
    }

    override fun showEmailError(error: String) {
        if (!error.isEmpty()) {
            root.email_view.error = error
        } else {
            disableEmailError()
        }
    }

}
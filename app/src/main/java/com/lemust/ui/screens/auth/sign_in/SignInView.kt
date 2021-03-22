package com.lemust.ui.screens.auth.sign_in

//import com.stfalcon.frescoimageviewer.ImageViewer
import android.content.Intent
import android.view.View
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.lemust.ui.base.BaseActivity
import com.lemust.ui.base.BaseFragment
import com.lemust.ui.base.BaseView
import com.lemust.ui.screens.auth.forgot_password.ForgotActivity
import com.lemust.ui.screens.auth.sign_up.SignUpFragment
import com.lemust.ui.screens.main.MainActivity
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_sign_in.view.*


class SignInView(var fragment: BaseFragment, var root: View) : SingInContract.View, BaseView(fragment.context!!) {
    override fun isEnabledScreen(isEnabled: Boolean) {
        root.empty_view.isClickable = !isEnabled
    }

    override fun isShowContent(isShow: Boolean) {
        if (isShow) {
            root.container_sign_in.visibility = View.VISIBLE
        } else {
            root.container_sign_in.visibility = View.VISIBLE

        }
    }

    override fun onForgotPasword(): Observable<Any> {
        return RxView.clicks(root.tv_forgot_password)
    }

    override fun showForgotPasswordScreen() {
        fragment.startActivity(Intent(fragment.context, ForgotActivity::class.java))
    }

    override fun hideKeyboard() {
        fragment.activity?.let {
            (it as BaseActivity).hideKeyboard()
        }
    }

    override fun isShowProgressLoader(isShow: Boolean) {
        try {
            fragment.activity?.let {
                (it as BaseActivity).showDefaultProgressLoader(isShow)

            }
        } catch (e: Exception) {
            System.err.print(e.localizedMessage)
        }

    }

    override fun openNextActivity() {
        var intent = Intent(fragment.activity, MainActivity::class.java)
        // intent.putExtra(MainActivity.isAvailableLocationKey,  AppDataHolder.isLocationAvailable)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        fragment.startActivity(intent)
    }

    init {
        RxTextView.textChanges(root.email_input).subscribe {
            disableEmailError()
        }
        RxTextView.textChanges(root.password_input).subscribe {
            disablePasswordError()
        }
    }

    private fun disableEmailError() {
        root.email_view.isErrorEnabled = false
    }


    override fun loginViaEmailPasswordAction(): Observable<Any> = RxView.clicks(root.btn_sign_in)

    override fun loginViaFacebookAction(): Observable<Any> = RxView.clicks(root.btn_sign_in_facebook)

    override fun forgotPasswordAction(): Observable<Any> = RxView.clicks(root.tv_forgot_password)
    override fun getEmail(): String = root.email_input.text.toString()

    override fun getPassword(): String = root.password_input.text.toString()

    override fun showEmailError(error: String) {
        if (!error.isEmpty()) {
            root.email_view.error = error
        } else {
            disableEmailError()
        }
    }

    override fun showPasswordError(error: String) {
        if (!error.isEmpty()) {
            root.password_view.error = error
        } else {
            disablePasswordError()
        }
    }

    private fun disablePasswordError() {
        root.password_view.isErrorEnabled = false
    }

    override fun showProgressViewState(show: Boolean) {
//        if (show) {
//            showView(view.progress_bar)
//            hideView(view.main_ui)
//        } else {
//            goneView(view.progress_bar)
//            showView(view.main_ui)
//        }
    }

    override fun showSignUpScreen() {
        fragment.activity?.let {
            (it as BaseActivity).putRootFragment(SignUpFragment.newInstance("", ""))
        }
    }

    override fun onSignOutAction(): Observable<Any> {
        return RxView.clicks(root.title_sign_in)
    }


}
package com.lemust.ui.screens.auth.sign_up

//import com.stfalcon.frescoimageviewer.ImageViewer
import android.content.Intent
import android.view.View
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.lemust.ui.base.BaseActivity
import com.lemust.ui.base.BaseFragment
import com.lemust.ui.base.BaseView
import com.lemust.ui.screens.auth.sign_in.SignInFragment
import com.lemust.ui.screens.main.MainActivity
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_sign_out.view.*


class SignUpView(var fragment: BaseFragment, var root: View) : SignUpContract.View, BaseView(fragment.context!!) {
    override fun hideKeyboard() {
        fragment.activity?.let {
            (it as BaseActivity).hideKeyboard()
        }
    }

    init {
        RxTextView.textChanges(root.email_input).subscribe {
            disableEmailError()
        }
        RxTextView.textChanges(root.password_input).subscribe {
            disablePasswordError()
        }
    }

    override fun isEnabledScreen(isEnabled: Boolean) {
        root.empty_view.isClickable = !isEnabled
    }

    private fun disableEmailError() {
        root.email_view.isErrorEnabled = false
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


    override fun registrationViaEmailPasswordAction(): Observable<Any> = RxView.clicks(root.btn_sign_up)

    override fun registrationViaFacebookAction(): Observable<Any> = RxView.clicks(root.btn_sign_up_facebook)

    override fun getEmail(): String = root.email_input.text.toString()

    override fun getPassword(): String = root.password_input.text.toString()

    override fun cleanFields() {
        root.email_input.setText("")
        root.password_input.setText("")
    }


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

    override fun showSignInScreen() {
        fragment.activity?.let {
            (it as BaseActivity).putRootFragment(SignInFragment.newInstance("", ""))
        }
    }

    override fun onSignInAction(): Observable<Any> {
        return RxView.clicks(root.title_sign_up)
    }
}
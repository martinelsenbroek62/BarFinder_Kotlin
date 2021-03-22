package com.lemust.ui.screens.profile.edit_user

//import com.stfalcon.frescoimageviewer.ImageViewer
import android.content.Context
import android.view.View
import android.view.WindowManager
import com.jakewharton.rxbinding2.view.RxView
import com.lemust.ui.base.BaseActivity
import com.lemust.ui.base.BaseView
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_edit_user.view.*
import kotlinx.android.synthetic.main.loader.view.*


class EditUserView(var fragment: BaseActivity, var root: View) : EditUserContract.View, BaseView(fragment!!) {
    override fun hideKeyboard() {
        fragment.hideKeyboard()
    }
    override fun changeTextInProgressBar(text: String) {
        root.title_loading.text = text
    }
    override fun setDescription(text: String) {
        root.tv_description_title.text = text
    }


    override fun onBackAction(): Observable<Any> {
        return (fragment as EditUserActivity).onBackAction
    }

    override fun setTitle(text: String) {
        root.tv_screen_title.text = text
    }

    override fun getAppContext(): Context {
        return fragment
    }

    override fun isVisibleFirstName(isVisible: Boolean) {
        if (isVisible) {
            root.layout_first_name.visibility = View.VISIBLE
            root.tv_first_name.requestFocus();
            fragment.window.setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        } else {
            root.layout_first_name.visibility = View.GONE

        }
    }

    override fun isVisibleLastName(isVisible: Boolean) {
        if (isVisible) {
            root.layout_second_name.visibility = View.VISIBLE
            root.tv_second_name.requestFocus();
            fragment.window.setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        } else {
            root.layout_second_name.visibility = View.GONE


        }
    }


    override fun setFirstName(firstName: String) {
        root.tv_first_name.postDelayed({
            root.tv_first_name.setText(firstName)
            root.tv_first_name.setSelection(root.tv_first_name.text.length);
        }, 100)
    }

    override fun setLastName(lastName: String) {
        root.tv_second_name.postDelayed({
            root.tv_second_name.setText(lastName)
            root.tv_second_name.setSelection(root.tv_second_name.text.length);
        }, 100)
    }


    override fun showFirstNameError(error: String) {
        if (!error.isEmpty()) {
            root.layout_first_name.error = error
        } else {
            disableFirstNameError()
        }
    }

    override fun showSeconNameError(error: String) {
        if (!error.isEmpty()) {
            root.layout_second_name.error = error
        } else {
            disableSecondNameError()
        }
    }

    override fun disableFirstNameError() {
        root.layout_first_name.isErrorEnabled = false
    }

    override fun disableSecondNameError() {
        root.layout_second_name.isErrorEnabled = false
    }


    init {
        initAction()
        initView()
    }

    private fun initView() {

    }

    private fun initAction() {
        root.iv_back.setOnClickListener { fragment.onBackPressed() }
    }

    override fun isShowProgressLoader(isShow: Boolean) {
        (fragment).showDefaultProgressLoader(isShow)

    }

    override fun getFirstNameText(): String {
        return root.tv_first_name.text.toString()
    }

    override fun getLastText(): String {
        return root.tv_second_name.text.toString()
    }

    override fun onApplyAction(): Observable<Any> {
        return RxView.clicks(root.tv_apply)
    }

    override fun finish() {
        fragment.finish()

    }
}
package com.lemust.ui.screens.auth.policy

//import com.stfalcon.frescoimageviewer.ImageViewer
import android.text.Html
import android.view.View
import com.jakewharton.rxbinding2.view.RxView
import com.lemust.R
import com.lemust.ui.base.BaseActivity
import com.lemust.ui.base.BaseView
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_privacy_policy.view.*
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper


class PrivacyPolicyView(var fragment: BaseActivity, var root: View) : PrivacyPolicyContract.View, BaseView(fragment!!) {
    override fun initButtonBackArrow(isArrow: Boolean) {
        if (isArrow) {
            root.iv_back.setImageResource(R.mipmap.icn_navigation_back)
        }else{
            root.iv_back.setImageResource(R.mipmap.icn_navigation_close)

        }
    }

    init {
        root.type_description.visibility = View.GONE
        root.tv_title.visibility = View.GONE
        root.tv_text.visibility = View.GONE
        root.tv_short_description.visibility = View.GONE
        OverScrollDecoratorHelper.setUpOverScroll(root.city_scroll)

    }

    override fun setTypeDescription(title: String) {
        root.type_description.visibility = View.VISIBLE
        root.type_description.text = title

    }

    override fun setTitle(title: String) {
        root.tv_title.visibility = View.VISIBLE
        root.tv_title.text = Html.fromHtml(title)
    }

    override fun setText(title: String) {
        root.tv_text.visibility = View.VISIBLE
        root.tv_text.text = Html.fromHtml(title)
    }

    override fun setShortDescription(title: String) {
        root.tv_short_description.visibility = View.VISIBLE
        root.tv_short_description.text = Html.fromHtml(title)
    }

    override fun isShowProgressLoader(isShow: Boolean) {
        (fragment).showDefaultProgressLoader(isShow)

    }

    override fun onBackAction(): Observable<Any> {
        return RxView.clicks(root.iv_back)
    }

    override fun back() {
        fragment.finish()
    }


}
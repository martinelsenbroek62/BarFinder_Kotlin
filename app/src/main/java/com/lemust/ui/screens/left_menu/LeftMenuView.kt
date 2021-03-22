package com.lemust.ui.screens.left_menu

//import com.stfalcon.frescoimageviewer.ImageViewer
import android.app.Activity
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Handler
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.jakewharton.rxbinding2.view.RxView
import com.lemust.R
import com.lemust.ui.AppConst
import com.lemust.ui.base.BaseView
import com.lemust.ui.screens.auth.AuthActivity
import com.lemust.ui.screens.auth.policy.PrivacyPolicyActivity
import com.lemust.ui.screens.left_menu.city.AvailableCityActivity
import com.lemust.ui.screens.left_menu.localization.ApplicationLanguageActivity
import com.lemust.ui.screens.left_menu.report.ReportActivity
//import com.lemust.ui.screens.left_menu.city.CityFragment
import com.lemust.ui.screens.main.MainActivity
import com.lemust.ui.screens.profile.ProfileActivity
import com.lemust.utils.AppHelper
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_left_menu.view.*


class LeftMenuView(var activity: MainActivity, var root: View) : LeftMenuContract.View, BaseView(activity) {
    override fun onTakeScreen() {
        activity.onTakeScreenAction.onNext(Any())
    }

    override fun isVisibleLeftMenu(): Boolean {
        return activity.navigation.isVisibleLeftMenu()
    }

    override fun onLanguageChanged(): Observable<Any> {
        return activity.onLanguageAction
    }

    override fun hideWithoutAnimation() {
        activity.navigation.hideMenuWithoutAnimated()
    }

    override fun hide() {
        activity.navigation.hideMenu()
    }

    var onProfuleResultAction = PublishSubject.create<Boolean>()

    init {
        initAction()

    }


    override fun openAuthScreen() {
        activity!!.startActivity(Intent(activity, AuthActivity::class.java))
        activity!!.finish()
    }


    override fun showLanguageScreen() {

        var intent=Intent(activity, ApplicationLanguageActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity!!.startActivityForResult(intent, AppConst.ACTIVITY_AVAILABLE_LANGUAGE_RESULT)
        // }

    }

    override fun showLocationScreen() {
        var intent=Intent(activity, AvailableCityActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivityForResult(intent, AppConst.ACTIVITY_AVAILABLE_CITY_RESULT)

//        Handler().postDelayed({
//            activity.navigation.hideMenu()
//            //
//        }, 400)

    }

    override fun showReportScreen() {
//        Handler().postDelayed({
        var intent = Intent(activity, ReportActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        activity.startActivityForResult(intent, MainActivity.ON_ACTIVITY_RESULT_PLACE_DETAILS)
//            hide()
//        }, 400)

    }


    override fun openFAQ() {


        PrivacyPolicyActivity.start(activity, PrivacyPolicyActivity.SLUG_FAQ, false)
//        Handler().post {
//            hide()
//
//        }
    }


    override fun getActivityContext(): Context {
        return activity
    }

    override fun openProfile() {
        var intent = Intent(activity, ProfileActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivityForResult(intent, AppConst.ACTIVITY_POFILE_RESULT)

    }

    override fun onBackPressed(): Observable<Any> {
        return activity.onBackPressedAction
    }

    override fun onProfileReloadDataAction(): Observable<Boolean> {
        return onProfuleResultAction
    }


    private fun initAction() {
        activity.onActivityResultListener.subscribe {
            hideWithoutAnimation()

            when (it.requestCode) {
                AppConst.ACTIVITY_POFILE_RESULT -> {
                    (activity as MainActivity).isSelectCurrentTime = false
                    if (it.resultCode == Activity.RESULT_OK) {
                        onProfuleResultAction.onNext(true)
                        activity.onLanguageAction.onNext(Any())
                    } else
                        onProfuleResultAction.onNext(false)


                }
            }


        }
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    override fun onDestroy() {
        onProfuleResultAction.onComplete()

    }


    override fun setDefaultAvatar() {
        Glide.with(activity)
                .load(R.drawable.icon_avatar_default)
                .into(root.iv_avatar);
    }

    override fun setUseInfo(name: String, email: String) {
        root.post {
            root.tv_name.text = name
            root.tv_email.text = email
        }
    }

    override fun setUserAvatar(ava: String) {
        Glide.with(activity)
                .load(ava).apply(RequestOptions()
                        .fitCenter().encodeQuality(10)
                        .format(DecodeFormat.PREFER_RGB_565)
                        .override(Target.SIZE_ORIGINAL)).listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        return true
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {

                        root.iv_avatar.setImageDrawable(resource)
                        root.invalidate()

                        return true
                    }
                }).submit()
    }


    override fun setUserAvatar(img: Bitmap) {
        Glide.with(activity)
                .load(img).apply(RequestOptions()
                        .fitCenter().encodeQuality(10)
                        .format(DecodeFormat.PREFER_RGB_565)
                        .override(Target.SIZE_ORIGINAL)).listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        return true
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {

                        root.iv_avatar.setImageDrawable(resource)
                        root.invalidate()

                        return true
                    }
                }).submit()

    }

    override fun onProfileItemAction(): Observable<Any> {
        return RxView.clicks(root.tv_profile)
    }

    override fun onChangeLocation(): Observable<Any> {
        return RxView.clicks(root.tv_change_location)
    }

    override fun onChangeLanguage(): Observable<Any> {
        return RxView.clicks(root.tv_change_language)

    }

    override fun onReport(): Observable<Any> {
        return RxView.clicks(root.item_send_feedback)
    }

    override fun onShare(): Observable<Any> {
        return RxView.clicks(root.tv_share_application)
    }

    override fun getRootView(): View {
        return root
    }

    override fun onFAQ(): Observable<Any> {
        return RxView.clicks(root.tv_faq)
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    override fun updateResources() {
        root.tv_profile_title.post {
            try {
                var res = AppHelper.locale.getCurrentLangResources(context)
                root!!.tv_profile_title.text = res.getText(R.string.title_my_profile)
                root!!.tv_language_title.text = res.getText(R.string.title_language)
                root!!.tv_change_location_title.text = res.getText(R.string.change_location)
                root!!.tv_send_feedback_title.text = res.getText(R.string.send_feedback)
                root!!.tv_share_application_title.text = res.getText(R.string.share_application)
            } catch (e: Exception) {
                System.err.print(e.stackTrace)
            }
        }

    }

}
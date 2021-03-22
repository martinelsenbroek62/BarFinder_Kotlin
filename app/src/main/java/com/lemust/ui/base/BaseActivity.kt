package com.lemust.ui.base

import android.Manifest
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LifecycleRegistry
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.hairdresser.services.socials.facebook.FacebookAuthHelper
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.single.PermissionListener
import com.lemust.LeMustApp
import com.lemust.ui.AppConst.TOKEN_NOT_VALID_KEY
import com.lemust.ui.screens.auth.AuthActivity
import com.lemust.ui.screens.splash.SplashActivity
import com.lemust.utils.AppDataHolder
import com.lemust.utils.AppHelper
import com.lemust.utils.SystemUtils
import com.squareup.otto.Bus
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject

    
abstract class BaseActivity : AppCompatActivity(), LifecycleOwner {
    private var lifecycle: LifecycleRegistry? = null
    private var screenManager: ScreenManager? = null
    protected var container: Int? = null
    public var onActivityResultListener = PublishSubject.create<OnActivityResult>()
    var consumerTokenNotValid: Disposable? = null

    fun handleTokenNotValid() {
        consumerTokenNotValid = AppHelper.api.tokenIsNotValid().subscribe {
            consumerTokenNotValid?.dispose()
            // if (this !is AuthActivity)
            AppHelper.api.tokenIsNotValid().onComplete()
            openAuthScreen()

        }
    }

    fun openAuthScreen() {
        hideKeyboard()
        FacebookAuthHelper.disconnectFromFacebook()
        AppDataHolder.skipCurrentCity()
        AppHelper.preferences.clearToken()
        AppDataHolder.actualUserPhoto = ""


        var intent = Intent(this, SplashActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        intent.putExtra(TOKEN_NOT_VALID_KEY, true)
        startActivity(intent)
        finish()

    }

    override fun onStart() {
        super.onStart()
        handleTokenNotValid()
    }

    override fun onStop() {
        super.onStop()
        consumerTokenNotValid?.let {
            if (!it.isDisposed) {
                it.dispose()
            }
        }
    }


    var activityEventBus: Bus
    var progressLoader: View? = null
    var animSet: Array<Int>? = null

    init {
        activityEventBus = Bus()
    }

    fun setDefaultProgressLoader(loaderView: View) {
        this.progressLoader = loaderView
    }

    protected fun setDefaultFragmentsAnimSet(animSet: Array<Int>) {
        this.animSet = animSet
    }

    fun showDefaultProgressLoader(isShow: Boolean) {
        if (isShow) {
            progressLoader?.let {
                progressLoader!!.visibility = View.VISIBLE

            }
        } else {
            progressLoader?.let {
                progressLoader!!.visibility = View.GONE

            }
        }

    }


    abstract fun getDefaultFragmentsContainer(): Int?

    // abstract fun getDefaultProgressLoader(): Int?

    protected fun getActivityBus() = activityEventBus


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppHelper.locale.setLocale(this, AppHelper.locale.getLanguage(this))
        lifecycle = LifecycleRegistry(this)
        SystemUtils.setTransparentForStatusBar(this)

        this.container = getDefaultFragmentsContainer()
        screenManager = ScreenManager(supportFragmentManager)
    }

    override fun onBackPressed() {
        //   onBackPressedListener.onNext(Any())
        hideSoftKeyBoard()
        screenManager?.also { if (it.performOnBackPressed()) return }
        super.onBackPressed()
    }

    open fun putFragment(fragment: BaseFragment) {
        hideSoftKeyBoard()
        container?.also { screenManager?.putFragment(fragment, animSet, it) }
    }

    open fun addFragment(fragment: BaseFragment) {
        hideSoftKeyBoard()
        container?.also { screenManager?.putFragment(fragment, animSet, it) }
    }

    open fun putFragmentForSetNewStackAfter(fragment: BaseFragment) {
        hideSoftKeyBoard()
        container?.also {
            screenManager?.putFragment(fragment, animSet, it)
        }
    }

    open fun replaceFragment(fragment: BaseFragment) {
        hideSoftKeyBoard()
        container?.also { screenManager?.replaceFragment(fragment, it) }
    }

    open fun putFragments(vararg fragments: BaseFragment) {
        hideSoftKeyBoard()
        container?.also { screenManager?.putFragments(it, *fragments) }
    }

    open fun setFragment(fragment: BaseFragment) {
        hideSoftKeyBoard()
        container?.also { screenManager?.setFragment(fragment, it) }
    }

    open fun putRootFragment(fragment: BaseFragment) {
        hideSoftKeyBoard()
        container?.also { screenManager?.putRootFragment(fragment, it) }
    }

    open fun popBackStack() {
        hideSoftKeyBoard()
        container?.also { screenManager?.popBackStack() }
    }

    fun popFragment(count: Int) {
        hideSoftKeyBoard()
        container?.also { screenManager?.popFragment(count) }
    }

    fun popFragment(tag: String, invalidate: Boolean) {
        hideSoftKeyBoard()
        container?.also { screenManager?.popFragment(tag, invalidate) }
    }

    fun setNewStack(rootFragment: BaseFragment, fragments: List<BaseFragment>) {
        hideSoftKeyBoard()
        container?.also { screenManager?.setNewStack(rootFragment, fragments, it) }
    }

    fun hasPoppedFragments(): Boolean = screenManager?.hasPoppedFragments() ?: false

    fun hideKeyboard() {
        hideSoftKeyBoard()
    }


    private fun hideSoftKeyBoard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        if (imm?.isAcceptingText == true && currentFocus != null) {
            imm.hideSoftInputFromWindow(currentFocus.windowToken, 0)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        onActivityResultListener.onComplete()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        onActivityResultListener.onNext(OnActivityResult(requestCode, resultCode, data))
        super.onActivityResult(requestCode, resultCode, data)
    }


    class OnActivityResult(var requestCode: Int, var resultCode: Int, var data: Intent?)


    fun getPermissionLocation(permission: PermissionListener) {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(permission).check()
    }


    fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
    }

}
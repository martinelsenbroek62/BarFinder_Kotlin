package com.lemust.ui.screens.splash

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.lemust.R
import com.lemust.ui.screens.auth.AuthActivity
import com.lemust.ui.screens.main.MainActivity
import com.lemust.ui.screens.welcome.WelcomeActivity
import com.lemust.utils.AppDataHolder
import com.lemust.utils.AppHelper
import com.lemust.utils.SystemUtils
import com.lemust.utils.Tools


class SplashActivity : AppCompatActivity() {
    private val WELCOME_PAGER_REQUEST = 1
    private var isViaDeeplink = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SystemUtils.setTransparentForStatusBar(this)
        setContentView(R.layout.activity_splash)
        startApp()

        AppHelper.api.initInvalidTokenObserver()
        Log.d("activity_test", "start app")


    }

    private fun startApp() {
        onTestIntent(intent)
        var isFirstStartApp = checkProgramStartsFirstTime()
        handleFirstStartApp(isFirstStartApp)
    }


    fun onTestIntent(intent: Intent) {
        val action = intent.getAction()
        val data = intent.getDataString()
        if (Intent.ACTION_VIEW.equals(action) && data != null) {
            AppDataHolder.cancelShareMode = true
            isViaDeeplink = true
            if (data.contains("share")) {
                val placeTypeId = Tools.getPlaceTypeKeySlugId(Uri.parse(data).getQueryParameter("placeTypeId"))
                val placeId = Uri.parse(data).getQueryParameter("placeId").toInt()
                val cityId = Uri.parse(data).getQueryParameter("cityId").toInt()
                AppDataHolder.sharePlaceDetailsData = AppDataHolder.PlaceDetailsShareData(placeTypeId, placeId, cityId, true)
            }
        }


    }


    private fun handleFirstStartApp(firstStartApp: Boolean) {
        if (firstStartApp) {
            var intent = Intent(this, WelcomeActivity::class.java)
            startActivityForResult(intent, WELCOME_PAGER_REQUEST)
        } else {
            handleAuth()

        }
    }


    private fun checkProgramStartsFirstTime(): Boolean {
        var isShowWelcomePager = false

        var isFirstStartApp = AppHelper.preferences.isAppStarted()
        if (isFirstStartApp) {
            isShowWelcomePager = true
        } else {
            isShowWelcomePager = false
            AppHelper.locale.setSystemLanguage(this, AppHelper.locale.getLanguage(this))
        }


        return isShowWelcomePager
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                WELCOME_PAGER_REQUEST -> {
                    AppHelper.preferences.saveAppStarted()
                    Log.d("activity_test", "handle auth result")

                    handleAuth()
                }
            }
        }
    }


    fun handleAuth() {
        if (AppHelper.preferences.getToken().isEmpty()) {
            AuthActivity.start(this)
        } else {
            MainActivity.start(this, isViaDeeplink)
        }
        finish()

    }
}


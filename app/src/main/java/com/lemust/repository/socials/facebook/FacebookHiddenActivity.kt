package com.hairdresser.services.socials.facebook

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import java.util.*


class FacebookHiddenActivity : AppCompatActivity() {
    private var callbackManager: CallbackManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FacebookSdk.sdkInitialize(this.applicationContext);

        startLoginViaFacebook()
    }

    private fun startLoginViaFacebook() {
        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                        FacebookAuthHelper.setFacebookAccessToken(loginResult.accessToken)
                        closeCurrentActivity()
                    }

                    override fun onCancel() {
                        FacebookAuthHelper.setFacebookLoginError(Throwable("Request for facebook login was canceled"))
                        closeCurrentActivity()
                    }

                    override fun onError(exception: FacebookException) {
                        FacebookAuthHelper.setFacebookLoginError(exception)
                        closeCurrentActivity()
                    }
                })
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"))
    }

    private fun closeCurrentActivity() {
        this@FacebookHiddenActivity.finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager?.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}

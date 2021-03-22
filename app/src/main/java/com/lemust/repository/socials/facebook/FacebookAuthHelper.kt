package com.hairdresser.services.socials.facebook

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.facebook.*
import com.facebook.FacebookSdk.getApplicationContext
import com.facebook.login.LoginBehavior
import com.facebook.login.LoginManager
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import org.json.JSONObject


object FacebookAuthHelper {
    val KEY_EMAIL = "email"
    private var publishSubject: PublishSubject<AccessToken>? = null

    fun startFacebookLogin(context: Context): Observable<AccessToken> {
        LoginManager.getInstance().loginBehavior = LoginBehavior.NATIVE_WITH_FALLBACK
        var currentAccessToken = AccessToken.getCurrentAccessToken()
        if (currentAccessToken == null) {
            publishSubject = PublishSubject.create()
            startGoogleLoginActivity(context)
            return publishSubject!!
        } else {
            return Observable.just(currentAccessToken)
        }
    }

    fun setFacebookAccessToken(accessToken: AccessToken) {
        publishSubject?.onNext(accessToken)
        publishSubject = null
    }

    fun setFacebookLoginError(throwable: Throwable) {
        publishSubject?.onError(throwable)
        publishSubject = null
    }

    private fun startGoogleLoginActivity(context: Context) {
        var intent = Intent(context, FacebookHiddenActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    fun disconnectFromFacebook() {

        if (AccessToken.getCurrentAccessToken() == null) {
            return  // already logged out
        }
        GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, GraphRequest.Callback {
            FacebookSdk.sdkInitialize(getApplicationContext());
            LoginManager.getInstance().logOut()
            AccessToken.setCurrentAccessToken(null);

        }).executeAndWait()


    }


    fun getUserDetails(accessToken: AccessToken): Observable<JSONObject> {
        var observable: PublishSubject<JSONObject> = PublishSubject.create()

        var request = GraphRequest.newMeRequest(accessToken, object : GraphRequest.GraphJSONObjectCallback {
            override fun onCompleted(profileResponse: JSONObject, response: GraphResponse?) {
                observable.onNext(profileResponse)
            }

        })
        val parameters = Bundle()
        parameters.putString("fields", "id,name,email")
        request.setParameters(parameters)

        request.executeAsync()

        return observable
    }
}
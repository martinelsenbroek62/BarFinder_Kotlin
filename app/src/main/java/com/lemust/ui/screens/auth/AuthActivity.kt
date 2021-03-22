package com.lemust.ui.screens.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import com.lemust.R
import com.lemust.ui.AppConst.TOKEN_NOT_VALID_KEY
import com.lemust.ui.base.BaseActivity
import com.lemust.ui.screens.auth.sign_up.SignUpFragment
import com.lemust.utils.SystemUtils
import com.lemust.utils.Tools
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.activity_auth.view.*
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent


class AuthActivity : BaseActivity() {
    var view: AuthContract.View? = null
    var presenter: AuthContract.Presenter? = null
    var remainder: AuthContract.Remainder? = null

    companion object {
        fun start(context: AppCompatActivity) {
            var intent = Intent(context, AuthActivity::class.java)
            intent.putExtra(TOKEN_NOT_VALID_KEY, context.intent.getBooleanExtra(TOKEN_NOT_VALID_KEY, false))
            intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION;
            context.startActivity(intent)
        }
    }

    override fun getDefaultFragmentsContainer(): Int? {
        return R.id.base_fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("activity_test","AuthActivity")

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        SystemUtils.setTransparentForStatusBar(this)

        setContentView(R.layout.activity_auth)
        var root = findViewById<View>(android.R.id.content)
        setDefaultProgressLoader(root.pb_map)

        //if token is not valid we showing dialog
        var isTokenNotValid = intent.getBooleanExtra(TOKEN_NOT_VALID_KEY, false)

        view = AuthView(this, root)
        remainder = AuthRemainder(this)
        presenter = AuthPresenter(view as AuthView, activityEventBus, this, remainder as AuthRemainder,isTokenNotValid)
        activityEventBus.register(presenter)

        putRootFragment(SignUpFragment.newInstance("", "sd"))

        KeyboardVisibilityEvent.setEventListener(this!!
        ) {
            if (it) {
                var layoutPar = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Tools.convertDpToPixel(230f, this).toInt())
                space.layoutParams = layoutPar
            } else {
                var layoutPar = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0)
                space.layoutParams = layoutPar

                scroll.post({
                    scroll.init()
                })
            }
        }


    }


    fun login() {
        presenter!!.login()
    }

    override fun onStop() {
        super.onStop()
        presenter?.onStop()
    }

    override fun onDestroy() {
        view!!.onDestroy()
        super.onDestroy()
    }
}

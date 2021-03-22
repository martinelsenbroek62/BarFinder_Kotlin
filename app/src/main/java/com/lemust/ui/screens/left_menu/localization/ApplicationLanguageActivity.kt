package com.lemust.ui.screens.left_menu.localization

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.lemust.R
import com.lemust.ui.base.BaseActivity
import com.lemust.utils.SystemUtils

class ApplicationLanguageActivity : BaseActivity() {
    var view: LanguageContract.View? = null
    var presenter: LanguageContract.Presenter? = null

    override fun getDefaultFragmentsContainer(): Int? {
        return android.R.id.content
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_localization_dialog)
        var root = findViewById<View>(android.R.id.content)
        SystemUtils.setTransparentForStatusBar(this)

        view = LanguageView(this, root)
        presenter = LanguagePresenter(view as LanguageView)

    }
}

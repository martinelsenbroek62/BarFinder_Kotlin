package com.lemust.ui.screens.gallery

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatDelegate
import android.view.View
import android.view.WindowManager
import com.lemust.R
import com.lemust.ui.base.BaseActivity
import com.lemust.utils.AppDataHolder
import com.lemust.utils.Tools

class GalleryActivity : BaseActivity() {
    override fun getDefaultFragmentsContainer(): Int? {
        return R.id.gallery_container
    }

    var view: GalleryContract.View? = null
    var presenter: GalleryContract.Presenter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            Tools.setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
        }
        if (Build.VERSION.SDK_INT >= 19) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        if (Build.VERSION.SDK_INT >= 21) {
            Tools.setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            window.statusBarColor = Color.TRANSPARENT
        }
        var images = AppDataHolder.getSavedPhotos()

        var placeTitle = intent.getStringExtra(PLACE_TITLE_KEY)
        var root = findViewById<View>(android.R.id.content)

        view = GalleryView(this, root)
        presenter = GalleryPresenter(view as GalleryView, activityEventBus, images,placeTitle)
    }

    companion object {
        val PLACE_TITLE_KEY = "place_title_key"
    }
}

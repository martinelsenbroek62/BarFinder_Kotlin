package com.lemust.ui.base.navigation

import android.animation.Animator
import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.view.isVisible
import com.lemust.R
import com.lemust.repository.models.rest.City
import com.lemust.ui.base.BaseActivity
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import android.view.ViewGroup.MarginLayoutParams



open class NavigationController {
    private var width_menu = 0.9;
     var isShowedLeftMenu = false
     var isShowedRightMenu = false
    private val duration = 250L

    private  var leftMenu: FrameLayout
    private  var rightMenu: FrameLayout
    private  var content: FrameLayout
    private  var contentTouchLayout: FrameLayout



    private var windowsManager: WindowManager


    private var fixedX = 0
    private var widthScreen = 0


    constructor(leftMenu: FrameLayout, rightMenu: FrameLayout, content: FrameLayout, contentTouchLayout: FrameLayout, windowsManager: WindowManager) {
        this.leftMenu = leftMenu
        this.rightMenu = rightMenu
        this.content = content
        this.contentTouchLayout = contentTouchLayout
        this.windowsManager = windowsManager

        initWidthContainer()
        initAction()
    }

    fun reset() {
        // screenStateAction.onComplete()
//        if (leftMenuFragment != null)
//            if (leftMenuFragment!!.isAdded)
//                supportFragmentManager.beginTransaction().remove(leftMenuFragment).commit()
//        if (rightMenuFragment != null)
//            if (rightMenuFragment!!.isAdded)
//                supportFragmentManager.beginTransaction().remove(rightMenuFragment).commit()
//        if (contentFragment != null)
//            if (contentFragment!!.isAdded)
//                supportFragmentManager.beginTransaction().remove(contentFragment).commit()

        contentTouchLayout.setOnTouchListener(null)

    }


//    fun getNavigationScreenState(): Observable<StateScreen> {
//        return screenStateAction
//    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initAction() {
        contentTouchLayout.setOnTouchListener { v, event ->
            var x = event!!.rawX


            when (event.action) {
                MotionEvent.ACTION_DOWN
                -> {

                    fixedX = event.x.toInt()

                }
                MotionEvent.ACTION_MOVE -> {
                    var dx = x
                    if (isShowedRightMenu) {
                        dx -= leftMenu.width - (leftMenu.width - fixedX)
                        var left = -content.width * (width_menu)
                        if (dx < 0 && dx > left)

                            content.animate()
                                    .x(dx)
                                    .setDuration(0)
                                    .start();

                    } else if (isShowedLeftMenu) {
                        if (dx - fixedX > 0)
                            content.animate()
                                    .x(dx - fixedX)
                                    .setDuration(0)
                                    .start();
                    }


                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {

                    if (isShowedLeftMenu) {
//                        if (x * width_menu < (widthScreen * width_menu) / 1.30) {
                        hideMenu()
//                        } else {
//                            showLeftMenu()
//                        }
                    } else if (isShowedRightMenu) {

                        hideMenu()
                    }


                }

            }
            true

        }


    }


//    fun addFragmentInLeftContainer(leftMenu: Fragment) {
//        this.leftMenuFragment = leftMenu
//        supportFragmentManager.beginTransaction().replace(R.id.fl_left_container, this.leftMenuFragment).commit()
//    }
//
//    fun addFragmentInContentContainer(content: Fragment) {
//        this.contentFragment = content
////        supportFragmentManager.beginTransaction().replace(R.id.fl_base_container_fragment, content).commit()
//
//        supportFragmentManager.beginTransaction().replace(R.id.fl_base_container_fragment,content,"test").commit()
//
//    }

//    fun removeContentFragment() {
//        if (contentFragment != null) {
//            if (contentFragment!!.isVisible) {
//                supportFragmentManager.beginTransaction().remove(contentFragment).commit()
//
//            }
//        }
//    }


//    fun addFragmentInRightContainer(rightMenu: Fragment) {
//        this.rightMenuFragment = rightMenu
//        supportFragmentManager.beginTransaction().replace(R.id.fl_right_container, this.rightMenuFragment).commit()
//    }

//    private fun inflateContainer() {
//        content = findViewById(R.id.fl_base_container)
//        rightMenu = findViewById(R.id.fl_right_container)
//        leftMenu = findViewById(R.id.fl_left_container)
//        contentTouchLayout = findViewById(R.id.fl_touch_layout)
//
//    }

    private fun initWidthContainer() {
        val metrics = DisplayMetrics()
        windowsManager.defaultDisplay.getMetrics(metrics)
        var widthContent = metrics.widthPixels.toFloat()
        leftMenu.layoutParams.width = (widthContent * width_menu).toInt()
        rightMenu.layoutParams.width = (widthContent * width_menu).toInt()
        widthScreen = metrics.widthPixels
    }


    public fun showRightMenu() {

//        val params = content.layoutParams as MarginLayoutParams
//        params.setMargins(0, 0, -30, 0)
//        content.layoutParams = params

//        rightMenu.let {
//            it.visibility = View.VISIBLE
//        }
//        leftMenu.let {
//            it.visibility = View.GONE
//        }



        isShowedRightMenu = true
        contentTouchLayout.visibility = View.VISIBLE
        val metrics = DisplayMetrics()
        windowsManager.defaultDisplay.getMetrics(metrics)
        var nexX = ((metrics.widthPixels * width_menu)).toInt()
        showMenu(nexX, true)
        rightMenu.visibility = View.VISIBLE
        leftMenu.visibility = View.GONE


    }

    public fun showLeftMenu() {
        leftMenu.let {
            it.visibility = View.VISIBLE
        }
        rightMenu.let {
            it.visibility = View.GONE
        }
        isShowedLeftMenu = true
        contentTouchLayout.visibility = View.VISIBLE
        val metrics = DisplayMetrics()
        windowsManager.defaultDisplay.getMetrics(metrics)
        var nexX = ((metrics.widthPixels * width_menu)).toInt()
        showMenu(nexX, false)
        rightMenu.visibility = View.GONE
        leftMenu.visibility = View.VISIBLE


    }


    private fun showMenu(toX: Int, isSowRight: Boolean) {
        var direction = 1
        if (isSowRight) {
            direction *= -1;
        }

        content.animate()
                .x((toX * direction).toFloat())
                .setDuration(duration)
                .start();

    }

    public fun hideMenu() {
        content.animate()
                .x(0f)
                .setDuration(duration)
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(p0: Animator?) {

                    }

                    override fun onAnimationEnd(p0: Animator?) {



                        contentTouchLayout.visibility = View.GONE
                        //currentStateScreen = StateScreen.MENU_HIDDEN
                        //   screenStateAction.onNext(currentStateScreen)
                        content.animate().setListener(null)



                    }

                    override fun onAnimationCancel(p0: Animator?) {
                        val params = content.layoutParams as MarginLayoutParams
                        params.setMargins(0, 0, 0, 0)
                        content.layoutParams = params

                        contentTouchLayout.visibility = View.GONE
                        //  currentStateScreen = StateScreen.MENU_HIDDEN
                        //screenStateAction.onNext(currentStateScreen)

                        content.animate().setListener(null)

                    }

                    override fun onAnimationStart(p0: Animator?) {
                        Timber.d("start menu hiding, content touch=tru")


                    }
                })
                .start();
        isShowedLeftMenu = false
        isShowedRightMenu = false
    }

    public fun hideMenuWithoutAnimated() {
        isShowedLeftMenu=false
        isShowedRightMenu=false
        content.x = 0F
        contentTouchLayout.visibility = View.GONE

    }

//    enum class StateScreen {
//        LEFT_MENU_SHOWED,
//        RIGHT_MENU_SHOWED,
//        MENU_HIDDEN
//    }

    fun isVisibleLeftMenu(): Boolean {
        return leftMenu.isVisible
    }

    fun isVisibleRightMenu(): Boolean {
        return rightMenu.isVisible
    }

    fun isVisibleMenu(): Boolean {
        return contentTouchLayout.isVisible
    }
}

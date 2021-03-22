package com.lemust.ui.screens.welcome

//import com.stfalcon.frescoimageviewer.ImageViewer
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.support.v4.view.ViewPager
import android.view.View
import com.jakewharton.rxbinding2.view.RxView
import com.lemust.R
import com.lemust.ui.base.BaseActivity
import com.lemust.ui.base.BaseView
import com.lemust.ui.screens.welcome.adapter.WelcomePagerAdapter
import com.lemust.ui.screens.welcome.adapter.WelcomePagerItem
import com.viewpagerindicator.CirclePageIndicator
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_welcome.view.*
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper
import java.util.*


class WelcomeView(var fragment: BaseActivity, var root: View) : WelcomeContract.View, BaseView(fragment) {
    override fun setVisibilityButtonSkip(isVisible: Boolean) {
        if (isVisible) {
            root.tv_skip.visibility = View.VISIBLE
        } else {
            root.tv_skip.visibility = View.INVISIBLE

        }
    }

    override fun onSkipAction(): Observable<Any> {
        return RxView.clicks(root.tv_skip)
    }


    private var mPager: ViewPager? = null
    private var list = ArrayList<WelcomePagerItem>()
    private var indicator: CirclePageIndicator? = null
    private var onPositionObservable = PublishSubject.create<Int>()

    init {
        initPager()
        OverScrollDecoratorHelper.setUpOverScroll(root.vp_welcome_pager);

    }

    override fun onButtonAction(): Observable<Any> {
        return RxView.clicks(root.btn_onboarding)
    }

    override fun setPositionViewPager(position: Int) {
        mPager!!.setCurrentItem(position, true)
    }

    override fun changeButtonTitle(title: String) {
        root.btn_onboarding.text = title
    }

    override fun onPositionAction(): Observable<Int> {
        return onPositionObservable
    }

    override fun initViewPager(pagers: ArrayList<WelcomePagerItem>) {
        list.clear()
        list.addAll(pagers)
        mPager!!.adapter!!.notifyDataSetChanged()
    }

    private fun initPager() {
        mPager = root.vp_welcome_pager

        indicator = root.findViewById(R.id.tv_indicator) as CirclePageIndicator

        var photoAdapter = WelcomePagerAdapter(fragment, list)
        mPager!!.adapter = photoAdapter
        indicator!!.fillColor = fragment.resources.getColor(R.color.colorIndicatorActive);
        indicator!!.strokeColor = fragment.resources.getColor(R.color.colorIndicatorPassive);
        indicator!!.pageColor = fragment.resources.getColor(R.color.colorIndicatorPassive)
        indicator!!.setViewPager(mPager)
        indicator!!.radius = 5 * fragment.resources.displayMetrics.density

        mPager!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                onPositionObservable.onNext(position)
                //    setCurrentPagerTitle(view, position % photoAdapter.getActualListCount())
            }
        })


    }

    override fun finishGotIt() {
        val intent = Intent()
        fragment.setResult(RESULT_OK, intent)
        fragment.finish()

    }


}
package com.lemust.ui.screens.gallery.viwer

import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lemust.LeMustApp
import com.lemust.R
import kotlinx.android.synthetic.main.activity_photo_viwer.view.*


class PhotoPreviewDialog : DialogFragment() {
    private var mPager: ViewPager? = null
    private var list = ArrayList<Bitmap>()
    private var title = ""
    var position = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.full_screen_dialog);
        if (dialog != null) {
            dialog.window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }


    }

    fun showDialog(manager: FragmentManager?, list: ArrayList<Bitmap>, position: Int, title: String) {
        this.list.addAll(list)
        this.position = position
        this.title = title
        if (list.isNotEmpty())
            super.show(manager, "")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.activity_photo_viwer, container, false)
        mPager = view.vp_photos
        if (list.isNotEmpty()) {
            initPager(view)
            initListener(view)
        }
        view.title_shooters.text = title
        return view
    }

    private fun initPager(view: View?) {
        setCurrentPagerTitle(view, position)
        var photoAdapter = PhotoPreviewAdapter(context!!, list)
        mPager!!.adapter = photoAdapter
        mPager!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                setCurrentPagerTitle(view, position % photoAdapter.getActualListCount())
            }
        })
        mPager!!.adapter!!.notifyDataSetChanged()

        var center = ((Int.MAX_VALUE / list.size) / 2) * list.size
        mPager!!.setCurrentItem(center + position, false)

//        mPager!!.currentItem = position
        mPager!!.invalidate()

    }

    private fun setCurrentPagerTitle(view: View?, position: Int) {
        view!!.tv_current_page.text = "${position + 1} ${LeMustApp.instance.resources.getString(R.string.title_of)} ${list.size} ${LeMustApp.instance.resources.getString(R.string.title_photos)}"
    }

    private fun initListener(view: View?) {
        view!!.iv_close.setOnClickListener {
            dismiss()
        }

    }


}

package com.lemust.ui.screens.welcome.adapter

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.bumptech.glide.Glide
import com.lemust.LeMustApp
import com.lemust.R
import com.lemust.utils.Tools
import kotlinx.android.synthetic.main.item_onboarding_pager.view.*
import java.util.*

class WelcomePagerAdapter(private val context: Context, private val welcomePages: ArrayList<WelcomePagerItem>) : PagerAdapter() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)


    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }


    override fun getCount(): Int {
        return welcomePages.size
    }

    override fun instantiateItem(view: ViewGroup, position: Int): Any {
        val layout = inflater.inflate(R.layout.item_onboarding_pager, view, false)
        var item = welcomePages[position]

        Glide.with(context)
                .load(item.resourcesImg)
                .into(layout.img_onboarding);
        layout.tv_onboarding_title.text = item.title
        layout.tv_onboarding_description.text = item.description

        if(position==0){
            var lp=FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
            lp.marginStart= Tools.convertDpToPixel(8f,LeMustApp.instance).toInt()
            layout.img_onboarding.layoutParams=lp
        }
        view.addView(layout, 0)


        return layout
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

}
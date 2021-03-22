package com.lemust.ui.screens.gallery.viwer

import android.content.Context
import android.graphics.Bitmap
import android.os.Parcelable
import android.support.v4.view.PagerAdapter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lemust.R
import kotlinx.android.synthetic.main.item_photo_pager.view.*
import java.util.*

class PhotoPreviewAdapter(private val context: Context, private val statics: ArrayList<Bitmap>) : PagerAdapter() {
    private val inflater: LayoutInflater


    init {
        inflater = LayoutInflater.from(context)


    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }


    override fun getCount(): Int {
        /// return statics.size
        return Int.MAX_VALUE
    }

    fun getActualListCount(): Int {
        return statics.size
    }



    override fun instantiateItem(view: ViewGroup, fakePosition: Int): View {
        val imageLayout = inflater.inflate(R.layout.item_photo_pager, view, false)
        var positionInList = fakePosition % getActualListCount();

        Log.d(PhotoPreviewAdapter::class.java.name, " Fake position: " + fakePosition + " Position in list : " + positionInList);
        var item = statics[positionInList]
        imageLayout.iv_photo.setImageBitmap(item)
        view.addView(imageLayout, 0)
        return imageLayout
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {}

    override fun saveState(): Parcelable? {
        return null
    }


}
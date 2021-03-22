package com.lemust.ui.screens.gallery

import android.content.Context
import android.graphics.Bitmap
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.lemust.R
import com.lemust.ui.base.BaseActivity
import com.lemust.ui.screens.gallery.other.ItemGalleryPreview
import com.lemust.ui.screens.gallery.viwer.PhotoPreviewDialog
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_gallery.view.*
import java.util.*


class GalleryView(var activity: BaseActivity, var root: View) : GalleryContract.View {
    override fun getContext():Context {
        return activity
    }

    override fun finish() {
        activity.finish()
    }

    override fun setPlaceTitle(placeTitle: String) {
        root.tv_place_title.text=placeTitle
    }

    private var galleryRecycler = root.findViewById<RecyclerView>(R.id.rv_gallery_full)
    private var galleryImages = ArrayList<ItemGalleryPreview>()
    private var galleryAdapter: RecyclerPreviewImageAdapter? = null

    init {
        initRecycler()
        root.iv_back.setOnClickListener {
            activity!!.onBackPressed()
        }
    }

    private fun initRecycler() {
      //var manager = ImageGalleryLayoutManager(activity)
      var manager = LinearLayoutManager(activity,LinearLayoutManager.VERTICAL, false)
        galleryAdapter = RecyclerPreviewImageAdapter(galleryImages, activity, false)
        galleryRecycler.layoutManager = manager
        galleryRecycler.adapter = this.galleryAdapter


    }

    override fun onPhotoPreviewClick(): Observable<Int> {
        return galleryAdapter!!.onClick
    }


    override fun showPhoto(list: ArrayList<Bitmap>, position: Int,title:String) {
        PhotoPreviewDialog().showDialog(activity.supportFragmentManager, list, position,title)
    }

    override fun initPhotos(items: ArrayList<ItemGalleryPreview>) {
        galleryImages.addAll(items)
        galleryAdapter!!.notifyDataSetChanged()
    }


}
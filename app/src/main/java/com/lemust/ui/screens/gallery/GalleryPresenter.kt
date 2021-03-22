package com.lemust.ui.screens.gallery

import android.graphics.Bitmap
import com.lemust.R
import com.lemust.ui.screens.gallery.other.ItemGalleryPreview
import com.squareup.otto.Bus


class GalleryPresenter(var view: GalleryContract.View, var eventBus: Bus, var images: ArrayList<Bitmap>, var placeTitle: String) : GalleryContract.Presenter {
    init {
        if (images.isEmpty()) view.finish()
        initData()
        initAction()
    }

    private fun initAction() {
        view.onPhotoPreviewClick().subscribe {
            if (images.isNotEmpty())
                view.showPhoto(images, it, placeTitle)
        }

    }

    private fun initData() {
        var bitmaps = ArrayList<ItemGalleryPreview>()
        var itemGallery = ItemGalleryPreview()
        val iterator = images.iterator()
        var i = 0
        while (iterator.hasNext()) {
            val it = iterator.next()
            when (i) {
                0 -> {
                    itemGallery.img1 = it
                    i++;
                }
                1 -> {
                    itemGallery.img2 = it
                    i++;

                }
                2 -> {
                    itemGallery.img3 = it
                    i++;
                }
                3 -> {
                    itemGallery.img4 = it
                    bitmaps.add(itemGallery)
                    itemGallery = ItemGalleryPreview()
                    i = 0
                }
            }
        }
        if (i <=3) {
            bitmaps.add(itemGallery)
        }



        view.initPhotos(bitmaps)
        view.setPlaceTitle(placeTitle + " · " + images.size.toString() + " ${view.getContext() .resources.getString(R.string.photos)}")
    }


}
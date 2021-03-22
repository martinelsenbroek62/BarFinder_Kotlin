package com.lemust.ui.screens.gallery

import android.content.Context
import android.graphics.Bitmap
import com.lemust.ui.screens.gallery.other.ItemGalleryPreview
import io.reactivex.Observable
import java.util.*


class GalleryContract {
    interface View {
        fun initPhotos(items:ArrayList<ItemGalleryPreview>)
        fun showPhoto(list: ArrayList<Bitmap>, position: Int,title:String)
        fun onPhotoPreviewClick(): Observable<Int>
        fun setPlaceTitle(placeTitle:String)
        fun finish()
        fun getContext():Context

    }

    interface Presenter {

    }
}

package com.lemust.ui.screens.gallery

import android.app.Activity
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.lemust.R
import com.lemust.ui.screens.gallery.other.ItemGalleryPreview
import com.lemust.ui.screens.gallery.other.PhotoParamsHelper
import com.lemust.utils.Tools
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_gallery.view.*


class RecyclerPreviewImageAdapter : RecyclerView.Adapter<RecyclerPreviewImageAdapter.ContentViewHolder> {
    var list = ArrayList<ItemGalleryPreview>()
    var onClick = PublishSubject.create<Int>()!!
//    var heightView = 0
//    private val generator: GenericRequestBuilder<GenerateParams, GenerateParams, Bitmap, GlideDrawable>? = null
//    var heightB = 0
//    var heightM = 0
    var startWith0 = true
    var paramsHelper:PhotoParamsHelper? =null

    constructor(itemList: ArrayList<ItemGalleryPreview>, activity: Activity, startWith0: Boolean) {
        this.list = itemList
//        val displayMetrics = DisplayMetrics()
        this.startWith0 = startWith0
//        activity!!.windowManager
//                .defaultDisplay
//                .getMetrics(displayMetrics)
//
//
////
////        activity!!.windowManager
////                .defaultDisplay
////                .getMetrics(displayMetrics)
//
//        var height = displayMetrics.heightPixels / (displayMetrics.heightPixels / (displayMetrics.widthPixels / 1.9)).toInt()
//        heightM = (height / 2.4).toInt()
      // heightB = heightM + heightM / 4
paramsHelper=PhotoParamsHelper(activity)
//        heightView = (heightB + heightM+Tools.convertDpToPixel(5F,activity)).toInt()


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentViewHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.item_gallery, parent, false)

        return ContentViewHolder(view, parent.context)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ContentViewHolder, position: Int) {
        holder.bindView(list[position])
    }


    inner class ContentViewHolder(itemView: View, var context: Context) : RecyclerView.ViewHolder(itemView) {
        fun bindView(item: ItemGalleryPreview) {
            //x`itemView.layoutParams.height = heightView
//            if (adapterPosition-1 == list.size - 1) {
//                if (item.img3 == null) {
//                    itemView.layoutParams.height = heightView/2
//                    itemView.img1.layoutParams.height = heightView/2
//                    itemView.img2.layoutParams.height = heightView/2
//                }
//            }

//            var margin = Tools.convertDpToPixel(4f, context).toInt()
//            var paramsM = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, heightM)
//            paramsM.setMargins(margin, margin, margin, margin)
//            var paramsB = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, heightB)
//            paramsB.setMargins(margin, margin, margin, margin)

            itemView.img1!!.layoutParams = paramsHelper!!.paramsM
            itemView.img2!!.layoutParams = paramsHelper!!.paramsB
            itemView.img3!!.layoutParams = paramsHelper!!.paramsB
            itemView.img4!!.layoutParams = paramsHelper!!.paramsM

            if (item.img1 != null) Glide.with(context).asBitmap()
                    .load(item.img1)
                    .into(itemView.img1);
            if (item.img2 != null) Glide.with(context).asBitmap()
                    .load(item.img2)
                    .into(itemView.img2);

            if (item.img3 != null) Glide.with(context).asBitmap()
                    .load(item.img3)
                    .into(itemView.img3)
            else
                itemView.img3.visibility = View.GONE

            if (item.img4 != null) Glide.with(context).asBitmap()
                    .load(item.img4)
                    .into(itemView.img4);
            else
                itemView.img4.visibility = View.GONE

            var currentPosition = adapterPosition;
            if (!startWith0)
                currentPosition--

            if (currentPosition == 0) {
                currentPosition = currentPosition;
            } else {
                currentPosition *= 4;

            }

            itemView.img1.setOnClickListener {
                onClick.onNext(currentPosition)
            }
            itemView.img2.setOnClickListener {
                onClick.onNext(currentPosition + 1)

            }
            itemView.img3.setOnClickListener {
                onClick.onNext(currentPosition + 2)

            }
            itemView.img4.setOnClickListener {
                onClick.onNext(currentPosition + 3)

            }


        }
    }
}
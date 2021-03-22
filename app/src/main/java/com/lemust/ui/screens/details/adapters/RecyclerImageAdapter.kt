package com.lemust.ui.screens.details.adapters

import android.content.Context
import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.lemust.R
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_image_galery.view.*


class RecyclerImageAdapter : RecyclerView.Adapter<RecyclerImageAdapter.ContentViewHolder> {
    var list = ArrayList<Bitmap>()
    var onClick = PublishSubject.create<Int>()!!
    //private val generator: GenericRequestBuilder<GenerateParams, GenerateParams, Bitmap, GlideDrawable>? = null

    constructor(itemList: ArrayList<Bitmap>) {
        this.list = itemList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentViewHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.item_image_galery, parent, false)

        return ContentViewHolder(view, parent.context)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ContentViewHolder, position: Int) {
        holder.bindView(list[position])
    }


    inner class ContentViewHolder(itemView: View, var context: Context) : RecyclerView.ViewHolder(itemView) {
        fun bindView(item: Bitmap) {
            itemView.setOnClickListener {
                onClick.onNext(adapterPosition)
            }
            Glide.with(context)
                    .load(item)
                    .into(itemView.imageView3);


        }
    }
}
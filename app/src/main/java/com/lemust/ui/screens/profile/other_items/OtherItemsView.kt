package com.lemust.ui.screens.profile.other_items

//import com.stfalcon.frescoimageviewer.ImageViewer
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.jakewharton.rxbinding2.view.RxView
import com.lemust.R
import com.lemust.ui.base.BaseActivity
import com.lemust.ui.base.BaseView
import com.lemust.ui.screens.profile.other_items.adapter.FavoriteMusicItem
import com.lemust.ui.screens.profile.other_items.adapter.PlaceTypesAdapter
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_place_type.view.*
import kotlinx.android.synthetic.main.loader.view.*


class OtherItemsView(var fragment: BaseActivity, var root: View) : OtherItemsContract.View, BaseView(fragment!!) {

    private var recyclerMusicTypes = root.findViewById<RecyclerView>(R.id.rv_music)
    private var musicItemsItems = ArrayList<FavoriteMusicItem>()
    private var placeTypeAdapter: PlaceTypesAdapter? = null

    init {
        initRecycler()
        root.iv_close.setOnClickListener { fragment.onBackPressed() }
    }
    override fun changeTextInProgressBar(text: String) {
        root.title_loading.text = text
    }

    override fun isShowProgressLoader(isShow: Boolean) {
        (fragment).showDefaultProgressLoader(isShow)

    }

    override fun setMusicTypeTitle(title: String) {
        root.tv_music_type.text = title
    }

    override fun onApplyAction(): Observable<Any> {
        return RxView.clicks(root.tv_apply)
    }

    override fun getFavoriteMusicsItem(): ArrayList<FavoriteMusicItem> {
        return placeTypeAdapter!!.itemsData
    }


    private fun initRecycler() {
        var managerSubFilters = LinearLayoutManager(fragment!!, LinearLayoutManager.VERTICAL, false)
        recyclerMusicTypes.layoutManager = managerSubFilters
        placeTypeAdapter = PlaceTypesAdapter(musicItemsItems)
        recyclerMusicTypes.adapter = placeTypeAdapter
        recyclerMusicTypes.isNestedScrollingEnabled = false;

    }

    override fun setMusicItems(items: List<FavoriteMusicItem>) {
        musicItemsItems.clear()
        musicItemsItems.addAll(items)
        placeTypeAdapter!!.notifyDataSetChanged()
    }


}
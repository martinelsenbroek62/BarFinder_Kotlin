package com.lemust.ui.screens.profile.favorites_place

//import com.stfalcon.frescoimageviewer.ImageViewer
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.jakewharton.rxbinding2.view.RxView
import com.lemust.R
import com.lemust.ui.base.BaseActivity
import com.lemust.ui.base.BaseView
import com.lemust.ui.screens.profile.favorites_place.adapter.FavoriteIPlacestem
import com.lemust.ui.screens.profile.favorites_place.adapter.FavoritesAdapter
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_favorite.view.*
import kotlinx.android.synthetic.main.loader.view.*


class FavoritePlaceView(var fragment: BaseActivity, var root: View) : FavoritePlaceContract.View, BaseView(fragment!!) {
    override fun getFavoriteMusicsItem(): ArrayList<FavoriteIPlacestem> {
        return placeTypeAdapter!!.itemsData    }

    private var recyclerMusicTypes = root.findViewById<RecyclerView>(R.id.rv_music)
    private var musicItemsItems = ArrayList<FavoriteIPlacestem>()
    private var placeTypeAdapter: FavoritesAdapter? = null

    init {
        initRecycler()
        root.iv_close.setOnClickListener { fragment.onBackPressed() }
    }


    override fun changeTextInProgressBar(text: String) {
        root.title_loading.text = text
    }

    private fun initRecycler() {
        var managerSubFilters = LinearLayoutManager(fragment!!, LinearLayoutManager.VERTICAL, false)
        recyclerMusicTypes.layoutManager = managerSubFilters
        placeTypeAdapter = FavoritesAdapter(musicItemsItems)
        recyclerMusicTypes.adapter = placeTypeAdapter
        recyclerMusicTypes.isNestedScrollingEnabled = false;

    }
    override fun setMusicItems(items: List<FavoriteIPlacestem>) {
        musicItemsItems.clear()
        musicItemsItems.addAll(items)
        placeTypeAdapter!!.notifyDataSetChanged()
    }
    override fun isShowProgressLoader(isShow: Boolean) {
        (fragment).showDefaultProgressLoader(isShow)

    }

    override fun onApplyAction(): Observable<Any> {
        return RxView.clicks(root.tv_apply)
    }

}
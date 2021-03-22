package com.lemust.ui.screens.profile.location

import android.content.Context
import android.inputmethodservice.KeyboardView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.lemust.R
import com.lemust.ui.base.BaseActivity
import com.lemust.ui.base.BaseView
import com.lemust.ui.screens.profile.location.adapter.SearchCityAdapter
import com.lemust.ui.screens.profile.location.adapter.SearchCitytem
import com.lemust.utils.Tools
import com.lemust.utils.onMainThread
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_search_city.view.*
import kotlinx.android.synthetic.main.loader.view.*
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import java.util.concurrent.TimeUnit


class SearchCityView(var fragment: BaseActivity, var root: View) : BaseView(fragment), SearchCityContract.View {
    override fun clearList() {
        placesAdapter?.let {
            it.itemsData.clear()
            it.notifyDataSetChanged()
        }    }


    private var recyclerPlaces = root.findViewById<RecyclerView>(R.id.rv_places)
    private var placeItems = ArrayList<SearchCitytem>()

    private var placesAdapter: SearchCityAdapter? = null
    private var clearAction = PublishSubject.create<Any>()
    private var onScrollDown = PublishSubject.create<Any>()
    override fun onScrollDown(): Observable<Any> {
        return onScrollDown
    }

    init {
        initRecycler()

        root.et_places.requestFocus();
        fragment.window.setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        root.iv_back.setOnClickListener { fragment.finish() }

    }

    override fun changeTextInProgressBar(text: String) {
        root.title_loading.text = text
    }

    override fun setDefaultCirt(city: String) {
        root.et_places.postDelayed({
            root.et_places.setText(city)
            root.et_places.setSelection(root.et_places.text.length);
        }, 100)
    }

    override fun onResetAction(): Observable<Any> {
        return RxView.clicks(root.tv_reset)
    }

    override fun clearCity() {
        placeItems.clear()
        placesAdapter!!.notifyDataSetChanged()
    }

    override fun hideKeyboard() {
        val imm = fragment.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(root.et_places!!.windowToken, 0)
    }

    var firstVisibleInListview: Int = 0
    private fun initRecycler() {
        var managerSubFilters = LinearLayoutManager(fragment!!, LinearLayoutManager.VERTICAL, false)
        recyclerPlaces.layoutManager = managerSubFilters
        placesAdapter = SearchCityAdapter(placeItems)
        recyclerPlaces.adapter = placesAdapter
        firstVisibleInListview = managerSubFilters.findFirstVisibleItemPosition()

        recyclerPlaces.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    onScrollDown.onNext(Any())
                }

            }


        })

    }

    override fun setData(data: List<SearchCitytem>) {
        placeItems.clear()
        placeItems.addAll(data)
        placesAdapter!!.notifyDataSetChanged()

    }

    override fun getPlacesName(): Observable<CharSequence> {
        return onMainThread(RxTextView.textChanges(root.findViewById(R.id.et_places)).skip(0).debounce(300, TimeUnit.MILLISECONDS))
    }

    override fun dismiss() {
        //   fragment.dismiss()
    }

    override fun setVisibleLoader(isVisible: Boolean) {
        if (isVisible) {
            root.pb_map_living_in.visibility = View.VISIBLE
        } else {
            root.pb_map_living_in.visibility = View.GONE

        }
    }

    override fun setVisibleContent(isVisible: Boolean) {
        if (isVisible) {
            root.rv_places.visibility = View.VISIBLE
        } else {
            root.rv_places.visibility = View.INVISIBLE

        }

    }


    override fun onTouchItemEvent(): Observable<SearchCitytem> {
        return placesAdapter!!.clickListener
    }


    override fun onClearAction(): Observable<Any> {
        return clearAction
    }
}
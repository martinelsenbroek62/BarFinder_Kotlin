package com.lemust.ui.screens.filters.base

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.jakewharton.rxbinding2.view.RxView
import com.lemust.R
import com.lemust.repository.models.filters.OptionDTO
import com.lemust.ui.base.BaseFragment
import com.lemust.ui.screens.filters.base.adapter.sub_filters.SubFiltersAdapter
import com.lemust.ui.screens.filters.base.adapter.sub_filters.SubFiltersItem
import com.lemust.ui.screens.filters.base.adapter.type_place.PlaceTypeAdapter
import com.lemust.ui.screens.filters.base.adapter.type_place.PlaceTypeItem
import com.lemust.ui.screens.filters.sub.SubFiltersFragment
import com.lemust.ui.screens.main.MainActivity
import com.lemust.utils.AppDataHolder
import com.lemust.utils.AppHelper
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_filters.view.*
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper


class FiltersView(var fragment: BaseFragment, var root: View) : FiltersContract.View {
    override fun onReloadFilter(): Observable<Any> {
return RxView.clicks(root.button_reload)   }

    override fun setVisibleErrorMsg(isVisible: Boolean) {
        if(isVisible){
            root.view_request_error.visibility=View.VISIBLE
        }else{
            root.view_request_error.visibility=View.GONE

        }
    }

    override fun showFilterException(e: Throwable) {
        (fragment.activity as MainActivity).onFilterExeptionAction.onNext(e)
    }

    override fun languageChanged(): Observable<Any> {
        return  (fragment.activity as  MainActivity).onLanguageAction
    }

    override fun hideFilters() {
        (fragment.activity as MainActivity).navigation.hideMenu()
    }

    override fun notifyAdapter() {
        subFiltersAdapter!!.notifyDataSetChanged()
    }

    private var recyclerPlaceType = root.findViewById<RecyclerView>(R.id.rv_place_type)
    private var recyclerSubFilters = root.findViewById<RecyclerView>(R.id.rv_subfilters)
    private var placesTypeItems = ArrayList<PlaceTypeItem>()
    private var subFiltersItems = ArrayList<SubFiltersItem>()

    private var placeTypeAdapter: PlaceTypeAdapter? = null
    private var subFiltersAdapter: SubFiltersAdapter? = null

    private val fadeInAnimation: Animation = AnimationUtils.loadAnimation(fragment.context, R.anim.fade_in);
    private val fadeOutAnimation: Animation = AnimationUtils.loadAnimation(fragment.context, R.anim.fade_out);

    var animSet = arrayOf(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)


    init {
        initRecycler()
        initAnimationListener()
        OverScrollDecoratorHelper.setUpOverScroll(root.rv_subfilters, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);

    }

    override fun onApplyFilters(): Observable<Any> {
        return subFiltersAdapter!!.onApplyAction
    }

    override fun onDestroy() {
        placeTypeAdapter!!.onClickObservable.onComplete()
        subFiltersAdapter!!.openSubFiltersObservable.onComplete()
        subFiltersAdapter!!.onApplyAction.onComplete()

    }

    private fun initAnimationListener() {
        fadeOutAnimation.fillAfter = true
        fadeOutAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {

            }

            override fun onAnimationEnd(p0: Animation?) {
                root.filter_progress_bar.post {
                    root.filter_progress_bar.visibility = View.GONE
                }
            }

            override fun onAnimationStart(p0: Animation?) {
            }
        })

        fadeInAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {

            }

            override fun onAnimationEnd(p0: Animation?) {
                root.content.post {
                    root.content.visibility = View.VISIBLE

                }

            }

            override fun onAnimationStart(p0: Animation?) {
            }
        })
    }


    override fun hideAnimateLoader() {
        root.filter_progress_bar.postDelayed({
            root.filter_progress_bar.startAnimation(fadeOutAnimation)
        }, 30)

    }

    override fun showAnimateContent() {
        root.rv_place_type.postDelayed({
            root.content.post {
                root.content.startAnimation(fadeInAnimation)
            }
        }, 50)
    }

    override fun onBackPressed(): Observable<Any> {
        return (fragment.activity as MainActivity).onBackPressedAction
    }

    override fun setVisibleContentFilter(isVisible: Boolean) {
        if (isVisible) {
            root.content_filter.visibility = View.VISIBLE
        } else {
            root.content_filter.visibility = View.INVISIBLE

        }
    }

    override fun hideScreens() {
        if (AppDataHolder.isOpenedSubScreens) {
            if (myf != null)
                fragment.childFragmentManager.beginTransaction().remove(myf).commit()
            AppDataHolder.isOpenedSubScreens = false

        }
    }


    override fun showContent() {
        root.contentFilters.isClickable = true


    }

    private fun initRecycler() {
        var manager = LinearLayoutManager(fragment.context!!, LinearLayoutManager.HORIZONTAL, false)
        recyclerPlaceType.layoutManager = manager
        placeTypeAdapter = PlaceTypeAdapter(placesTypeItems)
        recyclerPlaceType.adapter = this.placeTypeAdapter

        var managerSubFilters = LinearLayoutManager(fragment.context!!, LinearLayoutManager.VERTICAL, false)
        recyclerSubFilters.layoutManager = managerSubFilters
        subFiltersAdapter = SubFiltersAdapter(subFiltersItems)
        recyclerSubFilters.adapter = subFiltersAdapter


    }

    override fun setFilterPlaceTypeItems(filters: List<PlaceTypeItem>) {
        placesTypeItems.clear()
        placesTypeItems.addAll(filters)
        placeTypeAdapter!!.notifyDataSetChanged()
    }

    override fun setSubFilterItems(filters: List<SubFiltersItem>) {
        subFiltersItems.clear()
        subFiltersItems.addAll(filters)
        subFiltersAdapter!!.notifyDataSetChanged()
        // root.over_scroll.post({ root.over_scroll.init() })

    }


//    override fun showSubFiltersEmptyMessage(isShow: Boolean) {
//        if (isShow) {
//            root.tv_no_filters.visibility = View.VISIBLE
//            recyclerSubFilters.visibility = View.GONE
//        } else {
//            root.tv_no_filters.visibility = View.GONE
//            recyclerSubFilters.visibility = View.VISIBLE
//
//
//        }
//    }

    override fun cancelSelectedItems() {
        subFiltersItems.map { it.isSelected = false }
        subFiltersAdapter!!.notifyDataSetChanged()
    }


    override fun setVisibleContent(isVisible: Boolean) {
        if (isVisible) {
            root.content.visibility = View.VISIBLE
        } else {
            root.content.visibility = View.INVISIBLE

        }
    }

    override fun setVisibleLoader(isVisible: Boolean) {
        if (isVisible) {
            root.filter_progress_bar.visibility = View.VISIBLE
        } else {
            root.filter_progress_bar.visibility = View.GONE

        }
    }


    override fun selectFilterTypeById(id: Int): PlaceTypeItem? {

        var obj = placesTypeItems.filter { it.id == id }
        if (obj.isNotEmpty()) {
            var selectedItem = obj[0]
            if (selectedItem != null) {
                placesTypeItems.forEach { it.isSelected = false }
                selectedItem.isSelected = true
            }
            placeTypeAdapter!!.notifyDataSetChanged()
            return selectedItem
        } else {
            return null
        }
    }

    override fun onResetFilters(): Observable<Any> {
        return RxView.clicks(root.tv_reset)
    }


//    override fun onApplyFilters(): Observable<Any> {
//        return RxView.clicks(root.tv_apply_filters)
//    }

    override fun onChangedPlaceTypeId(): Observable<PlaceTypeItem> {
        return placeTypeAdapter!!.onClickObservable
    }

    override fun onOpenSubFiltersAction(): Observable<SubFiltersItem> {
        return subFiltersAdapter!!.openSubFiltersObservable
    }

    var myf: SubFiltersFragment? = null
    override fun openSubScreen(isMultiChoice: Boolean, id: Int, data: List<OptionDTO>, selectedItems: ArrayList<OptionDTO>?, screenName: String) {
        myf = SubFiltersFragment.newInstance(isMultiChoice, id, data as ArrayList<OptionDTO>, selectedItems, screenName)
        val transaction = fragment.childFragmentManager.beginTransaction()
        transaction.setCustomAnimations(animSet[0], animSet[1], animSet[2], animSet[3])
        transaction.addToBackStack(null);
        transaction.add(root.id, myf)
                .hide(myf)
        transaction.show(myf)
        transaction.commit()
        root.isFocusable = true
        root.isClickable = true
        root.isClickable = false

//        fragment.putFragment(myf!!)
    }

    override fun getAdapterDate(): List<SubFiltersItem> {
        return subFiltersAdapter!!.itemsData
    }


    override fun updateItemSubFilters(id: Int, data: ArrayList<OptionDTO>) {
        if (data.isEmpty()) {
            subFiltersItems.filter { it.filter.id == id.toLong() }.map { it.selectedSubFilters = arrayListOf() }
        } else {
            subFiltersItems.filter { it.filter.id == id.toLong() }.map { it.selectedSubFilters = data }
        }

        subFiltersAdapter!!.notifyDataSetChanged()
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    override fun updateLocaleResources() {
        var res = AppHelper.locale.getCurrentLangResources(fragment.context!!)

        root.tv_filters_title.text =res.getText(R.string.title_filters)
        root.tv_reset.text =res.getText(R.string.title_reset)
        //  root.tv_apply_filters.text = fragment.resources.getText(R.string.title_apply)
        root.title_place_type.text = res.getText(R.string.title_place_type)
        root.title_features.text = res.getText(R.string.title_features)
        //  root.tv_no_filters.text = fragment.resources.getText(R.string.title_no_filters)
    }

    override fun filterDetailsIsShowed(): Boolean = if (myf != null) {
        myf!!.isVisible

    } else false

}
package com.lemust.ui.screens.filters.sub

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.lemust.R
import com.lemust.ui.base.BaseFragment
import com.lemust.ui.screens.filters.sub.adapter.SubAdapter
import com.lemust.ui.screens.filters.sub.adapter.SubItem
import com.lemust.ui.screens.main.MainActivity
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_sub_filters.view.*


class SubFiltersView(var fragment: BaseFragment, var root: View, var isMultiChoice: Boolean) : SubFiltersContract.View {


    private var recyclerSubFilters = root.findViewById<RecyclerView>(R.id.rv_sub_filters)
    private var subFiltersItems = ArrayList<SubItem>()
    private var subFiltersAdapter: SubAdapter? = null


    init {
        initAction()
        initRecycler()
        fragment.fragmentManager!!.beginTransaction().hide(fragment).show(fragment).commit();

    }

    override fun onSelectAction(): Observable<SubItem> {
        return subFiltersAdapter!!.test
    }

    private fun initAction() {
        root.iv_back.setOnClickListener {
            fragment!!.activity!!.onBackPressed()
        }
    }

    private fun initRecycler() {
        var managerSubFilters = LinearLayoutManager(fragment.context!!, LinearLayoutManager.VERTICAL, false)
        recyclerSubFilters.layoutManager = managerSubFilters
        recyclerSubFilters.isNestedScrollingEnabled = false;
        subFiltersAdapter = SubAdapter(subFiltersItems, isMultiChoice)
        recyclerSubFilters.adapter = subFiltersAdapter
    }

    override fun setData(data: List<SubItem>) {
        subFiltersItems.addAll(data)
        subFiltersAdapter!!.notifyDataSetChanged()


    }

//    override fun onApplyAction(): Observable<Any> {
//        return RxView.clicks(root.tv_apply_subfilters)
//    }


    override fun onBackAction(): Observable<Any> {
        return (fragment.activity as MainActivity).onBackPressedAction
    }

    override fun closeScreen() {
        fragment.fragmentManager!!.popBackStack();
    }


    override fun getAdapterData(): ArrayList<SubItem> {
        return subFiltersAdapter!!.itemsData

    }

    override fun setTitleScreen(name: String) {
        root.tv_screen_title.text = name
    }

    override fun invalidate() {
        // fragment.getFragmentManager()!!.beginTransaction().detach(fragment).attach(fragment).commit();
        fragment.getFragmentManager()!!.beginTransaction().hide(fragment).show(fragment).commit();

    }


}
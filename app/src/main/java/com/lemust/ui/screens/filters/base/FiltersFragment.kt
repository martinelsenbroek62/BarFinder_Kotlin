package com.lemust.ui.screens.filters.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lemust.R
import com.lemust.repository.models.filters.OptionDTO
import com.lemust.ui.base.BaseActivity
import com.lemust.ui.base.BaseFragment

class FiltersFragment : BaseFragment() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_filters
    }


    var view: FiltersContract.View? = null
    var presenter: FiltersContract.Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var root = inflater.inflate(R.layout.fragment_filters, container, false)
        view = FiltersView(this, root)
        presenter = FiltersPresenter(view as FiltersView, (activity as BaseActivity).activityEventBus)
        lifecycle.addObserver(view as FiltersView)

        return root
    }

    companion object {
        fun newInstance(): FiltersFragment {
            val fragment = FiltersFragment()
            return fragment
        }
    }


    override fun onDestroyView() {
        view!!.onDestroy()
        presenter!!.onDestroy()
        super.onDestroyView()
        (activity as BaseActivity).activityEventBus.unregister(presenter)

    }

    override fun onBackPressed(): Boolean {
        return super.onBackPressed()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (presenter != null)
            presenter!!.onSaveState(outState)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter?.let {
            it.onRestoreState(savedInstanceState)
        }

    }


    class ChangeSubFilters(var data: ArrayList<OptionDTO>, var id: Int, var isMultiChoice: Boolean)
    class ClearFilters()
    class ShowContent()
    class CleanScreens()
    class HideContent
}

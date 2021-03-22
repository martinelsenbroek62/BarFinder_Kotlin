package com.lemust.ui.screens.filters.sub


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lemust.R
import com.lemust.repository.models.filters.OptionDTO
import com.lemust.ui.base.BaseActivity
import com.lemust.ui.base.BaseFragment
import com.lemust.ui.screens.filters.base.FiltersFragment
import com.lemust.utils.AppDataHolder

class SubFiltersFragment : BaseFragment() {

    var view: SubFiltersContract.View? = null
    var presenter: SubFiltersContract.Presenter? = null

    private var isMultiChoice: Boolean? = null
    private var screenName: String? = null
    private var data: ArrayList<OptionDTO>? = null
    private var selectedItems: ArrayList<OptionDTO>? = null
    private var parentFilterId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            isMultiChoice = arguments!!.getBoolean(IS_MULTI_CHOICE)
            data = arguments!!.getSerializable(LIST_DATA) as ArrayList<OptionDTO>?
            selectedItems = arguments!!.getSerializable(SELECTED_ITEMS) as ArrayList<OptionDTO>?
            parentFilterId = arguments!!.getInt(PARENT_FILTER_ID)
            screenName = arguments!!.getString(SCREEN_NAME)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_sub_filters
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var root = inflater.inflate(R.layout.fragment_sub_filters, container, false)
       AppDataHolder.isOpenedSubScreens=true
        view = SubFiltersView(this, root, isMultiChoice!!)
        presenter = SubFiltersPresenter(view as SubFiltersView, data!!, parentFilterId!!, (activity as BaseActivity).activityEventBus, isMultiChoice, selectedItems,screenName)
        return root
    }

    companion object {

        private val IS_MULTI_CHOICE = "is_multi_choice"
        private val LIST_DATA = "list_data"
        private val SCREEN_NAME = "screen_name"
        private val SELECTED_ITEMS = "selected_items"
        private val PARENT_FILTER_ID = "filte"

        fun newInstance(isMultiChoice: Boolean, id: Int, data: ArrayList<OptionDTO>, selectedItems: ArrayList<OptionDTO>?, screenName: String): SubFiltersFragment {
            val fragment = SubFiltersFragment()
            val args = Bundle()
            args.putBoolean(IS_MULTI_CHOICE, isMultiChoice)
            args.putSerializable(LIST_DATA, data)
            args.putSerializable(SELECTED_ITEMS, selectedItems)
            args.putInt(PARENT_FILTER_ID, id)
            args.putString(SCREEN_NAME, screenName)
            fragment.arguments = args
            return fragment
        }
    }


    override fun onBackPressed(): Boolean {
        activity?.let {
            (it as BaseActivity).activityEventBus.post(FiltersFragment.ShowContent())

        }
        if (fragmentManager!!.backStackEntryCount == 0) {
            super.onBackPressed()
        } else {
            AppDataHolder.isOpenedSubScreens = false
            fragmentManager!!.popBackStack()
        }
        return false
    }

}
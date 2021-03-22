package com.lemust.ui.screens.details.dialog

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.app.DialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lemust.R
import com.lemust.ui.screens.details.dialog.adapter.MenuAdapter
import com.lemust.ui.screens.details.dialog.adapter.MenuItem
import kotlinx.android.synthetic.main.dialog_bottom_sheet.view.*


class BottomSheetFragment : BottomSheetDialogFragment() {
    //    var placeDetailsAction = PublishSubject.create<Any>()
//    var placeDetailsAndMapAction = PublishSubject.create<Any>()

    private var recyclerMenu: RecyclerView? = null
    private var menuItems = ArrayList<MenuItem>()
     var menuAdapter: MenuAdapter? = null


//    var items = HashMap<Int, String>()

    var title1 = ""
    var title2 = ""
    var title3 = ""
    var root: View? = null

    init {

        menuAdapter = MenuAdapter(menuItems)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    }

    fun updateResources(items: ArrayList<MenuItem>) {
//     if(menuAdapter!=null){
//         menuAdapter!!.itemsData.clear()
//         menuAdapter!!.itemsData.addAll(items)
//         menuAdapter!!.notifyDataSetChanged()
//     }
        menuItems.clear()
        menuItems.addAll(items)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        root = inflater.inflate(R.layout.dialog_bottom_sheet, container, false)
        root!!.tv_cancel.setOnClickListener { dismiss() }

        menuAdapter!!.itemsData.clear()

        menuAdapter!!.itemsData.addAll(menuItems)
        recyclerMenu = root!!.findViewById(R.id.menu_items)
        var managerSubFilters = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
        recyclerMenu!!.layoutManager = managerSubFilters
        recyclerMenu!!.adapter = menuAdapter


//        if (title1.isNotEmpty() && title2.isNotEmpty()) {
//            root!!.btn_place_details_share.text = title1
//            root!!.btn_ap_and_place_details_share.text = title2
//        }
//
//        if (title3.isEmpty()) {
//            root!!.btn_3.visibility = View.GONE
//        }

//        root!!.btn_place_details_share.setOnClickListener {
//            placeDetailsAction.onNext(Any())
//        }
//        root!!.btn_ap_and_place_details_share.setOnClickListener {
//            placeDetailsAndMapAction.onNext(Any())
//        }
//
//        root!!.btn_3.setOnClickListener {
//            placeDetailsItemRemove.onNext(Any())
//        }

        return root
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.CustomBottomSheetDialogTheme);

        if (arguments != null) {
            if (arguments!!.containsKey(ARG_ITEMS)) {
                menuItems = arguments!!.getSerializable(ARG_ITEMS) as ArrayList<MenuItem>


            }

        }
    }

    companion object {
        private val ARG_ITEMS = "arg_items"
//        private val ARG_TITLE1 = "title1_key"
//        private val ARG_TITLE2 = "title2_key"
//        private val ARG_TITLE3 = "title3_key"


        fun newInstance(items: ArrayList<MenuItem>): BottomSheetFragment {
            val fragment = BottomSheetFragment()
            val args = Bundle()
            args.putSerializable(ARG_ITEMS, items)

            fragment.arguments = args
            return fragment
        }
    }
}

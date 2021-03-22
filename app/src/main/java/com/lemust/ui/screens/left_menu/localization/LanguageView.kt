package com.lemust.ui.screens.left_menu.localization

import android.app.Activity
import android.content.Context
import android.support.v4.app.DialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.jakewharton.rxbinding2.view.RxView
import com.lemust.R
import com.lemust.ui.base.BaseActivity
import com.lemust.ui.screens.left_menu.localization.adapter.LanguageAdapter
import com.lemust.ui.screens.left_menu.localization.adapter.LanguageItem
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_localization_dialog.view.*
import java.util.*


class LanguageView(var activity: BaseActivity, var root: View) : LanguageContract.View {
    override fun finish() {
        activity.setResult(Activity.RESULT_OK, activity.intent);
        activity.finish()

    }

    override fun getContext(): Context {
        return activity
    }


    private var recyclerCity = root.findViewById<RecyclerView>(R.id.rv_city)
    private var cityItems = ArrayList<LanguageItem>()
    private var cityAdapter: LanguageAdapter? = null

    init {
        // initAction()
        initRecycler()
        root.iv_close_locale.setOnClickListener {
            // fragment.dismiss()
            dismiss()
        }

    }

    override fun onApplyAction(): Observable<Any> {
        return RxView.clicks(root.tv_apply)
    }


    override fun setLanguages(languages: ArrayList<LanguageItem>) {
        cityItems.clear()
        cityItems.addAll(languages)
        cityAdapter!!.notifyDataSetChanged()
    }

    override fun changeLanguage(code: String) {
        root.tv_language_title.setText(R.string.title_language);

    }


    override fun onClickAction(): Observable<LanguageItem> {
        return cityAdapter!!.clickListener
    }

    private fun initRecycler() {
        var managerSubFilters = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerCity.layoutManager = managerSubFilters
        cityAdapter = LanguageAdapter(cityItems)
        recyclerCity.adapter = cityAdapter
        recyclerCity.isNestedScrollingEnabled = false;

    }

    override fun dismiss() {
        activity.finish()

        // fragment.dismiss()
    }


}
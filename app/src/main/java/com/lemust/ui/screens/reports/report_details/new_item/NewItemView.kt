package com.lemust.ui.screens.reports.report_details.new_item

import android.os.SystemClock
import android.view.MotionEvent
import android.view.View
import com.jakewharton.rxbinding2.view.RxView
import com.lemust.R
import com.lemust.ui.base.BaseActivity
import com.lemust.ui.base.BaseView
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_new_item.view.*


class NewItemView(var fragment: BaseActivity, var root: View) : BaseView(fragment), NewItemContract.View {
    override fun setSubTitle(name: String) {
        root.tv_description_title.text=(fragment.getText(R.string.write).toString()+" "+name)

    }

    override fun setTextHint(name: String) {
        root.tv_new_item.post({
            root.tv_new_item.hint = name
        })
    }

    override fun setTitle(name: String) {
        root.tv_title.text = name
    }

    init {
        root.iv_close.setOnClickListener {
            fragment.finish()
        }


        root.tv_place_name.postDelayed ({
            root.tv_place_name.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0f, 0f, 0))
            root.tv_place_name.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0f, 0f, 0))
            root.tv_place_name.requestFocus()
        },50)
    }

    override fun showError() {
        root.tv_new_item.error = root.tv_description_title.text

    }

    override fun getCurrentName(): String {
        return root.tv_place_name.text.toString()
    }

    override fun setCurrentName(name: String) {
        root.tv_place_name.postDelayed({
            root.tv_place_name.requestFocus()
            root.tv_place_name.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0f, 0f, 0))
            root.tv_place_name.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0f, 0f, 0))
            root.tv_place_name.setText(name)
            root.tv_place_name.setSelection(root.tv_place_name.text.length);
        }, 50)
    }

    override fun onApplyAction(): Observable<Any> {
        return RxView.clicks(root.tv_apply)
    }

}
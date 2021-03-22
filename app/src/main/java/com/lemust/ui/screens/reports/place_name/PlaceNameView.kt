package com.lemust.ui.screens.reports.place_name

import android.os.SystemClock
import android.view.MotionEvent
import android.view.View
import com.jakewharton.rxbinding2.view.RxView
import com.lemust.R
import com.lemust.ui.base.BaseActivity
import com.lemust.ui.base.BaseView
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_place_name_creator.view.*


class PlaceNameView(var fragment: BaseActivity, var root: View) : BaseView(fragment), PlaceNameContract.View {
    override fun showError() {
        root.layout_first_name.error = fragment.getString(R.string.title_cant_be_empty)

    }

    init {
      root.iv_close.setOnClickListener {
          fragment.finish()
      }

  }
    override fun getCurrentName(): String {
        return root.tv_place_name.text.toString()
    }

    override fun setCurrentName(name: String) {
        root.tv_place_name.postDelayed({
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
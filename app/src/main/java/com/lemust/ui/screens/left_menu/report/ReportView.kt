package com.lemust.ui.screens.left_menu.report

import android.app.Activity
import android.content.Context
import android.support.v4.app.DialogFragment
import android.view.View
import android.view.WindowManager
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.lemust.R
import com.lemust.ui.base.BaseView
import io.reactivex.Observable

import android.view.inputmethod.InputMethodManager
import kotlinx.android.synthetic.main.activity_report.view.*


class ReportView(var fragment: Activity, var root: View) : ReportContract.View, BaseView(fragment) {
    override fun showEmailKeyboard() {
        root.email_input.visibility=View.VISIBLE

        val imm = fragment.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(root.email_input, InputMethodManager.SHOW_IMPLICIT)
//        root.email_input.postDelayed ({
//            root.email_input.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0f, 0f, 0))
//            root.email_input.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0f, 0f, 0))
//            root.email_input.requestFocus()
//        },50)

    }

    override fun showTextKeyboard() {
        root.text_input.visibility=View.VISIBLE

        val imm = fragment.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(root.text_input, InputMethodManager.SHOW_IMPLICIT)
//     root.text_input.postDelayed ({
//            root.text_input.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0f, 0f, 0))
//            root.text_input.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0f, 0f, 0))
//            root.text_input.requestFocus()
//        },50)

    }

    init {
        root.iv_close_report.setOnClickListener {
            dismiss()
        }
        root.tv_send.setBackgroundResource(R.drawable.background_preview_pressed)


    }

    override fun dismiss() {
        fragment.finish()
    }


    override fun getSequenceEmail(): Observable<CharSequence> {
        return RxTextView.textChanges(root.findViewById(R.id.email_input))
    }

    override fun getSequenceText(): Observable<CharSequence> {
        return RxTextView.textChanges(root.findViewById(R.id.text_input))
    }

    override fun getEmail(): String {
        return root.email_input.text.toString()
    }


    override fun hideEmailFild() {
        root.email_view.visibility = View.GONE
    }

    override fun getText(): String {
        return root.text_input.text.toString()
    }

    override fun sendReportAction(): Observable<Any> {
        return RxView.clicks(root.tv_send)
    }

    override fun isEnableButtonSent(isEnabled: Boolean) {
        if (isEnabled) {
            root.tv_send.isEnabled = true
            root.tv_send.setBackgroundResource(R.drawable.button_filter_selector)
        } else {
            root.tv_send.isEnabled = false
            root.tv_send.setBackgroundResource(R.drawable.background_preview_pressed)


        }
    }

    override fun setEmail(email: String) {
        root.email_input.postDelayed({
            root.email_input.setText(email)
            root.email_input.setSelection(root.email_input.text.length);

        }, 50)
    }


}


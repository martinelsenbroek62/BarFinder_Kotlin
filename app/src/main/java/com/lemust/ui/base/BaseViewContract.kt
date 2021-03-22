package com.lemust.ui.base

import android.content.Context
import com.lemust.ui.base.dialog.DialogModel
import io.reactivex.Observable


interface BaseViewContract {
    fun showDialog(model: DialogModel): Observable<DialogModel.OnDialogResult>
    fun showToast(message: String)
    fun closeDialog()
    fun showErrorDialog(statusCode: Int?, message: String)
    fun showDialog(title: String, message: String)
    fun showPositiveDialog(title: String, message: String)
    fun getViewContext(): Context
    fun showPositiveDialogOkCallback(title: String, message: String): Observable<String>
    fun showNotConnectionDialog(title: String, message: String): Observable<Any>
    fun showSettingDialog(title: String, message: String): Observable<Any>
    fun showDialogWithTwoButtons(title: String, message: String, titleBtn1: String, titleBtn2: String, listener: BaseView.DialogController, isCancelable: Boolean? = null)
    fun showDialogWithOneButtons(title: String, message: String, titleBtn1: String, listener: BaseView.DialogController1?, isCancelable: Boolean? = null)
    fun showSettingDialog(title: String, message: String, positiveTitle: String, negativeString: String): Observable<Any>
}
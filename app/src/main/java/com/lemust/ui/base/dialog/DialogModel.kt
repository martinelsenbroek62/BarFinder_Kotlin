package com.lemust.ui.base.dialog

import android.content.Context
import android.support.v7.app.AlertDialog

class DialogModel {
    var context: Context? = null
    var titleFirstButton = ""
    var titleLastButton = ""
    var message = ""
    var title = ""
    var autoCloseFirstButton = false
    var autoCloseSecondButton = false
    var isSingle = true
    var isCancelable = false
    var isVisibleLoader = false




    fun build(context: Context, title: String,isVisibleLoader:Boolean=false): DialogModel {
        this.context = context
        this.title = title
        this.isVisibleLoader=isVisibleLoader
        return this
    }

    fun isAutoCloseFirstButton(isAutoСlose: Boolean) : DialogModel{
        this.autoCloseFirstButton = isAutoСlose
        return this
    }

    fun isCancable(isCancelable: Boolean): DialogModel {
        this.isCancelable = isCancelable
        return this

    }


    fun isAutoCloseSecondButton(isAutoСlose: Boolean) {
        this.autoCloseSecondButton = isAutoСlose
    }


    fun single(isSingle: Boolean): DialogModel {
        this.isSingle = isSingle
        return this

    }

    fun showFirstButton(title: String): DialogModel {
        this.titleFirstButton = title
        return this
    }

    fun showLastButton(title: String): DialogModel {
        this.titleLastButton = title
        return this
    }

    fun showMessage(message: String): DialogModel {
        this.message = message
        return this
    }


    enum class State {
        FIRST_BUTTON,
        SECOND_BUTTON,
        CANCEL,
        DIALOG_IS_SHOWED
    }

    class OnDialogResult(var clicked: State,var  dialog: AlertDialog)


}

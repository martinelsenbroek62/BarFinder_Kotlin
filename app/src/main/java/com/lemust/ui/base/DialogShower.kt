package com.lemust.ui.base

import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import com.lemust.R


class DialogShower(private var context: Context) {

    fun showErrorDialog(statusCode: Int?, message: String) {
        val builder: AlertDialog.Builder
        builder = AlertDialog.Builder(context,R.style.MyDialogTheme)

        var title = context.getString(R.string.title_error)
        if (statusCode != null) {
            title += " : " + statusCode
        }

        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                })
                .setCancelable(true)
                .show()
    }

    fun showDialogMessage(title : String, message : String){
        val builder: AlertDialog.Builder
        builder = AlertDialog.Builder(context,R.style.MyDialogTheme)
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                })
                .setCancelable(true)
                .show()
    }

}
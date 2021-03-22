package com.lemust.ui.base

import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.util.Log
import android.util.TypedValue
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.lemust.R
import com.lemust.ui.base.dialog.DialogModel
import com.lemust.utils.Tools
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.dialog_base.view.*
import kotlinx.android.synthetic.main.dialog_custom_positive.view.*
import kotlinx.android.synthetic.main.dialog_setting.view.*
import kotlinx.android.synthetic.main.fragment_place_details2.view.*



open class BaseView(val context: Context) : BaseViewContract {
    override fun closeDialog() {
        try {

        dialog?.let {
            if (it.isShowing) {
                it.dismiss()

            }
        }

        }catch (e:Exception){
            Log.e("Error",e.localizedMessage)}
    }


    protected var dialogShower = DialogShower(context)
    var dialog: AlertDialog? = null





    companion object {
        val DIALOG_KEY_EVENT_DISMISS = "dismiss"
        val DIALOG_KEY_EVENT_BACK_PRESSED = "on_back_pressed"
    }


    override fun showDialog(model: DialogModel): Observable<DialogModel.OnDialogResult> {

        var onListener = PublishSubject.create<DialogModel.OnDialogResult>()
        try {

            if (model.isSingle) {
                if (dialog != null)
                    if (dialog!!.isShowing) {
                        return onListener
                    }
            }


            //layout

            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val root: LinearLayout = inflater.inflate(R.layout.dialog_base, null) as LinearLayout

            var uiTitle = root.tv_title_base
            var uiMessage = root.tv_message_base
            var uiFirstButton = root.first_btn
            var uiSecondButton = root.second_btn
            var uiProgressBar = root.progress_bar

            uiFirstButton.visibility = View.GONE
            uiSecondButton.visibility = View.GONE
            uiMessage.visibility = View.GONE
            uiProgressBar.visibility = View.GONE

            uiTitle.text = model.title


            var isSmallText = false
            var maxLargeText = 11

            if (model.titleFirstButton.isNotEmpty()) {
                uiFirstButton.visibility = View.VISIBLE
                uiFirstButton.text = model.titleFirstButton
                if (model.titleFirstButton.length >= maxLargeText) {
                    isSmallText = true
                }
            }

            if (model.titleLastButton.isNotEmpty()) {
                uiSecondButton.visibility = View.VISIBLE
                uiSecondButton.text = model.titleLastButton

                if (model.titleLastButton.length >= maxLargeText) {
                    isSmallText = true
                }
            }

            if (isSmallText) {
                uiFirstButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f);
                uiSecondButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f);
            }
            if (model.isVisibleLoader) {
                uiProgressBar.visibility = View.VISIBLE

            }
            if (model.message.isNotEmpty()) {
                uiMessage.visibility = View.VISIBLE
                uiMessage.text = model.message




            }


            val builder: AlertDialog.Builder = AlertDialog.Builder(context, R.style.MyPositiveDialogTheme)
            builder.setView(root)
            dialog = builder.create()

            uiFirstButton.setOnClickListener {
                if (model.autoCloseFirstButton) dialog!!.dismiss()
                else {
                    onListener.onNext(DialogModel.OnDialogResult(DialogModel.State.FIRST_BUTTON, dialog!!))
                    onListener.onComplete()
                }
            }

            uiSecondButton.setOnClickListener {
                if (model.autoCloseSecondButton) dialog!!.dismiss()
                else {
                    onListener.onNext(DialogModel.OnDialogResult(DialogModel.State.SECOND_BUTTON, dialog!!))
                    onListener.onComplete()

                }
            }
            dialog!!.setCancelable(false)
            if (model.isCancelable) {
                dialog!!.setCanceledOnTouchOutside(true)
            } else
                dialog!!.setCanceledOnTouchOutside(false)

            dialog!!.window.setBackgroundDrawableResource(R.drawable.background_white_dialog)
            dialog!!.setOnDismissListener {
                onListener.onNext(DialogModel.OnDialogResult(DialogModel.State.CANCEL, dialog!!))
                onListener.onComplete()
            }
            dialog!!.show()
        } catch (e: Exception) {
            System.out.print(e.localizedMessage)
        }
        return onListener

    }


    override fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }


    override fun showErrorDialog(statusCode: Int?, message: String) {
        dialogShower.showErrorDialog(statusCode, message)
    }

    override fun showDialog(title: String, message: String) {
        val builder: AlertDialog.Builder
        builder = AlertDialog.Builder(context)

        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                })
                .setCancelable(true)
                .show()
    }

    override fun showPositiveDialog(title: String, message: String) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context, R.style.MyPositiveDialogTheme)
        var dialog: AlertDialog? = null
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: LinearLayout = inflater.inflate(R.layout.dialog_custom_positive, null) as LinearLayout
        view.tv_title.text = title
        view.tv_message.text = message
        view.btn_ok.setOnClickListener { dialog!!.dismiss() }
        builder.setView(view)
        dialog = builder.create()
        dialog.window.setBackgroundDrawableResource(R.drawable.background_white_dialog)
        dialog.show()
    }

    override fun showPositiveDialogOkCallback(title: String, message: String): Observable<String> {
        var subject = PublishSubject.create<String>()
        val builder: AlertDialog.Builder = AlertDialog.Builder(context, R.style.MyPositiveDialogTheme)
        var dialog: AlertDialog? = null
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: LinearLayout = inflater.inflate(R.layout.dialog_custom_positive, null) as LinearLayout
        view.tv_title.text = title
        if (message.isEmpty()) {
            view.tv_message.visibility = View.GONE
        } else {
            view.tv_message.visibility = View.VISIBLE
            view.tv_message.text = message
        }
        view.btn_ok.setOnClickListener {
            subject.onNext(DIALOG_KEY_EVENT_DISMISS)
            subject.onComplete()
            dialog!!.dismiss()
        }

        builder.setView(view)
        dialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.window.setBackgroundDrawableResource(R.drawable.background_white_dialog)
        dialog.show()
        dialog!!.setOnKeyListener { dialog, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                subject.onNext(DIALOG_KEY_EVENT_BACK_PRESSED)
                subject.onComplete()
                dialog!!.dismiss()

            }
            return@setOnKeyListener true
        }
        return subject
    }

    override fun showNotConnectionDialog(title: String, message: String): Observable<Any> {
        var subject = PublishSubject.create<Any>()
        val builder: AlertDialog.Builder = AlertDialog.Builder(context, R.style.MyPositiveDialogTheme)
        var dialog: AlertDialog? = null
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: LinearLayout = inflater.inflate(R.layout.dialog_custom_positive, null) as LinearLayout
        view.tv_title.text = title
        view.tv_message.text = message
        view.btn_ok.text = context.resources.getString(R.string.title_reload)
        view.btn_ok.setOnClickListener {
            subject.onNext(Any())
            subject.onComplete()
            dialog!!.dismiss()
        }
        builder.setView(view)
        dialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.window.setBackgroundDrawableResource(R.drawable.background_white_dialog)
        dialog.show()
        return subject
    }


    override fun showSettingDialog(title: String, message: String): Observable<Any> {
        var subject = PublishSubject.create<Any>()
        val builder: AlertDialog.Builder = AlertDialog.Builder(context, R.style.MyPositiveDialogTheme)
        var dialog: AlertDialog? = null
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: LinearLayout = inflater.inflate(R.layout.dialog_setting, null) as LinearLayout
        view.tv_title_setting.text = title
        view.tv_message_setting.text = message
        if (message.isEmpty()) view.tv_message_setting.visibility = View.GONE
        view.btn_ok_setting.text = "Setting"
        view.btn_ok_setting.setOnClickListener {
            subject.onNext(Any())
            subject.onComplete()
            dialog!!.dismiss()
        }
        view.btn_cancel.setOnClickListener {
            dialog!!.dismiss()
        }
        builder.setView(view)
        dialog = builder.create()
        dialog.setCanceledOnTouchOutside(true)
        dialog.window.setBackgroundDrawableResource(R.drawable.background_white_dialog)
        dialog.show()
        return subject
    }

    override fun showSettingDialog(title: String, message: String, positiveTitle: String, negativeString: String): Observable<Any> {
        var subject = PublishSubject.create<Any>()
        val builder: AlertDialog.Builder = AlertDialog.Builder(context, R.style.MyPositiveDialogTheme)
        var dialog: AlertDialog? = null
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: LinearLayout = inflater.inflate(R.layout.dialog_setting, null) as LinearLayout
        view.tv_title_setting.text = title
        view.tv_message_setting.text = message
        view.btn_ok_setting.text = positiveTitle
        view.btn_cancel.text = negativeString
        view.btn_ok_setting.setOnClickListener {
            subject.onNext(Any())
            subject.onComplete()
            dialog!!.dismiss()
        }
        view.btn_cancel.setOnClickListener {
            dialog!!.dismiss()
        }
        builder.setView(view)
        dialog = builder.create()
        dialog.setCanceledOnTouchOutside(true)
        dialog.window.setBackgroundDrawableResource(R.drawable.background_white_dialog)
        dialog.show()
        return subject
    }

    override fun showDialogWithTwoButtons(title: String, message: String, titleBtn1: String, titleBtn2: String, listener: DialogController, isCancable: Boolean?) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context, R.style.MyPositiveDialogTheme)
        var dialog: AlertDialog? = null
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: LinearLayout = inflater.inflate(R.layout.dialog_setting, null) as LinearLayout
        view.tv_title_setting.text = title
        view.tv_message_setting.text = message

        if (message.isEmpty()) view.tv_message_setting.visibility = View.GONE

        view.btn_ok_setting.text = titleBtn2
        view.btn_cancel.text = titleBtn1

        view.btn_ok_setting.setOnClickListener {
            listener.ok(dialog!!)
        }
        view.btn_cancel.setOnClickListener {
            listener.cancel(dialog!!)
        }


        builder.setView(view)
        dialog = builder.create()
        if (isCancable != null) {
            dialog.setCanceledOnTouchOutside(false)

        } else
            dialog.setCanceledOnTouchOutside(true)
        dialog.window.setBackgroundDrawableResource(R.drawable.background_white_dialog)
        dialog.show()
    }


    override fun showDialogWithOneButtons(title: String, message: String, titleBtn1: String, listener: DialogController1?, isCancelable: Boolean?) {

        try {


            val builder: AlertDialog.Builder = AlertDialog.Builder(context, R.style.MyPositiveDialogTheme)
            var dialog: AlertDialog? = null
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view: LinearLayout = inflater.inflate(R.layout.dialog_custom_positive, null) as LinearLayout
            view.tv_title.text = title
            view.tv_message.text = message


            view.btn_ok.text = titleBtn1
            if (message.isEmpty()) view.tv_message.visibility = View.GONE

            view.btn_ok.setOnClickListener {
                listener?.action1(dialog!!)
            }
            builder.setView(view)
            dialog = builder.create()
            if (isCancelable != null) {
                dialog.setCanceledOnTouchOutside(false)
            } else
                dialog.setCanceledOnTouchOutside(true)
            dialog.window.setBackgroundDrawableResource(R.drawable.background_white_dialog)
            dialog.setOnDismissListener {
                listener?.action1(dialog!!)

            }
            dialog.show()
        } catch (e: Exception) {
            System.out.print(e.localizedMessage)
        }
    }

    override fun getViewContext(): Context = context

    interface DialogController {
        fun ok(dialog: AlertDialog)
        fun cancel(dialog: AlertDialog)
    }

    interface DialogController1 {
        fun action1(dialog: AlertDialog)
    }
}
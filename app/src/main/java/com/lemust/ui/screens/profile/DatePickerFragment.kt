package com.lemust.ui.screens.profile

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.widget.DatePicker
import com.lemust.R
import io.reactivex.subjects.PublishSubject
import java.text.SimpleDateFormat
import java.util.*


class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {
    var date: SimpleDateFormat? = null
    fun showDialog(manager: FragmentManager, currentDate: String?) {
        if (currentDate != null) {
            date = SimpleDateFormat("yyyy-MM-dd")
            date!!.parse(currentDate)
        }
        show(manager, "")
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var calendar = Calendar.getInstance()

        if (date != null) {
            calendar = date!!.calendar
        }


        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)


        val pickerDialog = DatePickerDialog(activity!!, R.style.CustomDialogTheme, this, year, month, day)
        pickerDialog.datePicker.maxDate = System.currentTimeMillis()
        return pickerDialog
    }


    var dateEvent: PublishSubject<Date>? = PublishSubject.create()
    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        dateEvent!!.onNext(Date(year, month+1, day))
        dateEvent!!.onComplete()

    }

    public class Date(var year: Int, var month: Int, var day: Int)
}

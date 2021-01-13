package com.example.githubuser.ui.dialog

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import com.example.githubuser.R
import java.util.*


class TimeDialogFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    private var listener : DialogTimeListener ?= null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = activity as DialogTimeListener
    }

    override fun onDetach() {
        super.onDetach()
        if ( listener != null) {
            listener = null
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_time_dialog, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()

        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        val minute = calendar.get(Calendar.MINUTE)

        val formatHour24 = true

        return TimePickerDialog(context, this, hour, minute, formatHour24)
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        listener?.onDialogTimeSet(tag ?: "tag", hourOfDay, minute)
    }

    interface DialogTimeListener {
        fun onDialogTimeSet(tag: String, hourOfDay: Int, minute: Int)
    }

}
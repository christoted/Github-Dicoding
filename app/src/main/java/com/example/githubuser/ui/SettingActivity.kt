package com.example.githubuser.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.githubuser.R
import com.example.githubuser.schedule.AlarmReceiver
import com.example.githubuser.ui.dialog.TimeDialogFragment
import kotlinx.android.synthetic.main.activity_setting.*
import java.text.SimpleDateFormat
import java.util.*

class SettingActivity : AppCompatActivity(), View.OnClickListener, TimeDialogFragment.DialogTimeListener{

    companion object {
        private const val TIME_PICKER_REPEAT_TAG = "TimePickerRepeat"
    }

    private lateinit var alarmReceiver: AlarmReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        alarmReceiver = AlarmReceiver()

        btnSetTimeAlarm.setOnClickListener(this)
        btnStartRepeatAlarm.setOnClickListener(this)
        btnStopRepeatAlarm.setOnClickListener(this)


    }

    override fun onClick(view: View?) {
        val alarmReceiver = AlarmReceiver()
        if ( view == btnSetTimeAlarm) {
            val fragmentManager = supportFragmentManager
            val optionDialogFragment = TimeDialogFragment()
            optionDialogFragment.show(fragmentManager, TIME_PICKER_REPEAT_TAG)
        } else if ( view == btnStartRepeatAlarm) {
            val time = tvRestSetAlarm.text.toString()
            val message = etCustomMessage.text.toString()
            if (isNotEmptyTime(time) && isNotEmptyMessage(message)) {
                alarmReceiver.setRepeatAlarm(this, AlarmReceiver.TYPE_REPEATING_ALARM, time,message)
            } else if (isNotEmptyTime(time) && !isNotEmptyMessage(message)) {
                alarmReceiver.setRepeatAlarm(this, AlarmReceiver.TYPE_REPEATING_ALARM, time,tvDefaultMessage.text.toString())
            } else if ( !isNotEmptyTime(time) && isNotEmptyMessage(message)) {
                alarmReceiver.setRepeatAlarm(this, AlarmReceiver.TYPE_REPEATING_ALARM, tvDefaultTime.text.toString(),message)
            } else if (!isNotEmptyTime(time) && !isNotEmptyMessage(message)) {
                val times = tvDefaultTime.text.toString()
                val messages = tvDefaultMessage.text.toString()
                alarmReceiver.setRepeatAlarm(this, AlarmReceiver.TYPE_REPEATING_ALARM, times, messages)
                Log.d("Test 12345", "onClick: $time + $message")
             //   Toast.makeText(this, "$tvDefaultTime + $tvDefaultMessage", Toast.LENGTH_SHORT).show()
            }

        } else if ( view == btnStopRepeatAlarm) {
            alarmReceiver.cancelAlarm(this, AlarmReceiver.TYPE_REPEATING_ALARM)
        }
    }

    private fun isNotEmptyMessage(message: String): Boolean {
        if ( message.isEmpty()) {
            return false
        }

        return true
    }

    private fun isNotEmptyTime(time : String): Boolean {

        if ( time.isEmpty()) {
            return false
        }

        return true
    }

    override fun onDialogTimeSet(tag: String, hourOfDay: Int, minute: Int) {
        val calender = Calendar.getInstance()
        calender.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calender.set(Calendar.MINUTE, minute)

        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        when(tag) {
            TIME_PICKER_REPEAT_TAG -> {
                tvRestSetAlarm.text = dateFormat.format(calender.time)
            }
        }
    }
}
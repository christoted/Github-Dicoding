package com.example.githubuser.schedule

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.githubuser.R
import com.example.githubuser.ui.DetailActivity
import com.example.githubuser.ui.MainActivity
import okhttp3.internal.concurrent.Task
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        const val TYPE_REPEATING_ALARM = "RepeatingAlarm"
        const val EXTRA_MESSAGE = "Message"

        private const val ID_REPEATING = 101
        private const val TIME_FORMAT = "HH:mm"
    }



    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        val message = intent.getStringExtra(EXTRA_MESSAGE)
        val title = TYPE_REPEATING_ALARM
        val notifyId = ID_REPEATING
        showToast(context, title, message)
        showNotification(context, title, message, notifyId)
    }

    private fun showToast(context: Context, title: String, message: String?) {
        Toast.makeText(context, "$title - $message  ", Toast.LENGTH_SHORT).show()
    }

    private fun showNotification(context: Context, title: String, message: String?, notifyId : Int) {
        val channelId = "channel_1"
        val channelName = "RepeatAlarm Manager"

        val pendingIntent = TaskStackBuilder.create(context)
            .addParentStack(MainActivity::class.java)
            .addNextIntent(Intent(context, MainActivity::class.java))
            .getPendingIntent(110, PendingIntent.FLAG_UPDATE_CURRENT)


        val notificationManagerCompat = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_person)
            .setContentTitle(title)
            .setContentText(message)
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setVibrate(longArrayOf(1000,1000,1000,1000,1000))
            .setContentIntent(pendingIntent)
            .setSound(alarmSound)

        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            builder.setChannelId(channelId)
            notificationManagerCompat.createNotificationChannel(channel)
        }

        val notification = builder.build()
        notificationManagerCompat.notify(notifyId, notification)
    }

    fun setRepeatAlarm(context: Context, type: String, time: String, message: String) {
        if (isDateInvalid(time, TIME_FORMAT)) return

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra(EXTRA_MESSAGE, message)

        val timeArray = time.split(":".toRegex()).dropLastWhile {
            it.isEmpty()
        }.toTypedArray()

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]))
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]))
        calendar.set(Calendar.SECOND, 0)

        val pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING, intent, 0)
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)

        Toast.makeText(context, "Repeating Alarm Started $time", Toast.LENGTH_SHORT).show()

    }

    fun cancelAlarm(context: Context, type: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val requestCode = ID_REPEATING
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0)
        pendingIntent.cancel()

        alarmManager.cancel(pendingIntent)
        Toast.makeText(context, "Repeating Alarm Stopped", Toast.LENGTH_SHORT).show()
    }

    private fun isDateInvalid(date: String, dateFormat: String): Boolean {
        return try {
            val df = SimpleDateFormat(dateFormat, Locale.getDefault())
            df.isLenient = false
            df.parse(date)
            false
        } catch (e : ParseException) {
            true
        }
    }
}
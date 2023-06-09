package com.example.myapplication

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat


class BroadCastReceiverAct : AppCompatActivity() {

    lateinit var  tv : TextView
    private lateinit var builder : NotificationCompat.Builder
    private var data = "Hello"
    private val channelId = "Some Channel ID"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_broad_cast_reciever)


        tv = findViewById(R.id.statusOfBattery)

        registerReceiver(batteryData, IntentFilter(Intent.ACTION_BATTERY_CHANGED))

        findViewById<Button>(R.id.batterySettings).setOnClickListener {
            startActivity(Intent(this, ActionOnBroadCastReceiver::class.java))
        }
    }
    private fun batteryNotification() {
        createNotificationChannel()
        builder = NotificationCompat.Builder(this, channelId)
        builder.apply {
            setSmallIcon(R.drawable.ic_settings)
            setContentTitle("Activity First")
            setContentText(data)
            priority = NotificationCompat.PRIORITY_DEFAULT
        }
        with(NotificationManagerCompat.from(this)){
            if (ActivityCompat.checkSelfPermission(
                    this@BroadCastReceiverAct,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(2,builder.build())
        }
    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(channelId,"First Channel", NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = "This description of Notification"
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }
    private val batteryData : BroadcastReceiver = object : BroadcastReceiver(){
        @SuppressLint("SetTextI18n")
        override fun onReceive(context: Context?, intent: Intent?) {
            val percentageData = intent?.getIntExtra("level",0)
            tv.text = "Your battery percentage is "+percentageData.toString()+"%"
            data = tv.text.toString()
            batteryNotification()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(batteryData)
    }

}




package com.example.thesisapp.Reciever

import android.content.Context
import android.content.Intent
import android.content.BroadcastReceiver
import android.os.CountDownTimer
import android.util.Log
import android.provider.Settings
import com.example.thesisapp.Constant.DEVICEID
import com.example.thesisapp.Constant.TimeDelay1
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Alarm1: BroadcastReceiver(){

    private lateinit var database: DatabaseReference


    override fun onReceive(p0: Context?, p1: Intent?) {
        val sharedPreferences = p0?.getSharedPreferences("MY_PREFERENCES", Context.MODE_PRIVATE)
        val deviceID = sharedPreferences?.getString(DEVICEID,"NO ID")
        val timedelay = sharedPreferences?.getString(TimeDelay1,"30")
        val timers = (timedelay?.toLong())?.times(1000)!!
        Log.e("Timedelay", timers.toString())
        database = FirebaseDatabase.getInstance().getReference("device")
        database.child(deviceID.toString()).child("motor").child("Valve1").setValue("ON")
        database.child(deviceID.toString()).child("motor").child("Valve2").setValue("ON")
        Log.e("Alarm","Fired1")
        val timer = object: CountDownTimer((timedelay?.toLong())?.times(1000)!!, 1000) {
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {

                database.child(deviceID.toString()).child("motor").child("Valve2").setValue("OFF")
                database.child(deviceID.toString()).child("motor").child("Valve1").setValue("OFF")
                Log.e("Motor", "OFF")
            }
        }
        timer.start()
    }
}
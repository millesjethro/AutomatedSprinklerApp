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
        database = FirebaseDatabase.getInstance().getReference("device")
        database.child(DEVICEID).child("motor").child("Valve1").setValue("ON")
        database.child(DEVICEID).child("motor").child("Valve2").setValue("ON")
        Log.e("Alarm","Fired1")
        val timer = object: CountDownTimer(TimeDelay1.toLong()*1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {

                database.child(DEVICEID).child("motor").child("Valve2").setValue("OFF")
                database.child(DEVICEID).child("motor").child("Valve1").setValue("OFF")
                Log.e("Motor", "OFF")
            }
        }
        timer.start()
    }
}
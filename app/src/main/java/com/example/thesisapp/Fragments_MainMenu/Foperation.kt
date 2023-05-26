package com.example.thesisapp.Fragments_MainMenu

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.thesisapp.Constant.DEVICEID
import com.example.thesisapp.R
import com.example.thesisapp.Reciever.Alarm1
import com.example.thesisapp.databinding.FragmentFoperationBinding
import com.google.firebase.database.*
import java.util.*


class Foperation : Fragment(), View.OnClickListener {
    private lateinit var database: DatabaseReference
    private lateinit var binding: FragmentFoperationBinding
    private var OperationsNumber = 0
    private var alarmMgr: AlarmManager? = null
    private lateinit var alarmIntent: PendingIntent
    private var HumidLimit = ""
    private var MoistureLimit = ""
    private var DEVICEIDS = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFoperationBinding.inflate(inflater,container,false)
        val sharedPreferences = this.activity?.getSharedPreferences("MY_PREFERENCES", Context.MODE_PRIVATE)
        val deviceID = sharedPreferences?.getString(DEVICEID,"NO ID")
        DEVICEIDS = deviceID.toString()
        binding.currDevice.text = "CURRENT DEVICE: $DEVICEIDS"
        database = FirebaseDatabase.getInstance().getReference("device")
        toggleListener()
        checkDatabaseOpt()
        Log.e("ValueOpt",OperationsNumber.toString())
        checkDatabaseOpt1()
        checkDatabaseManualopt()
        manualOver()
        DisabledAll()
        textViewsChanges()
        binding.manualOverride.isEnabled = true
        binding.automaticOverride.isEnabled = true
        binding.sensorOverride.isEnabled = true

        binding.setBtnValve1.setOnClickListener(this)
        binding.pickBtnVavle1.setOnClickListener(this)
        binding.setBtnSensor.setOnClickListener(this)
        return binding.root
    }

    fun ChooseOperation(){
        database = FirebaseDatabase.getInstance().getReference("device")
        if(binding.manualOverride.isChecked){
            binding.automaticOverride.text = "Disable "
            binding.manualOverride.text = "Enabled "
            binding.sensorOverride.text = "Disabled "
            DisabledAll()
            binding.manualOverride.isEnabled = true
            binding.toggleValve1.isEnabled = true
            binding.toggleValve2.isEnabled = true
            OperationsNumber = 1
            database.child(DEVICEIDS).child("operation").child("number").setValue(OperationsNumber)

        }
        if(binding.automaticOverride.isChecked){
            binding.automaticOverride.text = "Enable "
            binding.manualOverride.text = "Disabled "
            binding.sensorOverride.text = "Disabled "

            DisabledAll()
            binding.automaticOverride.isEnabled = true
            binding.setBtnValve1.isEnabled = false
            binding.pickBtnVavle1.isEnabled = true
            OperationsNumber = 2
            database.child(DEVICEIDS).child("operation").child("number").setValue(OperationsNumber)
        }
        if(binding.sensorOverride.isChecked){
            binding.automaticOverride.text = "Disabled "
            binding.manualOverride.text = "Disabled "
            binding.sensorOverride.text = "Enabled "

            DisabledAll()
            binding.edtHumidLimit.isEnabled = true
            binding.edtMoistureLimit.isEnabled = true
            binding.sensorOverride.isEnabled = true
            binding.setBtnSensor.isEnabled = true
            OperationsNumber = 3
            database.child(DEVICEIDS).child("operation").child("number").setValue(OperationsNumber)
        }
        if(!binding.manualOverride.isChecked && !binding.automaticOverride.isChecked && !binding.sensorOverride.isChecked){
            binding.automaticOverride.text = "Disabled "
            binding.manualOverride.text = "Disabled "
            binding.sensorOverride.text = "Disabled "

            database.child(DEVICEIDS).child("motor").child("Valve1").setValue("OFF")
            database.child(DEVICEIDS).child("motor").child("Valve2").setValue("OFF")

            database.child(DEVICEIDS).child("operation").child("moisturelimit").setValue("101")
            database.child(DEVICEIDS).child("operation").child("humiditylimit").setValue("101")

            if(binding.setBtnSensor.text == "STOP"){
                binding.setBtnSensor.text == "SET"
            }

            binding.schedTxtValve1.text = "PICK TIME"

            binding.toggleValve1.isChecked = false
            binding.toggleValve2.isChecked = false

            DisabledAll()

            alarmMgr?.cancel(alarmIntent)
            Log.e("Alarm1", "Canceled")


            binding.manualOverride.isEnabled = true
            binding.automaticOverride.isEnabled = true
            binding.sensorOverride.isEnabled = true


            OperationsNumber = 0
            database.child(DEVICEIDS).child("operation").child("number").setValue(OperationsNumber)
        }
    }

    fun manualOver(){
        binding.toggleValve1.setOnCheckedChangeListener { _, _ ->
            if(binding.toggleValve1.isChecked){
                //Valve 1 is on
                if(OperationsNumber == 1) {
                    database.child(DEVICEIDS).child("motor").child("Valve1").setValue("ON")
                } else if(OperationsNumber == 2 && OperationsNumber == 3){
                    val toast =
                        Toast.makeText(activity, "You didn't choose any operation!", Toast.LENGTH_SHORT)
                    toast.show()
                } else {

                }
            }
            else{
                //Valve 1 is off
                if(OperationsNumber == 1) {
                    database.child(DEVICEIDS).child("motor").child("Valve1").setValue("OFF")
                } else if(OperationsNumber == 2 && OperationsNumber == 3){
                    val toast =
                        Toast.makeText(activity, "You didn't choose any operation!", Toast.LENGTH_SHORT)
                    toast.show()
                } else {

                }
            }
        }
        binding.toggleValve2.setOnCheckedChangeListener { _, _ ->
            if(binding.toggleValve2.isChecked){
                //Vavle 2 is on
                if(OperationsNumber == 1) {
                    database.child(DEVICEIDS).child("motor").child("Valve2").setValue("ON")
                } else if(OperationsNumber == 2 && OperationsNumber == 3){
                    val toast =
                        Toast.makeText(activity, "You didn't choose any operation!", Toast.LENGTH_SHORT)
                    toast.show()
                } else {

                }
            }
            else{
                //Valve 2 is off
                if(OperationsNumber == 1) {
                    database.child(DEVICEIDS).child("motor").child("Valve2").setValue("OFF")
                } else  if(OperationsNumber == 2 && OperationsNumber == 3){
                    val toast =
                        Toast.makeText(activity, "You didn't choose any operation!", Toast.LENGTH_SHORT)
                    toast.show()
                } else {

                }
            }
        }
    }

    fun autoOver1() {
        val time1 = binding.schedTxtValve1.text.toString().split(":")

        if(time1[0].toInt()>=13){
            val timers1 = time1[0].toInt() - 12
            val timers2 = time1[1].toInt()
            binding.schedTxtValve1.text = ("$timers1:$timers2 PM")
        }
        else if(time1[1].toInt()<=9){
            binding.schedTxtValve1.text = time1[0]+":0"+time1[1]+" AM"
        }
        else{
            binding.schedTxtValve1.text = time1[0]+":"+time1[1]+" AM"
        }

        alarmMgr = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmIntent = Intent(context, Alarm1::class.java).let { intent ->
            PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        }
        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, time1[0].toInt())
            set(Calendar.MINUTE, time1[1].toInt())
        }

        // With setInexactRepeating(), you have to use one of the AlarmManager interval
        // constants--in this case, AlarmManager.INTERVAL_DAY.
        alarmMgr?.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            alarmIntent
        )
        database.child(DEVICEIDS).child("operation").child("clockhour").setValue(time1[0])
        database.child(DEVICEIDS).child("operation").child("clockmin").setValue(time1[1])
        Log.e("Alarm:", "Alarm Set")
    }

    fun sensorOver(){
        HumidLimit = binding.edtHumidLimit.text.toString()
        MoistureLimit = binding.edtMoistureLimit.text.toString()
        var hum = false
        var mos = false
        if(HumidLimit.isNullOrEmpty()){
            hum = false
        }
        else if(HumidLimit.toInt() in 0..100){
            hum = true
        }
        else{
            binding.edtHumidLimit.error = "Value between 0 and 100!"
            hum = false
        }

        if(MoistureLimit.isNullOrEmpty()){
            mos = false
        }
        else if(MoistureLimit.toInt() in 0..100){
            mos = true
        }
        else{
            binding.edtMoistureLimit.error = "Value between 0 and 100!"
            mos = false
        }


        if(mos && hum){
            database.child(DEVICEIDS).child("operation").child("humiditylimit").setValue(HumidLimit)
            database.child(DEVICEIDS).child("operation").child("moisturelimit").setValue(MoistureLimit)
        }
        else if(!mos && hum){
            database.child(DEVICEIDS).child("operation").child("humiditylimit").setValue(HumidLimit)
        }
        else if(mos && !hum){
            database.child(DEVICEIDS).child("operation").child("moisturelimit").setValue(MoistureLimit)
        }
        else{
            binding.edtHumidLimit.error = "Put a value!"
            binding.edtMoistureLimit.error = "Put a value!"
        }
    }

    fun DisabledAll(){
        binding.toggleValve1.isEnabled = false
        binding.toggleValve2.isEnabled = false
        binding.setBtnValve1.isEnabled = false
        binding.setBtnSensor.isEnabled = false
        binding.manualOverride.isEnabled = false
        binding.edtHumidLimit.isEnabled = false
        binding.edtMoistureLimit.isEnabled = false
        binding.pickBtnVavle1.isEnabled = false
        binding.automaticOverride.isEnabled = false
        binding.sensorOverride.isEnabled = false
    }

    fun checkDatabaseManualopt(){
        database = FirebaseDatabase.getInstance().getReference("device")
        database.child(DEVICEIDS).child("motor").child("Valve1").get().addOnSuccessListener {
            binding.toggleValve1.isChecked = it.value == "ON"
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }

        database.child(DEVICEIDS).child("motor").child("Valve2").get().addOnSuccessListener {
            binding.toggleValve2.isChecked = it.value == "ON"
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }
    }

    fun checkDatabaseOpt(){
        database = FirebaseDatabase.getInstance().getReference("device")
        database.child(DEVICEIDS).child("operation").child("number").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.e("Value",snapshot.value.toString())
                when (snapshot.value.toString()) {
                    "0" ->{
                        binding.manualOverride.isChecked = false
                        binding.automaticOverride.isChecked = false
                        binding.sensorOverride.isChecked = false
                        OperationsNumber = 0
                        ChooseOperation()
                    }
                    "1" -> {
                        DisabledAll()
                        binding.manualOverride.isChecked = true
                        OperationsNumber = 1
                        checkDatabaseOpt1()
                        ChooseOperation()
                    }
                    "2" -> {
                        DisabledAll()
                        binding.automaticOverride.isChecked = true
                        OperationsNumber = 2
                        checkDatabaseOpt2()
                        ChooseOperation()
                    }
                    "3" -> {
                        DisabledAll()
                        binding.sensorOverride.isChecked = true
                        OperationsNumber = 3
                        checkDatabaseOpt3()
                        ChooseOperation()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })


    }

    fun checkDatabaseOpt1(){
        database = FirebaseDatabase.getInstance().getReference("device")
        database.child(DEVICEIDS).child("motor").child("Valve1")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    binding.toggleValve1.isChecked = snapshot.value == "ON"
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })


        database.child(DEVICEIDS).child("motor").child("Valve2")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    binding.toggleValve2.isChecked = snapshot.value == "ON"
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }

    fun checkDatabaseOpt2(){
        database.child(DEVICEIDS).child("motor").child("Valve1")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    binding.valve1Opt2.text = "Valve1: "+snapshot.value
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })


        database.child(DEVICEIDS).child("motor").child("Valve2")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    binding.valve2Opt2.text = "Valve2: "+snapshot.value
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

        var minutes = ""
        var hours = ""
        database = FirebaseDatabase.getInstance().getReference("device")
        database.child(DEVICEIDS).child("operation").child("clockhour")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.e("snapshot1",snapshot.value.toString())
                    if (snapshot.value.toString() == "None") {
                        binding.schedTxtValve1.text = "PICK TIME"
                        binding.setBtnValve1.text = "SET"
                        binding.setBtnValve1.isEnabled = false
                    } else {
                        hours = snapshot.value.toString()
                        if (minutes != "" && hours.toInt() >= 12) {
                            val hours2 = hours.toInt() - 12
                            binding.schedTxtValve1.text = "$hours2:$minutes PM"
                        } else {
                            binding.schedTxtValve1.text = "$hours:$minutes AM"
                        }
                        binding.setBtnValve1.isEnabled = true
                        binding.setBtnValve1.text = "STOP"
                        binding.pickBtnVavle1.isEnabled = false
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })

        database.child(DEVICEIDS).child("operation").child("clockmin")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.e("snapshot2",snapshot.value.toString())
                    if (snapshot.value.toString() == "None") {
                        binding.schedTxtValve1.text = "PICK TIME"
                        binding.setBtnValve1.text = "SET"
                        binding.setBtnValve1.isEnabled = false
                    } else {
                        minutes = snapshot.value.toString()
                        if (minutes != "" && hours.toInt() >= 12) {
                            val hours2 = hours.toInt() - 12
                            binding.schedTxtValve1.text = "$hours2:$minutes PM"
                        } else {
                            binding.schedTxtValve1.text = "$hours:$minutes AM"
                        }
                        binding.setBtnValve1.isEnabled = true
                        binding.setBtnValve1.text = "STOP"
                        binding.pickBtnVavle1.isEnabled = false
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })

        Log.e("Time","$hours:$minutes")


    }

    fun checkDatabaseOpt3(){
        var humidi = true
        var moistu = true
        database = FirebaseDatabase.getInstance().getReference("device")
        database.child(DEVICEIDS).child("operation").child("humiditylimit").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.value == "101"){
                    humidi = false
                }
                else{
                    humidi = true
                    val setval = snapshot.value.toString()
                    binding.edtHumidLimit.setText(setval)
                    binding.edtHumidLimit.isEnabled = false
                    binding.edtMoistureLimit.isEnabled = false
                    binding.setBtnSensor.text = "STOP"
                }

                if(!moistu && !humidi){
                    binding.setBtnSensor.isEnabled = true
                    binding.setBtnSensor.text = "SET"
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
        database.child(DEVICEIDS).child("operation").child("moisturelimit").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.value == "101"){
                    moistu = false
                }
                else{
                    moistu = true
                    val setval = snapshot.value.toString()
                    binding.edtMoistureLimit.setText(setval)
                    binding.edtHumidLimit.isEnabled = false
                    binding.edtMoistureLimit.isEnabled = false
                    binding.setBtnSensor.text = "STOP"
                }

                if(!moistu && !humidi){
                    binding.setBtnSensor.isEnabled = true
                    binding.setBtnSensor.text = "SET"
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })


    }

    fun toggleListener(){
        binding.manualOverride.setOnCheckedChangeListener { _, _ ->
            //checkDatabaseManualopt()
            ChooseOperation()
        }
        binding.automaticOverride.setOnCheckedChangeListener { _, _ ->
            ChooseOperation()
        }
        binding.sensorOverride.setOnCheckedChangeListener { _, _ ->
            ChooseOperation()
        }
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            (R.id.setBtnValve1)->{
                if(binding.setBtnValve1.text == "SET") {
                    binding.setBtnValve1.text = "STOP"
                    binding.pickBtnVavle1.isEnabled = false
                    autoOver1()
                }
                else{
                    alarmMgr = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    alarmIntent = Intent(context, Alarm1::class.java).let { intent ->
                        PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
                    }
                    alarmMgr?.cancel(alarmIntent)
                    Log.e("Alarm1", "Canceled")
                    database.child(DEVICEIDS).child("operation").child("clockhour").setValue("None")
                    database.child(DEVICEIDS).child("operation").child("clockmin").setValue("None")
                    binding.setBtnValve1.text = "SET"
                    binding.pickBtnVavle1.isEnabled = true
                }
            }
            (R.id.pickBtnVavle1)->{
                val c = Calendar.getInstance()

                // on below line we are getting our hour, minute.
                val hour = c.get(Calendar.HOUR_OF_DAY)
                val minute = c.get(Calendar.MINUTE)

                // on below line we are initializing
                // our Time Picker Dialog
                val timePickerDialog = TimePickerDialog(
                    context,
                    { _, hourOfDay, minute ->
                        // on below line we are setting selected
                        // time in our text view.
                        binding.schedTxtValve1.text = "$hourOfDay:$minute"
                    },
                    hour,
                    minute,
                    false
                )
                // at last we are calling show to
                // display our time picker dialog.
                timePickerDialog.show()
                binding.setBtnValve1.isEnabled = true
            }
            (R.id.setBtn_sensor)->{
                if(binding.setBtnSensor.text == "SET") {
                    binding.setBtnSensor.text = "STOP"

                    sensorOver()

                    binding.edtHumidLimit.isEnabled = false
                    binding.edtMoistureLimit.isEnabled = false
                }
                else{
                    binding.setBtnSensor.text = "SET"
                    database.child(DEVICEIDS).child("operation").child("moisturelimit").setValue("101")
                    database.child(DEVICEIDS).child("operation").child("humiditylimit").setValue("101")
                    binding.edtHumidLimit.isEnabled = true
                    binding.edtMoistureLimit.isEnabled = true
                }
            }
        }
    }

    fun textViewsChanges(){
        database.child(DEVICEIDS).child("sensors").child("humidity").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.humidityTxt.text = "HUMIDITY: "+snapshot.value+"%"
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
        database.child(DEVICEIDS).child("sensors").child("moisture").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.moistureTxt.text = "MOISTURE: "+snapshot.value+"%"
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        database.child(DEVICEIDS).child("sensors").child("temperature").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.temperatureTxt.text = "MOISTURE: "+snapshot.value+"C"
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}


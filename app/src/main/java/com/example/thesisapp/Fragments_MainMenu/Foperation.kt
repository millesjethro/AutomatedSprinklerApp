package com.example.thesisapp.Fragments_MainMenu

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFoperationBinding.inflate(inflater,container,false)
        database = FirebaseDatabase.getInstance().getReference("device")
        toggleListener()
        checkDatabaseOpt()
        checkDatabaseManualopt()
        manualOver()
        binding.setBtnValve1.setOnClickListener(this)
        binding.pickBtnVavle1.setOnClickListener(this)
        binding.setBtnSensor.setOnClickListener(this)
        return binding.root
    }

    fun ChooseOperation(){
        if(binding.manualOverride.isChecked){
            binding.automaticOverride.text = "Disable "
            binding.manualOverride.text = "Enabled "
            binding.sensorOverride.text = "Disabled "
            DisabledAll()
            binding.manualOverride.isEnabled = true
            binding.toggleValve1.isEnabled = true
            binding.toggleValve2.isEnabled = true
            OperationsNumber = 1
            database.child(DEVICEID).child("operation").child("number").setValue(OperationsNumber)

        }
        if(binding.automaticOverride.isChecked){
            binding.automaticOverride.text = "Enable "
            binding.manualOverride.text = "Disabled "
            binding.sensorOverride.text = "Disabled "

            DisabledAll()
            binding.automaticOverride.isEnabled = true
            binding.setBtnValve1.isEnabled = false
            OperationsNumber = 2
            database.child(DEVICEID).child("operation").child("number").setValue(OperationsNumber)
        }
        if(binding.sensorOverride.isChecked){
            binding.automaticOverride.text = "Disabled "
            binding.manualOverride.text = "Disabled "
            binding.sensorOverride.text = "Enabled "

            DisabledAll()
            binding.sensorOverride.isEnabled = true
            binding.setBtnSensor.isEnabled = true
            OperationsNumber = 3
            database.child(DEVICEID).child("operation").child("number").setValue(OperationsNumber)
        }
        if(!binding.manualOverride.isChecked && !binding.automaticOverride.isChecked && !binding.sensorOverride.isChecked){
            binding.automaticOverride.text = "Disabled "
            binding.manualOverride.text = "Disabled "
            binding.sensorOverride.text = "Disabled "

            database.child(DEVICEID).child("motor").child("Valve1").setValue("OFF")
            database.child(DEVICEID).child("motor").child("Valve2").setValue("OFF")

            database.child(DEVICEID).child("operation").child("moisturelimit").setValue("101")
            database.child(DEVICEID).child("operation").child("humidtylimit").setValue("101")

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
            database.child(DEVICEID).child("operation").child("number").setValue(OperationsNumber)
        }
    }

    fun manualOver(){
        binding.toggleValve1.setOnCheckedChangeListener { _, _ ->
            if(binding.toggleValve1.isChecked){
                //Valve 1 is on
                if(OperationsNumber == 1) {
                    database.child(DEVICEID).child("motor").child("Valve1").setValue("ON")
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
                    database.child(DEVICEID).child("motor").child("Valve1").setValue("OFF")
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
                    database.child(DEVICEID).child("motor").child("Valve2").setValue("ON")
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
                    database.child(DEVICEID).child("motor").child("Valve2").setValue("OFF")
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
            PendingIntent.getBroadcast(context, 0, intent, 0)
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
            database.child(DEVICEID).child("operation").child("humidtylimit").setValue(HumidLimit)
            database.child(DEVICEID).child("operation").child("moisturelimit").setValue(MoistureLimit)
        }
        else if(!mos && hum){
            database.child(DEVICEID).child("operation").child("humidtylimit").setValue(HumidLimit)
        }
        else if(mos && !hum){
            database.child(DEVICEID).child("operation").child("moisturelimit").setValue(MoistureLimit)
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
        binding.automaticOverride.isEnabled = false
        binding.sensorOverride.isEnabled = false
    }

    fun checkDatabaseManualopt(){
        database = FirebaseDatabase.getInstance().getReference("device")
        database.child(DEVICEID).child("motor").child("Valve1").get().addOnSuccessListener {
            binding.toggleValve1.isChecked = it.value == "ON"
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }

        database.child(DEVICEID).child("motor").child("Valve2").get().addOnSuccessListener {
            binding.toggleValve2.isChecked = it.value == "ON"
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }
    }

    fun checkDatabaseOpt(){
        database = FirebaseDatabase.getInstance().getReference("device")
        database.child(DEVICEID).child("operation").child("number").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.e("Value",snapshot.value.toString())
                when (snapshot.value.toString()) {
                    "0" ->{
                        binding.manualOverride.isChecked = false
                        binding.automaticOverride.isChecked = false
                        binding.sensorOverride.isChecked = false
                    }
                    "1" -> {
                        DisabledAll()
                        binding.manualOverride.isChecked = true
                        ChooseOperation()
                    }
                    "2" -> {
                        DisabledAll()
                        binding.automaticOverride.isChecked = true
                        ChooseOperation()
                    }
                    "3" -> {
                        DisabledAll()
                        binding.sensorOverride.isChecked = true
                        ChooseOperation()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    fun toggleListener(){
        binding.manualOverride.setOnCheckedChangeListener { _, _ ->
            checkDatabaseManualopt()
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
                    autoOver1()
                }
                else{
                    alarmMgr = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    alarmIntent = Intent(context, Alarm1::class.java).let { intent ->
                        PendingIntent.getBroadcast(context, 0, intent, 0)
                    }
                    alarmMgr?.cancel(alarmIntent)
                    Log.e("Alarm1", "Canceled")
                    binding.setBtnValve1.text = "SET"
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
                }
                else{
                    binding.setBtnSensor.text = "SET"
                    database.child(DEVICEID).child("operation").child("moisturelimit").setValue("101")
                    database.child(DEVICEID).child("operation").child("humidtylimit").setValue("101")
                }
            }
        }
    }
}


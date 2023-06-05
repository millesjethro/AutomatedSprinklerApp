package com.example.thesisapp.Fragments_MainMenu

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.thesisapp.Constant.DEVICEID
import com.example.thesisapp.R
import com.example.thesisapp.databinding.FragmentFhomeBinding
import com.google.firebase.database.*
import java.lang.Math.ceil
import java.lang.Math.round
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*

class Fhome : Fragment() {
    private lateinit var binding: FragmentFhomeBinding
    private lateinit var database: DatabaseReference
    private var DEVICEIDS = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFhomeBinding.inflate(inflater,container,false)
        val sharedPreferences = this.activity?.getSharedPreferences("MY_PREFERENCES", Context.MODE_PRIVATE)
        val deviceID = sharedPreferences?.getString(DEVICEID,"NO ID")
        DEVICEIDS = deviceID.toString()
        database = FirebaseDatabase.getInstance().getReference("device")
        dateToday()
        bgChanges()
        Motors()
        Sensors()
        return binding.root
    }

    fun dateToday(){
        val c = Calendar.getInstance()
        val today = c.get(Calendar.DAY_OF_MONTH)
        val month = c.get(Calendar.MONTH)+1
        val year = c.get(Calendar.YEAR)
        val x = c.get(Calendar.DAY_OF_WEEK)-1
        var month1 = ""
        var dayNow = ""
        Log.e("Week",x.toString())
        when(month){
            1->{
                month1 = "January"
            }
            2->{
                month1 = "February"
            }
            3->{
                month1 = "March"
            }
            4->{
                month1 = "April"
            }
            5->{
                month1 = "May"
            }
            6->{
                month1 = "June"
            }
            7->{
                month1 = "July"
            }
            8->{
                month1 = "August"
            }
            9->{
                month1 = "September"
            }
            10->{
                month1 = "October"
            }
            11->{
                month1 = "November"
            }
            12->{
                month1 = "December"
            }
        }
        when(x){
            1->{
                dayNow = "Monday"
            }
            2->{
                dayNow = "Tuesday"
            }
            3->{
                dayNow = "Wednesday"
            }
            4->{
                dayNow = "Thursday"
            }
            5->{
                dayNow = "Friday"
            }
            6->{
                dayNow = "Saturday"
            }
            7->{
                dayNow = "Sunday"
            }
        }
        binding.DayNow.text = dayNow
        binding.NumberYes.text = month1
        binding.NumberNow.text = today.toString()
        binding.NumberTom.text = year.toString()
    }
    fun bgChanges(){
        val c = Calendar.getInstance()
        val time = c.get(Calendar.HOUR_OF_DAY)
        Log.e("Time", time.toString())
        if(time <= 4 || time >= 18){
            binding.view.background = resources.getDrawable(R.drawable.night_time)
            binding.view2.background = resources.getDrawable(R.drawable.night_time)
            binding.view3.background = resources.getDrawable(R.drawable.night_time)
        }
        else{
            binding.view.background = resources.getDrawable(R.drawable.day_time)
            binding.view2.background = resources.getDrawable(R.drawable.day_time)
            binding.view3.background = resources.getDrawable(R.drawable.day_time)        }
    }
    fun Motors(){
        database.child(DEVICEIDS).child("motor").child("Valve1")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    binding.valve1Stats.text = snapshot.value.toString()
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })


        database.child(DEVICEIDS).child("motor").child("Valve2")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    binding.valve2Stats.text = snapshot.value.toString()
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }

    fun Sensors(){
        database.child(DEVICEIDS).child("sensors").child("humidity").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.txtHumid.text =  snapshot.value.toString()+" %"
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
        database.child(DEVICEIDS).child("sensors").child("moisture").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.txtMoist.text =   snapshot.value.toString()+" %"
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
        database.child(DEVICEIDS).child("sensors").child("temperature").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.txtTemp.text =  snapshot.value.toString()+" C°"
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })


        database.child(DEVICEIDS).child("sensors").child("humidity1").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.textHumid.text = snapshot.value.toString()+" %"
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
        database.child(DEVICEIDS).child("sensors").child("moisture1").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.textMoist.text =  snapshot.value.toString()+" %"
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
        database.child(DEVICEIDS).child("sensors").child("temperature1").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.textTemp.text = snapshot.value.toString()+" C°"
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}
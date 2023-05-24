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
import java.time.LocalDateTime
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
        val yesterday = c.get(Calendar.DAY_OF_MONTH)-1
        val tomorrow = c.get(Calendar.DAY_OF_MONTH)+1
        val x = c.get(Calendar.DAY_OF_WEEK)-1
        Log.e("Week",x.toString())
        val date = arrayOf("","","")

        when (x) {
            1 -> {
                date[0] = "Sunday"
                date[1] = "Monday"
                date[2] = "Tuesday"
            }
            2 -> {
                date[0] = "Monday"
                date[1] = "Tuesday"
                date[2] = "Wednesday"
            }
            3 -> {
                date[0] = "Tuesday"
                date[1] = "Wednesday"
                date[2] = "Thursday"
            }
            4 -> {
                date[0] = "Wednesday"
                date[1] = "Thursday"
                date[2] = "Friday"
            }
            5 -> {
                date[0] = "Thursday"
                date[1] = "Friday"
                date[2] = "Saturday"
            }
            6 -> {
                date[0] = "Friday"
                date[1] = "Saturday"
                date[2] = "Sunday"
            }
            7 -> {
                date[0] = "Saturday"
                date[1] = "Sunday"
                date[2] = "Monday"
            }
        }

        binding.NumberYes.text = yesterday.toString()
        binding.NumberNow.text = today.toString()
        binding.NumberTom.text = tomorrow.toString()


        binding.DayYes.text = date[0]
        binding.DayNow.text = date[1]
        binding.DayTom.text = date[2]
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
                binding.txtHumid.text = snapshot.value.toString()+"%"
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
        database.child(DEVICEIDS).child("sensors").child("moisture").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.txtMoist.text = snapshot.value.toString()+"%"
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
        database.child(DEVICEIDS).child("sensors").child("temperature").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.txtTemp.text = snapshot.value.toString()+" C°"
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}
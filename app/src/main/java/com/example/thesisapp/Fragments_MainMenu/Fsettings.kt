package com.example.thesisapp.Fragments_MainMenu

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.thesisapp.Constant.DEVICEID
import com.example.thesisapp.R
import com.example.thesisapp.databinding.FragmentFsettingsBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class Fsettings : Fragment(),View.OnClickListener {
    private lateinit var database: DatabaseReference
    private lateinit var binding: FragmentFsettingsBinding
    private var DevID = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFsettingsBinding.inflate(inflater,container,false)
        val sharedPreferences = this.activity?.getSharedPreferences("MY_PREFERENCES", Context.MODE_PRIVATE)
        val deviceID = sharedPreferences?.getString(DEVICEID,"NO ID")
        binding.curDevice.text = "CURRENT DEVICE ID: $deviceID"
        database = FirebaseDatabase.getInstance().getReference("device")

        binding.passDevice.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.deviceId.text.toString().isNullOrEmpty()) {
                    binding.deviceId.error = "This field can't be left empty"
                } else {
                    database.child(binding.deviceId.text.toString()).child("password").get()
                        .addOnSuccessListener {
                            if (binding.passDevice.text.toString() == it.value.toString()) {
                                DevID = true
                            } else {
                                binding.passDevice.error = "Wrong Credentials"
                            }
                        }
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        binding.saveBtnId.setOnClickListener(this)
        binding.saveBtnSecs.setOnClickListener(this)
        return binding.root
    }

    override fun onClick(p0: View?) {
        val sharedPreferences = this.activity?.getSharedPreferences("MY_PREFERENCES", Context.MODE_PRIVATE)
        when(p0!!.id){
            (R.id.saveBtnId)-> {
                if (binding.deviceId.text.toString().isEmpty()) {
                    binding.deviceId.error = "No ID was entered!"
                }
                if(DevID == true){
                    val editor = sharedPreferences?.edit()
                    editor?.putString(DEVICEID, binding.deviceId.text.toString())
                    editor?.apply()

                    val deviceID = sharedPreferences?.getString(DEVICEID, "NO ID")
                    binding.curDevice.text = "CURRENT DEVICE ID: $deviceID"
                }
                else{
                    binding.deviceId.error = "You have entered a wrong ID!"
                }
            }
            (R.id.saveBtnSecs)->{
                val deviceID = sharedPreferences?.getString(DEVICEID, "NO ID")
                if(deviceID != "NO ID") {
                    database.child(deviceID.toString()).child("operation").child("timedelay")
                        .setValue(binding.valveOnSeconds.text.toString().toInt())
                } else {
                    binding.valveOnSeconds.error = "There Were No ID detected!"
                }
            }
        }
    }
}
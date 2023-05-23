package com.example.thesisapp.Fragments_MainMenu

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.thesisapp.Constant.DEVICEID
import com.example.thesisapp.Constant.TimeDelay1
import com.example.thesisapp.R
import com.example.thesisapp.databinding.FragmentFsettingsBinding


class Fsettings : Fragment(),View.OnClickListener {

    private lateinit var binding: FragmentFsettingsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFsettingsBinding.inflate(inflater,container,false)
        val sharedPreferences = this.activity?.getSharedPreferences("MY_PREFERENCES", Context.MODE_PRIVATE)
        val deviceID = sharedPreferences?.getString(DEVICEID,"NO ID")
        binding.curDevice.text = "CURRENT DEVICE ID: $deviceID"
        val currSecs = sharedPreferences?.getString(TimeDelay1,"NO TIME")
        binding.txtCurrSecs.text = "CURRENT SECONDS: $currSecs"
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
                } else {
                    val editor = sharedPreferences?.edit()
                    editor?.putString(DEVICEID, binding.deviceId.text.toString())
                    editor?.apply()

                    val deviceID = sharedPreferences?.getString(DEVICEID, "NO ID")
                    binding.curDevice.text = "CURRENT DEVICE ID: $deviceID"
                }
            }
            (R.id.saveBtnSecs)->{
                if(binding.valveOnSeconds.text.toString().toInt() <= 10){
                    binding.valveOnSeconds.error = "The input was to low"
                }
                else {
                    val editor = sharedPreferences?.edit()
                    editor?.putString(TimeDelay1, binding.valveOnSeconds.text.toString())
                    editor?.apply()

                    val currSecs = sharedPreferences?.getString(TimeDelay1, "NO TIME")
                    binding.txtCurrSecs.text = "CURRENT SECONDS: $currSecs"
                }
            }
        }
    }
}
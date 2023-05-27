package com.example.thesisapp

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.thesisapp.Constant.DEVICEID
import com.example.thesisapp.databinding.ActivityUserInfoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.util.*

class UserInfo : AppCompatActivity(),View.OnClickListener {

    private lateinit var binding: ActivityUserInfoBinding
    private lateinit var databaseD: DatabaseReference
    private lateinit var databaseU: DatabaseReference
    private lateinit var auths: FirebaseAuth
    private var USERID = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        databaseD = FirebaseDatabase.getInstance().getReference("device")
        databaseU = FirebaseDatabase.getInstance().getReference("users")
        auths = Firebase.auth
        binding.btnSave.setOnClickListener(this)
        binding.bdayPicker.setOnClickListener(this)
        USERID = auths.currentUser?.uid.toString()
        binding.txtDevicePass.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(binding.txtDeviceID.text.toString().isNullOrEmpty()){
                    binding.txtDeviceID.error = "This field can't be left empty"
                }
                else {
                    databaseD.child(binding.txtDeviceID.text.toString()).child("password").get().addOnSuccessListener {
                        if (binding.txtDevicePass.text.toString() == it.value.toString()) {
                            DevID = true
                        } else {
                            binding.txtDevicePass.error = "Wrong Credentials"
                        }
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
    }
    private var Name = false
    private var DevID = false

    override fun onClick(p0: View?) {
        val sharedPreferences = this.getSharedPreferences("MY_PREFERENCES", Context.MODE_PRIVATE)

        when(p0!!.id){
            (R.id.bdayPicker)->{
                
                val c = Calendar.getInstance()

                // on below line we are getting
                // our day, month and year.
                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)

                // on below line we are creating a
                // variable for date picker dialog.
                val datePickerDialog = DatePickerDialog(
                    // on below line we are passing context.
                    this,
                    { _, year, monthOfYear, dayOfMonth ->
                        // on below line we are setting
                        // date to our text view.
                        binding.bdayPicker.text =
                            (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                    },
                    // on below line we are passing year, month
                    // and day for the selected date in our date picker.
                    year,
                    month,
                    day
                )
                // at last we are calling show
                // to display our date picker dialog.
                datePickerDialog.show()
            }
            (R.id.btnSave)->{
                CheckIsNull()
                if(Name == true && DevID == true){
                    databaseU.child(USERID).child("Name").setValue(binding.txtFullName.text.toString())
                    databaseU.child(USERID).child("City").setValue(binding.txtCity.text.toString())
                    databaseU.child(USERID).child("HouseNum").setValue(binding.txtHouseNum.text.toString())
                    databaseU.child(USERID).child("Street").setValue(binding.txtStreet.text.toString())
                    databaseU.child(USERID).child("Province").setValue(binding.txtProvince.text.toString())
                    databaseU.child(USERID).child("Birthday").setValue(binding.bdayPicker.text.toString())

                    val editor = sharedPreferences?.edit()
                    editor?.putString(DEVICEID, binding.txtDeviceID.text.toString())
                    editor?.apply()

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                else{
                    val toast = Toast.makeText(applicationContext,"Please Check the information above", Toast.LENGTH_SHORT)
                    toast.show()
                }
            }
        }
    }

    fun CheckIsNull(){
        if(binding.txtFullName.text.toString().isNullOrEmpty()){
            binding.txtFullName.error = "This field can't be left empty"
        }
        else{
            Name = true
        }
    }
}
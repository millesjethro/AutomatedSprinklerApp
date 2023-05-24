package com.example.thesisapp.Fragments_MainMenu

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.thesisapp.LoginActivity
import com.example.thesisapp.MainActivity
import com.example.thesisapp.R
import com.example.thesisapp.databinding.FragmentFaccountBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.util.*

class Faccount : Fragment(), View.OnClickListener{

    private lateinit var binding: FragmentFaccountBinding
    private lateinit var auths: FirebaseAuth
    private lateinit var database: DatabaseReference
    private var bday = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFaccountBinding.inflate(inflater,container,false)
        database = FirebaseDatabase.getInstance().getReference("users")
        auths = Firebase.auth
        binding.logoutBtn.setOnClickListener(this)

        database.child(auths.currentUser?.uid.toString()).child("Birthday").get().addOnSuccessListener {
            binding.txtvBday.text = "Birthday: "+it.value.toString()
            bday = it.value.toString()
            if(bday != ""){
                ageCheker()
            }
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }
        database.child(auths.currentUser?.uid.toString()).child("Name").get().addOnSuccessListener {
            binding.txtvName.text = "NAME: "+it.value.toString()
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }



        binding.txtvEmail.text = "Email: "+auths.currentUser?.email


        return binding.root
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            (R.id.logoutBtn)->{
                auths.signOut()

                val intent = Intent(context, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }

    fun ageCheker(){
        val c = Calendar.getInstance()
        val m1 = c.get(Calendar.MONTH)
        val d1 = c.get(Calendar.DAY_OF_MONTH)
        val y1 = c.get(Calendar.YEAR)
        var birth = bday.split("-")
        var bd = birth[0].toInt() - d1
        var md = birth[1].toInt() - m1
        var yd = y1 - birth[2].toInt()

        if(md <= 0){
            if(bd <= 0){
                binding.txtvAge.text = "Age: $yd"
            }
            else{
                yd--
                binding.txtvAge.text = "Age: $yd"
            }
        }
        else{
            yd--
            binding.txtvAge.text = "Age: $yd"
        }
    }
}
package com.example.thesisapp

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.thesisapp.Constant.CURRENT_EMAIL
import com.example.thesisapp.Constant.CURRENT_PASSWORD
import com.example.thesisapp.Constant.DEVICEID
import com.example.thesisapp.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var auths: FirebaseAuth
    private lateinit var databaseU: DatabaseReference
    private var USERID = ""
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        auths = Firebase.auth
        databaseU = FirebaseDatabase.getInstance().getReference("users")
        navController= Navigation.findNavController(this,R.id.nav_host_fragment_container)
        setupWithNavController(binding.bottomNavigationView,navController)
        USERID = auths.currentUser?.uid.toString()
        CheckDevice()
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        window.decorView.setOnApplyWindowInsetsListener { view, windowInsets ->
            windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
            view.onApplyWindowInsets(windowInsets)
        }
    }
    fun CheckDevice(){
        val sharedPreferences = this.getSharedPreferences("MY_PREFERENCES", Context.MODE_PRIVATE)
        databaseU.child(USERID).child("DeviceIDs").get()
            .addOnSuccessListener {
                if(it.value.toString() == ""){
                    val editor = sharedPreferences?.edit()
                    editor?.putString(DEVICEID, "NO ID")
                    editor?.apply()
                } else {
                    val editor = sharedPreferences?.edit()
                    editor?.putString(DEVICEID, it.value.toString())
                    editor?.apply()
                }
            }
    }

}
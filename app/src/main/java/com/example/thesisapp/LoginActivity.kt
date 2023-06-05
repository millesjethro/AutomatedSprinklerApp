package com.example.thesisapp

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.thesisapp.Constant.CURRENT_EMAIL
import com.example.thesisapp.Constant.CURRENT_PASSWORD
import com.example.thesisapp.Constant.DEVICEID
import com.example.thesisapp.databinding.ActivityLoginBinding
import com.example.thesisapp.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auths: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var databaseU: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        database = FirebaseDatabase.getInstance().getReference("users")
        auths = Firebase.auth
        databaseU = FirebaseDatabase.getInstance().getReference("users")
        val currentUser = auths.currentUser
        if (currentUser != null) {
            database.child(auths.currentUser?.uid.toString()).child("Name").get().addOnSuccessListener {
                if(it.value.toString() == ""){
                    val intent = Intent(this, UserInfo::class.java)
                    startActivity(intent)
                }
                else{
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
            }
        }
        binding.LoginBtn.setOnClickListener(this)
        binding.RegisBtn.setOnClickListener(this)
        binding.fpass.setOnClickListener(this)
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        window.decorView.setOnApplyWindowInsetsListener { view, windowInsets ->
            windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
            view.onApplyWindowInsets(windowInsets)
        }
    }

    override fun onClick(p0: View?) {
        val sharedPreferences = this.getSharedPreferences("MY_PREFERENCES", Context.MODE_PRIVATE)
        val email = binding.EmailLogTxt.text.toString()
        val password = binding.passLogTxt.text.toString()

        when(p0!!.id){
            (R.id.LoginBtn)->{
                if(email.isNullOrEmpty() || password.isNullOrEmpty()){
                    Toast.makeText(applicationContext, "Either Password or Email is Empty", Toast.LENGTH_LONG)
                }
                else {
                    auths.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success")
                                val user = auths.currentUser
                                database.child(user?.uid.toString()).child("Name").get().addOnSuccessListener {
                                    if(it.value.toString() == ""){
                                        val editor = sharedPreferences?.edit()
                                        editor?.putString(CURRENT_PASSWORD, binding.passLogTxt.text.toString())
                                        editor?.apply()

                                        val intent = Intent(this, UserInfo::class.java)
                                        startActivity(intent)
                                    }
                                    else{
                                        val editor = sharedPreferences?.edit()
                                        editor?.putString(CURRENT_EMAIL, binding.EmailLogTxt.text.toString())
                                        editor?.putString(CURRENT_PASSWORD, binding.passLogTxt.text.toString())
                                        editor?.apply()
                                        database.child(user?.uid.toString()).child("DeviceIDs").get().addOnSuccessListener {
                                            editor?.putString(DEVICEID, it.value.toString())
                                            editor?.apply()
                                        }
                                        val intent = Intent(this, MainActivity::class.java)
                                        startActivity(intent)
                                    }
                                }
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.exception)
                                val a = task.exception.toString().split(":")
                                val toast =
                                    Toast.makeText(applicationContext, a[1], Toast.LENGTH_SHORT)
                                toast.show()
                            }
                        }
                }
            }
            (R.id.RegisBtn)->{
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            }
            (R.id.fpass)->{
                if(binding.EmailLogTxt.text.toString().isNullOrEmpty()){
                    binding.EmailLogTxt.error = "Please put an Email"
                }
                else {
                    Firebase.auth.sendPasswordResetEmail(binding.EmailLogTxt.text.toString())
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d(TAG, "Email sent.")
                            }
                        }
                }
            }
        }
    }
}
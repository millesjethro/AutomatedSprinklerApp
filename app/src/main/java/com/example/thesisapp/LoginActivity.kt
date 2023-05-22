package com.example.thesisapp

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.thesisapp.databinding.ActivityLoginBinding
import com.example.thesisapp.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auths: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        auths = Firebase.auth
        val currentUser = auths.currentUser
        if (currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.LoginBtn.setOnClickListener(this)
        binding.RegisBtn.setOnClickListener(this)

        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        window.decorView.setOnApplyWindowInsetsListener { view, windowInsets ->
            windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
            view.onApplyWindowInsets(windowInsets)
        }
    }

    override fun onClick(p0: View?) {
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
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)

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
        }
    }
}
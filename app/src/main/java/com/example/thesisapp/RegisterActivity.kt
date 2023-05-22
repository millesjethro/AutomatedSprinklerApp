package com.example.thesisapp

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.thesisapp.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auths: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        auths = Firebase.auth

        val currentUser = auths.currentUser
        if (currentUser != null) {
            //val intent = Intent(this, MainActivity::class.java)
            //startActivity(intent)
        }

        binding.RegisterBtn.setOnClickListener(this)
        binding.BackBtn.setOnClickListener(this)

        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        window.decorView.setOnApplyWindowInsetsListener { view, windowInsets ->
            windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
            view.onApplyWindowInsets(windowInsets)
        }

        ConfirmPassword()
        binding.RegisterBtn.isEnabled = false
        CheckPass()
        isItEmpty()
    }

    override fun onClick(p0: View?) {
        val email = binding.emailRegTxt.text.toString()
        val password = binding.passRegTxt.text.toString()
        when(p0!!.id){
            (R.id.Register_btn)->{
                auths.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success
                            Log.d(TAG, "createUserWithEmail:success")
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                        } else {
                            // If sign in fails
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            val a = task.exception.toString().split(":")
                            val toast = Toast.makeText(applicationContext,a[1], Toast.LENGTH_SHORT)
                            toast.show()
                        }
                    }
            }
            (R.id.Back_btn)->{
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }

    fun ConfirmPassword(){
        binding.confPassRegTxt.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val passwordText = binding.passRegTxt.text.toString()
                val passwordConfText = binding.confPassRegTxt.text.toString()

                if(passwordText != passwordConfText){
                    binding.confPassRegTxt.error = "Password do not match!"
                    binding.RegisterBtn.isEnabled = false
                }
                else{
                    binding.RegisterBtn.isEnabled = true
                }
            }
            override fun afterTextChanged(p0: Editable?) {

            }

        })
    }
    fun CheckPass(){

        binding.passRegTxt.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val password = binding.passRegTxt.text.toString()
                val LowerCase: Pattern = Pattern.compile("[a-z]")
                val UpperCase: Pattern = Pattern.compile("[A-Z]")
                val Digit: Pattern = Pattern.compile("\\d")
                if(LowerCase.matcher(password).find() && UpperCase.matcher(password).find() && Digit.matcher(password).find() && !(password.contains(" ")) && password.length >= 8) {
                    binding.confPassRegTxt.isEnabled = true
                }
                else{
                    binding.confPassRegTxt.isEnabled = false
                    binding.passRegTxt.error = "- No Spaces\n- More Than 8 Characters\n- Must have Uppercase\n- It is Alphanumeric"
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
    }

    fun isItEmpty(){
        binding.RegisterBtn.isEnabled =
            !(binding.passRegTxt.text?.isEmpty()!! && binding.emailRegTxt.text?.isEmpty()!!)
    }

}
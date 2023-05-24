package com.example.thesisapp.Fragments_MainMenu

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.thesisapp.Constant.CURRENT_EMAIL
import com.example.thesisapp.Constant.CURRENT_PASSWORD
import com.example.thesisapp.Constant.DEVICEID
import com.example.thesisapp.LoginActivity
import com.example.thesisapp.MainActivity
import com.example.thesisapp.R
import com.example.thesisapp.databinding.FragmentFaccountBinding
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text
import java.net.PasswordAuthentication
import java.util.*
import java.util.regex.Pattern

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
        binding.passwordResetBtn.isEnabled = false
        binding.newPassword.isEnabled = false

        val sharedPreferences = this.activity?.getSharedPreferences("MY_PREFERENCES", Context.MODE_PRIVATE)
        val currPass = sharedPreferences?.getString(CURRENT_PASSWORD,"PASSWORD")

        binding.logoutBtn.setOnClickListener(this)
        binding.passwordResetBtn.setOnClickListener(this)

        binding.oldPassword.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(currPass == binding.oldPassword.text.toString()){
                    binding.newPassword.isEnabled = true
                }
                else{
                    binding.oldPassword.error = "Wrong old password"
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        binding.newPassword.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val password = binding.newPassword.text.toString()
                val LowerCase: Pattern = Pattern.compile("[a-z]")
                val UpperCase: Pattern = Pattern.compile("[A-Z]")
                val Digit: Pattern = Pattern.compile("\\d")
                if(LowerCase.matcher(password).find() && UpperCase.matcher(password).find() && Digit.matcher(password).find() && !(password.contains(" ")) && password.length >= 8) {
                    binding.passwordResetBtn.isEnabled = true
                }
                else{
                    binding.passwordResetBtn.isEnabled = false
                    binding.newPassword.error = "- No Spaces\n- More Than 8 Characters\n- Must have Uppercase\n- It is Alphanumeric"
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })


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
        val txtNewPass = binding.newPassword.text
        val sharedPreferences = this.activity?.getSharedPreferences("MY_PREFERENCES", Context.MODE_PRIVATE)
        val currPass = sharedPreferences?.getString(CURRENT_PASSWORD,"PASSWORD")
        val currEmail = sharedPreferences?.getString(CURRENT_EMAIL,"PASSWORD")


        when(p0!!.id){

            (R.id.logoutBtn)->{
                val editor = sharedPreferences?.edit()
                editor?.putString(CURRENT_PASSWORD, "NO PASSWORD")
                editor?.putString(CURRENT_EMAIL, "NO EMAIL")
                editor?.apply()

                auths.signOut()

                val intent = Intent(context, LoginActivity::class.java)
                startActivity(intent)
            }
            (R.id.passwordResetBtn)->{
                Log.e("PASS", txtNewPass.toString())
                if(txtNewPass.toString() == ""){
                    binding.newPassword.error = "empty"
                }
                else{
                    val user = Firebase.auth.currentUser!!
                    val credential = EmailAuthProvider
                        .getCredential(currEmail.toString(), currPass.toString())
                    user.reauthenticate(credential)
                        .addOnCompleteListener {
                            auths.currentUser?.updatePassword(txtNewPass.toString())
                                ?.addOnSuccessListener {
                                    val toast =
                                        Toast.makeText(context, "Successfully Updated", Toast.LENGTH_SHORT)
                                    toast.show()

                                    val editor = sharedPreferences?.edit()
                                    editor?.putString(CURRENT_PASSWORD, binding.newPassword.text.toString())
                                    editor?.apply()
                                }
                        }


                }
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
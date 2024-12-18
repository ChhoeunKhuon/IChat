package com.num.ichat.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

import com.num.ichat.databinding.ActivityNumberBinding

class NumberActivity : AppCompatActivity() {

    private lateinit var binding : ActivityNumberBinding
    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNumberBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        auth = FirebaseAuth.getInstance()
//
//        if(auth.currentUser == null){
//            startActivity(Intent(this, NumberActivity::class.java))
//            finish()
//        }

        binding.button.setOnClickListener {
            if(binding.phoneNumber.text!!.isEmpty()){
                Toast.makeText(this, "Enter Phone Number", Toast.LENGTH_SHORT).show()
            }else{
                val intent = Intent(this, OTPActivity::class.java)
                intent.putExtra("number", binding.phoneNumber.text!!.toString())
                startActivity(intent)
            }

        }
    }
}
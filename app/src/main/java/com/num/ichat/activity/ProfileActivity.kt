package com.num.ichat.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.num.ichat.MainActivity
import com.num.ichat.R
import com.num.ichat.databinding.ActivityProfileBinding
import com.num.ichat.model.UserModel
import java.util.Date

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var storage: FirebaseStorage
    private var selectedImg: Uri? = null
    private lateinit var dialog: AlertDialog
    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase services
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()

        // Initialize dialog
        dialog = AlertDialog.Builder(this)
            .setMessage("Updating Profile...")
            .setCancelable(false)
            .create()

        // Register the image picker launcher
        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                selectedImg = result.data?.data
                binding.userImage.setImageURI(selectedImg)
            }
        }

        // Image click listener
        binding.userImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
            }
            imagePickerLauncher.launch(intent)
        }

        // Button click listener
        binding.continueBtn.setOnClickListener {
            if (binding.userName.text.isNullOrEmpty()) {
                Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show()
            } else if (selectedImg == null) {
                Toast.makeText(this, "Please select your image", Toast.LENGTH_SHORT).show()
            } else {
                upLoadData()
            }
        }
    }

    private fun upLoadData() {
        dialog.show()
        val reference = storage.reference.child("profile").child(Date().time.toString())

        selectedImg?.let { uri ->
            reference.putFile(uri).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    reference.downloadUrl.addOnSuccessListener { url ->
                        uploadInfo(url.toString())
                    }
                } else {
                    dialog.dismiss()
                    Toast.makeText(this, "Upload failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun uploadInfo(imgUrl: String) {
        val user = UserModel(
            auth.uid!!,
            binding.userName.text.toString(),
            auth.currentUser?.phoneNumber ?: "",
            imgUrl
        )

        database.reference.child("users")
            .child(auth.uid!!)
            .setValue(user)
            .addOnSuccessListener {
                dialog.dismiss()
                Toast.makeText(this, "Data inserted", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            .addOnFailureListener {
                dialog.dismiss()
                Toast.makeText(this, "Failed to insert data", Toast.LENGTH_SHORT).show()
            }
    }
}

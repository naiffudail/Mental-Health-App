package com.example.healthapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.healthapp.databinding.ActivityResetPasswordRequestBinding
import com.google.firebase.auth.FirebaseAuth

class ResetPasswordRequestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResetPasswordRequestBinding
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordRequestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }

        // Change button text as requested
        binding.changePasswordButton.text = "CHANGE PASSWORD"

        binding.changePasswordButton.setOnClickListener {
            val email = binding.emailResetEditText.text.toString().trim()
            val newPass = binding.newPasswordEditText.text.toString().trim()
            val confirmPass = binding.confirmPasswordEditText.text.toString().trim()

            if (email.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (newPass != confirmPass) {
                Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // IMPORTANT LOGIC NOTE:
            // Firebase security rules do not allow changing a password without the 
            // current password OR a reset link if you are logged out.
            
            // To make this work for a user who forgot their password:
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this,
                            "Success! A secure link has been sent to $email. Please click it to confirm your new password.",
                            Toast.LENGTH_LONG
                        ).show()
                        
                        // Redirect to login after requesting change
                        startActivity(Intent(this, signin::class.java))
                        finish()
                    } else {
                        Toast.makeText(
                            this,
                            "Error: ${task.exception?.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }
    }
}

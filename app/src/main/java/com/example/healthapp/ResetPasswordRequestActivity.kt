package com.example.healthapp

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

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.sendResetLinkButton.setOnClickListener {
            val email = binding.emailResetEditText.text.toString().trim()

            if (email.isEmpty()) {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Firebase logic to send reset email
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this,
                            "Instructions sent to $email",
                            Toast.LENGTH_LONG
                        ).show()
                        finish() // Return to sign in screen
                    } else {
                        Toast.makeText(
                            this,
                            "Error: ${task.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }
}
package com.example.healthapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.healthapp.databinding.ActivityResetPasswordRequestBinding
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ResetPasswordRequestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResetPasswordRequestBinding
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordRequestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }

        binding.changePasswordButton.setOnClickListener {
            val email = binding.emailResetEditText.text.toString().trim()
            val currentPassword = binding.currentPasswordEditText.text.toString().trim()
            val newPassword = binding.newPasswordEditText.text.toString().trim()

            if (email.isEmpty() || currentPassword.isEmpty() || newPassword.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Official Firebase Re-authentication logic
            val credential = EmailAuthProvider.getCredential(email, currentPassword)

            // We try to sign in first to ensure we have a 'currentUser' to work with
            auth.signInWithEmailAndPassword(email, currentPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        user?.reauthenticate(credential)?.addOnCompleteListener { reauthTask ->
                            if (reauthTask.isSuccessful) {
                                // Now we can safely update the password
                                user.updatePassword(newPassword).addOnCompleteListener { updateTask ->
                                    if (updateTask.isSuccessful) {
                                        Toast.makeText(this, "Password updated!", Toast.LENGTH_SHORT).show()
                                        checkRoleAndLogin()
                                    } else {
                                        Toast.makeText(this, "Update failed: ${updateTask.exception?.message}", Toast.LENGTH_LONG).show()
                                    }
                                }
                            } else {
                                Toast.makeText(this, "Re-authentication failed", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(this, "Incorrect email or current password", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun checkRoleAndLogin() {
        val userId = auth.currentUser?.uid ?: return
        database.child("Users").child(userId).get().addOnSuccessListener { snapshot ->
            val role = snapshot.child("role").value.toString()
            if (role == "Admin") {
                startActivity(Intent(this, Home2Activity::class.java))
            } else {
                startActivity(Intent(this, HomeActivity::class.java))
            }
            finish()
        }.addOnFailureListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }
}

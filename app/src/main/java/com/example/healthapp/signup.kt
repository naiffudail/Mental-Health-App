package com.example.healthapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class signup : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        // Get the role passed from LoginActivity
        val role = intent.getStringExtra("ROLE") ?: "User"

        val fullNameEditText = findViewById<EditText>(R.id.editTextText1)
        val phoneEditText = findViewById<EditText>(R.id.editTextPhone1)
        val emailEditText = findViewById<EditText>(R.id.editTextTextEmailAddress)
        val passwordEditText = findViewById<EditText>(R.id.editTextTextPassword2)
        val signupButton = findViewById<Button>(R.id.button4)
        val loginLink = findViewById<TextView>(R.id.textViewLoginLink)

        signupButton.setOnClickListener {
            val fullName = fullNameEditText.text.toString()
            val phone = phoneEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (fullName.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val userId = auth.currentUser?.uid
                        val user = User(fullName, phone, email, role)

                        if (userId != null) {
                            database.reference.child("Users").child(userId).setValue(user)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Registration as $role successful", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(this, signin::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this, "Failed to save user data", Toast.LENGTH_SHORT).show()
                                }
                        }
                    } else {
                        Toast.makeText(this, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        loginLink.setOnClickListener {
            val intent = Intent(this, signin::class.java)
            startActivity(intent)
            finish()
        }
    }
}

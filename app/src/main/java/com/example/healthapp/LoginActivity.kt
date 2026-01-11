package com.example.healthapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val adminButton = findViewById<Button>(R.id.admin_button)
        adminButton.setOnClickListener {
            val intent = Intent(this, signup::class.java)
            intent.putExtra("ROLE", "Admin")
            startActivity(intent)
        }

        val userButton = findViewById<Button>(R.id.user_button)
        userButton.setOnClickListener {
            val intent = Intent(this, signup::class.java)
            intent.putExtra("ROLE", "User")
            startActivity(intent)
        }
    }
}
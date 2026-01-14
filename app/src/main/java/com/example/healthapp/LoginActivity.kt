package com.example.healthapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val adminButton = findViewById<Button>(R.id.admin_button)
        adminButton.setOnClickListener {
            showConfirmationDialog("Admin")
        }

        val userButton = findViewById<Button>(R.id.user_button)
        userButton.setOnClickListener {
            showConfirmationDialog("User")
        }
    }

    private fun showConfirmationDialog(role: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirm Choice")
        builder.setMessage("Are you sure you want to proceed as $role?")
        builder.setPositiveButton("Yes") { _, _ ->
            val intent = Intent(this, signup::class.java)
            intent.putExtra("ROLE", role)
            startActivity(intent)
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        val alert = builder.create()
        alert.show()
    }
}
package com.example.healthapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class signup : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        var button4 = findViewById<Button>(R.id.button4)
        button4.setOnClickListener{
            val intent2 = Intent(this,signin::class.java)
            startActivity(intent2)
        }
    }
}
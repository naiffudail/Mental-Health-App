package com.example.healthapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class signin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        var button5 = findViewById<Button>(R.id.button5)
        button5.setOnClickListener{
            val intent3 = Intent(this,HomeActivity::class.java)
            startActivity(intent3)
        }
    }
}
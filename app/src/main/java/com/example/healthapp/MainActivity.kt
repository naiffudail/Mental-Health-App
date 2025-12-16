package com.example.healthapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var button3 = findViewById<Button>(R.id.button3)
        button3.setOnClickListener{
            val intent1 = Intent(this,signup::class.java)
            startActivity(intent1)
        }


    }
}
package com.example.healthapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class CommunityActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community)

        val whatsappButton: Button = findViewById(R.id.whatsappButton)
        val telegramButton: Button = findViewById(R.id.telegramButton)

        whatsappButton.setOnClickListener {
            // Replace with your WhatsApp group link
            val whatsappLink = "https://chat.whatsapp.com/YOUR_GROUP_ID"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(whatsappLink))
            startActivity(intent)
        }

        telegramButton.setOnClickListener {
            // Replace with your Telegram group link
            val telegramLink = "https://t.me/YOUR_GROUP_NAME"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(telegramLink))
            startActivity(intent)
        }
    }
}

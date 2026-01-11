package com.example.healthapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.healthapp.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Back button functionality
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnSendChat.setOnClickListener {
            val message = binding.messageEditText.text.toString()
            if (message.isNotEmpty()) {
                // Here you would normally send the message to a backend (like Firebase)
                Toast.makeText(this, "Message sent: $message", Toast.LENGTH_SHORT).show()
                binding.messageEditText.text.clear()
            }
        }
    }
}
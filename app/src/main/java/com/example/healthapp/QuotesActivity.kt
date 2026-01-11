package com.example.healthapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.healthapp.databinding.ActivityQuotesBinding

class QuotesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuotesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Back button functionality
        binding.backButton.setOnClickListener {
            onBackPressed()
        }
    }
}

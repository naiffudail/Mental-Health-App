package com.example.healthapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.healthapp.databinding.ActivityHome2Binding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHome2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHome2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rescheduleButton1.setOnClickListener {
            showToast("Reschedule button 1 clicked")
        }

        binding.rescheduleButton2.setOnClickListener {
            showToast("Reschedule button 2 clicked")
        }

        binding.happyImageView.setOnClickListener {
            showToast("You're feeling happy!")
        }

        binding.calmImageView.setOnClickListener {
            showToast("You're feeling calm!")
        }

        binding.angryImageView.setOnClickListener {
            showToast("You're feeling angry!")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
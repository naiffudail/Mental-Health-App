package com.example.healthapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthapp.databinding.ActivityAppointmentBinding

class AppointmentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAppointmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppointmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        
        binding.backButton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupRecyclerView() {
        // You would typically define a Doctor model class and an adapter
        // For now, this is a placeholder for the setup
        binding.doctorsRecyclerView.layoutManager = LinearLayoutManager(this)
        // binding.doctorsRecyclerView.adapter = DoctorAdapter(doctorList)
    }
}
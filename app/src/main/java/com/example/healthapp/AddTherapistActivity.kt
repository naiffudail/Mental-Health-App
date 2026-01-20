package com.example.healthapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.healthapp.databinding.ActivityAddTherapistBinding
import com.google.firebase.database.FirebaseDatabase

class AddTherapistActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTherapistBinding
    private val database = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTherapistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnSaveTherapist.setOnClickListener {
            saveTherapist()
        }

        binding.btnViewAllTherapists.setOnClickListener {
            val intent = Intent(this, AppointmentActivity::class.java)
            startActivity(intent)
        }
    }

    private fun saveTherapist() {
        val fullName = binding.etTherapistFullName.text.toString().trim()
        val certificateId = binding.etCertificateId.text.toString().trim()

        if (fullName.isEmpty()) {
            binding.etTherapistFullName.error = "Name required"
            return
        }
        if (certificateId.isEmpty()) {
            binding.etCertificateId.error = "Certificate ID required"
            return
        }

        val therapistId = database.child("Therapists").push().key ?: return
        val therapistData = hashMapOf(
            "therapistId" to therapistId,
            "fullName" to fullName,
            "certificateId" to certificateId
        )

        database.child("Therapists").child(therapistId).setValue(therapistData)
            .addOnSuccessListener {
                Toast.makeText(this, "Therapist saved successfully!", Toast.LENGTH_SHORT).show()
                // Optional: Clear fields after saving
                binding.etTherapistFullName.text?.clear()
                binding.etCertificateId.text?.clear()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to save: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}

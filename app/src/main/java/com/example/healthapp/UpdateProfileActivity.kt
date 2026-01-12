package com.example.healthapp

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.healthapp.databinding.ActivityUpdateProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class UpdateProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateProfileBinding
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Back button
        binding.btnBack.setOnClickListener { finish() }

        // Load existing data
        loadUserData()

        // Date of Birth Picker
        binding.etDOB.setOnClickListener {
            showDatePicker()
        }

        // Save button
        binding.btnSave.setOnClickListener {
            saveProfileChanges()
        }
    }

    private fun loadUserData() {
        val userId = auth.currentUser?.uid ?: return
        database.child("Users").child(userId).get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val fullName = snapshot.child("fullName").value?.toString() ?: ""
                val email = snapshot.child("email").value?.toString() ?: ""
                val phone = snapshot.child("phoneNumber").value?.toString() ?: ""
                val dob = snapshot.child("dob").value?.toString() ?: ""

                binding.etFullName.setText(fullName)
                binding.etEmail.setText(email)
                binding.etPhone.setText(phone)
                binding.etDOB.setText(dob)
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to load profile", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            binding.etDOB.setText(formattedDate)
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun saveProfileChanges() {
        val userId = auth.currentUser?.uid ?: return
        val fullName = binding.etFullName.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()
        val dob = binding.etDOB.text.toString().trim()

        if (fullName.isEmpty()) {
            Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        val updates = hashMapOf<String, Any>(
            "fullName" to fullName,
            "phoneNumber" to phone,
            "dob" to dob
        )

        database.child("Users").child(userId).updateChildren(updates)
            .addOnSuccessListener {
                Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Update failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}

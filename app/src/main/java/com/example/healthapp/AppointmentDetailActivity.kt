package com.example.healthapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.healthapp.databinding.ActivityAppointmentDetailBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*

class AppointmentDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAppointmentDetailBinding
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference
    
    private val userList = mutableListOf<User>()
    private val userNames = mutableListOf<String>()
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppointmentDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSpinner()
        loadUsers()

        binding.menuButton.setOnClickListener {
            onBackPressed()
        }

        binding.etSelectedDate.setOnClickListener {
            showDatePicker()
        }

        binding.etSelectedTime.setOnClickListener {
            showTimePicker()
        }

        binding.bookAppointmentButton.setOnClickListener {
            bookAppointment()
        }
    }

    private fun setupSpinner() {
        adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, userNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.patientSpinner.adapter = adapter
    }

    private fun loadUsers() {
        database.child("Users").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                userNames.clear()
                for (userSnapshot in snapshot.children) {
                    val user = userSnapshot.getValue(User::class.java)
                    if (user != null) {
                        // Ensure we use the database key as the UID if it's missing in the object
                        val finalUser = user.copy(uid = userSnapshot.key)
                        userList.add(finalUser)
                        userNames.add(user.fullName ?: "Unknown")
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AppointmentDetailActivity, "Failed to load users", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val selectedDate = "$dayOfMonth/${month + 1}/$year"
                binding.etSelectedDate.setText(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val timePickerDialog = TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                val amPm = if (hourOfDay < 12) "AM" else "PM"
                val hour = if (hourOfDay % 12 == 0) 12 else hourOfDay % 12
                val selectedTime = String.format("%02d:%02d %s", hour, minute, amPm)
                binding.etSelectedTime.setText(selectedTime)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            false
        )
        timePickerDialog.show()
    }

    private fun bookAppointment() {
        val selectedUserIndex = binding.patientSpinner.selectedItemPosition
        if (selectedUserIndex == -1 || userList.isEmpty()) {
            Toast.makeText(this, "Please select a patient", Toast.LENGTH_SHORT).show()
            return
        }

        val selectedUser = userList[selectedUserIndex]
        val date = binding.etSelectedDate.text.toString()
        val time = binding.etSelectedTime.text.toString()

        if (date.isEmpty() || time.isEmpty()) {
            Toast.makeText(this, "Please select date and time", Toast.LENGTH_SHORT).show()
            return
        }

        val appointmentId = database.child("Appointments").push().key ?: return
        val appointmentData = hashMapOf(
            "appointmentId" to appointmentId,
            "patientId" to selectedUser.uid,
            "patientName" to selectedUser.fullName,
            "date" to date,
            "time" to time,
            "status" to "Booked",
            "bookedBy" to auth.currentUser?.uid
        )

        database.child("Appointments").child(appointmentId).setValue(appointmentData)
            .addOnSuccessListener {
                Toast.makeText(this, "Appointment booked successfully!", Toast.LENGTH_LONG).show()
                finish()
            }
            .addOnFailureListener {
                // If it fails here, it is definitely a permission issue in Firebase Rules
                Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_LONG).show()
            }
    }
}

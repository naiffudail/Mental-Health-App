package com.example.healthapp

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthapp.databinding.UserAppointmentlistBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class UserAppointmentListActivity : AppCompatActivity() {

    private lateinit var binding: UserAppointmentlistBinding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var appointmentList: ArrayList<Appointment>
    private lateinit var adapter: UserAppointmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = UserAppointmentlistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        appointmentList = ArrayList()
        adapter = UserAppointmentAdapter(appointmentList)

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        binding.rvUserAppointments.layoutManager = LinearLayoutManager(this)
        binding.rvUserAppointments.adapter = adapter

        loadUserInfo()
        loadUserAppointments()
    }

    private fun loadUserInfo() {
        val userId = auth.currentUser?.uid ?: return
        database.child("Users").child(userId).get().addOnSuccessListener { snapshot ->
            val user = snapshot.getValue(User::class.java)
            if (user != null) {
                binding.tvUserFullName.text = user.fullName
            }
        }.addOnFailureListener {
            binding.tvUserFullName.text = "User"
        }
    }

    private fun loadUserAppointments() {
        val currentUserId = auth.currentUser?.uid ?: return
        
        database.child("Appointments").orderByChild("patientId").equalTo(currentUserId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    appointmentList.clear()
                    for (postSnapshot in snapshot.children) {
                        val appointment = postSnapshot.getValue(Appointment::class.java)
                        if (appointment != null) {
                            appointmentList.add(appointment)
                        }
                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@UserAppointmentListActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}

package com.example.healthapp

import android.app.AlertDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthapp.databinding.ActivityAppointmentBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.*

class AppointmentActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityAppointmentBinding
    private lateinit var database: DatabaseReference
    private lateinit var appointmentList: ArrayList<Appointment>
    private lateinit var filteredList: ArrayList<Appointment>
    private lateinit var adapter: AppointmentAdapter
    private var currentTabIsActive = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppointmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().reference
        appointmentList = ArrayList()
        filteredList = ArrayList()
        
        // Initialize adapter with long-click listener
        adapter = AppointmentAdapter(filteredList) { appointment ->
            if (currentTabIsActive) {
                showCancelDialog(appointment)
            }
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Appointments"

        setupRecyclerView()
        setupTabs()
        loadAppointments()

        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navView.setNavigationItemSelectedListener(this)
    }

    private fun setupRecyclerView() {
        binding.doctorsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.doctorsRecyclerView.adapter = adapter
    }

    private fun loadAppointments() {
        database.child("Appointments").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                appointmentList.clear()
                for (postSnapshot in snapshot.children) {
                    val appointment = postSnapshot.getValue(Appointment::class.java)
                    if (appointment != null) {
                        appointmentList.add(appointment)
                    }
                }
                filterAppointments()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AppointmentActivity, "Database Error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun filterAppointments() {
        filteredList.clear()
        for (item in appointmentList) {
            if (currentTabIsActive) {
                // Show Booked or Active in the first tab
                if (item.status == "Active" || item.status == "Booked") {
                    filteredList.add(item)
                }
            } else {
                // Show Inactive in the second tab
                if (item.status == "Inactive") {
                    filteredList.add(item)
                }
            }
        }
        adapter.notifyDataSetChanged()
    }

    private fun showCancelDialog(appointment: Appointment) {
        AlertDialog.Builder(this)
            .setTitle("Cancel Appointment")
            .setMessage("Are you sure you want to move this appointment to inactive?")
            .setPositiveButton("Yes") { _, _ ->
                moveAppointmentToInactive(appointment)
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun moveAppointmentToInactive(appointment: Appointment) {
        val id = appointment.appointmentId ?: return
        database.child("Appointments").child(id).child("status").setValue("Inactive")
            .addOnSuccessListener {
                Toast.makeText(this, "Appointment moved to Inactive", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to update status", Toast.LENGTH_SHORT).show()
            }
    }

    private fun setupTabs() {
        binding.activeTab.setOnClickListener {
            if (!currentTabIsActive) {
                currentTabIsActive = true
                updateTabsUI()
                filterAppointments()
            }
        }
        binding.inactiveTab.setOnClickListener {
            if (currentTabIsActive) {
                currentTabIsActive = false
                updateTabsUI()
                filterAppointments()
            }
        }
    }

    private fun updateTabsUI() {
        val selectedColor = Color.parseColor("#E97068")
        val unselectedTextColor = Color.parseColor("#BDBDBD")
        val white = Color.WHITE

        if (currentTabIsActive) {
            binding.activeTab.setBackgroundResource(R.drawable.active_tab_background)
            binding.activeTab.backgroundTintList = ColorStateList.valueOf(selectedColor)
            binding.activeTab.setTextColor(white)
            
            binding.inactiveTab.background = null
            binding.inactiveTab.setTextColor(unselectedTextColor)
        } else {
            binding.inactiveTab.setBackgroundResource(R.drawable.active_tab_background)
            binding.inactiveTab.backgroundTintList = ColorStateList.valueOf(selectedColor)
            binding.inactiveTab.setTextColor(white)
            
            binding.activeTab.background = null
            binding.activeTab.setTextColor(unselectedTextColor)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> startActivity(Intent(this, Home2Activity::class.java))
            R.id.nav_profile -> startActivity(Intent(this, UpdateProfileActivity::class.java))
            R.id.nav_community -> startActivity(Intent(this, CommunityActivity::class.java))
            R.id.nav_chat -> startActivity(Intent(this, ChatActivity::class.java))
            R.id.nav_quotes -> startActivity(Intent(this, QuotesActivity::class.java))
            R.id.nav_music -> startActivity(Intent(this, MusicActivity::class.java))
            R.id.nav_appointment -> { /* Already here */ }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}

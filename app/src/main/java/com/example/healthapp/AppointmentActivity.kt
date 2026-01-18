package com.example.healthapp

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthapp.databinding.ActivityAppointmentBinding
import com.google.android.material.navigation.NavigationView

class AppointmentActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityAppointmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppointmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Appointment"

        setupRecyclerView()
        setupTabs()

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
    }

    private fun setupTabs() {
        binding.activeTab.setOnClickListener {
            updateTabs(true)
        }
        binding.inactiveTab.setOnClickListener {
            updateTabs(false)
        }
    }

    private fun updateTabs(isActiveSelected: Boolean) {
        val selectedColor = Color.parseColor("#E97068")
        val unselectedTextColor = Color.parseColor("#BDBDBD")
        val white = Color.WHITE

        if (isActiveSelected) {
            // Active Selected
            binding.activeTab.setBackgroundResource(R.drawable.active_tab_background)
            binding.activeTab.backgroundTintList = ColorStateList.valueOf(selectedColor)
            binding.activeTab.setTextColor(white)

            // Inactive Unselected
            binding.inactiveTab.background = null
            binding.inactiveTab.setTextColor(unselectedTextColor)
        } else {
            // Inactive Selected
            binding.inactiveTab.setBackgroundResource(R.drawable.active_tab_background)
            binding.inactiveTab.backgroundTintList = ColorStateList.valueOf(selectedColor)
            binding.inactiveTab.setTextColor(white)

            // Active Unselected
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
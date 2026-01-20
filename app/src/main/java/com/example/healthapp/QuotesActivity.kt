package com.example.healthapp

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.example.healthapp.databinding.ActivityQuotesBinding
import com.google.android.material.navigation.NavigationView

class QuotesActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityQuotesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup Toolbar and Drawer
        setSupportActionBar(binding.toolbar)
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

        // Share buttons setup
        setupShareButtons()
    }

    private fun setupShareButtons() {
        binding.btnShare1.setOnClickListener {
            shareQuote("Check out this inspiring quote from MindCare!")
        }

        binding.btnShare2.setOnClickListener {
            shareQuote("Check out this inspiring quote from MindCare!")
        }
    }

    private fun shareQuote(message: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, message)
        startActivity(Intent.createChooser(intent, "Share via"))
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> startActivity(Intent(this, HomeActivity::class.java))
            R.id.nav_profile -> startActivity(Intent(this, UpdateProfileActivity::class.java))
            R.id.nav_appointment -> startActivity(Intent(this, AppointmentActivity::class.java))
            R.id.nav_chat -> startActivity(Intent(this, ChatActivity::class.java))
            R.id.nav_quotes -> { /* Already here */ }
            R.id.nav_music -> startActivity(Intent(this, MusicActivity::class.java))
            R.id.nav_draw -> startActivity(Intent(this, DrawingActivity::class.java))
            R.id.nav_game -> startActivity(Intent(this, TicTacToeActivity::class.java))
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

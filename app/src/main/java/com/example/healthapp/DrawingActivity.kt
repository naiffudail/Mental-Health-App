package com.example.healthapp

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.example.healthapp.databinding.ActivityDrawingBinding
import com.google.android.material.navigation.NavigationView

class DrawingActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityDrawingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDrawingBinding.inflate(layoutInflater)
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

        // Color selection
        binding.colorWhite.setOnClickListener { binding.drawingView.setColor("#FFFFFF") }
        binding.colorRed.setOnClickListener { binding.drawingView.setColor("#FF0000") }
        binding.colorGreen.setOnClickListener { binding.drawingView.setColor("#00FF00") }
        binding.colorBlue.setOnClickListener { binding.drawingView.setColor("#0000FF") }
        binding.colorBlack.setOnClickListener { binding.drawingView.setColor("#000000") }
        binding.colorYellow.setOnClickListener { binding.drawingView.setColor("#FFFF00") }
        binding.colorCyan.setOnClickListener { binding.drawingView.setColor("#00FFFF") }
        binding.colorMagenta.setOnClickListener { binding.drawingView.setColor("#FF00FF") }

        // Tools
        binding.btnBrush.setOnClickListener {
            binding.drawingView.setEraser(false)
            binding.drawingView.setStyle("Normal")
            binding.drawingView.setBrushSize(20f)
        }

        binding.btnEraser.setOnClickListener {
            binding.drawingView.setEraser(true)
            binding.drawingView.setBrushSize(50f)
        }

        binding.btnNeon.setOnClickListener {
            binding.drawingView.setEraser(false)
            binding.drawingView.setStyle("Neon")
            binding.drawingView.setBrushSize(30f)
        }

        binding.btnMarker.setOnClickListener {
            binding.drawingView.setEraser(false)
            binding.drawingView.setStyle("Marker")
            binding.drawingView.setBrushSize(40f)
        }

        binding.btnDash.setOnClickListener {
            binding.drawingView.setEraser(false)
            binding.drawingView.setStyle("Dash")
            binding.drawingView.setBrushSize(10f)
        }

        binding.btnClear.setOnClickListener {
            binding.drawingView.clearCanvas()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> startActivity(Intent(this, HomeActivity::class.java))
            R.id.nav_profile -> startActivity(Intent(this, UpdateProfileActivity::class.java))
            R.id.nav_community -> startActivity(Intent(this, CommunityActivity::class.java))
            R.id.nav_chat -> startActivity(Intent(this, ChatActivity::class.java))
            R.id.nav_quotes -> startActivity(Intent(this, QuotesActivity::class.java))
            R.id.nav_music -> startActivity(Intent(this, MusicActivity::class.java))
            R.id.nav_draw -> { /* Already here */ }
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

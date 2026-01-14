package com.example.healthapp

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import com.example.healthapp.databinding.ActivityHomeBinding
import com.google.android.material.navigation.NavigationView

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityHomeBinding
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 1. Enable Edge-to-Edge
        enableEdgeToEdge()
        
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 2. Hide System Bars (Full Screen Immersive Mode)
        hideSystemBars()

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

        setupClickListeners()
    }

    private fun hideSystemBars() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
            val controller = window.insetsController
            if (controller != null) {
                controller.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                controller.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
        }
    }

    private fun setupClickListeners() {
        binding.rescheduleButton1.setOnClickListener {
            showToast("Reschedule button 1 clicked")
        }
        binding.rescheduleButton2.setOnClickListener {
            showToast("Reschedule button 2 clicked")
        }

        binding.happyImageView.setOnClickListener { 
            playSound(R.raw.pop1)
            showFeelingDialog(R.drawable.happy, "Happy", "#FFEB3B") 
        }
        binding.calmImageView.setOnClickListener { 
            playSound(R.raw.pop2)
            showFeelingDialog(R.drawable.calm, "Calm", "#81D4FA") 
        }
        binding.angryImageView.setOnClickListener { 
            playSound(R.raw.pop3)
            showFeelingDialog(R.drawable.angry, "Angry", "#EF5350") 
        }
        binding.sadImageView.setOnClickListener { 
            playSound(R.raw.pop2)
            showFeelingDialog(R.drawable.sad, "Sad", "#90CAF9") 
        }
        binding.anxiousImageView.setOnClickListener {
            playSound(R.raw.pop3)
            showFeelingDialog(R.drawable.anxious, "Anxious", "#CE93D8") 
        }
        binding.stressedImageView.setOnClickListener { 
            playSound(R.raw.pop3)
            showFeelingDialog(R.drawable.stressed, "Stressed", "#FFAB91") 
        }
        binding.tiredImageView.setOnClickListener {
            playSound(R.raw.pop3)
            showFeelingDialog(R.drawable.tired, "Tired", "#B0BEC5") 
        }
        binding.neutralImageView.setOnClickListener {
            playSound(R.raw.pop1)
            showFeelingDialog(R.drawable.neutral, "Neutral", "#E0E0E0") 
        }
    }

    private fun playSound(soundResId: Int) {
        // Release previous player if any
        mediaPlayer?.release()
        // Create and play specific sound
        mediaPlayer = MediaPlayer.create(this, soundResId)
        mediaPlayer?.start()
    }

    private fun showFeelingDialog(imageResId: Int, feeling: String, colorHex: String) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_feeling, null)
        val container = dialogView.findViewById<LinearLayout>(R.id.dialogContainer)
        val imageView = dialogView.findViewById<ImageView>(R.id.dialogImageView)
        val textView = dialogView.findViewById<TextView>(R.id.dialogTextView)

        val shape = GradientDrawable()
        shape.cornerRadius = 50f
        shape.setColor(Color.parseColor(colorHex))
        shape.setStroke(4, Color.WHITE)
        container.background = shape

        imageView.setImageResource(imageResId)
        textView.text = "You're feeling $feeling!"

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        
        val animation = AnimationUtils.loadAnimation(this, R.anim.pop_in)
        dialogView.startAnimation(animation)

        dialog.show()

        dialogView.postDelayed({
            if (dialog.isShowing) {
                dialogView.animate().alpha(0f).setDuration(300).withEndAction {
                    dialog.dismiss()
                }.start()
            }
        }, 1200)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> { /* Already here */ }
            R.id.nav_profile -> startActivity(Intent(this, UpdateProfileActivity::class.java))
            R.id.nav_community -> startActivity(Intent(this, CommunityActivity::class.java))
            R.id.nav_chat -> startActivity(Intent(this, ChatActivity::class.java))
            R.id.nav_music -> startActivity(Intent(this, MusicActivity::class.java))
            R.id.nav_quotes -> startActivity(Intent(this, QuotesActivity::class.java))
            R.id.nav_draw -> startActivity(Intent(this, DrawingActivity::class.java))
            R.id.nav_game -> startActivity(Intent(this, TicTacToeActivity::class.java))
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

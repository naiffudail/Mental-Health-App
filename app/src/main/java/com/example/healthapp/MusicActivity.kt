package com.example.healthapp

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.example.healthapp.databinding.ActivityMusicBinding
import com.google.android.material.navigation.NavigationView

class MusicActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMusicBinding
    private var mediaPlayer: MediaPlayer? = null
    private val handler = Handler(Looper.getMainLooper())
    
    private val songs = listOf(R.raw.music_file, R.raw.pop1, R.raw.pop2, R.raw.pop3)
    private val songTitles = listOf("My Little Edinburgh", "Pop Song 1", "Pop Song 2", "Pop Song 3")
    private var currentSongIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMusicBinding.inflate(layoutInflater)
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

        setupMusicPlayer()

    }

    private fun setupMusicPlayer() {
        // Initialize the first song but don't play it yet
        prepareSong()

        binding.buttonPlay.setOnClickListener {
            if (mediaPlayer == null) {
                prepareSong()
            }
            
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
                binding.buttonPlay.setImageResource(android.R.drawable.ic_media_play)
            } else {
                mediaPlayer?.start()
                binding.buttonPlay.setImageResource(android.R.drawable.ic_media_pause)
                updateSeekBar()
            }
        }

        binding.buttonNext.setOnClickListener {
            currentSongIndex = (currentSongIndex + 1) % songs.size
            playSong()
        }

        binding.buttonPrevious.setOnClickListener {
            currentSongIndex = if (currentSongIndex > 0) currentSongIndex - 1 else songs.size - 1
            playSong()
        }

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) mediaPlayer?.seekTo(progress)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun prepareSong() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        
        mediaPlayer = MediaPlayer.create(this, songs[currentSongIndex])
        binding.textViewSongTitle.text = songTitles[currentSongIndex]
        binding.seekBar.max = mediaPlayer?.duration ?: 0
        binding.seekBar.progress = 0
        binding.buttonPlay.setImageResource(android.R.drawable.ic_media_play)
    }

    private fun playSong() {
        prepareSong()
        mediaPlayer?.start()
        binding.buttonPlay.setImageResource(android.R.drawable.ic_media_pause)
        updateSeekBar()
    }

    private fun updateSeekBar() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                binding.seekBar.progress = it.currentPosition
                handler.postDelayed({ updateSeekBar() }, 1000)
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> startActivity(Intent(this, HomeActivity::class.java))
            R.id.nav_diary -> Toast.makeText(this, "Diary clicked", Toast.LENGTH_SHORT).show()
            R.id.nav_profile -> startActivity(Intent(this, UpdateProfileActivity::class.java))
            R.id.nav_community -> startActivity(Intent(this, CommunityActivity::class.java))
            R.id.nav_chat -> startActivity(Intent(this, ChatActivity::class.java))
            R.id.nav_quotes -> startActivity(Intent(this, QuotesActivity::class.java))
            R.id.nav_music -> { /* Already here */ }
            R.id.nav_draw -> startActivity(Intent(this, DrawingActivity::class.java))
            R.id.nav_game -> startActivity(Intent(this, TicTacToeActivity::class.java))
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        handler.removeCallbacksAndMessages(null)
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}

package com.example.healthapp

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.Toast

class MusicActivity : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null
    private lateinit var seekBar: SeekBar
    private lateinit var playButton: ImageButton
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music)

        playButton = findViewById(R.id.buttonPlay)
        seekBar = findViewById(R.id.seekBar)
        val homeButton = findViewById<Button>(R.id.buttonHomepage)

        // Initialize MediaPlayer
        try {
            mediaPlayer = MediaPlayer.create(this, R.raw.music_file)
            if (mediaPlayer == null) {
                Toast.makeText(this, "Music file not found in res/raw", Toast.LENGTH_LONG).show()
            } else {
                setupSeekBar()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        playButton.setOnClickListener {
            if (mediaPlayer?.isPlaying == true) {
                pauseMusic()
            } else {
                playMusic()
            }
        }

        findViewById<ImageButton>(R.id.buttonNext).setOnClickListener {
            restartMusic()
        }

        findViewById<ImageButton>(R.id.buttonPrevious).setOnClickListener {
            restartMusic()
        }

        homeButton.setOnClickListener {
            finish()
        }
    }

    private fun playMusic() {
        mediaPlayer?.start()
        playButton.setImageResource(android.R.drawable.ic_media_pause)
        updateSeekBar()
    }

    private fun pauseMusic() {
        mediaPlayer?.pause()
        playButton.setImageResource(android.R.drawable.ic_media_play)
    }

    private fun restartMusic() {
        mediaPlayer?.seekTo(0)
        playMusic()
    }

    private fun setupSeekBar() {
        mediaPlayer?.let {
            seekBar.max = it.duration
            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    if (fromUser) {
                        it.seekTo(progress)
                    }
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }
    }

    private fun updateSeekBar() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                seekBar.progress = it.currentPosition
                handler.postDelayed({ updateSeekBar() }, 1000)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
        handler.removeCallbacksAndMessages(null)
    }
}

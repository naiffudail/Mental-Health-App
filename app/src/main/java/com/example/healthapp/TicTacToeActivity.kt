package com.example.healthapp

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.example.healthapp.databinding.ActivityTicTacToeBinding
import com.google.android.material.navigation.NavigationView
import kotlin.random.Random

class TicTacToeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityTicTacToeBinding
    private var board = arrayOfNulls<String>(9)
    private var isPlayerTurn = true
    private var gameActive = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTicTacToeBinding.inflate(layoutInflater)
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

        setupGame()

        binding.resetButton.setOnClickListener {
            resetGame()
        }
        
        // Initialize buttons as transparent
        resetGame()
    }

    private fun setupGame() {
        val buttons = arrayOf(
            binding.button0, binding.button1, binding.button2,
            binding.button3, binding.button4, binding.button5,
            binding.button6, binding.button7, binding.button8
        )

        for (i in 0..8) {
            buttons[i].setOnClickListener {
                if (gameActive && isPlayerTurn && board[i] == null) {
                    makeMove(i, "X")
                    if (gameActive) {
                        isPlayerTurn = false
                        binding.statusTextView.text = "Bot is thinking..."
                        binding.root.postDelayed({ botMove() }, 600)
                    }
                }
            }
        }
    }

    private fun makeMove(position: Int, player: String) {
        board[position] = player
        val buttons = arrayOf(
            binding.button0, binding.button1, binding.button2,
            binding.button3, binding.button4, binding.button5,
            binding.button6, binding.button7, binding.button8
        )
        
        val button = buttons[position]
        button.text = player
        button.isEnabled = false

        // Change from transparent to a solid color when clicked
        if (player == "X") {
            button.setBackgroundColor(Color.parseColor("#A0E3E2")) // Player Color
            button.setTextColor(Color.WHITE)
        } else {
            button.setBackgroundColor(Color.parseColor("#F16C68")) // Bot Color
            button.setTextColor(Color.WHITE)
        }

        if (checkWin(player, board)) {
            binding.statusTextView.text = if (player == "X") "You Win!" else "You Lose!"
            gameActive = false
        } else if (board.all { it != null }) {
            binding.statusTextView.text = "It's a Draw!"
            gameActive = false
        }
    }

    private fun botMove() {
        if (!gameActive) return

        val bestMove = getBestMove()
        makeMove(bestMove, "O")
        
        if (gameActive) {
            isPlayerTurn = true
            binding.statusTextView.text = "Your Turn (X)"
        }
    }

    private fun getBestMove(): Int {
        for (i in 0..8) {
            if (board[i] == null) {
                board[i] = "O"
                if (checkWin("O", board)) {
                    board[i] = null
                    return i
                }
                board[i] = null
            }
        }
        for (i in 0..8) {
            if (board[i] == null) {
                board[i] = "X"
                if (checkWin("X", board)) {
                    board[i] = null
                    return i
                }
                board[i] = null
            }
        }
        if (board[4] == null) return 4
        val corners = intArrayOf(0, 2, 6, 8)
        val availableCorners = corners.filter { board[it] == null }
        if (availableCorners.isNotEmpty()) return availableCorners.random()
        val available = mutableListOf<Int>()
        for (i in 0..8) {
            if (board[i] == null) available.add(i)
        }
        return available.random()
    }

    private fun checkWin(player: String, currentBoard: Array<String?>): Boolean {
        val winPatterns = arrayOf(
            intArrayOf(0, 1, 2), intArrayOf(3, 4, 5), intArrayOf(6, 7, 8),
            intArrayOf(0, 3, 6), intArrayOf(1, 4, 7), intArrayOf(2, 5, 8),
            intArrayOf(0, 4, 8), intArrayOf(2, 4, 6)
        )
        for (pattern in winPatterns) {
            if (currentBoard[pattern[0]] == player && 
                currentBoard[pattern[1]] == player && 
                currentBoard[pattern[2]] == player) {
                return true
            }
        }
        return false
    }

    private fun resetGame() {
        board = arrayOfNulls<String>(9)
        isPlayerTurn = true
        gameActive = true
        binding.statusTextView.text = "Your Turn (X)"
        
        val buttons = arrayOf(
            binding.button0, binding.button1, binding.button2,
            binding.button3, binding.button4, binding.button5,
            binding.button6, binding.button7, binding.button8
        )
        for (button in buttons) {
            button.text = ""
            button.isEnabled = true
            // Reset to transparent/default state
            button.setBackgroundColor(Color.TRANSPARENT)
            // Optional: Add a subtle border if you want them visible while empty
            button.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0) 
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
            R.id.nav_draw -> startActivity(Intent(this, DrawingActivity::class.java))
            R.id.nav_game -> { /* Already here */ }
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

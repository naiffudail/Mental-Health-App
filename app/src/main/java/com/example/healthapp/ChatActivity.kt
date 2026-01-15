package com.example.healthapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthapp.databinding.ActivityChatBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityChatBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var messageList: ArrayList<Message>
    private lateinit var chatAdapter: ChatAdapter
    private var currentUserName: String = "Anonymous"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
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

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        
        messageList = ArrayList()
        chatAdapter = ChatAdapter(messageList)
        
        binding.chatRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.chatRecyclerView.adapter = chatAdapter

        // Get Friend Name from Intent if coming from Contacts
        val friendName = intent.getStringExtra("FRIEND_NAME")
        if (friendName != null) {
            binding.toolbar.findViewById<android.widget.TextView>(R.id.chatUserName).text = friendName
        } else {
            // Otherwise show current user name
            val userId = auth.currentUser?.uid
            if (userId != null) {
                database.child("Users").child(userId).child("fullName").get().addOnSuccessListener {
                    currentUserName = it.value.toString()
                    binding.toolbar.findViewById<android.widget.TextView>(R.id.chatUserName).text = currentUserName
                }
            }
        }

        // Load messages in real-time
        database.child("Messages").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                messageList.clear()
                for (postSnapshot in snapshot.children) {
                    val message = postSnapshot.getValue(Message::class.java)
                    if (message != null) {
                        message.messageId = postSnapshot.key
                        messageList.add(message)
                    }
                }
                chatAdapter.notifyDataSetChanged()
                if (messageList.isNotEmpty()) {
                    binding.chatRecyclerView.scrollToPosition(messageList.size - 1)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ChatActivity, "Failed to load chat", Toast.LENGTH_SHORT).show()
            }
        })

        // Go to Contacts List
        binding.toolbar.findViewById<View>(R.id.btnGoToContacts).setOnClickListener {
            startActivity(Intent(this, ContactsActivity::class.java))
        }

        binding.btnSendChat.setOnClickListener {
            val messageText = binding.messageEditText.text.toString()
            if (messageText.isNotEmpty()) {
                val messageObject = Message(
                    senderId = auth.currentUser?.uid,
                    senderName = currentUserName,
                    messageText = messageText,
                    timestamp = System.currentTimeMillis()
                )

                database.child("Messages").push().setValue(messageObject)
                    .addOnSuccessListener {
                        binding.messageEditText.text.clear()
                    }
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> startActivity(Intent(this, HomeActivity::class.java))
            R.id.nav_profile -> startActivity(Intent(this, UpdateProfileActivity::class.java))
            R.id.nav_community -> startActivity(Intent(this, CommunityActivity::class.java))
            R.id.nav_chat -> { /* Already here */ }
            R.id.nav_music -> startActivity(Intent(this, MusicActivity::class.java))
            R.id.nav_quotes -> startActivity(Intent(this, QuotesActivity::class.java))
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

package com.example.healthapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthapp.databinding.ActivityChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity() {

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

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        
        messageList = ArrayList()
        chatAdapter = ChatAdapter(messageList)
        
        binding.chatRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.chatRecyclerView.adapter = chatAdapter

        // Get current user's name and update Header
        val userId = auth.currentUser?.uid
        if (userId != null) {
            database.child("Users").child(userId).child("fullName").get().addOnSuccessListener {
                currentUserName = it.value.toString()
                // Update the Header UI with real name
                binding.chatUserName.text = currentUserName
            }
        }

        // Load messages in real-time
        database.child("Messages").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                messageList.clear()
                for (postSnapshot in snapshot.children) {
                    val message = postSnapshot.getValue(Message::class.java)
                    if (message != null) {
                        message.messageId = postSnapshot.key // Assign key for deletion
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

        // Back button functionality
        binding.btnBack.setOnClickListener {
            onBackPressed()
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
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to send message", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}

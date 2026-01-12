package com.example.healthapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthapp.databinding.ActivityContactsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ContactsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContactsBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var contactList: ArrayList<User>
    private lateinit var contactAdapter: ContactAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        contactList = ArrayList()
        contactAdapter = ContactAdapter(contactList) { selectedUser ->
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra("FRIEND_NAME", selectedUser.fullName)
            startActivity(intent)
        }

        binding.rvContacts.layoutManager = LinearLayoutManager(this)
        binding.rvContacts.adapter = contactAdapter

        val currentUserId = auth.currentUser?.uid
        if (currentUserId != null) {
            database.child("Users").child(currentUserId).child("fullName").get().addOnSuccessListener {
                binding.tvCurrentUserName.text = it.value?.toString() ?: "No Name"
            }
        }

        binding.btnAddFriend.setOnClickListener {
            startActivity(Intent(this, SearchFriendActivity::class.java))
        }

        binding.btnSearchFriend.setOnClickListener {
            startActivity(Intent(this, SearchFriendActivity::class.java))
        }

        binding.btnMenu.setOnClickListener { finish() }

        loadFriends()
    }

    private fun loadFriends() {
        val currentUserId = auth.currentUser?.uid ?: return
        
        // Listen to your Friends list
        database.child("Friends").child(currentUserId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                contactList.clear()
                val friendIds = snapshot.children.mapNotNull { it.key }
                
                if (friendIds.isEmpty()) {
                    contactAdapter.notifyDataSetChanged()
                    return
                }

                // For each friend ID, get their user details
                for (id in friendIds) {
                    database.child("Users").child(id).get().addOnSuccessListener { userSnapshot ->
                        val user = userSnapshot.getValue(User::class.java)
                        if (user != null) {
                            contactList.add(user)
                            contactList.sortBy { it.fullName?.lowercase() }
                            contactAdapter.notifyDataSetChanged()
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ContactsActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

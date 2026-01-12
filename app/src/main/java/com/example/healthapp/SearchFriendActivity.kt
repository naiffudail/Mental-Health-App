package com.example.healthapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.healthapp.databinding.ActivitySearchFriendBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class SearchFriendActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchFriendBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private var foundUserId: String? = null
    private var foundUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchFriendBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        binding.btnBackSearch.setOnClickListener { finish() }

        binding.btnSearchAction.setOnClickListener {
            val username = binding.etSearchUsername.text.toString().trim()
            if (username.isNotEmpty()) {
                searchUser(username)
            } else {
                Toast.makeText(this, "Enter a name to search", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnAddAction.setOnClickListener {
            addFriend()
        }
    }

    private fun searchUser(username: String) {
        binding.cardResult.visibility = View.GONE
        binding.tvNoResult.visibility = View.GONE

        // Searching by fullName in database
        // Note: This is case-sensitive in Firebase (e.g., 'khairul' is different from 'Khairul')
        database.child("Users").orderByChild("fullName").equalTo(username)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (userSnapshot in snapshot.children) {
                            foundUserId = userSnapshot.key
                            foundUser = userSnapshot.getValue(User::class.java)
                            
                            if (foundUserId == auth.currentUser?.uid) {
                                Toast.makeText(this@SearchFriendActivity, "You cannot add yourself", Toast.LENGTH_SHORT).show()
                                return
                            }

                            // Display the real data found in database
                            binding.tvResultName.text = foundUser?.fullName
                            binding.tvResultEmail.text = foundUser?.email
                            binding.cardResult.visibility = View.VISIBLE
                            return // Stop after finding the first match
                        }
                    } else {
                        binding.tvNoResult.visibility = View.VISIBLE
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // This will tell you if the index is missing!
                    Toast.makeText(this@SearchFriendActivity, "Error: ${error.message}", Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun addFriend() {
        val currentUserId = auth.currentUser?.uid ?: return
        val friendId = foundUserId ?: return

        // Save to your private Friends list
        database.child("Friends").child(currentUserId).child(friendId).setValue(true)
            .addOnSuccessListener {
                Toast.makeText(this, "${foundUser?.fullName} added to your contacts!", Toast.LENGTH_SHORT).show()
                finish() // Go back to the contacts list
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to add friend", Toast.LENGTH_SHORT).show()
            }
    }
}

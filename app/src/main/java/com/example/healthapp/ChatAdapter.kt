package com.example.healthapp

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ChatAdapter(private val messageList: ArrayList<Message>) :
    RecyclerView.Adapter<ChatAdapter.MessageViewHolder>() {

    private val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val currentMessage = messageList[position]
        holder.senderName.text = currentMessage.senderName
        holder.messageText.text = currentMessage.messageText

        // Handle long click to delete chat
        holder.itemView.setOnLongClickListener {
            if (currentMessage.senderId == currentUserId) {
                showDeleteDialog(holder.itemView, currentMessage.messageId)
            }
            true
        }
    }

    private fun showDeleteDialog(view: View, messageId: String?) {
        if (messageId == null) return

        AlertDialog.Builder(view.context)
            .setTitle("Delete Message")
            .setMessage("Are you sure you want to delete this message?")
            .setPositiveButton("Delete") { _, _ ->
                FirebaseDatabase.getInstance().reference.child("Messages").child(messageId).removeValue()
                    .addOnSuccessListener {
                        Toast.makeText(view.context, "Message deleted", Toast.LENGTH_SHORT).show()
                    }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val senderName: TextView = itemView.findViewById(R.id.senderName)
        val messageText: TextView = itemView.findViewById(R.id.messageText)
    }
}

package com.example.healthapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserAppointmentAdapter(private val appointmentList: ArrayList<Appointment>) :
    RecyclerView.Adapter<UserAppointmentAdapter.UserAppointmentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAppointmentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user_appointment, parent, false)
        return UserAppointmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserAppointmentViewHolder, position: Int) {
        val appointment = appointmentList[position]
        
        // Mapping as requested:
        // $21,000 -> Date
        holder.tvDate.text = appointment.date
        
        // Frederic Bosch -> Time
        holder.tvTime.text = appointment.time
    }

    override fun getItemCount(): Int = appointmentList.size

    class UserAppointmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDate: TextView = itemView.findViewById(R.id.tvAppointmentDate)
        val tvTime: TextView = itemView.findViewById(R.id.tvAppointmentTime)
    }
}

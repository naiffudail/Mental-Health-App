package com.example.healthapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AppointmentAdapter(
    private val appointmentList: ArrayList<Appointment>,
    private val onItemLongClick: (Appointment) -> Unit
) : RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_doctor, parent, false)
        return AppointmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        val currentItem = appointmentList[position]
        
        holder.patientName.text = currentItem.patientName
        holder.appointmentDate.text = currentItem.date
        holder.appointmentTime.text = currentItem.time

        // Long click to trigger "Delete" (move to inactive)
        holder.itemView.setOnLongClickListener {
            onItemLongClick(currentItem)
            true
        }
    }

    override fun getItemCount(): Int = appointmentList.size

    class AppointmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val patientName: TextView = itemView.findViewById(R.id.doctorName)
        val appointmentDate: TextView = itemView.findViewById(R.id.doctorSpecialty)
        val appointmentTime: TextView = itemView.findViewById(R.id.doctorDescription)
    }
}

package com.example.healthapp

data class Appointment(
    val appointmentId: String? = null,
    val patientId: String? = null,
    val patientName: String? = null,
    val date: String? = null,
    val time: String? = null,
    val status: String? = null,
    val bookedBy: String? = null
)

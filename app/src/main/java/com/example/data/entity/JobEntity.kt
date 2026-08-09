package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "jobs")
data class JobEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String,
    val imageResName: String = "img_hero_banner",
    val city: String,
    val area: String,
    val locationDetails: String,
    val payAmount: Double,
    val payType: String, // "PER_HOUR", "PER_DAY", "PER_JOB"
    val date: String,
    val startTime: String,
    val endTime: String,
    val workersNeeded: Int,
    val minAge: Int,
    val requirements: String,
    val instructions: String,
    val equipment: String = "None required",
    val dressRequirements: String = "Neat casual wear",
    val transportInfo: String = "Self transportation",
    val foodInfo: String = "Snacks/Refreshments provided",
    val applicationInstructions: String = "Submit application with your availability",
    val status: String = "AVAILABLE", // "AVAILABLE", "ALMOST_FULL", "FILLED", "COMPLETED", "HIDDEN"
    val jobType: String = "Other", // "Catering", "Event helper", "Shop assistance", "Organizing", "Computer/online tasks", "Other"
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

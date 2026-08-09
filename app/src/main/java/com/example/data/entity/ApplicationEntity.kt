package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "applications")
data class ApplicationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val jobId: Long,
    val userId: String,
    val applicantName: String,
    val applicantAge: Int,
    val generalArea: String,
    val availability: String,
    val message: String,
    val appliedDate: String,
    val appliedTimestamp: Long = System.currentTimeMillis(),
    val status: String = "PENDING" // "PENDING", "REVIEWED", "ACCEPTED", "REJECTED"
)

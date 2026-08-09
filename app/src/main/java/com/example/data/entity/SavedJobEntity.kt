package com.example.data.entity

import androidx.room.Entity

@Entity(tableName = "saved_jobs", primaryKeys = ["userId", "jobId"])
data class SavedJobEntity(
    val userId: String,
    val jobId: Long,
    val savedAt: Long = System.currentTimeMillis()
)

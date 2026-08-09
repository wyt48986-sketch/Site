package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String, // UUID or username
    val name: String,
    val age: Int,
    val email: String,
    val generalArea: String,
    val passwordHash: String,
    val isAdmin: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

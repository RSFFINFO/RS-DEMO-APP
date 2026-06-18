package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val username: String,
    val passwordHash: String,
    val fullName: String,
    val createdAt: Long = System.currentTimeMillis()
)

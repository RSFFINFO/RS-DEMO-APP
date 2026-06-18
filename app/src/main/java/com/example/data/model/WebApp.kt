package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "web_apps")
data class WebApp(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val url: String,
    val iconName: String,
    val iconColor: String,
    val packageName: String,
    val creatorUsername: String,
    val createdAt: Long = System.currentTimeMillis()
)

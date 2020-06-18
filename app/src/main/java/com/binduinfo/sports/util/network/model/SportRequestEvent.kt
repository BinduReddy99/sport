package com.binduinfo.sports.util.network.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SportRequestEvent(
    @PrimaryKey(autoGenerate = false)
    val _id: String,
    val imagePath: String,
    val isSeleted: Boolean,
    val name: String,
    val sportType: String
)
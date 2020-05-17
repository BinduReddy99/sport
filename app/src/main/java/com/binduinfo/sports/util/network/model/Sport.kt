package com.binduinfo.sports.util.network.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Sport(
    @PrimaryKey(autoGenerate = false)
    val _id: String,
    val imagePath: String,
    val name: String,
    val sportType: String,
    var isSeleted: Boolean = false
)
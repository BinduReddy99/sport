package com.binduinfo.sports.data.db.entity

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.binduinfo.sports.util.network.model.Sport

@Dao
interface SportsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveAllSports(sports: List<Sport>)

    @Query("SELECT * FROM Sport")
    fun getSports(): LiveData<List<Sport>>

    @Query("SELECT * FROM Sport WHERE sportType IN(:sportType)")
    fun getSports(sportType: String): LiveData<List<Sport>>

    @Query("UPDATE Sport SET isSelected=:isSelected WHERE _id = :id ")
    fun updateItem(id: String, isSelected: Boolean)

    @Query("SELECT _id FROM Sport WHERE isSelected = :isSelected ")
    fun selectSelectedSports(isSelected: Boolean = true): List<String>

}
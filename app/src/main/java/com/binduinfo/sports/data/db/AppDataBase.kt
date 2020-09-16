package com.binduinfo.sports.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.binduinfo.sports.data.db.entity.SportsDao
import com.binduinfo.sports.util.network.model.Sport

private const val DATA_BASE = "Sports.db"

@Database(entities = [Sport::class], version = 1)
abstract class AppDataBase : RoomDatabase() {
    abstract fun getUserSport(): SportsDao


    companion object {
        @Volatile
        private var instance: AppDataBase? = null
        private val LOCK = Any()
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDataBase::class.java,
                DATA_BASE
            ).build()
    }

}
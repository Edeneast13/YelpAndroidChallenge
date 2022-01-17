package com.ngmatt.weedmapsandroidcodechallenge.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(BusinessEntity::class), version = 4)
abstract class AppDatabase : RoomDatabase() {
    abstract fun businessDao(): BusinessDao

    companion object {
        @Volatile
        private var APP_DATABASE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return APP_DATABASE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).fallbackToDestructiveMigration().build()
                APP_DATABASE = instance
                instance
            }
        }
    }
}
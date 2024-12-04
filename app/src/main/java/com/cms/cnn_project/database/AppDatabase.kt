package com.cms.cnn_project.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.cms.cnn_project.dao.FoodDao
import com.cms.cnn_project.entity.Food

@Database(entities = [Food::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun foodDao(): FoodDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "food_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
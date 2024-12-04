package com.cms.cnn_project.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.cms.cnn_project.entity.Food

@Dao
interface FoodDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFood(food: Food)

    @Update
    suspend fun updateFood(food: Food)

    @Delete
    suspend fun deleteFood(food: Food)

    @Query("SELECT * FROM food_table ORDER BY id ASC")
    fun getAllImages(): LiveData<List<Food>>

    @Query("SELECT label FROM food_table")
    fun getAllLabels(): LiveData<List<String>>

}
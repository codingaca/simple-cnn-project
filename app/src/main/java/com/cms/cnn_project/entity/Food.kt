package com.cms.cnn_project.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food_table")
data class Food(
    var imageUrl: String,
    var name: String,
    var label: String
){
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}

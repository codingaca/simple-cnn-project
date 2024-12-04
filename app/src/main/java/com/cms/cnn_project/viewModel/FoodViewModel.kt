package com.cms.cnn_project.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import com.cms.cnn_project.database.AppDatabase
import com.cms.cnn_project.entity.Food
import kotlinx.coroutines.launch

class FoodViewModel(application: Application): AndroidViewModel(application) {
    private val dao = AppDatabase.getInstance(application).foodDao()
    val allImages: LiveData<List<Food>> = dao.getAllImages()
    val allLabels: LiveData<List<String>> = dao.getAllLabels()

    fun insertImage(food: Food) = viewModelScope.launch {
        dao.insertFood(food)
    }

    fun updateFood(food: Food) = viewModelScope.launch {
        dao.updateFood(food)
    }

    fun deleteImage(food: Food) = viewModelScope.launch {
        dao.deleteFood(food)
    }
}
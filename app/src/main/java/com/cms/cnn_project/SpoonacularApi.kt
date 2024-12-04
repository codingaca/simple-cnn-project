package com.cms.cnn_project

import com.cms.cnn_project.entity.Recipe
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SpoonacularApi {
    @GET("recipes/findByIngredients")
    fun getRecipes(
        @Query("ingredients") ingredients: String,
        @Query("number") number: Int = 10,
        @Query("apiKey") apiKey: String
    ): Call<List<Recipe>>
}
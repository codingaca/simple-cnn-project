package com.cms.cnn_project

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cms.cnn_project.adapter.RecipeAdapter
import com.cms.cnn_project.databinding.FragmentRecipeBinding
import com.cms.cnn_project.entity.Recipe
import com.cms.cnn_project.viewModel.FoodViewModel
import retrofit2.Call

class RecipeFragment : Fragment() {

    private lateinit var binding: FragmentRecipeBinding
    private lateinit var foodViewModel: FoodViewModel
    private var recipeAdapter: RecipeAdapter = RecipeAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecipeBinding.inflate(inflater, container, false)

        foodViewModel = ViewModelProvider(this)[FoodViewModel::class.java]
        binding.recipeRv.adapter = recipeAdapter
        binding.recipeRv.layoutManager = LinearLayoutManager(requireContext())

        binding.recipeReloadFab.setOnClickListener {
            foodViewModel.allLabels.observe(viewLifecycleOwner) { labels ->
                val ingredients = labels.joinToString(",")
                fetchRecipesFromApi(ingredients)
            }
        }

        return binding.root
    }

    private fun fetchRecipesFromApi(ingredients: String) {
        val apiKey = "MY_API_KEY"

        RetrofitClient.instance.getRecipes(ingredients, apiKey = apiKey).enqueue(object : retrofit2.Callback<List<Recipe>> {
            override fun onResponse(call: Call<List<Recipe>>, response: retrofit2.Response<List<Recipe>>) {
                if (response.isSuccessful) {
                    val recipes = response.body()
                    if (recipes != null) {
                        recipeAdapter.setRecipes(recipes) // RecyclerView 반영
                    }
                }
            }




            override fun onFailure(call: Call<List<Recipe>>, t: Throwable) {
                Log.e("API Error", t.message ?: "Unknown error")
            }
        })
    }

}
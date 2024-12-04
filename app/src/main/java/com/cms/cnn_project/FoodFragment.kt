package com.cms.cnn_project

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cms.cnn_project.adapter.FoodAdapter
import com.cms.cnn_project.databinding.FragmentFoodBinding
import com.cms.cnn_project.entity.Food
import com.cms.cnn_project.viewModel.FoodViewModel

class FoodFragment : Fragment() {

    private lateinit var binding: FragmentFoodBinding
    private lateinit var foodViewModel: FoodViewModel
    private lateinit var foodAdapter: FoodAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFoodBinding.inflate(inflater, container, false)
        foodViewModel = ViewModelProvider(this)[FoodViewModel::class.java]

        foodAdapter = FoodAdapter(
            onEditClick = {food -> showEditDialog(food)},
            onDeleteClick = {food -> foodViewModel.deleteImage(food)}
        )
        binding.foodRv.adapter = foodAdapter
        binding.foodRv.layoutManager = LinearLayoutManager(requireContext())

        foodViewModel.allImages.observe(viewLifecycleOwner) { foods ->
            foodAdapter.setFoods(foods)
        }

        return binding.root
    }

    private fun showEditDialog(food: Food) {
        val dialog = AlertDialog.Builder(context as MainActivity)
        dialog.setTitle("이름 수정")

        val input = EditText(context as MainActivity)
        input.setText(food.name)
        dialog.setView(input)

        dialog.setPositiveButton("저장") { _, _ ->
            val newName = input.text.toString()
            if (newName.isNotBlank()) {
                food.name = newName
                food.label = translateLabelToEnglish(newName)
                foodViewModel.updateFood(food)
            }
        }
        dialog.setNegativeButton("취소", null)
        dialog.show()
    }

}
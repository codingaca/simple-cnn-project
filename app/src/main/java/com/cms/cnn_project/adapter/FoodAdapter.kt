package com.cms.cnn_project.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cms.cnn_project.R
import com.cms.cnn_project.entity.Food

class FoodAdapter(
    private val onEditClick: (Food) -> Unit,
    private val onDeleteClick: (Food) -> Unit
) : RecyclerView.Adapter<FoodAdapter.ViewHolder>() {

    private var foods = emptyList<Food>()

    fun setFoods(newFoods: List<Food>) {
        foods = newFoods
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img = itemView.findViewById<ImageView>(R.id.item_image)
        val nameKo = itemView.findViewById<TextView>(R.id.item_name_ko)
        val nameEn = itemView.findViewById<TextView>(R.id.item_name_en)
        val editButton: ImageView = itemView.findViewById(R.id.item_edit_btn)
        val deleteButton: ImageView = itemView.findViewById(R.id.item_delete_btn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_food, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: FoodAdapter.ViewHolder, position: Int) {
        val food = foods[position]
        Glide.with(holder.itemView.context).load(food.imageUrl).into(holder.img)
        holder.nameKo.text = food.name
        holder.nameEn.text = "(${food.label})"
        holder.editButton.setOnClickListener { onEditClick(food) }
        holder.deleteButton.setOnClickListener { onDeleteClick(food) }
    }

    override fun getItemCount(): Int = foods.size
}
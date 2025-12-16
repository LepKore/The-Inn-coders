package com.example.laposada.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.laposada.R
import com.example.laposada.dataClass.FoodDataClass
import com.example.laposada.databinding.ItemFoodBinding

class FoodAdapter
    (private val onItemClick: (FoodDataClass) -> Unit)
    : RecyclerView.Adapter<FoodAdapter.FoodCardViewHolder>()  {
    private val dataCards = mutableListOf<FoodDataClass>()
    private val context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodAdapter.FoodCardViewHolder {
        return FoodCardViewHolder(
            ItemFoodBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FoodCardViewHolder, position: Int) {
        holder.binding(dataCards[position])
    }

    override fun getItemCount(): Int {
        return dataCards.size
    }


    // Donde hacer la logica

    inner class FoodCardViewHolder(private val binding: ItemFoodBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun binding(data: FoodDataClass) {
            binding.linearLayoutFoodImage.setBackgroundResource(R.drawable.papas_fritas)
            binding.textViewFoodName.text = data.nombre
            binding.root.setOnClickListener {
                onItemClick(data)
            }

        }
    }

    fun addDataCards(list: List<FoodDataClass>) {
        dataCards.clear()
        dataCards.addAll(list)
        notifyDataSetChanged()
    }
}
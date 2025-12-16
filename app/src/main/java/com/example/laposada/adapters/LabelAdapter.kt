package com.example.laposada.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.laposada.dataClass.FoodDataClass
import com.example.laposada.databinding.ItemLabelsBinding

class LabelAdapter: RecyclerView.Adapter<
        LabelAdapter.LabelCardViewHolder
        >() {
    private val dataCards = mutableListOf<String>()
    private val context: Context? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LabelAdapter.LabelCardViewHolder {
        return LabelCardViewHolder(
            ItemLabelsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: LabelAdapter.LabelCardViewHolder, position: Int) {
        holder.binding(dataCards[position])
    }

    override fun getItemCount(): Int {
        return dataCards.size
    }

    inner class LabelCardViewHolder(private val binding: ItemLabelsBinding):
        RecyclerView.ViewHolder(binding.root) {
            fun binding(data: String) {
                binding.textViewLabel.text = data
            }

        }
    fun addDataCards(list: List<String>) {
        dataCards.clear()
        dataCards.addAll(list)
    }

}
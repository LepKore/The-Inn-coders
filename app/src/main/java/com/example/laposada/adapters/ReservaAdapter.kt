package com.example.laposada.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.laposada.dataBase.DaoGame
import com.example.laposada.dataBase.GeneralDataBase
import com.example.laposada.dataClass.FoodDataClass
import com.example.laposada.dataClass.ReservaDataClass
import com.example.laposada.dataClass.ReservaUI
import com.example.laposada.databinding.ItemFoodBinding
import com.example.laposada.databinding.ReservaItemBinding
import com.example.laposada.pantallas.menuComida.FoodMenuActivity.Companion.DATABASE_NAME
import java.io.File

class ReservaAdapter
    (private val onItemClick: (ReservaUI) -> Unit)
    : RecyclerView.Adapter<ReservaAdapter.ReservaViewHolder>()  {
    private val dataCards = mutableListOf<  ReservaUI>()
    private val context: Context? = null

    lateinit var daoGame: DaoGame

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservaViewHolder {
        return ReservaViewHolder(
            ReservaItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ReservaViewHolder, position: Int) {
        holder.binding(dataCards[position])
    }

    override fun getItemCount(): Int {
        return dataCards.size
    }


    // Donde hacer la logica

    inner class ReservaViewHolder(
        private val binding: ReservaItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun binding(data: ReservaUI) {
            binding.mesa.text = "Mesa #${data.mesa}"
            binding.fecha.text = data.dia
            binding.hora.text = "De ${data.horaInicio} a ${data.horaFin}"
            binding.juego.text = data.nombreJuego
        }
    }


    fun addDataCards(list: List<ReservaUI>) {
        dataCards.clear()
        dataCards.addAll(list)
        notifyDataSetChanged()
    }
}
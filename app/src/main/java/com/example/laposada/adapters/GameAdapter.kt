package com.example.laposada.adapters

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.laposada.dataClass.GameDataClass
import com.example.laposada.databinding.ItemGameBinding
import java.io.File

class GameAdapter(private val onClick: (GameDataClass) -> Unit) : RecyclerView.Adapter<GameAdapter.GameViewHolder>() {

    private var games = listOf<GameDataClass>()

    fun addDataCards(newGames: List<GameDataClass>) {
        games = newGames
        notifyDataSetChanged()
    }

    inner class GameViewHolder(val binding: ItemGameBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(game: GameDataClass) {
            val imageFile = File(binding.root.context.filesDir, game.imagenCaja)
            binding.imageViewGame.background =
                Drawable.createFromPath(imageFile.path)
            binding.textViewGameName.text = game.nombre
            binding.root.setOnClickListener {
                onClick(game)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val binding = ItemGameBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GameViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        holder.bind(games[position])
    }

    override fun getItemCount() = games.size
}
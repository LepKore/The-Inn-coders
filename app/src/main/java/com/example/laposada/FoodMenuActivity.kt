package com.example.laposada

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.laposada.databinding.ActivityFoodMenuBinding
import com.example.laposada.databinding.ItemFoodBinding

class FoodMenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFoodMenuBinding
    private val foodList = listOf(
        FoodItem("Hamburguesa", R.drawable.hamburguesaa),
        FoodItem("Papas fritas", R.drawable.papas_fritas),
        FoodItem("Pizza", R.drawable.pizza),
        FoodItem("Coca-Cola", R.drawable.coca_cola),
        FoodItem("Sandwich", R.drawable.sandwich)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFoodMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupRecyclerView()
        setupListeners()
    }

    private fun setupRecyclerView() {
        val adapter = FoodAdapter(foodList)
        binding.recyclerViewFood.layoutManager = GridLayoutManager(this, 2)
        binding.recyclerViewFood.adapter = adapter
    }

    private fun setupListeners() {
        binding.buttonBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        
        binding.buttonEdit.setOnClickListener {
            val intent = Intent(this, EditMenuOptionsActivity::class.java)
            startActivity(intent)
        }
    }

    data class FoodItem(val name: String, val imageResId: Int)

    class FoodAdapter(private val items: List<FoodItem>) : RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {

        inner class FoodViewHolder(val binding: ItemFoodBinding) : RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
            val binding = ItemFoodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return FoodViewHolder(binding)
        }

        override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
            val item = items[position]
            holder.binding.textViewFoodName.text = item.name
            holder.binding.imageViewFood.setImageResource(item.imageResId)
        }

        override fun getItemCount() = items.size
    }
}

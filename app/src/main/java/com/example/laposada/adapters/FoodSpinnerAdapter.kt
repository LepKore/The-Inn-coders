package com.example.laposada.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.laposada.dataClass.FoodDataClass
import com.example.laposada.R

class FoodSpinnerAdapter(
    context: Context,
    private val foodList: List<FoodDataClass>
): ArrayAdapter<FoodDataClass>(
    context,
    android.R.layout.simple_spinner_item,
    foodList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.food_spinner_item, parent, false)
        val textView = view.findViewById<TextView>(R.id.textViewSpinner)
        textView.text = foodList[position].nombre
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.food_spinner_item, parent, false)
        val textView = view.findViewById<TextView>(R.id.textViewSpinner)
        textView.text = foodList[position].nombre

        return view
    }

}
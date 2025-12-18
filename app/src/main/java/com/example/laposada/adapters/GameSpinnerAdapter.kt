package com.example.laposada.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.laposada.R
import com.example.laposada.dataClass.GameDataClass

class GameSpinnerAdapter(
    context: Context,
    private val gameList: List<GameDataClass>
): ArrayAdapter<GameDataClass>(
    context,
    android.R.layout.simple_spinner_item,
    gameList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.spinner_item, parent, false)
        val textView = view.findViewById<TextView>(R.id.textViewSpinner)
        textView.text = gameList[position].nombre
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.spinner_item, parent, false)
        val textView = view.findViewById<TextView>(R.id.textViewSpinner)
        textView.text = gameList[position].nombre

        return view
    }
}
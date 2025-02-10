package com.example.color

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class ColorListAdapter(context: Context, colors: List<String>) :
    ArrayAdapter<String>(context, 0, colors) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_color, parent, false)

        val colorHex = getItem(position) ?: "#FFFFFF"
        val colorSquare: View = view.findViewById(R.id.colorSquare)
        val colorText: TextView = view.findViewById(R.id.colorText)

        colorText.text = colorHex

        try {
            colorSquare.setBackgroundColor(Color.parseColor(colorHex))
        } catch (e: IllegalArgumentException) {
            colorSquare.setBackgroundColor(Color.GRAY) // Pokud je špatný HEX, dáme šedou
        }

        return view
    }
}
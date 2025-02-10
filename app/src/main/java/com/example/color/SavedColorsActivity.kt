package com.example.color

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class SavedColorsActivity : AppCompatActivity() {
    private lateinit var savedColorsListView: ListView
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_saved_colors)

        savedColorsListView = findViewById(R.id.savedColorsListView)
        db = AppDatabase.getDatabase(this)

        loadCategories()
    }

    private fun loadCategories() {
        lifecycleScope.launch {
            val categories = db.colorDao().getAllCategories()
            val adapter = ArrayAdapter(this@SavedColorsActivity, android.R.layout.simple_list_item_1, categories)
            savedColorsListView.adapter = adapter

            savedColorsListView.setOnItemClickListener { _, _, position, _ ->
                val selectedCategory = categories[position]
                showColors(selectedCategory)
            }
        }
    }

    private fun showColors(category: String) {
        lifecycleScope.launch {
            val colors = db.colorDao().getColorsByCategory(category)
            val colorStrings = colors.map { it.hexCode }
            val adapter = ArrayAdapter(this@SavedColorsActivity, android.R.layout.simple_list_item_1, colorStrings)
            savedColorsListView.adapter = adapter
        }
    }
}
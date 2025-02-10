package com.example.color.ui.dashboard

import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.color.AppDatabase
import com.example.color.ColorListAdapter
import com.example.color.R
import kotlinx.coroutines.launch


class ListFragment : Fragment(R.layout.fragment_saved_colors) {

    private lateinit var savedColorsListView: ListView
    private lateinit var backButton: Button
    private lateinit var db: AppDatabase

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        savedColorsListView = view.findViewById(R.id.savedColorsListView)
        backButton = view.findViewById(R.id.backButton)
        db = AppDatabase.getDatabase(requireContext())

        loadCategories()
    }

    private fun loadCategories() {
        lifecycleScope.launch {
            val categories = db.colorDao().getAllCategories()
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, categories)
            savedColorsListView.adapter = adapter

            savedColorsListView.setOnItemClickListener { _, _, position, _ ->
                val selectedCategory = categories[position]
                showColors(selectedCategory)
            }

            savedColorsListView.setOnItemLongClickListener { _, _, position, _ ->
                val selectedCategory = categories[position]
                confirmDeleteCategory(selectedCategory)
                true
            }
        }
    }

    private fun showColors(category: String) {
        lifecycleScope.launch {
            val colors = db.colorDao().getColorsByCategory(category).map { it.hexCode }

            if (colors.isEmpty()) {
                Toast.makeText(requireContext(), "Tato kategorie je prázdná!", Toast.LENGTH_SHORT)
                    .show()
            } else {
                val adapter = ColorListAdapter(requireContext(), colors)
                savedColorsListView.adapter = adapter
                savedColorsListView.setOnItemClickListener { _, _, position, _ ->
                    copyToClipboard(colors[position])
                }
                savedColorsListView.setOnItemLongClickListener { _, _, position, _ ->
                    confirmDeleteColor(colors[position], category)
                    true
                }
                backButton.visibility = View.VISIBLE
                backButton.setOnClickListener {
                    loadCategories()
                    backButton.visibility = View.GONE
                }
            }
        }
    }

    private fun confirmDeleteColor(colorHex: String, category: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("Smazat barvu?")
            .setMessage("Opravdu chcete smazat barvu $colorHex?")
            .setPositiveButton("Ano") { _, _ ->
                deleteColor(colorHex, category)
            }
            .setNegativeButton("Ne", null)
            .show()
    }

    private fun deleteColor(colorHex: String, category: String) {
        lifecycleScope.launch {
            db.colorDao().deleteColor(colorHex)
            showColors(category)
        }
    }

    private fun confirmDeleteCategory(category: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("Smazat kategorii?")
            .setMessage("Opravdu chcete smazat kategorii $category včetně všech barev?")
            .setPositiveButton("Ano") { _, _ ->
                deleteCategory(category)
            }
            .setNegativeButton("Ne", null)
            .show()
    }

    private fun deleteCategory(category: String) {
        lifecycleScope.launch {
            db.colorDao().deleteCategory(category)
            loadCategories()
        }
    }

    private fun copyToClipboard(colorHex: String) {
        val clipboard =
            requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Hex Color", colorHex)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(requireContext(), "Barva $colorHex zkopírována!", Toast.LENGTH_SHORT)
            .show()
    }
}
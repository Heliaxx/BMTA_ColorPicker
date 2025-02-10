package com.example.color.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.color.AppDatabase
import com.example.color.ColorEntity
import com.example.color.R
import kotlinx.coroutines.launch

class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var imageView: ImageView
    private lateinit var colorInfo: TextView
    private lateinit var saveButton: Button
    private lateinit var db: AppDatabase
    private lateinit var pickImageButton: Button
    private lateinit var takePhotoButton: Button
    private val imageViewModel: ImageViewModel by activityViewModels()
    private var photoUri: Uri? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        imageView = view.findViewById(R.id.imageView)
        colorInfo = view.findViewById(R.id.colorInfo)
        saveButton = view.findViewById(R.id.saveButton)
        db = AppDatabase.getDatabase(requireContext())
        pickImageButton = view.findViewById(R.id.pickImageButton)
        takePhotoButton = view.findViewById(R.id.takePhotoButton)

        imageViewModel.imageUri.observe(viewLifecycleOwner) { photoUri ->
            photoUri?.let {
                imageView.setImageURI(it)
            }
        }

        pickImageButton.setOnClickListener {
            imagePickerLauncher.launch("image/*")
        }

        // Vybrání obrázku z galerie
        pickImageButton.setOnClickListener {
            imagePickerLauncher.launch("image/*")
        }

        // Pořízení fotografie (uložíme ji přímo do MediaStore)
        takePhotoButton.setOnClickListener {
            if (checkCameraPermission()) {
                startCamera()
            }
        }

        imageView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val bitmap = (imageView.drawable as BitmapDrawable).bitmap

                val imageViewWidth = imageView.width
                val imageViewHeight = imageView.height
                val bitmapWidth = bitmap.width
                val bitmapHeight = bitmap.height

                val x = event.x.toInt()
                val y = event.y.toInt()

                // Přepočet souřadnic kliknutí na souřadnice v bitmapě
                val imageX = (x.toFloat() / imageViewWidth * bitmapWidth).toInt()
                val imageY = (y.toFloat() / imageViewHeight * bitmapHeight).toInt()

                // Ověření, že souřadnice jsou v platném rozsahu bitmapy
                if (imageX in 0 until bitmapWidth && imageY in 0 until bitmapHeight) {
                    getPixelColor(imageX, imageY)
                }
            }
            true
        }

        // Kliknutí na barvu -> kopírování do schránky
        colorInfo.setOnClickListener {
            copyToClipboard(colorInfo.text.toString())
        }

        saveButton.setOnClickListener {
            val colorText = colorInfo.text.toString().split(" ")[1] // HEX kód barvy
            showCategoryDialog(colorText)
        }
    }

    private fun showCategoryDialog(hexCode: String) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_select_category, null)
        val categorySpinner: Spinner = dialogView.findViewById(R.id.categorySpinner)
        val newCategoryInput: EditText = dialogView.findViewById(R.id.newCategoryInput)
        val addCategoryCheckbox: CheckBox = dialogView.findViewById(R.id.addCategoryCheckbox)

        lifecycleScope.launch {
            val categories = db.colorDao().getAllCategories().toMutableList()
            if (!categories.contains("Nezařazeno")) {
                categories.add(0, "Nezařazeno")
            }

            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, categories)
            categorySpinner.adapter = adapter
        }

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Vyber kategorii")
            .setView(dialogView)
            .setPositiveButton("Uložit") { _, _ ->
                val selectedCategory = if (addCategoryCheckbox.isChecked) {
                    newCategoryInput.text.toString().trim()
                } else {
                    categorySpinner.selectedItem.toString()
                }

                if (selectedCategory.isNotEmpty()) {
                    saveColor(hexCode, selectedCategory)
                } else {
                    Toast.makeText(requireContext(), "Musíš vybrat nebo zadat kategorii!", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Zrušit", null)
            .create()

        addCategoryCheckbox.setOnCheckedChangeListener { _, isChecked ->
            newCategoryInput.visibility = if (isChecked) View.VISIBLE else View.GONE
        }

        dialog.show()
    }

    private fun getPixelColor(x: Int, y: Int) {
        val drawable = imageView.drawable as? BitmapDrawable ?: return
        val bitmap = drawable.bitmap ?: return

        val pixel = bitmap.getPixel(x, y)
        val red = (pixel shr 16) and 0xFF
        val green = (pixel shr 8) and 0xFF
        val blue = pixel and 0xFF
        val hexColor = String.format("#%02X%02X%02X", red, green, blue)

        colorInfo.text = "Barva: $hexColor"
        colorInfo.setTextColor(pixel)
    }

    private fun copyToClipboard(text: String) {
        val clipboard = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Barva", text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(requireContext(), "Barva zkopírována: $text", Toast.LENGTH_SHORT).show()
    }

    private fun saveColor(hexCode: String, category: String) {
        val colorDao = db.colorDao()
        lifecycleScope.launch {
            colorDao.insertColor(ColorEntity(hexCode = hexCode, category = category))
            Toast.makeText(requireContext(), "Barva uložena do $category", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startCamera() {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.TITLE, "Nová Fotka")
            put(MediaStore.Images.Media.DESCRIPTION, "Pořízeno aplikací Color Picker")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }
        photoUri = requireActivity().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        if (photoUri != null) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            }
            cameraLauncher.launch(intent)
        } else {
            Toast.makeText(requireContext(), "Chyba při ukládání fotky!", Toast.LENGTH_SHORT).show()
        }
    }

    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            imageViewModel.setImageUri(it)
            imageView.setImageURI(it)
        }
    }

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            photoUri?.let {
                imageViewModel.setImageUri(it)
                imageView.setImageURI(it)
            }
        }
    }

    private fun checkCameraPermission(): Boolean {
        return if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA), 100)
            false
        } else {
            true
        }
    }
}
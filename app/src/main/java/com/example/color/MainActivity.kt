package com.example.color

//import android.os.Bundle
//import com.google.android.material.bottomnavigation.BottomNavigationView
//import androidx.appcompat.app.AppCompatActivity
//import androidx.navigation.findNavController
//import androidx.navigation.ui.AppBarConfiguration
//import androidx.navigation.ui.setupActionBarWithNavController
//import androidx.navigation.ui.setupWithNavController
//import com.example.color.databinding.ActivityMainBinding
//
//class MainActivity : AppCompatActivity() {
//
//    private lateinit var binding: ActivityMainBinding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        val navView: BottomNavigationView = binding.navView
//
//        val navController = findNavController(R.id.nav_host_fragment_activity_main)
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        val appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
//            )
//        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        navView.setupWithNavController(navController)
//    }
//}
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.MotionEvent
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.widget.Toast
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.color.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch



class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.bottomNavigation

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}


//class MainActivity : AppCompatActivity() {
//
//    private lateinit var imageView: ImageView
//    private lateinit var colorInfo: TextView
//    private lateinit var saveButton: Button
//    private lateinit var savedColorsButton: Button
//    private lateinit var categoryInput: EditText
//    private lateinit var db: AppDatabase
//    private lateinit var pickImageButton: Button
//    private lateinit var takePhotoButton: Button
//    private var photoUri: Uri? = null  // Uložíme URI pro dočasnou fotku
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        imageView = findViewById(R.id.imageView)
//        colorInfo = findViewById(R.id.colorInfo)
//        saveButton = findViewById(R.id.saveButton)
//        savedColorsButton = findViewById(R.id.savedColorsButton)
//        categoryInput = findViewById(R.id.categoryInput)
//
//        db = AppDatabase.getDatabase(this)
//
//        // Kliknutí na obrázek pro detekci barvy
//        imageView.setOnTouchListener { _, event ->
//            val x = event.x.toInt()
//            val y = event.y.toInt()
//            getPixelColor(x, y)
//            true
//        }
//
//        pickImageButton = findViewById(R.id.pickImageButton)
//        takePhotoButton = findViewById(R.id.takePhotoButton)
//
//        // Vybrání obrázku z galerie (moderní způsob bez potřeby oprávnění)
//        pickImageButton.setOnClickListener {
//            imagePickerLauncher.launch("image/*")
//        }
//
//        // Pořízení fotografie (uložíme ji přímo do MediaStore)
//        takePhotoButton.setOnClickListener {
//            if (checkCameraPermission()) {
//                startCamera()
//            }
//        }
//
//        // Kliknutí na barvu -> kopírování do schránky
//        colorInfo.setOnClickListener {
//            copyToClipboard(colorInfo.text.toString())
//        }
//
//        // Uložení barvy do kategorie
//        saveButton.setOnClickListener {
//            val category = categoryInput.text.toString().trim()
//            if (category.isNotEmpty()) {
//                val colorText = colorInfo.text.toString().split(" ")[1] // Získání HEX kódu
//                saveColor(colorText, category)
//            } else {
//                Toast.makeText(this, "Zadejte kategorii!", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//        // Přechod na uložené barvy
//        savedColorsButton.setOnClickListener {
//            val intent = Intent(this, SavedColorsActivity::class.java)
//            startActivity(intent)
//        }
//    }
//
//    private fun getPixelColor(x: Int, y: Int) {
//        val drawable = imageView.drawable as? BitmapDrawable ?: return
//        val bitmap = drawable.bitmap ?: return
//
//        val imageViewWidth = imageView.width
//        val imageViewHeight = imageView.height
//        val bitmapWidth = bitmap.width
//        val bitmapHeight = bitmap.height
//
//        val scale = imageViewWidth.toFloat() / bitmapWidth
//        val displayedBitmapHeight = bitmapHeight * scale
//        val topPadding = (imageViewHeight - displayedBitmapHeight) / 2
//
//        val bitmapX = (x / scale).toInt()
//        val bitmapY = ((y - topPadding) / scale).toInt()
//
//        if (bitmapX in 0 until bitmapWidth && bitmapY in 0 until bitmapHeight) {
//            val pixel = bitmap.getPixel(bitmapX, bitmapY)
//            val red = (pixel shr 16) and 0xFF
//            val green = (pixel shr 8) and 0xFF
//            val blue = pixel and 0xFF
//            val hexColor = String.format("#%02X%02X%02X", red, green, blue)
//
//            colorInfo.text = "Barva: $hexColor"
//            colorInfo.setTextColor(pixel)
//        }
//    }
//
//    private fun startCamera() {
//        val contentValues = ContentValues().apply {
//            put(MediaStore.Images.Media.TITLE, "Nová Fotka")
//            put(MediaStore.Images.Media.DESCRIPTION, "Pořízeno aplikací Color Picker")
//            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
//        }
//        photoUri =
//            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
//
//        if (photoUri != null) {
//            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
//                putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
//            }
//            cameraLauncher.launch(intent)
//        } else {
//            Toast.makeText(this, "Chyba při ukládání fotky!", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    // Výběr obrázku z galerie
//    private val imagePickerLauncher =
//        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
//            uri?.let {
//                imageView.setImageURI(it)
//            }
//        }
//
//    // Pořízení fotografie
//    private val cameraLauncher =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            if (result.resultCode == Activity.RESULT_OK) {
//                photoUri?.let {
//                    imageView.setImageURI(it)
//                }
//            }
//        }
//
//    private fun checkCameraPermission(): Boolean {
//        return if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
//            != PackageManager.PERMISSION_GRANTED
//        ) {
//            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 100)
//            false
//        } else {
//            true
//        }
//    }
//
//    private fun copyToClipboard(text: String) {
//        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
//        val clip = ClipData.newPlainText("Barva", text)
//        clipboard.setPrimaryClip(clip)
//        Toast.makeText(this, "Barva zkopírována: $text", Toast.LENGTH_SHORT).show()
//    }
//
//    private fun saveColor(hexCode: String, category: String) {
//        val colorDao = db.colorDao()
//        lifecycleScope.launch {
//            colorDao.insertColor(ColorEntity(hexCode = hexCode, category = category))
//            Toast.makeText(this@MainActivity, "Barva uložena do $category", Toast.LENGTH_SHORT)
//                .show()
//        }
//    }
//}




//class MainActivity : AppCompatActivity() {
//    private lateinit var imageView: ImageView
//    private lateinit var colorInfo: TextView
//    private lateinit var pickImageButton: Button
//    private lateinit var takePhotoButton: Button
//
//    val savedColorsButton: Button = findViewById(R.id.savedColorsButton)
//    savedColorsButton.setOnClickListener {
//        val intent = Intent(this, SavedColorsActivity::class.java)
//        startActivity(intent)
//    }
//
//    private var photoUri: Uri? = null  // Uložíme URI pro dočasnou fotku
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        imageView = findViewById(R.id.imageView)
//        colorInfo = findViewById(R.id.colorInfo)
//        pickImageButton = findViewById(R.id.pickImageButton)
//        takePhotoButton = findViewById(R.id.takePhotoButton)
//
//        // Vybrání obrázku z galerie (moderní způsob bez potřeby oprávnění)
//        pickImageButton.setOnClickListener {
//            imagePickerLauncher.launch("image/*")
//        }
//
//        // Pořízení fotografie (uložíme ji přímo do MediaStore)
//        takePhotoButton.setOnClickListener {
//            if (checkCameraPermission()) {
//                startCamera()
//            }
//        }
//
//        colorInfo.setOnClickListener {
//            copyToClipboard(colorInfo.text.toString())
//        }
//
//        // Získání barvy při dotyku na obrázku
//        imageView.setOnTouchListener { _, event ->
//            if (event.action == MotionEvent.ACTION_DOWN) {
//                getPixelColor(event.x.toInt(), event.y.toInt())
//            }
//            true
//        }
//    }
//
//    private fun startCamera() {
//        val contentValues = ContentValues().apply {
//            put(MediaStore.Images.Media.TITLE, "Nová Fotka")
//            put(MediaStore.Images.Media.DESCRIPTION, "Pořízeno aplikací Color Picker")
//            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
//        }
//        photoUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
//
//        if (photoUri != null) {
//            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
//                putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
//            }
//            cameraLauncher.launch(intent)
//        } else {
//            Toast.makeText(this, "Chyba při ukládání fotky!", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    // Výběr obrázku z galerie
//    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
//        uri?.let {
//            imageView.setImageURI(it)
//        }
//    }
//
//    // Pořízení fotografie
//    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//        if (result.resultCode == Activity.RESULT_OK) {
//            photoUri?.let {
//                imageView.setImageURI(it)
//            }
//        }
//    }
//
//    private fun getPixelColor(x: Int, y: Int) {
//        val drawable = imageView.drawable as? BitmapDrawable
//        val bitmap = drawable?.bitmap ?: return
//
//        val imageViewWidth = imageView.width
//        val imageViewHeight = imageView.height
//        val bitmapWidth = bitmap.width
//        val bitmapHeight = bitmap.height
//
//        // Získání skutečné velikosti obrázku uvnitř ImageView
//        val imageViewScale = imageViewWidth.toFloat() / bitmapWidth
//        val displayedBitmapHeight = bitmapHeight * imageViewScale
//
//        // Výpočet posunutí kvůli `fitCenter`
//        val topPadding = (imageViewHeight - displayedBitmapHeight) / 2
//
//        // Přepočet kliknutých souřadnic na souřadnice v bitmapě
//        val bitmapX = (x / imageViewScale).toInt()
//        val bitmapY = ((y - topPadding) / imageViewScale).toInt()
//
//        // Kontrola, zda jsme v rámci bitmapy
//        if (bitmapX in 0 until bitmapWidth && bitmapY in 0 until bitmapHeight) {
//            val pixel = bitmap.getPixel(bitmapX, bitmapY)
//            val red = (pixel shr 16) and 0xFF
//            val green = (pixel shr 8) and 0xFF
//            val blue = pixel and 0xFF
//            val hexColor = String.format("#%02X%02X%02X", red, green, blue)
//
//            colorInfo.text = "Barva: $hexColor (R: $red, G: $green, B: $blue)"
//            colorInfo.setTextColor(pixel)
//        } else {
//            colorInfo.text = "Klikni na obrázek!"
//        }
//    }
//
//    private fun checkCameraPermission(): Boolean {
//        return if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
//            != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 100)
//            false
//        } else {
//            true
//        }
//    }
//
//    private fun saveColor(hexCode: String, category: String) {
//        val colorDao = AppDatabase.getDatabase(this).colorDao()
//        lifecycleScope.launch {
//            colorDao.insertColor(ColorEntity(hexCode = hexCode, category = category))
//            Toast.makeText(this@MainActivity, "Barva uložena do $category", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    val savedColorsButton: Button = findViewById(R.id.savedColorsButton)
//    savedColorsButton.setOnClickListener {
//        val intent = Intent(this, SavedColorsActivity::class.java)
//        startActivity(intent)
//    }
//
//    private fun copyToClipboard(text: String) {
//        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
//        val clip = ClipData.newPlainText("Barva", text)
//        clipboard.setPrimaryClip(clip)
//        Toast.makeText(this, "Barva zkopírována: $text", Toast.LENGTH_SHORT).show()
//    }
//
//}
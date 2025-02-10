package com.example.color

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ColorDao {
    @Insert
    suspend fun insertColor(color: ColorEntity)

    @Query("SELECT * FROM colors WHERE category = :category")
    suspend fun getColorsByCategory(category: String): List<ColorEntity>

    @Query("SELECT DISTINCT category FROM colors")
    suspend fun getAllCategories(): List<String>

    @Query("DELETE FROM colors WHERE hexCode = :colorHex")
    suspend fun deleteColor(colorHex: String)

    @Query("DELETE FROM colors WHERE category = :category")
    suspend fun deleteCategory(category: String)
}
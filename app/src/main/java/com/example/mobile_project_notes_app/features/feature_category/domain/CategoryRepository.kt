package com.example.mobile_project_notes_app.features.feature_category.domain

import com.example.mobile_project_notes_app.data.dao.CategoryDao
import com.example.mobile_project_notes_app.data.entity.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    val allCategories: Flow<List<Category>>
    suspend fun insertCategory(category: Category)

    suspend fun deleteCategory(category: Category)

    suspend fun deleteCategoryByName(categoryName: String)
}
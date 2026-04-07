package com.example.mobile_project_notes_app.features.feature_category

import com.example.mobile_project_notes_app.data.dao.CategoryDao
import com.example.mobile_project_notes_app.data.entity.Category
import kotlinx.coroutines.flow.Flow

class CategoryRepository(private val categoryDao: CategoryDao) {

    val allCategories: Flow<List<Category>> = categoryDao.getAllCategories()

    suspend fun insertCategory(category: Category) {
        categoryDao.insertCategory(category)
    }

    suspend fun deleteCategory(category: Category) {
        categoryDao.deleteCategory(category)
    }

    suspend fun deleteCategoryByName(categoryName: String) {
        categoryDao.deleteCategoryByName(categoryName)
    }
}
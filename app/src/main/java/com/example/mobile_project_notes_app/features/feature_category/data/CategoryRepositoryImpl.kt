package com.example.mobile_project_notes_app.features.feature_category.data

import com.example.mobile_project_notes_app.data.dao.CategoryDao
import com.example.mobile_project_notes_app.data.entity.Category
import com.example.mobile_project_notes_app.features.feature_category.domain.CategoryRepository
import kotlinx.coroutines.flow.Flow

class CategoryRepositoryImpl(private val categoryDao: CategoryDao) : CategoryRepository {

    override val allCategories: Flow<List<Category>> = categoryDao.getAllCategories()

    override suspend fun insertCategory(category: Category) {
        categoryDao.insertCategory(category)
    }

    override suspend fun deleteCategory(category: Category) {
        categoryDao.deleteCategory(category)
    }

    override suspend fun deleteCategoryByName(categoryName: String) {
        categoryDao.deleteCategoryByName(categoryName)
    }
}
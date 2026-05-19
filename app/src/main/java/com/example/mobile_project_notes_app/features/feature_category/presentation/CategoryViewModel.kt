package com.example.mobile_project_notes_app.features.feature_category.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_project_notes_app.data.entity.Category
import com.example.mobile_project_notes_app.features.feature_category.domain.CategoryRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CategoryViewModel(
    private val repository: CategoryRepository
) : ViewModel() {

    val allCategories: StateFlow<List<Category>> =
        repository.allCategories.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addCategory(categoryName: String) {
        if (categoryName.isNotBlank()) {
            viewModelScope.launch {
                repository.insertCategory(Category(categoryName = categoryName.trim()))
            }
        }
    }

    fun deleteCategory(categoryName: String) {
        viewModelScope.launch {
            repository.deleteCategoryByName(categoryName)
        }
    }
}
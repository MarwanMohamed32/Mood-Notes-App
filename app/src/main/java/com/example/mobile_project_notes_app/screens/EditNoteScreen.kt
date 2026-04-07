package com.example.mobile_project_notes_app.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobile_project_notes_app.viewmodel.CategoryViewModel
import com.example.mobile_project_notes_app.viewmodel.NoteViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNoteScreen(
    noteId: Int,
    onBackClick: () -> Unit,
    categoryViewModel: CategoryViewModel = koinViewModel(),
    noteViewModel: NoteViewModel = koinViewModel()
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("") }
    var isFavourite by remember { mutableStateOf(false) }
    var noteColor by remember { mutableStateOf(0L) }
    var expanded by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }

    val categories by categoryViewModel.allCategories.collectAsState()

    // Load note data
    LaunchedEffect(noteId) {
        noteViewModel.getNoteById(noteId)?.let { note ->
            title = note.title
            description = note.description
            selectedCategory = note.categoryName
            isFavourite = note.favourite
            noteColor = note.bgColor
            isLoading = false
        }
    }

    val selectedColor = Color(noteColor.toULong())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Note") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { isFavourite = !isFavourite }) {
                        Icon(
                            imageVector = if (isFavourite) Icons.Filled.Star else Icons.Outlined.Star,
                            contentDescription = "Favourite",
                            tint = if (isFavourite) Color(0xFFFFBE0B) else MaterialTheme.colorScheme.onSurface
                        )
                    }
                    IconButton(
                        onClick = {
                            noteViewModel.deleteNoteById(noteId)
                            onBackClick()
                        }
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color.Red
                        )
                    }
                    IconButton(
                        onClick = {
//                            if (title.isNotBlank() && description.isNotBlank() && selectedCategory.isNotBlank()) {
//                                noteViewModel.updateNoteById(
//                                    noteId = noteId,
//                                    title = title,
//                                    description = description,
//                                    categoryName = selectedCategory,
//                                    isFavourite = isFavourite
//                                )
//                                onBackClick()
//                            }
                        },
                        enabled = title.isNotBlank() && description.isNotBlank() && selectedCategory.isNotBlank()
                    ) {
                        Icon(Icons.Default.Check, contentDescription = "Save")
                    }
                }
            )
        }
    ) { padding ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
            ) {
                // Color preview
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .background(selectedColor)
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Title input
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Title") },
                        placeholder = { Text("Enter note title") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = selectedColor,
                            focusedLabelColor = selectedColor
                        )
                    )

                    // Category dropdown
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = it }
                    ) {
                        OutlinedTextField(
                            value = selectedCategory,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Category") },
                            placeholder = { Text("Select a category") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = selectedColor,
                                focusedLabelColor = selectedColor
                            )
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            categories.forEach { category ->
                                DropdownMenuItem(
                                    text = { Text(category.categoryName) },
                                    onClick = {
                                        selectedCategory = category.categoryName
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    // Description input
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description") },
                        placeholder = { Text("Write your note here...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = selectedColor,
                            focusedLabelColor = selectedColor
                        ),
                        maxLines = 15
                    )

                    // Note preview card
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(selectedColor.copy(alpha = 0.3f))
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Preview",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = title.ifBlank { "Your title here" },
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = description.ifBlank { "Your description here" },
                            fontSize = 14.sp,
                            maxLines = 3
                        )
                        if (selectedCategory.isNotBlank()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row {
                                Text(
                                    text = selectedCategory,
                                    fontSize = 12.sp,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(selectedColor)
                                        .padding(horizontal = 8.dp, vertical = 4.dp),
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
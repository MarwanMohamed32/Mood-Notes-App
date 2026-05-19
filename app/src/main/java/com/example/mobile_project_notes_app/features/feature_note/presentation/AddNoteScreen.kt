package com.example.mobile_project_notes_app.features.feature_note.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.toColorLong
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobile_project_notes_app.R
import com.example.mobile_project_notes_app.components.Common.GlassyIconButton
import com.example.mobile_project_notes_app.components.Note.FilterBottomSheet
import com.example.mobile_project_notes_app.data.entity.Category
import com.example.mobile_project_notes_app.models.NoteColor
import com.example.mobile_project_notes_app.models.NoteType
import com.example.mobile_project_notes_app.features.feature_category.presentation.CategoryViewModel
import com.example.mobile_project_notes_app.ui.theme.AppColors
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteScreen(
    noteType: NoteType,
    onBackClick: () -> Unit,
    categoryViewModel: CategoryViewModel = koinViewModel(),
    noteViewModel: NoteViewModel = koinViewModel()
) {

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(Category("All")) }
    var isFavourite by remember { mutableStateOf(false) }
    var isSheetOpen by remember { mutableStateOf(false) }
    var selectedColor by remember {
        mutableStateOf(
            NoteColor(
                colorName = "Vanilla Oat",
                AppColors.Note.vanillaOat,
                AppColors.Note.vanillaOatText
            )
        )
    }

    Scaffold()
    { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .background(selectedColor.bgColor)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    GlassyIconButton(
                        icon = painterResource(R.drawable.back_ico),
                        color = Color.Black,
                        onClick = {
                            onBackClick()
                        }
                    )
                    Spacer(
                        modifier = Modifier.weight(1f)
                    )
                    Row(
                        modifier = Modifier,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        GlassyIconButton(
                            icon = painterResource(R.drawable.filter_ico),
                            color = Color.Black,
                            onClick = {
                                isSheetOpen = true
                            }
                        )
                        GlassyIconButton(
                            icon = painterResource(R.drawable.done_ico),
                            color = Color.Black,
                            onClick = {
                                noteViewModel.addNote(
                                    title = title,
                                    description = description,
                                    categoryName = selectedCategory.categoryName,
                                    bgColor = selectedColor.bgColor.toColorLong(),
                                    textHoveredColor = selectedColor.textHoveredColor.toColorLong()
                                )
                                onBackClick()
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.padding(vertical = 24.dp))

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Title input
                    BasicTextField(
                        value = title,
                        onValueChange = { title = it },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = LocalTextStyle.current.copy(
                            fontSize = 84.sp,
                            lineHeight = 84.sp
                        ),
                        cursorBrush = SolidColor(Color.Black),
                        decorationBox = { innerTextField ->
                            Box {
                                if (title.isEmpty()) {
                                    Text(
                                        text = "Enter\nNote\nTitle",
                                        style = LocalTextStyle.current.copy(fontSize = 84.sp),
                                        color = Color.Gray,
                                        lineHeight = 94.sp
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )
                    Spacer(modifier = Modifier.padding(vertical = 12.dp))

                    // Description input
                    BasicTextField(
                        value = description,
                        onValueChange = { description = it },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = LocalTextStyle.current.copy(
                            fontSize = 18.sp,
                            lineHeight = 18.sp
                        ),
                        cursorBrush = SolidColor(Color.Black),
                        decorationBox = { innerTextField ->
                            Box {
                                if (description.isEmpty()) {
                                    Text(
                                        text = "Tap here to type.",
                                        style = LocalTextStyle.current.copy(fontSize = 18.sp),
                                        color = Color.Gray,
                                        lineHeight = 18.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )
                    FilterBottomSheet(
                        isSheetOpen = isSheetOpen,
                        onSheetStateChange = { isSheetOpen = it },
                        onChangeApplied = { color, category ->
                            selectedColor = color
                            selectedCategory = category
                            isSheetOpen = false
                        },
                        categoryViewModel = categoryViewModel
                    )
                }
            }

        }
    }
}

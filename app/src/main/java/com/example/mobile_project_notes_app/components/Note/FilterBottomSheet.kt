package com.example.mobile_project_notes_app.components.Note

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobile_project_notes_app.data.entity.Category
import com.example.mobile_project_notes_app.models.NoteColor
import com.example.mobile_project_notes_app.viewmodel.CategoryViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.forEach


val noteColors = listOf(
    NoteColor(colorName = "Vanilla Oat", Color(0xFFF6ECC9), Color(0xFFF1D04C)),
    NoteColor(colorName = "Terracotta Punch", Color(0xFFEA7A53), Color(0xFFE0562E)),
    NoteColor(colorName = "Golden Marigold", Color(0xFFE7B93F), Color(0xFFD39A1A)),
    NoteColor(colorName = "Powder Sky", Color(0xFF99B7DD), Color(0xFF5F8FCB)),
    NoteColor(colorName = "Spring Pea", Color(0xFFADD276), Color(0xFF7FB63B)),
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FilterBottomSheet(
    isSheetOpen: Boolean,
    onSheetStateChange: (Boolean) -> Unit,
    onChangeApplied: (NoteColor, Category) -> Unit,
    categoryViewModel: CategoryViewModel
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    var selectedColor by remember { mutableStateOf<NoteColor>(noteColors[0]) }
    val categoryList by categoryViewModel.allCategories.collectAsState(initial = emptyList())
    var selectedCategory by remember { mutableStateOf<Category>(Category("All")) }

    if (isSheetOpen) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {
                onSheetStateChange(false)
            }
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 18.dp)
                    .fillMaxWidth(),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = "Settings",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )

                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        IconButton(onClick = { onSheetStateChange(false) }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Color Selector
                Text(
                    "Color",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(20.dp))

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    noteColors.forEach { noteColor ->
                        Row(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Color(0xFFE8E8E8))
                                .border(
                                    width = 2.dp,
                                    color = if (selectedColor == noteColor) Color.Black else Color.Transparent,
                                    shape = CircleShape
                                )
                                .clickable { selectedColor = noteColor }
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(noteColor.bgColor)
                                    .size(12.dp)
                            )
                            Text(
                                noteColor.colorName,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
                Spacer(
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                )
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFE8E8E8))
                )
                Spacer(
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                )

//                Category Selector

                Text(
                    "Category",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(20.dp))

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    val allCategories = listOf(Category("All")) + categoryList

                    allCategories.forEach { category ->
                        Row(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Color(0xFFE8E8E8))
                                .border(
                                    width = 2.dp,
                                    color = if (selectedCategory == category) Color.Black else Color.Transparent,
                                    shape = CircleShape
                                )
                                .clickable { selectedCategory = category }
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = category.categoryName,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
                Spacer(
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                )

//                Apply Button

                Button(
                    onClick = {
                        onChangeApplied(selectedColor, selectedCategory)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.White
                    ),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xFF40A474),
                                        Color(0xFF165A2F)
                                    )
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Apply",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                        )
                    }
                }
            }
        }
    }
}
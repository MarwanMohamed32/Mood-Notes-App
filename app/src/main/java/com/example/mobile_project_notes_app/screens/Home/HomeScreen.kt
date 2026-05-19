package com.example.mobile_project_notes_app.screens.Home

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobile_project_notes_app.R
import com.example.mobile_project_notes_app.components.Common.AnimatedFab
import com.example.mobile_project_notes_app.features.feature_category.presentation.CategoryBottomSheet
import com.example.mobile_project_notes_app.components.Common.GlassyIconButton
import com.example.mobile_project_notes_app.components.Note.NoteComponent
import com.example.mobile_project_notes_app.components.Common.SearchBar
import com.example.mobile_project_notes_app.models.NoteType
import com.example.mobile_project_notes_app.ui.theme.CoralBlaze
import com.example.mobile_project_notes_app.ui.theme.HoneyGold
import com.example.mobile_project_notes_app.ui.theme.MidnightIndigo
import com.example.mobile_project_notes_app.ui.theme.TropicalTeal
import com.example.mobile_project_notes_app.features.feature_category.presentation.CategoryViewModel
import com.example.mobile_project_notes_app.features.feature_note.presentation.NoteViewModel
import com.example.mobile_project_notes_app.models.Category
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    onMenuClick: () -> Unit,
    onNavigateToAddNote: (NoteType) -> Unit,
    onNavigateToEditNote: (Int) -> Unit,
    categoryViewModel: CategoryViewModel = koinViewModel(),
    noteViewModel: NoteViewModel = koinViewModel()
) {
    var isSheetOpen by rememberSaveable { mutableStateOf(false) }
    var selectedCategory by rememberSaveable { mutableStateOf("All") }

    val categories by categoryViewModel.allCategories.collectAsState()
    val allNotes by noteViewModel.allNotes.collectAsState()

    val filteredNotes = when (selectedCategory) {
        "All" -> allNotes.sortedBy { it.uid }
        "Favourite" -> allNotes.filter { it.favourite }.sortedBy { it.uid }
        else -> allNotes.filter { it.categoryName == selectedCategory }.sortedBy { it.uid }
    }

    Scaffold(
        floatingActionButton = {
            AnimatedFab(
                onMenuItemClick = { type -> onNavigateToAddNote(type) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                GlassyIconButton(
                    icon = painterResource(R.drawable.burger_menu_ico),
                    color = Color.Black,
                    onClick = onMenuClick
                )

                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = TropicalTeal)) { append("My") }
                        append("\n")
                        withStyle(style = SpanStyle(color = CoralBlaze)) { append("No") }
                        withStyle(style = SpanStyle(color = MidnightIndigo)) { append("te") }
                        withStyle(style = SpanStyle(color = HoneyGold)) { append("s") }
                    },
                    style = TextStyle(
                        fontSize = 72.sp,
                        lineHeight = 72.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    textAlign = TextAlign.End
                )
            }

            SearchBar()
            Spacer(Modifier.padding(vertical = 1.dp))

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Category(
                        "All",
                        onClick = { selectedCategory = "All" },
                        isSelected = selectedCategory == "All"
                    )
                }
                item {
                    Category(
                        "Favourite",
                        onClick = { selectedCategory = "Favourite" },
                        isSelected = selectedCategory == "Favourite"
                    )
                }
                items(categories) { category ->
                    Category(
                        categoryName = category.categoryName,
                        onClick = { selectedCategory = category.categoryName },
                        isSelected = selectedCategory == category.categoryName
                    )
                }
                item {
                    Category(categoryName = "+", onClick = { isSheetOpen = true })
                }
            }

            Spacer(modifier = Modifier.padding(vertical = 8.dp))

            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalItemSpacing = 6.dp
            ) {
                items(items = filteredNotes, key = { it.uid }) { note ->
                    NoteComponent(
                        title = note.title,
                        modifier = Modifier,
                        noteContent = note.description,
                        isFavourite = note.favourite,
                        bgColor = Color(note.bgColor.toULong()),
                        onClick = { onNavigateToEditNote(note.uid) },
                        onLongPress = { noteViewModel.deleteNoteById(note.uid) },
                        onFavouriteClick = {
                            noteViewModel.toggleFavourite(note.uid, it)
                            Log.d("favourite", "${note.favourite}")
                        }
                    )
                }
            }
        }
    }

    CategoryBottomSheet(
        isSheetOpen = isSheetOpen,
        onSheetStateChange = { isSheetOpen = false },
        onAddCategory = { categoryName ->
            categoryViewModel.addCategory(categoryName)
            isSheetOpen = false
        }
    )
}

package com.example.mobile_project_notes_app.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
import com.example.mobile_project_notes_app.components.AnimatedFab
import com.example.mobile_project_notes_app.components.Category.Category
import com.example.mobile_project_notes_app.components.Category.CategoryBottomSheet
import com.example.mobile_project_notes_app.components.GlassyIconButton
import com.example.mobile_project_notes_app.components.SearchBar
import com.example.mobile_project_notes_app.ui.theme.CoralBlaze
import com.example.mobile_project_notes_app.ui.theme.HoneyGold
import com.example.mobile_project_notes_app.ui.theme.MidnightIndigo
import com.example.mobile_project_notes_app.ui.theme.TropicalTeal

val categories = listOf(
    "Favourite",
    "Work"
)

@Composable
fun HomeScreen() {
    var isSheetOpen by rememberSaveable {
        mutableStateOf(false)
    }

    Scaffold(
        floatingActionButton = {
            AnimatedFab()
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
                GlassyIconButton(icon = painterResource(R.drawable.burger_menu_ico), onClick = {})
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = TropicalTeal)) {
                            append("My")
                        }
                        append("\n")
                        withStyle(style = SpanStyle(color = CoralBlaze)) {
                            append("No")
                        }
                        withStyle(style = SpanStyle(color = MidnightIndigo)) {
                            append("te")
                        }
                        withStyle(style = SpanStyle(color = HoneyGold)) {
                            append("s")
                        }
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
                    Category(categoryName = "All", onClick = {})
                }
                items(categories) { categoryName ->
                    Category(categoryName = categoryName, onClick = {})
                }
                item {
                    Category(categoryName = "+", onClick = { isSheetOpen = true })
                }
            }

        }
    }
    CategoryBottomSheet(isSheetOpen, onSheetStateChange = { isSheetOpen = false })


}
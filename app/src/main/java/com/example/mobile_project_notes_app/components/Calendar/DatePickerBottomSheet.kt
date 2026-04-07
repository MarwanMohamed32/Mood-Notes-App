package com.example.mobile_project_notes_app.components.Calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import java.util.Calendar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerBottomSheet(
    isSheetOpen: Boolean,
    onSheetStateChange: (Boolean) -> Unit,
    onDateSelected: (month: Int, year: Int) -> Unit = { _, _ -> }
) {
    val sheetState = rememberModalBottomSheetState()
    val currentDate = Calendar.getInstance()

    var selectedMonth by rememberSaveable { mutableIntStateOf(currentDate.get(Calendar.MONTH)) }
    var selectedYear by rememberSaveable { mutableIntStateOf(currentDate.get(Calendar.YEAR)) }

    if (isSheetOpen) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {
                onSheetStateChange(false)
            }
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Select The Date",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Spacer(Modifier.padding(vertical = 12.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)

                ) {
                    ScrollableColumn(
                        modifier = Modifier.weight(1f),
                        items = getMonthNames(),
                        initialIndex = selectedMonth,
                        onItemSelected = { index ->
                            selectedMonth = index
                        }
                    )
                    ScrollableColumn(
                        modifier = Modifier.weight(1f),
                        items = getYearRange(),
                        initialIndex = getYearRange().indexOf(selectedYear.toString()),
                        onItemSelected = { index ->
                            selectedYear = getYearRange()[index].toInt()
                        }
                    )

                }
                Spacer(Modifier.padding(vertical = 24.dp))
                Button(
                    onClick = {
                        onDateSelected(
                            selectedMonth, selectedYear
                        )
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
                            "Confirm",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                        )
                    }
                }

            }

        }
    }
}

@Composable
fun ScrollableColumn(
    modifier: Modifier,
    items: List<String>,
    initialIndex: Int,
    onItemSelected: (Int) -> Unit
) {
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = initialIndex)
    val itemHeight = 44.dp

    val snapBehavior = rememberSnapFlingBehavior(lazyListState = listState)
    val centerIndex by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val viewportCenter = layoutInfo.viewportStartOffset + layoutInfo.viewportSize.height / 2

            layoutInfo.visibleItemsInfo
                .minByOrNull { item ->
                    val itemCenter = item.offset + item.size / 2
                    kotlin.math.abs(itemCenter - viewportCenter)
                }?.index ?: initialIndex
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow {
            val layoutInfo = listState.layoutInfo
            val viewportCenter = layoutInfo.viewportStartOffset + layoutInfo.viewportSize.height / 2

            layoutInfo.visibleItemsInfo
                .minByOrNull { item ->
                    val itemCenter = item.offset + item.size / 2
                    kotlin.math.abs(itemCenter - viewportCenter)
                }?.index
        }
            .distinctUntilChanged()
            .collect { index ->
                index?.let { onItemSelected(it) }
            }
    }

    Box(modifier = modifier) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState,
            contentPadding = PaddingValues(vertical = 78.dp),
            flingBehavior = snapBehavior
        ) {
            items(items.size) { index ->
                val isSelected = index == centerIndex
                Text(
                    text = items[index],
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(itemHeight)
                        .padding(vertical = 12.dp)
                        .alpha(if (isSelected) 1f else 0.4f),
                    textAlign = TextAlign.Center,
                    fontSize = if (isSelected) 20.sp else 16.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected)
                        Color.Black
                    else
                        Color.Gray
                )
            }
        }


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeight)
                .align(Alignment.Center)
                .background(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                )
        )
    }
}

fun getMonthNames(): List<String> {
    return listOf(
        "January", "February", "March", "April",
        "May", "June", "July", "August",
        "September", "October", "November", "December"
    )
}

fun getYearRange(): List<String> {
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    return (currentYear - 50..currentYear + 10).map { it.toString() }
}
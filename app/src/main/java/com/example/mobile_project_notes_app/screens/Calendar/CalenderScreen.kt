package com.example.mobile_project_notes_app.screens.Calendar

import BorderedCircledIcon
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mobile_project_notes_app.R
import com.example.mobile_project_notes_app.components.Calendar.DatePickerBottomSheet
import com.example.mobile_project_notes_app.components.Common.GlassyIconButton
import com.example.mobile_project_notes_app.viewmodel.CalendarViewModel
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

data class CalendarDay(
    val dayOfMonth: Int,
    val date: String,
    val mood: String? = null,
    val isCurrentMonth: Boolean = true
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    onMenuClick: () -> Unit,
    calendarViewModel: CalendarViewModel = koinViewModel()
) {
    var isSheetOpen by rememberSaveable { mutableStateOf(false) }
    var currentDate by remember { mutableStateOf(Calendar.getInstance()) }
    val calendarDayEntries by calendarViewModel.moodEntriesForMonth.collectAsState()

    val calendar = remember(currentDate) { generateCalendarDays(currentDate) }
    val monthYear = remember(currentDate) {
        SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(currentDate.time)
    }

    val moodMap = remember(calendarDayEntries) {
        calendarDayEntries.associateBy { it.date }
    }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .statusBarsPadding()
                    .padding(vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    GlassyIconButton(
                        icon = painterResource(R.drawable.burger_menu_ico),
                        color = Color.Black,
                        contentDescription = "Menu",
                        iconSize = 28.dp,
                        modifier = Modifier,
                        onClick = { onMenuClick() }
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Mood Calendar",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Text(
                            text = monthYear,
                            fontWeight = FontWeight.Medium,
                            color = Color.Gray
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))

                    // Date Selector
                    GlassyIconButton(
                        icon = painterResource(R.drawable.calendar_ico),
                        color = Color.Black,
                        contentDescription = "Calendar",
                        iconSize = 28.dp,
                        modifier = Modifier,
                        onClick = { isSheetOpen = true }
                    )
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.padding(vertical = 8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat").forEach { day ->
                    Text(
                        text = day,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.padding(vertical = 8.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(calendar) { day ->
                    CalendarDayCell(
                        day = day,
                        mood = moodMap[day.date]?.mood
                    )
                }
            }

            Spacer(modifier = Modifier.padding(vertical = 8.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFBEFBAD))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Monthly Mood Summary",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
                Text(
                    text = "Happy",
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "You're feeling calm & optimistic. \nKeep up the good vibes!",
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    lineHeight = 16.sp
                )
            }

            DatePickerBottomSheet(
                isSheetOpen = isSheetOpen,
                onSheetStateChange = { isSheetOpen = it },
                onDateSelected = { month, year ->
                    val newDate = Calendar.getInstance().apply {
                        set(Calendar.YEAR, year)
                        set(Calendar.MONTH, month)
                    }
                    currentDate = newDate
                    isSheetOpen = false
                }
            )
        }
    }
}

@Composable
fun CalendarDayCell(day: CalendarDay, mood: String?) {
    if (day.dayOfMonth <= 0) return

    val isValentinesDay = day.date.endsWith("-02-14")
    val moodResId: Int = if (isValentinesDay) {
        R.drawable.manoon
    } else {
        when (mood?.trim()) {
            "Good" -> R.drawable.good_mood
            "NotBad", "Not Bad" -> R.drawable.not_bad_mood
            "Bad" -> R.drawable.angry_mood
            else -> R.drawable.good_mood
        }
    }

    Log.d("Mood:  $mood", "mood")

    val textColor = if (day.isCurrentMonth) {
        if (mood != null) Color.White else Color.Gray
    } else {
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
    }

    if (isValentinesDay) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.2f)
                .clip(RoundedCornerShape(8.dp))
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFA9E3ED),
                            Color(0xFF4899F7)
                        )
                    )
                )
        ) {
            Image(
                painter = painterResource(moodResId),
                contentDescription = "Valentine's Day",
                modifier = Modifier.fillMaxSize()
            )
        }
    } else if (mood == null) {
        Box(
            modifier = Modifier
                .aspectRatio(1.2f)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFDADADA)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = day.dayOfMonth.toString(),
                color = textColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.2f)
                .clip(RoundedCornerShape(8.dp))
                .background(
                    when (mood) {
                        "Good" -> Color(0xFFBEFBAD)
                        "NotBad", "Not Bad" -> Color(0xFFFE9460)
                        "Bad" -> Color(0xFFFE878B)
                        else -> Color(0xFFDADADA)
                    }
                )
        ) {
            Image(
                painter = painterResource(moodResId),
                contentDescription = "Mood indicator",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

fun generateCalendarDays(currentDate: Calendar): List<CalendarDay> {
    val calendar = currentDate.clone() as Calendar
    calendar.set(Calendar.DAY_OF_MONTH, 1)

    val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
    val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

    val days = mutableListOf<CalendarDay>()
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    for (i in 1 until firstDayOfWeek) {
        days.add(CalendarDay(0, "", null, false))
    }

    for (day in 1..daysInMonth) {
        calendar.set(Calendar.DAY_OF_MONTH, day)
        val dateString = sdf.format(calendar.time)
        days.add(CalendarDay(day, dateString, null, true))
    }

    return days
}

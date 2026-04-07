package com.example.mobile_project_notes_app.components.Note

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobile_project_notes_app.R
import com.example.mobile_project_notes_app.components.Common.GlassyIconButton
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteComponent(
    title: String,
    noteContent: String,
    bgColor: Color,
    modifier: Modifier = Modifier,
    isFavourite: Boolean,
    onClick: () -> Unit = {},
    onLongPress: () -> Unit = {},
    onFavouriteClick: (Boolean) -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    val currentTime: String = remember { getCurrentTime() }
    val isLongNote = (title.length + noteContent.length) > 80
    val cardHeight = if (isLongNote) 500.dp else 250.dp


    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(cardHeight)
            .clip(RoundedCornerShape(32.dp))
            .background(color = bgColor)
            .combinedClickable(
                onClick = onClick,
                onLongClick = { showDeleteDialog = true }
            )
            .padding(vertical = 18.dp, horizontal = 18.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .width(50.dp)
                    .height(3.dp)
                    .clip(RoundedCornerShape(999.dp))
                    .background(Color.Black)
            )
            Spacer(modifier = Modifier.padding(vertical = 1.5.dp))
            Box(
                modifier = Modifier
                    .width(30.dp)
                    .height(3.dp)
                    .clip(RoundedCornerShape(999.dp))
                    .background(Color.Black)
            )

        }

        Spacer(modifier = Modifier.padding(vertical = 8.dp))

//        Note Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Medium,
                fontSize = 24.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            GlassyIconButton(
                icon = if (isFavourite) painterResource(R.drawable.filled_heart_ico) else painterResource
                    (R.drawable.outlined_heart_ico),
                color = if (isFavourite) Color.Red else Color.Black,
                contentDescription = "favourite",
                size = 54.dp,
                iconSize = 24.dp,
                onClick = { onFavouriteClick(!isFavourite) }
            )
        }

        Spacer(modifier = Modifier.padding(vertical = 6.dp))
        Text(
            text = noteContent,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            overflow = TextOverflow.Ellipsis,
            maxLines = if (isLongNote) 14 else 4
        )
        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = currentTime,
            fontWeight = FontWeight.Medium
        )
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Note") },
            text = { Text("Are you sure you want to delete this note?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onLongPress()
                        showDeleteDialog = false
                    }
                ) {
                    Text("Delete", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

private fun getCurrentTime(): String {
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    return sdf.format(Date())
}
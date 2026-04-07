package com.example.mobile_project_notes_app.components.Common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mobile_project_notes_app.navigation.Route

@Composable
fun GlassyIconButton(
    icon: Painter,
    color: Color,
    contentDescription: String? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 64.dp,
    iconSize: Dp = 28.dp,
    backgroundAlpha: Float = 0.2f,
    borderAlpha: Float = 0.25f,
    blurRadius: Dp = 20.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(CircleShape)
                .background(Color.Gray.copy(alpha = backgroundAlpha))
                .blur(blurRadius)
                .border(
                    width = 1.dp,
                    color = Color.White.copy(alpha = borderAlpha),
                    shape = CircleShape
                )
        )

        Icon(
            painter = icon,
            contentDescription = contentDescription,
            tint = color,
            modifier = Modifier.size(iconSize)
        )
    }
}

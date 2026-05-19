package com.example.mobile_project_notes_app.components.Common

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.mobile_project_notes_app.R
import com.example.mobile_project_notes_app.models.NoteType

data class FabMenuEntry(
    val label: String,
    @DrawableRes val iconRes: Int,
    val type: NoteType
)

@Composable
fun AnimatedFab(
    onMenuItemClick: (NoteType) -> Unit = {}
) {
    var isExpanded by remember { mutableStateOf(false) }

    val menuItems = listOf(
        FabMenuEntry("Text Note", R.drawable.written_note_ico, NoteType.TEXT),
        FabMenuEntry("Image Note", R.drawable.image_note_ico, NoteType.IMAGE),
    )

    Box(contentAlignment = Alignment.BottomCenter) {
        menuItems.forEachIndexed { index, item ->
            val offsetY by animateDpAsState(
                targetValue = if (isExpanded) (-(index + 1) * 60 - 20).dp else 0.dp,
                animationSpec = tween(
                    durationMillis = 500,
                    delayMillis = 0,
                    easing = FastOutSlowInEasing
                ),
                label = "offset_$index"
            )

            val scale by animateFloatAsState(
                targetValue = if (isExpanded) 1f else 0f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                ),
                label = "scale_$index"
            )

            FabMenuItem(
                label = item.label,
                offsetY = offsetY,
                scale = scale,
                iconRes = item.iconRes,
                onClick = {
                    onMenuItemClick(item.type)
                    isExpanded = false
                }
            )
        }

        MainFab(
            isExpanded = isExpanded,
            onClick = { isExpanded = !isExpanded }
        )
    }
}

@Composable
private fun MainFab(
    isExpanded: Boolean,
    onClick: () -> Unit
) {
    val rotation by animateFloatAsState(
        targetValue = if (isExpanded) 225f else 0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "rotation"
    )

    Icon(
        modifier = Modifier
            .size(56.dp)
            .clip(CircleShape)
            .background(Color.Black)
            .clickable(onClick = onClick)
            .padding(16.dp)
            .rotate(rotation),
        imageVector = Icons.Default.Add,
        contentDescription = if (isExpanded) "Close menu" else "Open menu",
        tint = Color.White
    )
}

@Composable
fun FabMenuItem(
    label: String,
    offsetY: Dp,
    scale: Float,
    @DrawableRes iconRes: Int,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .offset(y = offsetY)
            .scale(scale)
            .size(48.dp)
            .clip(CircleShape)
            .background(Color.Black),
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = iconRes),
            contentDescription = label,
            tint = Color.White,
            modifier = Modifier.size(20.dp)
        )
    }

}

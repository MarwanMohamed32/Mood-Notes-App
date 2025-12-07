package com.example.mobile_project_notes_app.components.MoodComponents

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun CheckpointSlider(
    checkpoints: List<String>,
    currentIndex: Int,
    onCheckpointClick: (Int) -> Unit,
    sliderColor: Color,
    circleColor: Color,
    modifier: Modifier = Modifier
) {
    require(checkpoints.size >= 2) { "CheckpointSlider needs at least 2 checkpoints" }

    var isDragging by remember { mutableStateOf(false) }
    var dragProgress by remember { mutableFloatStateOf(currentIndex.toFloat()) }

    LaunchedEffect(currentIndex) {
        if (!isDragging) {
            dragProgress = currentIndex.toFloat()
        }
    }

    val displayProgress by animateFloatAsState(
        targetValue = dragProgress,
        animationSpec = tween(durationMillis = if (isDragging) 150 else 250),
        label = "sliderProgress"
    )

    Canvas(
        modifier = modifier
            .width(320.dp)
            .height(100.dp)
            .pointerInput(checkpoints.size) {
                detectTapGestures { offset ->
                    val spacing = size.width / (checkpoints.size - 1)
                    val tappedProgress = (offset.x / spacing)
                        .coerceIn(0f, (checkpoints.size - 1).toFloat())

                    val tappedIndex = tappedProgress.roundToInt()
                    dragProgress = tappedIndex.toFloat()
                    onCheckpointClick(tappedIndex)
                }
            }
            .pointerInput(checkpoints.size) {
                detectDragGestures(
                    onDragStart = {
                        isDragging = true
                    },
                    onDragEnd = {
                        isDragging = false
                        val snappedIndex = dragProgress.roundToInt()
                            .coerceIn(0, checkpoints.lastIndex)
                        dragProgress = snappedIndex.toFloat()
                        onCheckpointClick(snappedIndex)
                    },
                    onDragCancel = {
                        isDragging = false
                        val snappedIndex = dragProgress.roundToInt()
                            .coerceIn(0, checkpoints.lastIndex)
                        dragProgress = snappedIndex.toFloat()
                        onCheckpointClick(snappedIndex)
                    },
                    onDrag = { change, _ ->
                        change.consume()

                        val spacing = size.width / (checkpoints.size - 1)
                        val newProgress = (change.position.x / spacing)
                            .coerceIn(0f, (checkpoints.size - 1).toFloat())

                        val newIndex = newProgress.roundToInt()
                        val oldIndex = dragProgress.roundToInt()

                        dragProgress = newProgress

                        if (newIndex != oldIndex) {
                            onCheckpointClick(newIndex)
                        }
                    }
                )
            }
    ) {
        val spacing = size.width / (checkpoints.size - 1)
        val circleRadius = 12.dp.toPx()
        val lineY = size.height / 2

        drawLine(
            color = sliderColor,
            start = Offset(0f, lineY),
            end = Offset(size.width, lineY),
            strokeWidth = 10.dp.toPx()
        )

        checkpoints.forEachIndexed { index, _ ->
            val x = index * spacing
            drawCircle(
                color = sliderColor,
                radius = circleRadius,
                center = Offset(x, lineY)
            )
        }

        val thumbX = displayProgress * spacing
        drawCircle(
            color = circleColor,
            radius = circleRadius * 2f,
            center = Offset(thumbX, lineY)
        )
    }
}
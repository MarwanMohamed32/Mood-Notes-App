package com.example.mobile_project_notes_app.components.MoodComponents

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun EmojiFace(
    faceColor: Color, offsetEyeHeight: Dp,
    offsetEyeWidth: Dp,
    offsetSpace: Dp,
    offsetStartAngle: Float,
    offsetSweepAngle: Float,
    offsetTopLeftHeight: Float,
    offsetTopLeftWidth: Float,
    offsetMouthWidth: Float,
    offsetMouthHeight: Float
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.spacedBy(offsetSpace)
        ) {
            Box(
                modifier = Modifier
                    .height(offsetEyeHeight)
                    .width(offsetEyeWidth)
                    .clip(CircleShape)
                    .background(faceColor)
            )
            Box(
                modifier = Modifier
                    .height(offsetEyeHeight)
                    .width(offsetEyeWidth)
                    .clip(CircleShape)
                    .background(faceColor)
            )

        }

        Canvas(
            modifier = Modifier
                .size(100.dp)
                .padding(16.dp)
        ) {
            drawArc(
                color = faceColor,
                startAngle = offsetStartAngle,
                sweepAngle = offsetSweepAngle,
                useCenter = false,
                style = Stroke(width = 50f, cap = StrokeCap.Round),
                topLeft = Offset(
                    size.width * offsetTopLeftWidth,
                    size.height * offsetTopLeftHeight
                ),
                size = Size(size.width * offsetMouthWidth, size.height * offsetMouthHeight)
            )
        }
    }
}
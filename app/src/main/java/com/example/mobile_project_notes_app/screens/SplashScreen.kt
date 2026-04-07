package com.example.mobile_project_notes_app.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobile_project_notes_app.MadeSoulMaze
import com.example.mobile_project_notes_app.ui.theme.CoralBlaze
import com.example.mobile_project_notes_app.ui.theme.HoneyGold
import com.example.mobile_project_notes_app.ui.theme.MidnightIndigo
import com.example.mobile_project_notes_app.ui.theme.TropicalTeal
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.mobile_project_notes_app.ExternalLibs.polarLineTo
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SplashScreen(onNavigateToHome: () -> Unit) {
    val density = LocalDensity.current

    val moodyScale = remember { Animatable(2.5f) }
    val moodyOffsetX = remember { Animatable(0f) }

    val notesAppOffsetX = remember { Animatable(with(density) { 300.dp.toPx() }) }
    val notesAppAlpha = remember { Animatable(0f) }

    val progressList = remember { List(4) { Animatable(0f) } }
    val flowerProgress = remember { Animatable(0f) }



    LaunchedEffect(Unit) {
        moodyScale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 750, easing = FastOutSlowInEasing)
        )
        delay(300)

        launch {
            moodyOffsetX.animateTo(
                targetValue = with(density) { -60.dp.toPx() },
                animationSpec = tween(durationMillis = 650, easing = FastOutSlowInEasing)
            )
        }
        launch {
            notesAppOffsetX.animateTo(
                targetValue = with(density) { 110.dp.toPx() },
                animationSpec = tween(durationMillis = 650, easing = FastOutSlowInEasing)
            )
        }
        launch {
            notesAppAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
            )
        }
        delay(400)
        progressList.forEachIndexed { index, animatable ->
            launch {
                delay(index * 150L)
                animatable.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(1000, easing = FastOutSlowInEasing)
                )
            }
        }
        delay(1200)
        launch {
            while (true) {
                flowerProgress.snapTo(0f)
                flowerProgress.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(1500, easing = FastOutSlowInEasing)
                )
            }
        }
        onNavigateToHome()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .windowInsetsPadding(WindowInsets.systemBars),
        contentAlignment = Alignment.Center
    ) {
        val pathMeasure = remember { PathMeasure() }
        val totalLength = remember { mutableFloatStateOf(0f) }

        Canvas(modifier = Modifier.fillMaxSize()) {
            var lineY = 200f
            val bottom = 2;
            val radius = 100f
            val colors = listOf(TropicalTeal, CoralBlaze, HoneyGold, MidnightIndigo)
            for (i in 0..3) {
                val path = Path().apply {
                    moveTo(size.width, lineY)
                    lineTo(size.width - 350f, lineY)

                    arcTo(
                        rect = Rect(
                            left = size.width - 350f - radius,
                            top = lineY,
                            right = size.width - 350f + radius,
                            bottom = lineY + bottom * radius
                        ),
                        startAngleDegrees = 270f,
                        sweepAngleDegrees = -180f,
                        forceMoveTo = false
                    )
                    lineTo(size.width, lineY + bottom * radius)
                }
                pathMeasure.setPath(path, false)
                totalLength.floatValue = pathMeasure.length
                val drawLength = totalLength.floatValue * progressList[i].value

                drawPath(
                    path = path,
                    color = colors[i],
                    style = Stroke(
                        width = 40.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(
                            intervals = floatArrayOf(drawLength, totalLength.floatValue),
                            phase = 0f
                        )
                    )
                )
                lineY += 200;

            }

            val origin = Offset(size.width / 2f, size.height - 350f)

            val flower = Path().apply {
                for (i in 0..300) {
                    val degrees = (i / 300f) * 360f
                    val theta = degrees * PI / 180
                    val distance = (100 * sin(theta * 4)).toFloat()

                    val x = origin.x + distance * cos(theta).toFloat()
                    val y = origin.y + distance * sin(theta).toFloat()

                    if (i == 0) moveTo(x, y) else lineTo(x, y)
                }
                close()
            }

            pathMeasure.setPath(flower, false)
            val totalLength = pathMeasure.length
            val drawLength = totalLength * flowerProgress.value

            drawPath(
                path = flower,
                color = TropicalTeal,
                style = Stroke(
                    width = 2.2.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(
                        intervals = floatArrayOf(drawLength, totalLength),
                        phase = 0f
                    )
                )
            )
        }

        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = TropicalTeal)) { append("M") }
                withStyle(style = SpanStyle(color = CoralBlaze)) { append("O") }
                withStyle(style = SpanStyle(color = CoralBlaze)) { append("O") }
                withStyle(style = SpanStyle(color = MidnightIndigo)) { append("D") }
                withStyle(style = SpanStyle(color = HoneyGold)) { append("Y") }
            },
            style = TextStyle(
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = TropicalTeal,
                fontFamily = MadeSoulMaze
            ),
            modifier = Modifier.graphicsLayer {
                scaleX = moodyScale.value
                scaleY = moodyScale.value
                translationX = moodyOffsetX.value
            }
        )

        Text(
            text = "Notes\nApp",
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MidnightIndigo,
                fontFamily = MadeSoulMaze,
                lineHeight = (20).sp,
                letterSpacing = 5.sp
            ),
            modifier = Modifier.graphicsLayer {
                translationX = notesAppOffsetX.value
                alpha = notesAppAlpha.value
            }
        )

    }
}

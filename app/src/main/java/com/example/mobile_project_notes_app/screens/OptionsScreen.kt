package com.example.mobile_project_notes_app.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobile_project_notes_app.R
import kotlinx.coroutines.delay
import kotlin.math.roundToInt
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavController
import com.example.mobile_project_notes_app.navigation.Route
import com.example.mobile_project_notes_app.ui.theme.AppColors

@Composable
fun OptionsScreen(
    isMenuOpen: Boolean,
    onMenuOpenChange: (Boolean) -> Unit,
    onNavigate: (Route) -> Unit
) {
    Scaffold(
        modifier = Modifier
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        AppColors.actionGradientStart,
                        AppColors.actionGradientEnd
                    )
                )
            ),
        containerColor = Color.Transparent
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(
                    vertical = 24.dp,
                    horizontal = 32.dp
                )
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AnimatedGlassyIcon(
                    icon = painterResource(R.drawable.profile_ico),
                    contentDescription = "profile-icon",
                    isMenuOpen = isMenuOpen,
                    fromLeft = true
                )
                AnimatedGlassyIcon(
                    modifier = Modifier.clickable(onClick = { onMenuOpenChange(false) }),
                    icon = painterResource(R.drawable.close_ico),
                    contentDescription = "close-icon",
                    isMenuOpen = isMenuOpen,
                    fromLeft = false
                )
            }

            Spacer(modifier = Modifier.padding(vertical = 24.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Option(
                    icon = painterResource(R.drawable.home_ico),
                    label = "Home",
                    index = 0,
                    isMenuOpen = isMenuOpen,
                    onClick = {
                        onNavigate(Route.HomeScreen)
                        onMenuOpenChange(false)
                    }
                )
                Option(
                    icon = painterResource(R.drawable.calendar_ico),
                    label = "Calendar",
                    index = 1,
                    isMenuOpen = isMenuOpen,
                    onClick = {
                        onNavigate(Route.CalendarScreen)
                        onMenuOpenChange(false)
                    }
                )

                Option(
                    icon = painterResource(R.drawable.logout_ico),
                    label = "Exit",
                    index = 2,
                    isMenuOpen = isMenuOpen,
                    onClick = { kotlin.system.exitProcess(0) }
                )
            }
        }
    }
}

@Composable
fun Option(
    icon: Painter,
    label: String,
    index: Int,
    isMenuOpen: Boolean,
    onClick: () -> Unit = {}
) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    val screenWidthDp = configuration.screenWidthDp.dp
    val startOffsetX = with(density) { -screenWidthDp.toPx() }

    val offsetX = remember(startOffsetX) { Animatable(startOffsetX) }

    LaunchedEffect(isMenuOpen) {
        if (isMenuOpen) {
            delay(index * 20L)
            offsetX.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 400)
            )
        } else {
            offsetX.snapTo(startOffsetX)
        }
    }

    Row(
        modifier = Modifier
            .offset {
                IntOffset(offsetX.value.roundToInt(), 0)
            }
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(28.dp),
            painter = icon,
            contentDescription = label,
            tint = Color.White
        )
        Spacer(modifier = Modifier.padding(horizontal = 6.dp))
        Text(
            text = label,
            color = Color.White,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        )
    }
}

@Composable
fun AnimatedGlassyIcon(
    icon: Painter,
    contentDescription: String?,
    isMenuOpen: Boolean,
    fromLeft: Boolean,
    modifier: Modifier = Modifier,
    size: Dp = 54.dp,
    iconSize: Dp = 20.dp
) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    val screenWidthDp = configuration.screenWidthDp.dp
    val startOffsetX = with(density) {
        if (fromLeft) -screenWidthDp.toPx() else screenWidthDp.toPx()
    }

    val offsetX = remember(startOffsetX) { Animatable(startOffsetX) }

    LaunchedEffect(isMenuOpen) {
        if (isMenuOpen) {
            offsetX.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 400)
            )
        } else {
            offsetX.animateTo(
                targetValue = startOffsetX,
                animationSpec = tween(durationMillis = 400)
            )
        }
    }

    Box(
        modifier = modifier
            .size(size)
            .offset {
                IntOffset(offsetX.value.roundToInt(), 0)
            },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.2f))
                .blur(20.dp)
                .border(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.25f),
                    shape = CircleShape
                )
        )

        Icon(
            painter = icon,
            contentDescription = contentDescription,
            tint = Color.White,
            modifier = Modifier.size(iconSize)
        )
    }
}

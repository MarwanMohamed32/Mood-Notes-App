package com.example.mobile_project_notes_app.screens

import android.R.attr.scaleX
import android.R.attr.translationX
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mobile_project_notes_app.screens.home.HomeScreen
import com.example.mobile_project_notes_app.screens.options.OptionsScreen
import kotlinx.coroutines.delay

@Composable
fun MainScreen(navController: NavController) {
    var isMenuOpen by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(1000)
        isMenuOpen = true
    }


    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val cornerRadius by animateDpAsState(
        targetValue = if (isMenuOpen) 30.dp else 0.dp,
        animationSpec = tween(400),
        label = "cornerRadius"
    )

    val offsetX by animateFloatAsState(
        targetValue = if (isMenuOpen) screenWidth.value * 1.7f else 0f,
        animationSpec = tween(durationMillis = 400),
        label = "offsetX"
    )

    val scale by animateFloatAsState(
        targetValue = if (isMenuOpen) 0.67f else 1f,
        animationSpec = tween(durationMillis = 400),
        label = "scale"
    )

    Box {
        OptionsScreen(
            isMenuOpen,
            onMenuOpenChange = { isMenuOpen = it })
        Box(
            modifier = Modifier
                .graphicsLayer {
                    translationX = offsetX
                    scaleX = scale
                    scaleY = scale
                }
                .clip(RoundedCornerShape(cornerRadius))

        ) {
            HomeScreen()
        }
    }
}
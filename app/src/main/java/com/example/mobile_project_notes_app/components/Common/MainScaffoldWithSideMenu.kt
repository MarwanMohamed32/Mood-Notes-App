package com.example.mobile_project_notes_app.components.Common

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mobile_project_notes_app.navigation.Route
import com.example.mobile_project_notes_app.screens.OptionsScreen

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun MainScaffoldWithSideMenu(
    isMenuOpen: Boolean,
    onMenuOpenChange: (Boolean) -> Unit,
    onNavigate: (Route) -> Unit,
    onBackClick: () -> Unit = {},
    frontContent: @Composable (onMenuClick: () -> Unit) -> Unit,
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val toggleMenu = { onMenuOpenChange(!isMenuOpen) }

    val cornerRadius by animateDpAsState(
        targetValue = if (isMenuOpen) 30.dp else 0.dp,
        animationSpec = tween(400),
        label = "cornerRadius"
    )

    val offsetX by animateFloatAsState(
        targetValue = if (isMenuOpen) screenWidth.value * 1.7f else 0f,
        animationSpec = tween(400),
        label = "offsetX"
    )

    val scale by animateFloatAsState(
        targetValue = if (isMenuOpen) 0.67f else 1f,
        animationSpec = tween(400),
        label = "scale"
    )

    Box {
        OptionsScreen(
            isMenuOpen = isMenuOpen,
            onMenuOpenChange = onMenuOpenChange,
            onNavigate = onNavigate,
        )

        Box(
            modifier = Modifier
                .graphicsLayer {
                    translationX = offsetX
                    scaleX = scale
                    scaleY = scale
                }
                .clip(RoundedCornerShape(cornerRadius))
        ) {

            frontContent(toggleMenu)
        }
    }
}

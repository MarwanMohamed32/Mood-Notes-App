import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun BorderedCircledIcon(
    modifier: Modifier = Modifier,
    navController: NavController,
    iconPainter: Painter,
    description: String,
    iconSize: Dp,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .size(48.dp)
            .border(2.dp, Color.LightGray, CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = iconPainter,
            contentDescription = description,
            tint = Color.Unspecified,
            modifier = Modifier.size(iconSize)
        )
    }
}
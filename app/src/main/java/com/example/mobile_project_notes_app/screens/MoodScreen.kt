package com.example.mobile_project_notes_app.screens

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mobile_project_notes_app.components.MoodComponents.CheckpointSlider
import com.example.mobile_project_notes_app.components.MoodComponents.EmojiFace
import com.example.mobile_project_notes_app.components.MoodComponents.MoodSubmitButton
import com.example.mobile_project_notes_app.viewmodel.CalendarViewModel
import org.koin.androidx.compose.koinViewModel

enum class Mood {
    Good, NotBad, Bad
}

private data class MoodStyle(
    val bgColor: Color,
    val textBg: Color,
    val faceColor: Color,
    val textColor: Color,
    val sliderColor: Color,

    val eyeWidth: Dp,
    val eyeHeight: Dp,
    val mouthHeight: Float,
    val mouthWidth: Float,
    val topLeftWidth: Float,
    val topLeftHeight: Float,
    val startAngle: Float,
    val sweepAngle: Float,
    val space: Dp,
)

private fun Mood.style(): MoodStyle = when (this) {
    Mood.Good -> MoodStyle(
        bgColor = Color(0xFFA8BE4F),
        textBg = Color(0xFFB9CB72),
        faceColor = Color(0xFF133000),
        textColor = Color(0xff748F1E),
        sliderColor = Color(0xff748F1E).copy(alpha = 0.5f),

        eyeWidth = 150.dp,
        eyeHeight = 150.dp,
        mouthHeight = 0.6f,
        mouthWidth = 0.8f,
        topLeftWidth = 0.1f,
        topLeftHeight = -0.4f,
        startAngle = 15f,
        sweepAngle = 150f,
        space = 12.dp
    )

    Mood.NotBad -> MoodStyle(
        bgColor = Color(0xFFDDA03A),
        textBg = Color(0xFFE4B361).copy(alpha = 0.4f),
        faceColor = Color(0xFF4A1F04),
        textColor = Color(0xFFB7770F),
        sliderColor = Color(0xFFB7770F).copy(alpha = 0.5f),

        eyeWidth = 100.dp,
        eyeHeight = 40.dp,
        mouthHeight = 0.4f,
        mouthWidth = 0.6f,
        topLeftWidth = 0.22f,
        topLeftHeight = -0.2f,
        startAngle = -15f,
        sweepAngle = -150f,
        space = 32.dp
    )

    Mood.Bad -> MoodStyle(
        bgColor = Color(0xFFFF775B),
        textBg = Color(0xFFFF927C).copy(alpha = 0.4f),
        faceColor = Color(0xFF750A00),
        textColor = Color(0xFFDD3F22),
        sliderColor = Color(0xFFDD3F22).copy(alpha = 0.5f),

        eyeWidth = 50.dp,
        eyeHeight = 50.dp,
        mouthHeight = 0.4f,
        mouthWidth = 0.6f,
        topLeftWidth = 0.22f,
        topLeftHeight = -0.2f,
        startAngle = -15f,
        sweepAngle = -150f,
        space = 32.dp
    )
}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun MoodScreen(onNavigate: () -> Unit, calendarViewModel: CalendarViewModel = koinViewModel()) {
    val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp

    var opened by remember { mutableStateOf(false) }
    var ending by remember { mutableStateOf(false) }
    var currentIndex by remember { mutableIntStateOf(0) }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(opened) {
        if (opened) {
            focusRequester.requestFocus()
        }
    }

    val mood = when (currentIndex) {
        0 -> Mood.Good
        1 -> Mood.NotBad
        2 -> Mood.Bad
        else -> Mood.Good
    }

    val style = mood.style()

    // ---------- Animated Colors ----------
    val bgColor by animateColorAsState(
        style.bgColor,
        tween(600), label = "bgColor"
    )
    val textBg by animateColorAsState(
        style.textBg,
        tween(600), label = "textBg"
    )
    val faceColor by animateColorAsState(
        style.faceColor,
        tween(600), label = "faceColor"
    )
    val textColor by animateColorAsState(
        style.textColor,
        tween(600), label = "textColor"
    )
    val sliderColor by animateColorAsState(
        style.sliderColor,
        tween(600), label = "sliderColor"
    )

    // ---------- Open/Close Animations ----------
    val offsetTitle by animateDpAsState(
        targetValue = if (opened) (-200).dp else 0.dp,
        animationSpec = tween(600),
        label = "offsetTitle"
    )
    val titleHeight by animateDpAsState(
        targetValue = if (opened) 0.dp else 80.dp,
        animationSpec = tween(600),
        label = "titleHeight"
    )
    val allMoodTextHeight by animateDpAsState(
        targetValue = if (opened) 0.dp else 70.dp,
        animationSpec = tween(600),
        label = "allMoodTextHeight"
    )
    val offsetAllMoodText by animateDpAsState(
        targetValue = if (opened) {
            when (mood) {
                Mood.Bad -> screenWidthDp * -5
                else -> screenWidthDp * 5
            }
        } else 0.dp,
        animationSpec = tween(600),
        label = "offsetAllMoodText"
    )
    val opacityAllMoodText by animateFloatAsState(
        targetValue = if (opened) 0f else 1f,
        animationSpec = tween(600),
        label = "opacityAllMoodText"
    )
    val offsetSlider by animateDpAsState(
        targetValue = if (opened) (-500).dp else 0.dp,
        animationSpec = tween(600),
        label = "offsetSlider"
    )
    val sliderHeight by animateDpAsState(
        targetValue = if (opened) 0.dp else 100.dp,
        animationSpec = tween(600),
        label = "sliderHeight"
    )
    val opacitySlider by animateFloatAsState(
        targetValue = if (opened) 0f else 1f,
        animationSpec = tween(600),
        label = "opacitySlider"
    )

    val offsetSubmitBtn by animateDpAsState(
        targetValue = if (opened) (-500).dp else 0.dp,
        animationSpec = tween(600),
        label = "offsetSubmitBtn"
    )
    val submitBtnHeight by animateDpAsState(
        targetValue = if (opened) 0.dp else 60.dp,
        animationSpec = tween(600),
        label = "submitBtnHeight"
    )
    val opacitySubmitBtn by animateFloatAsState(
        targetValue = if (opened) 0f else 1f,
        animationSpec = tween(600),
        label = "opacitySubmitBtn"
    )

    // ---------- Ending Param Animations ----------
    val buttonWidth by animateDpAsState(
        targetValue = if (ending) 370.dp else 130.dp,
        animationSpec = tween(600),
        label = "buttonWidth"
    )
    val endingTextHeight by animateDpAsState(
        targetValue = if (ending) 200.dp else 0.dp,
        animationSpec = tween(600),
        label = "endingTextHeight"
    )

    val endingTextOffset by animateDpAsState(
        targetValue = if (ending) 0.dp else (200).dp,
        animationSpec = tween(600),
        label = "endingTextOffset"
    )

    val noteTextFieldHeight by animateDpAsState(
        targetValue = if (ending) 170.dp else 170.dp,
        animationSpec = tween(600),
        label = "endingTextHeight"
    )

    val noteTextFieldOffset by animateDpAsState(
        targetValue = if (ending) 500.dp else 0.dp,
        animationSpec = tween(600),
        label = "endingTextOffset"
    )
    val opacityNoteTextField by animateFloatAsState(
        targetValue = if (ending) 0f else 1f,
        animationSpec = tween(200),
        label = "opacityEndingText"
    )

    var isSubmitted by remember { mutableStateOf(false) }


    // ---------- Face Param Animations ----------
    val eyeWidth by animateDpAsState(
        style.eyeWidth,
        tween(600), label = "eyeWidth"
    )
    val eyeHeight by animateDpAsState(
        style.eyeHeight,
        tween(600), label = "eyeHeight"
    )
    val mouthHeight by animateFloatAsState(
        style.mouthHeight,
        tween(600), label = "mouthHeight"
    )
    val mouthWidth by animateFloatAsState(
        style.mouthWidth,
        tween(600), label = "mouthWidth"
    )
    val topLeftWidth by animateFloatAsState(
        style.topLeftWidth,
        tween(600), label = "topLeftWidth"
    )
    val topLeftHeight by animateFloatAsState(
        style.topLeftHeight,
        tween(600),
        label = "topLeftHeight"
    )
    val startAngle by animateFloatAsState(
        style.startAngle,
        tween(600), label = "startAngle"
    )
    val sweepAngle by animateFloatAsState(
        style.sweepAngle,
        tween(600), label = "sweepAngle"
    )
    val space by animateDpAsState(
        style.space,
        tween(600), label = "space"
    )

    // ---------- Text Sliding Offsets ----------
    val textOffsets = rememberTextOffsets(currentIndex, screenWidthDp)

    val checkpoints = remember { listOf("Good", "Not Bad", "Bad") }

    Scaffold(
        containerColor = bgColor,
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 18.dp)
                .fillMaxSize(),
            verticalArrangement = if (opened) Arrangement.spacedBy(8.dp) else Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            MoodTopBar(faceColor)

            Text(
                text = "How Was Your Mood Today?\nWrite a Note! :D",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp,
                modifier = Modifier
                    .offset(y = offsetTitle)
                    .height(titleHeight)
            )

            Spacer(modifier = Modifier.padding(vertical = 24.dp))

            EmojiFace(
                faceColor = faceColor,
                offsetEyeWidth = eyeWidth,
                offsetEyeHeight = eyeHeight,
                offsetSpace = space,
                offsetMouthWidth = mouthWidth,
                offsetMouthHeight = mouthHeight,
                offsetStartAngle = startAngle,
                offsetSweepAngle = sweepAngle,
                offsetTopLeftWidth = topLeftWidth,
                offsetTopLeftHeight = topLeftHeight
            )

            MoodTextRow(
                height = allMoodTextHeight,
                alpha = opacityAllMoodText,
                offsetX = offsetAllMoodText,
                textColor = textColor,
                offsets = textOffsets
            )

            CheckpointSlider(
                checkpoints = checkpoints,
                currentIndex = currentIndex,
                sliderColor = sliderColor,
                circleColor = faceColor,
                onCheckpointClick = { currentIndex = it },
                modifier = Modifier
                    .offset(offsetSlider)
                    .height(sliderHeight)
                    .alpha(opacitySlider)
            )

            AddNoteBtn(
                sliderColor = sliderColor,
                faceColor = faceColor,
                onClick = { opened = true },
                modifier = Modifier
                    .offset(y = offsetSubmitBtn)
                    .height(submitBtnHeight)
                    .alpha(
                        opacitySubmitBtn
                    )
            )

            if (opened) {
                if (ending) {
                    Column(
                        modifier = Modifier
                            .width(370.dp)
                            .height(endingTextHeight)
                            .offset(y = endingTextOffset),
                        verticalArrangement = Arrangement.spacedBy(18.dp)
                    ) {
                        Text(
                            text = "Thank you for your feedback!",
                            fontWeight = FontWeight.W900,
                            fontSize = 42.sp,
                            lineHeight = 42.sp,
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = when (mood) {
                                Mood.Good -> "Love to hear that! We’re glad things are going well — keep it up!"
                                Mood.NotBad -> "Thanks for checking in. Hope the rest of your day gets a little brighter."
                                Mood.Bad -> "Sorry it’s been tough. Thanks for telling us — we’ll keep working to do better."
                            },
                            fontWeight = FontWeight.W700,
                            fontSize = 18.sp,
                            lineHeight = 18.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                val note = rememberTextFieldState("")
                Box(
                    modifier = Modifier
                        .width(370.dp)
                        .height(170.dp)
                ) {
                    TextField(
                        state = note, modifier = Modifier
                            .clip(RoundedCornerShape(28.dp))
                            .alpha(opacityNoteTextField)
                            .border(2.dp, faceColor, RoundedCornerShape(28.dp))
                            .fillMaxWidth()
                            .height(noteTextFieldHeight)
                            .offset(noteTextFieldOffset),
                        placeholder = { Text(text = "Add a Note") },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = textBg,
                            unfocusedContainerColor = textBg,
                            disabledContainerColor = textBg,
                            focusedPlaceholderColor = faceColor,
                            unfocusedPlaceholderColor = faceColor
                        )
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(8.dp)
                    ) {
                        MoodSubmitButton(
                            label = if (isSubmitted) "Continue to Home Page" else "Submit",
                            faceColor = faceColor,
                            onClick = {
                                if (!isSubmitted) {
                                    val moodString = when (mood) {
                                        Mood.Good -> "Good"
                                        Mood.NotBad -> "NotBad"
                                        Mood.Bad -> "Bad"
                                    }
                                    calendarViewModel.addMoodEntry(
                                        mood = moodString,
                                        note = note.text.toString()
                                    )
                                    ending = true
                                    isSubmitted = true
                                } else {
                                    onNavigate()
                                }
                            },
                            modifier = Modifier.width(buttonWidth),
                            horizontalArrangement = if (ending) Arrangement.Center else Arrangement.SpaceBetween
                        )
                    }
                }
            }
        }
    }
}

// -------------------- Helpers / Small Composables --------------------

@Composable
private fun MoodTopBar(faceColor: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        CircleIconButton(
            faceColor = faceColor,
            icon = Icons.Default.Close,
            contentDesc = "Close",
            onClick = {}
        )
        CircleIconButton(
            faceColor = faceColor,
            icon = Icons.Outlined.Info,
            contentDesc = "Info",
            onClick = {}
        )
    }
}

@Composable
private fun CircleIconButton(
    faceColor: Color,
    icon: ImageVector,
    contentDesc: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(60.dp)
            .clip(CircleShape)
            .background(faceColor.copy(alpha = 0.1f)),
        contentAlignment = Alignment.Center
    ) {
        IconButton(onClick = onClick) {
            Icon(
                imageVector = icon,
                contentDescription = contentDesc,
                modifier = Modifier.size(28.dp),
                tint = faceColor
            )
        }
    }
}

@Composable
private fun MoodTextRow(
    height: Dp,
    alpha: Float,
    offsetX: Dp,
    textColor: Color,
    offsets: Triple<Dp, Dp, Dp>
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .alpha(alpha)
            .offset(x = offsetX),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Good",
            fontSize = 70.sp,
            fontWeight = FontWeight.ExtraBold,
            color = textColor,
            modifier = Modifier.offset(x = offsets.first)
        )
        Text(
            text = "Not Bad",
            fontSize = 70.sp,
            fontWeight = FontWeight.ExtraBold,
            color = textColor,
            modifier = Modifier.offset(x = offsets.second)
        )
        Text(
            text = "Bad",
            fontSize = 70.sp,
            fontWeight = FontWeight.ExtraBold,
            color = textColor,
            modifier = Modifier.offset(x = offsets.third)
        )
    }
}

@Composable
private fun AddNoteBtn(
    sliderColor: Color,
    faceColor: Color,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .width(350.dp)
            .height(60.dp)
            .background(sliderColor)
            .padding(start = 26.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Add Note",
                color = faceColor,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )

            MoodSubmitButton(
                label = "Submit",
                faceColor = faceColor,
                onClick = onClick,
                modifier = Modifier,
                horizontalArrangement = Arrangement.SpaceBetween
            )

        }
    }
}

@Composable
private fun rememberTextOffsets(
    currentIndex: Int,
    screenWidthDp: Dp
): Triple<Dp, Dp, Dp> {
    val first by animateDpAsState(
        targetValue = when (currentIndex) {
            0 -> 0.dp
            1 -> -screenWidthDp
            2 -> -screenWidthDp * 2
            else -> 0.dp
        },
        animationSpec = tween(600),
        label = "firstIdx"
    )
    val second by animateDpAsState(
        targetValue = when (currentIndex) {
            0 -> screenWidthDp
            1 -> 0.dp
            2 -> -screenWidthDp
            else -> 0.dp
        },
        animationSpec = tween(600),
        label = "secondIdx"
    )
    val third by animateDpAsState(
        targetValue = when (currentIndex) {
            0 -> screenWidthDp * 2
            1 -> screenWidthDp
            2 -> 0.dp
            else -> 0.dp
        },
        animationSpec = tween(600),
        label = "thirdIdx"
    )

    return Triple(first, second, third)
}

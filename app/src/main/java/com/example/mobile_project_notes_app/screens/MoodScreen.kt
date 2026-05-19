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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobile_project_notes_app.components.MoodComponents.CheckpointSlider
import com.example.mobile_project_notes_app.components.MoodComponents.EmojiFace
import com.example.mobile_project_notes_app.components.MoodComponents.MoodSubmitButton
import com.example.mobile_project_notes_app.features.feature_calendar.presentation.CalendarViewModel
import com.example.mobile_project_notes_app.ui.theme.AppColors
import org.koin.androidx.compose.koinViewModel

// -------------------- Mood Model & Styles --------------------

enum class Mood(val label: String, val feedbackMessage: String) {
    Good("Good", "Love to hear that! We're glad things are going well — keep it up!"),
    NotBad("Not Bad", "Thanks for checking in. Hope the rest of your day gets a little brighter."),
    Bad("Bad", "Sorry it's been tough. Thanks for telling us — we'll keep working to do better.");

    companion object {
        fun fromIndex(index: Int) = entries.getOrElse(index) { Good }
    }
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
        bgColor = AppColors.Mood.goodBackground, textBg = AppColors.Mood.goodTextBackground,
        faceColor = AppColors.Mood.goodFace, textColor = AppColors.Mood.goodText,
        sliderColor = AppColors.Mood.goodText.copy(alpha = 0.5f),
        eyeWidth = 150.dp, eyeHeight = 150.dp,
        mouthHeight = 0.6f, mouthWidth = 0.8f,
        topLeftWidth = 0.1f, topLeftHeight = -0.4f,
        startAngle = 15f, sweepAngle = 150f, space = 12.dp
    )
    Mood.NotBad -> MoodStyle(
        bgColor = AppColors.Mood.notBadBackground, textBg = AppColors.Mood.notBadTextBackground.copy(alpha = 0.4f),
        faceColor = AppColors.Mood.notBadFace, textColor = AppColors.Mood.notBadText,
        sliderColor = AppColors.Mood.notBadText.copy(alpha = 0.5f),
        eyeWidth = 100.dp, eyeHeight = 40.dp,
        mouthHeight = 0.4f, mouthWidth = 0.6f,
        topLeftWidth = 0.22f, topLeftHeight = -0.2f,
        startAngle = -15f, sweepAngle = -150f, space = 32.dp
    )
    Mood.Bad -> MoodStyle(
        bgColor = AppColors.Mood.badBackground, textBg = AppColors.Mood.badTextBackground.copy(alpha = 0.4f),
        faceColor = AppColors.Mood.badFace, textColor = AppColors.Mood.badText,
        sliderColor = AppColors.Mood.badText.copy(alpha = 0.5f),
        eyeWidth = 50.dp, eyeHeight = 50.dp,
        mouthHeight = 0.4f, mouthWidth = 0.6f,
        topLeftWidth = 0.22f, topLeftHeight = -0.2f,
        startAngle = -15f, sweepAngle = -150f, space = 32.dp
    )
}

// -------------------- Animation Helpers --------------------

private const val ANIM_DURATION = 600

@Composable
private fun animDp(target: Dp, label: String) =
    animateDpAsState(target, tween(ANIM_DURATION), label = label)

@Composable
private fun animFloat(target: Float, label: String) =
    animateFloatAsState(target, tween(ANIM_DURATION), label = label)

@Composable
private fun animColor(target: Color, label: String) =
    animateColorAsState(target, tween(ANIM_DURATION), label = label)

/** All animated values derived from the current [MoodStyle]. */
private data class AnimatedMoodColors(
    val bgColor: Color,
    val textBg: Color,
    val faceColor: Color,
    val textColor: Color,
    val sliderColor: Color,
)

@Composable
private fun animateMoodColors(style: MoodStyle): AnimatedMoodColors {
    val bg by animColor(style.bgColor, "bgColor")
    val textBg by animColor(style.textBg, "textBg")
    val face by animColor(style.faceColor, "faceColor")
    val text by animColor(style.textColor, "textColor")
    val slider by animColor(style.sliderColor, "sliderColor")
    return AnimatedMoodColors(bg, textBg, face, text, slider)
}

private data class AnimatedFaceParams(
    val eyeWidth: Dp, val eyeHeight: Dp,
    val mouthHeight: Float, val mouthWidth: Float,
    val topLeftWidth: Float, val topLeftHeight: Float,
    val startAngle: Float, val sweepAngle: Float,
    val space: Dp,
)

@Composable
private fun animateFaceParams(style: MoodStyle): AnimatedFaceParams {
    val ew by animDp(style.eyeWidth, "eyeWidth")
    val eh by animDp(style.eyeHeight, "eyeHeight")
    val mh by animFloat(style.mouthHeight, "mouthHeight")
    val mw by animFloat(style.mouthWidth, "mouthWidth")
    val tlw by animFloat(style.topLeftWidth, "topLeftWidth")
    val tlh by animFloat(style.topLeftHeight, "topLeftHeight")
    val sa by animFloat(style.startAngle, "startAngle")
    val sw by animFloat(style.sweepAngle, "sweepAngle")
    val sp by animDp(style.space, "space")
    return AnimatedFaceParams(ew, eh, mh, mw, tlw, tlh, sa, sw, sp)
}

/** Slide-away animation values when [opened] is true. */
private data class SlideAwayState(
    val titleOffset: Dp, val titleHeight: Dp,
    val moodTextHeight: Dp, val moodTextOffset: Dp, val moodTextAlpha: Float,
    val sliderOffset: Dp, val sliderHeight: Dp, val sliderAlpha: Float,
    val btnOffset: Dp, val btnHeight: Dp, val btnAlpha: Float,
)

@Composable
private fun animateSlideAway(opened: Boolean, mood: Mood, screenWidth: Dp): SlideAwayState {
    val titleOff by animDp(if (opened) (-200).dp else 0.dp, "offsetTitle")
    val titleH by animDp(if (opened) 0.dp else 80.dp, "titleHeight")
    val mtH by animDp(if (opened) 0.dp else 70.dp, "moodTextHeight")
    val mtOff by animDp(
        if (opened) screenWidth * if (mood == Mood.Bad) -5 else 5 else 0.dp,
        "moodTextOffset"
    )
    val mtA by animFloat(if (opened) 0f else 1f, "moodTextAlpha")
    val slOff by animDp(if (opened) (-500).dp else 0.dp, "sliderOffset")
    val slH by animDp(if (opened) 0.dp else 100.dp, "sliderHeight")
    val slA by animFloat(if (opened) 0f else 1f, "sliderAlpha")
    val btnOff by animDp(if (opened) (-500).dp else 0.dp, "btnOffset")
    val btnH by animDp(if (opened) 0.dp else 60.dp, "btnHeight")
    val btnA by animFloat(if (opened) 0f else 1f, "btnAlpha")
    return SlideAwayState(titleOff, titleH, mtH, mtOff, mtA, slOff, slH, slA, btnOff, btnH, btnA)
}

// -------------------- Main Screen --------------------

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun MoodScreen(onNavigate: () -> Unit, calendarViewModel: CalendarViewModel = koinViewModel()) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    var opened by remember { mutableStateOf(false) }
    var ending by remember { mutableStateOf(false) }
    var currentIndex by remember { mutableIntStateOf(0) }
    var isSubmitted by remember { mutableStateOf(false) }

    val mood = Mood.fromIndex(currentIndex)
    val style = mood.style()
    val colors = animateMoodColors(style)
    val face = animateFaceParams(style)
    val slide = animateSlideAway(opened, mood, screenWidth)
    val textOffsets = rememberTextOffsets(currentIndex, screenWidth)

    // Ending animations
    val buttonWidth by animDp(if (ending) 370.dp else 130.dp, "buttonWidth")
    val endingTextHeight by animDp(if (ending) 200.dp else 0.dp, "endingH")
    val endingTextOffset by animDp(if (ending) 0.dp else 200.dp, "endingOff")
    val noteFieldOffset by animDp(if (ending) 500.dp else 0.dp, "noteOff")
    val noteFieldAlpha by animateFloatAsState(
        if (ending) 0f else 1f, tween(200), label = "noteAlpha"
    )

    Scaffold(
        containerColor = colors.bgColor,
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
            MoodTopBar(colors.faceColor)

            Text(
                text = "How Was Your Mood Today?\nWrite a Note! :D",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp,
                modifier = Modifier
                    .offset(y = slide.titleOffset)
                    .height(slide.titleHeight)
            )

            Spacer(modifier = Modifier.padding(vertical = 24.dp))

            EmojiFace(
                faceColor = colors.faceColor,
                offsetEyeWidth = face.eyeWidth, offsetEyeHeight = face.eyeHeight,
                offsetSpace = face.space,
                offsetMouthWidth = face.mouthWidth, offsetMouthHeight = face.mouthHeight,
                offsetStartAngle = face.startAngle, offsetSweepAngle = face.sweepAngle,
                offsetTopLeftWidth = face.topLeftWidth, offsetTopLeftHeight = face.topLeftHeight
            )

            MoodTextRow(
                height = slide.moodTextHeight,
                alpha = slide.moodTextAlpha,
                offsetX = slide.moodTextOffset,
                textColor = colors.textColor,
                offsets = textOffsets
            )

            CheckpointSlider(
                checkpoints = Mood.entries.map { it.label },
                currentIndex = currentIndex,
                sliderColor = colors.sliderColor,
                circleColor = colors.faceColor,
                onCheckpointClick = { currentIndex = it },
                modifier = Modifier
                    .offset(slide.sliderOffset)
                    .height(slide.sliderHeight)
                    .alpha(slide.sliderAlpha)
            )

            AddNoteBtn(
                sliderColor = colors.sliderColor,
                faceColor = colors.faceColor,
                onClick = { opened = true },
                modifier = Modifier
                    .offset(y = slide.btnOffset)
                    .height(slide.btnHeight)
                    .alpha(slide.btnAlpha)
            )

            if (opened) {
                NoteInputSection(
                    mood = mood,
                    colors = colors,
                    ending = ending,
                    isSubmitted = isSubmitted,
                    buttonWidth = buttonWidth,
                    endingTextHeight = endingTextHeight,
                    endingTextOffset = endingTextOffset,
                    noteFieldOffset = noteFieldOffset,
                    noteFieldAlpha = noteFieldAlpha,
                    onSubmit = { noteText ->
                        calendarViewModel.addMoodEntry(mood = mood.name, note = noteText)
                        ending = true
                        isSubmitted = true
                    },
                    onNavigate = onNavigate
                )
            }
        }
    }
}

// -------------------- Extracted Composables --------------------

@Composable
private fun NoteInputSection(
    mood: Mood,
    colors: AnimatedMoodColors,
    ending: Boolean,
    isSubmitted: Boolean,
    buttonWidth: Dp,
    endingTextHeight: Dp,
    endingTextOffset: Dp,
    noteFieldOffset: Dp,
    noteFieldAlpha: Float,
    onSubmit: (String) -> Unit,
    onNavigate: () -> Unit,
) {
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
                text = mood.feedbackMessage,
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
            state = note,
            modifier = Modifier
                .clip(RoundedCornerShape(28.dp))
                .alpha(noteFieldAlpha)
                .border(2.dp, colors.faceColor, RoundedCornerShape(28.dp))
                .fillMaxWidth()
                .height(170.dp)
                .offset(noteFieldOffset),
            placeholder = { Text(text = "Add a Note") },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = colors.textBg,
                unfocusedContainerColor = colors.textBg,
                disabledContainerColor = colors.textBg,
                focusedPlaceholderColor = colors.faceColor,
                unfocusedPlaceholderColor = colors.faceColor
            )
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(8.dp)
        ) {
            MoodSubmitButton(
                label = if (isSubmitted) "Continue to Home Page" else "Submit",
                faceColor = colors.faceColor,
                onClick = {
                    if (!isSubmitted) onSubmit(note.text.toString()) else onNavigate()
                },
                modifier = Modifier.width(buttonWidth),
                horizontalArrangement = if (ending) Arrangement.Center else Arrangement.SpaceBetween
            )
        }
    }
}

@Composable
private fun MoodTopBar(faceColor: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        CircleIconButton(faceColor, Icons.Default.Close, "Close") {}
        CircleIconButton(faceColor, Icons.Outlined.Info, "Info") {}
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
            Icon(icon, contentDesc, Modifier.size(28.dp), tint = faceColor)
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
    val labels = listOf("Good" to offsets.first, "Not Bad" to offsets.second, "Bad" to offsets.third)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .alpha(alpha)
            .offset(x = offsetX),
        contentAlignment = Alignment.Center
    ) {
        labels.forEach { (text, offset) ->
            Text(
                text = text,
                fontSize = 70.sp,
                fontWeight = FontWeight.ExtraBold,
                color = textColor,
                modifier = Modifier.offset(x = offset)
            )
        }
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
            Text("Add Note", color = faceColor, fontWeight = FontWeight.Medium, fontSize = 16.sp)
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
private fun rememberTextOffsets(currentIndex: Int, screenWidth: Dp): Triple<Dp, Dp, Dp> {
    val offsets = Mood.entries.map { mood ->
        val idx = mood.ordinal
        val target = when {
            idx < currentIndex -> -screenWidth * (currentIndex - idx)
            idx > currentIndex -> screenWidth * (idx - currentIndex)
            else -> 0.dp
        }
        animDp(target, "textOffset$idx").value
    }
    return Triple(offsets[0], offsets[1], offsets[2])
}

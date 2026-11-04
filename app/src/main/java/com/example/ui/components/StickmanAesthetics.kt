package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.data.model.StickmanType
import com.example.ui.theme.CyanNeon
import com.example.ui.theme.GoldAscent
import com.example.ui.theme.MintNeon
import com.example.ui.theme.TextSecondary

@Composable
fun StickmanIcon(
    type: StickmanType,
    modifier: Modifier = Modifier,
    tint: Color = CyanNeon,
    size: Dp = 48.dp
) {
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        when (type) {
            StickmanType.POSTURE_CHAD -> StickmanPostureCanvas(tint)
            StickmanType.DEBLOAT_WATER -> StickmanWaterCanvas(tint)
            StickmanType.MEWING_JAW -> StickmanMewingCanvas(tint)
            StickmanType.ICE_ROLLER -> StickmanIceRollerCanvas(tint)
            StickmanType.SLEEP_REST -> StickmanSleepCanvas(tint)
            StickmanType.WORKOUT_FRAME -> StickmanFrameCanvas(tint)
        }
    }
}

@Composable
fun StickmanPostureCanvas(tint: Color) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val strokeW = w * 0.08f

        // Head
        drawCircle(
            color = tint,
            radius = w * 0.15f,
            center = Offset(w * 0.5f, h * 0.22f),
            style = Stroke(width = strokeW)
        )

        // Spine (upright, confident)
        drawLine(
            color = tint,
            start = Offset(w * 0.5f, h * 0.37f),
            end = Offset(w * 0.5f, h * 0.65f),
            strokeWidth = strokeW,
            cap = StrokeCap.Round
        )

        // Broad Shoulders & Arms (Hands on hips posture)
        val leftArm = Path().apply {
            moveTo(w * 0.5f, h * 0.42f)
            lineTo(w * 0.25f, h * 0.48f)
            lineTo(w * 0.38f, h * 0.62f)
        }
        drawPath(
            path = leftArm,
            color = tint,
            style = Stroke(width = strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )

        val rightArm = Path().apply {
            moveTo(w * 0.5f, h * 0.42f)
            lineTo(w * 0.75f, h * 0.48f)
            lineTo(w * 0.62f, h * 0.62f)
        }
        drawPath(
            path = rightArm,
            color = tint,
            style = Stroke(width = strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )

        // Strong confident legs
        drawLine(
            color = tint,
            start = Offset(w * 0.5f, h * 0.65f),
            end = Offset(w * 0.32f, h * 0.92f),
            strokeWidth = strokeW,
            cap = StrokeCap.Round
        )
        drawLine(
            color = tint,
            start = Offset(w * 0.5f, h * 0.65f),
            end = Offset(w * 0.68f, h * 0.92f),
            strokeWidth = strokeW,
            cap = StrokeCap.Round
        )
    }
}

@Composable
fun StickmanWaterCanvas(tint: Color) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val strokeW = w * 0.08f

        // Head (tilted back slightly drinking)
        drawCircle(
            color = tint,
            radius = w * 0.14f,
            center = Offset(w * 0.42f, h * 0.24f),
            style = Stroke(width = strokeW)
        )

        // Body
        drawLine(
            color = tint,
            start = Offset(w * 0.42f, h * 0.38f),
            end = Offset(w * 0.42f, h * 0.68f),
            strokeWidth = strokeW,
            cap = StrokeCap.Round
        )

        // Arm holding water bottle up to mouth
        val arm = Path().apply {
            moveTo(w * 0.42f, h * 0.45f)
            lineTo(w * 0.65f, h * 0.40f)
            lineTo(w * 0.52f, h * 0.26f)
        }
        drawPath(arm, tint, style = Stroke(width = strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round))

        // Bottle outline
        drawRoundRect(
            color = MintNeon,
            topLeft = Offset(w * 0.52f, h * 0.16f),
            size = Size(w * 0.18f, h * 0.22f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f, 4f),
            style = Stroke(width = strokeW * 0.7f)
        )

        // Legs
        drawLine(color = tint, start = Offset(w * 0.42f, h * 0.68f), end = Offset(w * 0.32f, h * 0.92f), strokeWidth = strokeW, cap = StrokeCap.Round)
        drawLine(color = tint, start = Offset(w * 0.42f, h * 0.68f), end = Offset(w * 0.55f, h * 0.92f), strokeWidth = strokeW, cap = StrokeCap.Round)
    }
}

@Composable
fun StickmanMewingCanvas(tint: Color) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val strokeW = w * 0.08f

        // Profile head outline with sharp chiseled jawline
        val profile = Path().apply {
            moveTo(w * 0.40f, h * 0.15f) // top of skull
            cubicTo(w * 0.65f, h * 0.15f, w * 0.70f, h * 0.35f, w * 0.65f, h * 0.45f) // back of skull
            lineTo(w * 0.55f, h * 0.65f) // neck
            moveTo(w * 0.40f, h * 0.15f) // forehead
            lineTo(w * 0.25f, h * 0.32f) // brow
            lineTo(w * 0.20f, h * 0.40f) // nose tip
            lineTo(w * 0.26f, h * 0.45f) // under nose
            lineTo(w * 0.24f, h * 0.52f) // lips
            lineTo(w * 0.22f, h * 0.58f) // sharp chin
            lineTo(w * 0.45f, h * 0.58f) // chiseled jawline back to gonial angle
            lineTo(w * 0.46f, h * 0.75f) // front neck
        }
        drawPath(profile, tint, style = Stroke(width = strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round))

        // Stickman tongue resting against palate
        val tongue = Path().apply {
            moveTo(w * 0.30f, h * 0.50f)
            quadraticTo(w * 0.38f, h * 0.42f, w * 0.45f, h * 0.44f)
        }
        drawPath(tongue, MintNeon, style = Stroke(width = strokeW * 0.9f, cap = StrokeCap.Round))
    }
}

@Composable
fun StickmanIceRollerCanvas(tint: Color) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val strokeW = w * 0.08f

        // Stickman head
        drawCircle(
            color = tint,
            radius = w * 0.18f,
            center = Offset(w * 0.45f, h * 0.32f),
            style = Stroke(width = strokeW)
        )

        // Hand holding ice roller to cheek
        val arm = Path().apply {
            moveTo(w * 0.50f, h * 0.60f)
            lineTo(w * 0.75f, h * 0.52f)
            lineTo(w * 0.65f, h * 0.36f)
        }
        drawPath(arm, tint, style = Stroke(width = strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round))

        // Ice roller head on cheek
        drawRoundRect(
            color = MintNeon,
            topLeft = Offset(w * 0.55f, h * 0.26f),
            size = Size(w * 0.12f, h * 0.16f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f, 6f),
            style = Stroke(width = strokeW * 0.8f)
        )

        // Torso
        drawLine(
            color = tint,
            start = Offset(w * 0.45f, h * 0.50f),
            end = Offset(w * 0.45f, h * 0.85f),
            strokeWidth = strokeW,
            cap = StrokeCap.Round
        )
    }
}

@Composable
fun StickmanSleepCanvas(tint: Color) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val strokeW = w * 0.08f

        // Horizontal resting stickman
        // Pillow
        drawRoundRect(
            color = TextSecondary.copy(alpha = 0.4f),
            topLeft = Offset(w * 0.12f, h * 0.48f),
            size = Size(w * 0.26f, h * 0.18f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f, 6f),
            style = Stroke(width = strokeW * 0.6f)
        )

        // Head resting
        drawCircle(
            color = tint,
            radius = w * 0.14f,
            center = Offset(w * 0.24f, h * 0.48f),
            style = Stroke(width = strokeW)
        )

        // Flat relaxed spine
        drawLine(
            color = tint,
            start = Offset(w * 0.38f, h * 0.54f),
            end = Offset(w * 0.78f, h * 0.54f),
            strokeWidth = strokeW,
            cap = StrokeCap.Round
        )

        // Sleeping ZZZ
        val zPath = Path().apply {
            moveTo(w * 0.38f, h * 0.24f)
            lineTo(w * 0.48f, h * 0.24f)
            lineTo(w * 0.38f, h * 0.32f)
            lineTo(w * 0.48f, h * 0.32f)
        }
        drawPath(zPath, MintNeon, style = Stroke(width = strokeW * 0.6f, cap = StrokeCap.Round))
    }
}

@Composable
fun StickmanFrameCanvas(tint: Color) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val strokeW = w * 0.08f

        // Bar overhead
        drawLine(
            color = TextSecondary.copy(alpha = 0.5f),
            start = Offset(w * 0.1f, h * 0.12f),
            end = Offset(w * 0.9f, h * 0.12f),
            strokeWidth = strokeW * 0.8f,
            cap = StrokeCap.Round
        )

        // Arms reaching up to bar
        drawLine(color = tint, start = Offset(w * 0.42f, h * 0.32f), end = Offset(w * 0.28f, h * 0.12f), strokeWidth = strokeW, cap = StrokeCap.Round)
        drawLine(color = tint, start = Offset(w * 0.58f, h * 0.32f), end = Offset(w * 0.72f, h * 0.12f), strokeWidth = strokeW, cap = StrokeCap.Round)

        // Head between arms
        drawCircle(color = tint, radius = w * 0.13f, center = Offset(w * 0.5f, h * 0.24f), style = Stroke(width = strokeW))

        // Torso elongated
        drawLine(color = tint, start = Offset(w * 0.5f, h * 0.36f), end = Offset(w * 0.5f, h * 0.68f), strokeWidth = strokeW, cap = StrokeCap.Round)

        // Legs hanging straight down
        drawLine(color = tint, start = Offset(w * 0.48f, h * 0.68f), end = Offset(w * 0.48f, h * 0.94f), strokeWidth = strokeW, cap = StrokeCap.Round)
        drawLine(color = tint, start = Offset(w * 0.52f, h * 0.68f), end = Offset(w * 0.52f, h * 0.94f), strokeWidth = strokeW, cap = StrokeCap.Round)
    }
}

@Composable
fun AnimatedStickmanAscensionHero(
    glowScore: Int,
    modifier: Modifier = Modifier,
    size: Dp = 120.dp
) {
    val infiniteTransition = rememberInfiniteTransition(label = "stickman_aura")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    val auraColor = when {
        glowScore >= 80 -> GoldAscent
        glowScore >= 50 -> CyanNeon
        else -> MintNeon
    }

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = this.size.width
            val h = this.size.height
            val center = Offset(w * 0.5f, h * 0.5f)

            // Dynamic glowing aura rings
            drawCircle(
                color = auraColor.copy(alpha = 0.15f),
                radius = (w * 0.42f) * pulse,
                center = center
            )
            drawCircle(
                color = auraColor.copy(alpha = 0.35f),
                radius = (w * 0.32f) * pulse,
                center = center,
                style = Stroke(width = 2.dp.toPx())
            )
        }

        // Stickman Posture
        StickmanPostureCanvas(tint = Color.White)
    }
}

@Composable
fun StickmanFaceGuideOverlay(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // Oval face guide
        drawOval(
            color = CyanNeon.copy(alpha = 0.65f),
            topLeft = Offset(w * 0.22f, h * 0.16f),
            size = Size(w * 0.56f, h * 0.52f),
            style = Stroke(
                width = 2.dp.toPx(),
                pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(16f, 16f), 0f)
            )
        )

        // Center eye line
        drawLine(
            color = CyanNeon.copy(alpha = 0.4f),
            start = Offset(w * 0.28f, h * 0.38f),
            end = Offset(w * 0.72f, h * 0.38f),
            strokeWidth = 1.5.dp.toPx()
        )

        // Jaw angle guidelines
        val jawGuide = Path().apply {
            moveTo(w * 0.26f, h * 0.48f)
            lineTo(w * 0.50f, h * 0.66f)
            lineTo(w * 0.74f, h * 0.48f)
        }
        drawPath(
            jawGuide,
            color = MintNeon.copy(alpha = 0.6f),
            style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
        )
    }
}

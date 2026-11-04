package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.ui.theme.CyanNeon
import com.example.ui.theme.MintNeon
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.TextPrimary
import kotlin.math.roundToInt

@Composable
fun ComparisonSlider(
    beforeImageUri: String,
    afterImageUri: String,
    beforeLabel: String = "BASELINE",
    afterLabel: String = "ASCENDED",
    modifier: Modifier = Modifier
) {
    var splitFraction by remember { mutableFloatStateOf(0.5f) }
    val context = LocalContext.current
    val density = LocalDensity.current

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(280.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceDark)
            .clipToBounds()
    ) {
        val widthPx = constraints.maxWidth.toFloat()
        val splitPositionX = widthPx * splitFraction
        val splitPositionDp = with(density) { splitPositionX.toDp() }

        // After Image (Base Layer - fills full width)
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(afterImageUri)
                .crossfade(true)
                .build(),
            contentDescription = "Ascended photo",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // After Label (Bottom Right)
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(12.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(MintNeon.copy(alpha = 0.85f))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = afterLabel,
                fontSize = 10.sp,
                fontWeight = FontWeight.Black,
                color = Color.Black
            )
        }

        // Before Image (Clipped overlay to splitPositionDp)
        Box(
            modifier = Modifier
                .width(splitPositionDp)
                .fillMaxHeight()
                .clipToBounds()
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(beforeImageUri)
                    .crossfade(true)
                    .build(),
                contentDescription = "Baseline photo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(this@BoxWithConstraints.maxWidth)
            )

            // Before Label (Bottom Left)
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color.Black.copy(alpha = 0.75f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = beforeLabel,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    color = TextPrimary
                )
            }
        }

        // Divider Line
        Box(
            modifier = Modifier
                .offset { IntOffset(splitPositionX.roundToInt(), 0) }
                .fillMaxHeight()
                .width(2.5.dp)
                .background(CyanNeon)
        )

        // Drag Handle
        Box(
            modifier = Modifier
                .offset {
                    IntOffset(
                        (splitPositionX - with(density) { 20.dp.toPx() }).roundToInt(),
                        (constraints.maxHeight / 2 - with(density) { 20.dp.toPx() }).roundToInt()
                    )
                }
                .size(40.dp)
                .clip(CircleShape)
                .background(CyanNeon)
                .border(2.dp, Color.White, CircleShape)
                .pointerInput(Unit) {
                    detectHorizontalDragGestures { _, dragAmount ->
                        val newPos = splitPositionX + dragAmount
                        splitFraction = (newPos / widthPx).coerceIn(0.05f, 0.95f)
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.SwapHoriz,
                contentDescription = "Drag comparison slider",
                tint = Color.Black,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

package com.example.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.AscensionTier
import com.example.data.model.Habit
import com.example.ui.components.AnimatedStickmanAscensionHero
import com.example.ui.components.GlowCard
import com.example.ui.components.StickmanIcon
import com.example.ui.theme.BackgroundDark
import com.example.ui.theme.CyanNeon
import com.example.ui.theme.GoldAscent
import com.example.ui.theme.MintNeon
import com.example.ui.theme.SurfaceBorder
import com.example.ui.theme.SurfaceContainerDark
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun DashboardScreen(
    ascensionScore: Int,
    debloatScore: Int,
    currentTier: AscensionTier,
    habits: List<Habit>,
    waterMl: Int,
    onToggleHabit: (String) -> Unit,
    onAddWater: (Int) -> Unit,
    onNavigateToProtocols: () -> Unit,
    onNavigateToAICoach: () -> Unit,
    modifier: Modifier = Modifier
) {
    val animatedScore by animateFloatAsState(
        targetValue = ascensionScore / 100f,
        animationSpec = tween(1000),
        label = "score"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Hero Banner with Stickman Visual
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .border(1.dp, CyanNeon.copy(alpha = 0.35f), RoundedCornerShape(20.dp))
            ) {
                // Banner background image
                Image(
                    painter = painterResource(id = R.drawable.hero_ascend_stickman_1793772394134),
                    contentDescription = "Ascend Stickman Hero",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Dark gradient overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    Color.Black.copy(alpha = 0.85f),
                                    Color.Black.copy(alpha = 0.35f)
                                )
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(CyanNeon.copy(alpha = 0.2f))
                            .border(1.dp, CyanNeon.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "STICKMAN GLOW PROTOCOL",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyanNeon,
                            letterSpacing = 1.sp
                        )
                    }

                    Column {
                        Text(
                            text = "Daily Facial Ascension",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                        Text(
                            text = "Elevate bone structure, flush facial water, optimize frame.",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Tier: ${currentTier.name}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldAscent
                        )
                        Text(
                            text = "Learn Steps →",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MintNeon,
                            modifier = Modifier
                                .clickable { onNavigateToProtocols() }
                                .padding(4.dp)
                        )
                    }
                }
            }
        }

        // Tier & Score Card
        item {
            GlowCard(
                borderColor = GoldAscent.copy(alpha = 0.4f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "ASCENSION STATUS",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldAscent,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = currentTier.name,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = currentTier.description,
                            fontSize = 12.sp,
                            color = TextSecondary,
                            lineHeight = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Circular Progress with Stickman Avatar
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(86.dp)
                    ) {
                        CircularProgressIndicator(
                            progress = { 1f },
                            modifier = Modifier.fillMaxSize(),
                            color = SurfaceContainerHigh,
                            strokeWidth = 6.dp,
                            strokeCap = StrokeCap.Round
                        )
                        CircularProgressIndicator(
                            progress = { animatedScore },
                            modifier = Modifier.fillMaxSize(),
                            color = GoldAscent,
                            strokeWidth = 6.dp,
                            strokeCap = StrokeCap.Round
                        )
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "$ascensionScore",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black,
                                color = TextPrimary
                            )
                            Text(
                                text = "GLOW",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldAscent
                            )
                        }
                    }
                }
            }
        }

        // Debloat Gauge & Hydration Flush Card
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Debloat Meter
                GlowCard(
                    modifier = Modifier.weight(1f),
                    borderColor = CyanNeon.copy(alpha = 0.35f)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "DEBLOAT INDEX",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyanNeon
                        )
                        Icon(
                            imageVector = Icons.Default.Speed,
                            contentDescription = "Debloat status",
                            tint = CyanNeon,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "$debloatScore%",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Black,
                        color = TextPrimary
                    )
                    Text(
                        text = if (debloatScore >= 80) "Chiseled Mandible" else "Water Retention Active",
                        fontSize = 11.sp,
                        color = if (debloatScore >= 80) MintNeon else GoldAscent
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { debloatScore / 100f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = CyanNeon,
                        trackColor = SurfaceContainerHigh
                    )
                }

                // Water Flush Meter
                GlowCard(
                    modifier = Modifier.weight(1f),
                    borderColor = MintNeon.copy(alpha = 0.35f)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "WATER FLUSH",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = MintNeon
                        )
                        Icon(
                            imageVector = Icons.Default.Opacity,
                            contentDescription = "Water intake",
                            tint = MintNeon,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "${waterMl}ml",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = TextPrimary
                    )
                    Text(
                        text = "Goal: 3500ml",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(MintNeon.copy(alpha = 0.15f))
                            .clickable { onAddWater(250) }
                            .padding(vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "+250ml Drink",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MintNeon
                        )
                    }
                }
            }
        }

        // AI Coach Callout Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                SurfaceContainerDark,
                                SurfaceContainerHigh
                            )
                        )
                    )
                    .border(1.dp, CyanNeon.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                    .clickable { onNavigateToAICoach() }
                    .padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(CyanNeon.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "AI Coach",
                            tint = CyanNeon,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "AI Aesthetic Coach (Free)",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Instant debloat audit & personalized look advice based on your habits.",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }

                    Text(
                        text = "Audit →",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyanNeon
                    )
                }
            }
        }

        // Section Title: Daily Protocols
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "TODAY'S PROTOCOLS",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp,
                    color = TextPrimary
                )
                val completedCount = habits.count { it.completed }
                Text(
                    text = "$completedCount/${habits.size} Complete",
                    fontSize = 12.sp,
                    color = MintNeon,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Habit list preview (top 5)
        items(habits.take(5)) { habit ->
            HabitRowCard(habit = habit, onToggle = { onToggleHabit(habit.id) })
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun HabitRowCard(
    habit: Habit,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(if (habit.completed) SurfaceContainerHigh else SurfaceDark)
            .border(
                1.dp,
                if (habit.completed) MintNeon.copy(alpha = 0.5f) else SurfaceBorder,
                RoundedCornerShape(16.dp)
            )
            .clickable { onToggle() }
            .padding(14.dp)
            .testTag("habit_card_${habit.id}")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Stickman Icon Canvas
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(SurfaceContainerDark),
                contentAlignment = Alignment.Center
            ) {
                StickmanIcon(
                    type = habit.stickmanType,
                    tint = if (habit.completed) MintNeon else CyanNeon,
                    size = 32.dp
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = habit.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (habit.completed) TextPrimary else TextPrimary
                )
                Text(
                    text = habit.subtitle,
                    fontSize = 11.sp,
                    color = TextSecondary,
                    lineHeight = 15.sp
                )
            }

            // Checkbox Indicator
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(if (habit.completed) MintNeon else SurfaceContainerDark)
                    .border(
                        1.5.dp,
                        if (habit.completed) MintNeon else SurfaceBorder,
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (habit.completed) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Completed",
                        tint = Color.Black,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

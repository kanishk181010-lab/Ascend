package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.protocols.LooksmaxProtocol
import com.example.data.protocols.LooksmaxxingProtocols
import com.example.data.protocols.ProtocolStep
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
fun ProtocolsScreen(
    onNavigateToAICoach: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            Text(
                text = "LOOKSMAXXING BLUEPRINT",
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 2.sp,
                color = TextPrimary
            )
            Text(
                text = "Scientific protocols to eradicate facial bloat and ascend physical frame.",
                fontSize = 12.sp,
                color = TextSecondary
            )
        }

        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = SurfaceDark,
            contentColor = CyanNeon,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = CyanNeon
                )
            }
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Protocols", fontWeight = FontWeight.Bold, fontSize = 13.sp) },
                selectedContentColor = CyanNeon,
                unselectedContentColor = TextSecondary
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Debloat Calc", fontWeight = FontWeight.Bold, fontSize = 13.sp) },
                selectedContentColor = CyanNeon,
                unselectedContentColor = TextSecondary
            )
        }

        if (selectedTab == 0) {
            ProtocolsListView(onNavigateToAICoach)
        } else {
            DebloatCalculatorView()
        }
    }
}

@Composable
fun ProtocolsListView(onNavigateToAICoach: () -> Unit) {
    var expandedProtocolId by remember { mutableStateOf("debloat_24h") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Visual stickman illustration preview
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, MintNeon.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.stickman_debloat_1793772410026),
                    contentDescription = "Stickman Debloat Technique",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f))
                        .padding(14.dp),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Column {
                        Text(
                            text = "STICKMAN ANATOMY GUIDE",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = MintNeon
                        )
                        Text(
                            text = "Lymphatic Drainage & Ice Compression",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }

        items(LooksmaxxingProtocols.ALL_PROTOCOLS) { proto ->
            val isExpanded = expandedProtocolId == proto.id
            ProtocolCard(
                protocol = proto,
                isExpanded = isExpanded,
                onToggleExpand = {
                    expandedProtocolId = if (isExpanded) "" else proto.id
                }
            )
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun ProtocolCard(
    protocol: LooksmaxProtocol,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit
) {
    GlowCard(
        borderColor = if (isExpanded) CyanNeon.copy(alpha = 0.5f) else SurfaceBorder,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onToggleExpand() },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(CyanNeon.copy(alpha = 0.15f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = protocol.tag,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyanNeon
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = protocol.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    color = TextPrimary
                )
                Text(
                    text = protocol.keyStat,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MintNeon
                )
            }

            Icon(
                imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = "Expand protocol",
                tint = CyanNeon,
                modifier = Modifier.size(26.dp)
            )
        }

        AnimatedVisibility(visible = isExpanded) {
            Column(modifier = Modifier.padding(top = 14.dp)) {
                Text(
                    text = protocol.summary,
                    fontSize = 12.sp,
                    color = TextSecondary,
                    lineHeight = 16.sp
                )
                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "ACTION STEPS:",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    color = GoldAscent
                )
                Spacer(modifier = Modifier.height(8.dp))

                protocol.steps.forEach { step ->
                    StepItem(step = step)
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

@Composable
fun StepItem(step: ProtocolStep) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceDark)
            .border(1.dp, SurfaceBorder, RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Stickman Icon for this step
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(SurfaceContainerDark),
                contentAlignment = Alignment.Center
            ) {
                StickmanIcon(type = step.stickmanType, size = 28.dp, tint = CyanNeon)
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Step ${step.stepNumber}: ${step.title}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "${step.durationMinutes}m",
                        fontSize = 11.sp,
                        color = MintNeon,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = "Stickman Action: ${step.stickmanInstruction}",
                    fontSize = 11.sp,
                    color = CyanNeon
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Why it works: ${step.scientificReason}",
                    fontSize = 10.sp,
                    color = TextMuted,
                    lineHeight = 14.sp
                )
            }
        }
    }
}

@Composable
fun DebloatCalculatorView() {
    var sodiumInputMg by remember { mutableFloatStateOf(2600f) }
    var waterInputMl by remember { mutableFloatStateOf(2200f) }
    var potassiumLevel by remember { mutableIntStateOf(1) } // 0 = Low, 1 = Moderate, 2 = High
    var sleepHours by remember { mutableFloatStateOf(6.5f) }

    // Computation
    val sodiumRatio = sodiumInputMg / 2000f
    val waterRatio = waterInputMl / 3500f
    val potassiumMultiplier = when (potassiumLevel) {
        0 -> 1.3f
        1 -> 1.0f
        else -> 0.7f
    }
    val sleepPenalty = if (sleepHours < 7f) 1.25f else 0.85f

    val bloatScoreIndex = ((sodiumRatio * 45f * potassiumMultiplier * sleepPenalty) - (waterRatio * 30f))
        .toInt()
        .coerceIn(5, 95)

    val hoursToFlush = when {
        bloatScoreIndex >= 70 -> 24
        bloatScoreIndex >= 40 -> 12
        else -> 4
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            GlowCard(
                borderColor = CyanNeon.copy(alpha = 0.4f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "ESTIMATED FACIAL BLOAT",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyanNeon
                        )
                        Text(
                            text = "$bloatScoreIndex%",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Black,
                            color = if (bloatScoreIndex > 50) GoldAscent else MintNeon
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "TIME TO FULL FLUSH",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextSecondary
                        )
                        Text(
                            text = "~$hoursToFlush Hours",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MintNeon
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                LinearProgressIndicator(
                    progress = { bloatScoreIndex / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = if (bloatScoreIndex > 50) GoldAscent else MintNeon,
                    trackColor = SurfaceContainerHigh
                )

                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = if (bloatScoreIndex > 50) {
                        "⚠️ Extracellular water retention detected. Osmotic pressure is softening jawline angle. Execute potassium flush immediately."
                    } else {
                        "✨ Cellular hydration optimal. Subcutaneous tissue is lean with high gonial angle visibility."
                    },
                    fontSize = 11.sp,
                    color = TextSecondary
                )
            }
        }

        item {
            GlowCard(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "CALCULATOR PARAMETERS",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Sodium Slider
                Text(
                    text = "Daily Sodium: ${sodiumInputMg.toInt()}mg",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
                Slider(
                    value = sodiumInputMg,
                    onValueChange = { sodiumInputMg = it },
                    valueRange = 800f..5000f,
                    colors = SliderDefaults.colors(thumbColor = CyanNeon, activeTrackColor = CyanNeon)
                )

                // Water Slider
                Text(
                    text = "Daily Water: ${waterInputMl.toInt()}ml / 3500ml",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
                Slider(
                    value = waterInputMl,
                    onValueChange = { waterInputMl = it },
                    valueRange = 1000f..5000f,
                    colors = SliderDefaults.colors(thumbColor = MintNeon, activeTrackColor = MintNeon)
                )

                // Potassium Intake
                Text(
                    text = "Potassium Intake (Coconut water, bananas, avocado):",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Low (<1500mg)", "Moderate (2500mg)", "High (>4000mg)").forEachIndexed { index, label ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (potassiumLevel == index) CyanNeon else SurfaceContainerDark)
                                .clickable { potassiumLevel = index }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (potassiumLevel == index) Color.Black else TextSecondary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                // Sleep Slider
                Text(
                    text = "Sleep: ${String.format("%.1f", sleepHours)} Hours",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
                Slider(
                    value = sleepHours,
                    onValueChange = { sleepHours = it },
                    valueRange = 4f..10f,
                    colors = SliderDefaults.colors(thumbColor = GoldAscent, activeTrackColor = GoldAscent)
                )
            }
        }

        // Action Recommendation
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(SurfaceDark)
                    .border(1.dp, MintNeon.copy(alpha = 0.4f), RoundedCornerShape(14.dp))
                    .padding(14.dp)
            ) {
                Column {
                    Text(
                        text = "PRESCRIPTION FOR MAXIMUM LEANNESS:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MintNeon
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    val recWater = (3500 - waterInputMl.toInt()).coerceAtLeast(0)
                    Text(
                        text = "1. Drink ${recWater}ml cold water within next 4 hours.\n2. Ingest 1 serving potassium (coconut water/banana) to force cellular sodium excretion.\n3. Perform 5 minutes downward neck lymphatic sweep.",
                        fontSize = 12.sp,
                        color = TextPrimary,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

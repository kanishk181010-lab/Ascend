package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Fireplace
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Habit
import com.example.data.model.HabitCategory
import com.example.data.model.StickmanType
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitMatrixScreen(
    habits: List<Habit>,
    selectedCategory: HabitCategory?,
    waterMl: Int,
    sodiumMg: Int,
    sleepHours: Float,
    onSelectCategory: (HabitCategory?) -> Unit,
    onToggleHabit: (String) -> Unit,
    onAddCustomHabit: (String, String, HabitCategory, StickmanType) -> Unit,
    onAddWater: (Int) -> Unit,
    onUpdateSodium: (Int) -> Unit,
    onUpdateSleep: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddDialog by remember { mutableStateOf(false) }

    val filteredHabits = if (selectedCategory == null) {
        habits
    } else {
        habits.filter { it.category == selectedCategory }
    }

    Box(modifier = modifier.fillMaxSize().background(BackgroundDark)) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Text(
                    text = "AESTHETIC MATRIX",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp,
                    color = TextPrimary
                )
                Text(
                    text = "Daily micro-habits that directly dictate facial bone leanness and posture.",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }

            // Quick Metrics Adjustment Bar
            item {
                GlowCard(
                    borderColor = CyanNeon.copy(alpha = 0.3f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "DEBLOAT TELEMETRY",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyanNeon,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    // Water Quick Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "Water Consumed", fontSize = 12.sp, color = TextSecondary)
                            Text(text = "${waterMl}ml", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MintNeon.copy(alpha = 0.2f))
                                    .clickable { onAddWater(250) }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text("+250ml", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MintNeon)
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(CyanNeon.copy(alpha = 0.2f))
                                    .clickable { onAddWater(500) }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text("+500ml", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CyanNeon)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Sodium Slider
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Sodium Level", fontSize = 12.sp, color = TextSecondary)
                            Text(
                                text = "${sodiumMg}mg ${if (sodiumMg <= 2000) "(Optimal)" else "(Puffiness Risk)"}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (sodiumMg <= 2000) MintNeon else GoldAscent
                            )
                        }
                        Slider(
                            value = sodiumMg.toFloat(),
                            onValueChange = { onUpdateSodium(it.toInt()) },
                            valueRange = 800f..4500f,
                            colors = SliderDefaults.colors(
                                thumbColor = CyanNeon,
                                activeTrackColor = CyanNeon,
                                inactiveTrackColor = SurfaceContainerHigh
                            )
                        )
                    }

                    // Sleep Slider
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Sleep Duration", fontSize = 12.sp, color = TextSecondary)
                            Text(
                                text = "${String.format("%.1f", sleepHours)}h (Back Sleep)",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MintNeon
                            )
                        }
                        Slider(
                            value = sleepHours,
                            onValueChange = { onUpdateSleep(it) },
                            valueRange = 4f..10f,
                            steps = 11,
                            colors = SliderDefaults.colors(
                                thumbColor = MintNeon,
                                activeTrackColor = MintNeon,
                                inactiveTrackColor = SurfaceContainerHigh
                            )
                        )
                    }
                }
            }

            // Category Chips
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        CategoryPill(
                            label = "All Habits",
                            isSelected = selectedCategory == null,
                            onClick = { onSelectCategory(null) }
                        )
                    }
                    items(HabitCategory.values()) { cat ->
                        CategoryPill(
                            label = cat.displayName,
                            isSelected = selectedCategory == cat,
                            onClick = { onSelectCategory(cat) }
                        )
                    }
                }
            }

            // Habits List
            items(filteredHabits) { habit ->
                HabitDetailedCard(habit = habit, onToggle = { onToggleHabit(habit.id) })
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }

        // Floating Action Button to Add Custom Habit
        FloatingActionButton(
            onClick = { showAddDialog = true },
            containerColor = CyanNeon,
            contentColor = Color.Black,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
                .testTag("add_custom_habit_fab")
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Custom Habit")
        }
    }

    if (showAddDialog) {
        AddCustomHabitDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { title, desc, cat, stickman ->
                onAddCustomHabit(title, desc, cat, stickman)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun CategoryPill(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) CyanNeon else SurfaceContainerDark)
            .border(1.dp, if (isSelected) CyanNeon else SurfaceBorder, RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) Color.Black else TextSecondary
        )
    }
}

@Composable
fun HabitDetailedCard(
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
            .testTag("detailed_habit_${habit.id}")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceContainerDark),
                contentAlignment = Alignment.Center
            ) {
                StickmanIcon(
                    type = habit.stickmanType,
                    tint = if (habit.completed) MintNeon else CyanNeon,
                    size = 36.dp
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(habit.category.badgeColorHex).copy(alpha = 0.15f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = habit.category.displayName.uppercase(),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(habit.category.badgeColorHex)
                        )
                    }

                    if (habit.streak > 0) {
                        Text(
                            text = "🔥 ${habit.streak}d",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldAscent
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = habit.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = habit.subtitle,
                    fontSize = 11.sp,
                    color = TextSecondary,
                    lineHeight = 15.sp
                )
            }

            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(if (habit.completed) MintNeon else SurfaceContainerDark)
                    .border(1.5.dp, if (habit.completed) MintNeon else SurfaceBorder, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (habit.completed) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Completed",
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCustomHabitDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, HabitCategory, StickmanType) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedCat by remember { mutableStateOf(HabitCategory.DEBLOAT) }
    var selectedStickman by remember { mutableStateOf(StickmanType.DEBLOAT_WATER) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SurfaceDark,
        title = {
            Text(
                text = "Add Ascension Habit",
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Habit Title", color = TextSecondary) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = CyanNeon,
                        unfocusedBorderColor = SurfaceBorder
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("habit_title_input")
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Aesthetic Benefit", color = TextSecondary) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = CyanNeon,
                        unfocusedBorderColor = SurfaceBorder
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("habit_desc_input")
                )

                Text("Category:", fontSize = 12.sp, color = TextSecondary)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(HabitCategory.values()) { cat ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (selectedCat == cat) CyanNeon else SurfaceContainerDark)
                                .clickable { selectedCat = cat }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = cat.displayName,
                                fontSize = 10.sp,
                                color = if (selectedCat == cat) Color.Black else TextSecondary
                            )
                        }
                    }
                }

                Text("Stickman Visual:", fontSize = 12.sp, color = TextSecondary)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(StickmanType.values()) { sType ->
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (selectedStickman == sType) CyanNeon.copy(alpha = 0.25f) else SurfaceContainerDark)
                                .border(1.dp, if (selectedStickman == sType) CyanNeon else SurfaceBorder, RoundedCornerShape(8.dp))
                                .clickable { selectedStickman = sType },
                            contentAlignment = Alignment.Center
                        ) {
                            StickmanIcon(type = sType, size = 30.dp, tint = if (selectedStickman == sType) CyanNeon else TextSecondary)
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onConfirm(title, description.ifBlank { "Daily aesthetic protocol" }, selectedCat, selectedStickman)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = CyanNeon, contentColor = Color.Black),
                modifier = Modifier.testTag("save_habit_button")
            ) {
                Text("Add Protocol")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = TextSecondary)
            }
        }
    )
}

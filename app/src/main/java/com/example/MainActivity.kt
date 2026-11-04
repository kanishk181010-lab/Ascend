package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AscendTopBar
import com.example.ui.screens.AICoachScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.HabitMatrixScreen
import com.example.ui.screens.ProfileSettingsDialog
import com.example.ui.screens.ProgressVaultScreen
import com.example.ui.screens.ProtocolsScreen
import com.example.ui.theme.BackgroundDark
import com.example.ui.theme.CyanNeon
import com.example.ui.theme.GoldAscent
import com.example.ui.theme.MintNeon
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.SurfaceBorder
import com.example.ui.theme.SurfaceContainerDark
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.AscendViewModel

enum class AscendNavTab(val title: String, val icon: ImageVector, val tag: String) {
    DASHBOARD("Ascend", Icons.Default.Home, "nav_dashboard"),
    HABITS("Matrix", Icons.Default.Checklist, "nav_habits"),
    PROTOCOLS("Looksmax", Icons.Default.FitnessCenter, "nav_protocols"),
    VAULT("Vault", Icons.Default.PhotoLibrary, "nav_vault"),
    AI_COACH("Coach AI", Icons.Default.AutoAwesome, "nav_coach")
}

class MainActivity : ComponentActivity() {

    private val viewModel: AscendViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                AscendApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun AscendApp(viewModel: AscendViewModel) {
    var currentTab by remember { mutableStateOf(AscendNavTab.DASHBOARD) }
    var showProfileDialog by remember { mutableStateOf(false) }

    val habits by viewModel.habits.collectAsState()
    val photos by viewModel.photos.collectAsState()
    val chatMessages by viewModel.chatMessages.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    val waterMl by viewModel.waterMl.collectAsState()
    val sodiumMg by viewModel.sodiumMg.collectAsState()
    val sleepHours by viewModel.sleepHours.collectAsState()
    val debloatScore by viewModel.debloatScore.collectAsState()
    val ascensionScore by viewModel.ascensionScore.collectAsState()
    val currentTier by viewModel.currentTier.collectAsState()
    val selectedCategory by viewModel.selectedHabitCategory.collectAsState()
    val isAiThinking by viewModel.isAiThinking.collectAsState()

    // Handle Back Button navigation
    if (currentTab != AscendNavTab.DASHBOARD) {
        BackHandler {
            currentTab = AscendNavTab.DASHBOARD
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = BackgroundDark,
        topBar = {
            AscendTopBar(
                userProfile = userProfile,
                streak = userProfile.currentStreak,
                onProfileClick = { showProfileDialog = true }
            )
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .background(SurfaceDark)
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .border(0.5.dp, SurfaceBorder),
                containerColor = SurfaceDark,
                contentColor = CyanNeon
            ) {
                AscendNavTab.values().forEach { tab ->
                    val isSelected = currentTab == tab
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { currentTab = tab },
                        icon = {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = tab.title,
                                modifier = Modifier.size(22.dp)
                            )
                        },
                        label = {
                            Text(
                                text = tab.title,
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Black,
                            selectedTextColor = CyanNeon,
                            indicatorColor = CyanNeon,
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextMuted
                        ),
                        modifier = Modifier.testTag(tab.tag)
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                AscendNavTab.DASHBOARD -> {
                    DashboardScreen(
                        ascensionScore = ascensionScore,
                        debloatScore = debloatScore,
                        currentTier = currentTier,
                        habits = habits,
                        waterMl = waterMl,
                        onToggleHabit = { viewModel.toggleHabit(it) },
                        onAddWater = { viewModel.logWater(it) },
                        onNavigateToProtocols = { currentTab = AscendNavTab.PROTOCOLS },
                        onNavigateToAICoach = {
                            viewModel.requestDailyCritique()
                            currentTab = AscendNavTab.AI_COACH
                        }
                    )
                }

                AscendNavTab.HABITS -> {
                    HabitMatrixScreen(
                        habits = habits,
                        selectedCategory = selectedCategory,
                        waterMl = waterMl,
                        sodiumMg = sodiumMg,
                        sleepHours = sleepHours,
                        onSelectCategory = { viewModel.selectCategory(it) },
                        onToggleHabit = { viewModel.toggleHabit(it) },
                        onAddCustomHabit = { t, s, c, st -> viewModel.addCustomHabit(t, s, c, st) },
                        onAddWater = { viewModel.logWater(it) },
                        onUpdateSodium = { viewModel.updateSodium(it) },
                        onUpdateSleep = { viewModel.updateSleep(it) }
                    )
                }

                AscendNavTab.PROTOCOLS -> {
                    ProtocolsScreen(
                        onNavigateToAICoach = { currentTab = AscendNavTab.AI_COACH }
                    )
                }

                AscendNavTab.VAULT -> {
                    ProgressVaultScreen(
                        photos = photos,
                        onAddPhoto = { uri, angle, notes -> viewModel.addPhoto(uri, angle, notes) },
                        onDeletePhoto = { viewModel.deletePhoto(it) }
                    )
                }

                AscendNavTab.AI_COACH -> {
                    AICoachScreen(
                        messages = chatMessages,
                        isThinking = isAiThinking,
                        onSendMessage = { viewModel.sendCoachMessage(it) },
                        onRequestDailyAudit = { viewModel.requestDailyCritique() }
                    )
                }
            }
        }
    }

    if (showProfileDialog) {
        ProfileSettingsDialog(
            userProfile = userProfile,
            onConnectGoogle = { email, name -> viewModel.connectGoogle(email, name) },
            onToggleGhostMode = { viewModel.toggleGhostMode(it) },
            onDismiss = { showProfileDialog = false }
        )
    }
}

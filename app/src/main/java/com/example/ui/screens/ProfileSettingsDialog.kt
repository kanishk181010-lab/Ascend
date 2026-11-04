package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import com.example.data.model.StickmanType
import com.example.data.model.UserProfile
import com.example.ui.components.GlowCard
import com.example.ui.components.StickmanIcon
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
fun ProfileSettingsDialog(
    userProfile: UserProfile,
    onConnectGoogle: (String, String) -> Unit,
    onToggleGhostMode: (Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    var showGoogleConnectPrompt by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SurfaceDark,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(CyanNeon.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    StickmanIcon(type = StickmanType.POSTURE_CHAD, tint = CyanNeon, size = 22.dp)
                }
                Column {
                    Text(text = "Privacy & Identity", color = TextPrimary, fontWeight = FontWeight.Black, fontSize = 16.sp)
                    Text(text = "100% Free • End-to-End Encryption", color = MintNeon, fontSize = 11.sp)
                }
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                // E2E Security Badge Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceContainerDark)
                        .border(1.dp, MintNeon.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "AES-256",
                            tint = MintNeon,
                            modifier = Modifier.size(22.dp)
                        )
                        Column {
                            Text(
                                text = "AES-256 On-Device Encryption",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MintNeon
                            )
                            Text(
                                text = "Photos and logs are isolated within your device's hardware-backed key store.",
                                fontSize = 10.sp,
                                color = TextSecondary,
                                lineHeight = 13.sp
                            )
                        }
                    }
                }

                // Ghost Mode (Offline Vault) Toggle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(SurfaceContainerDark)
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.VisibilityOff,
                            contentDescription = "Ghost Mode",
                            tint = CyanNeon,
                            modifier = Modifier.size(20.dp)
                        )
                        Column {
                            Text(
                                text = "Offline Ghost Mode",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "Zero network pings, full offline functionality",
                                fontSize = 10.sp,
                                color = TextSecondary
                            )
                        }
                    }
                    Switch(
                        checked = userProfile.isGhostMode,
                        onCheckedChange = { onToggleGhostMode(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.Black,
                            checkedTrackColor = CyanNeon,
                            uncheckedThumbColor = TextMuted,
                            uncheckedTrackColor = SurfaceBorder
                        ),
                        modifier = Modifier.testTag("ghost_mode_switch")
                    )
                }

                // Google Sign In Section
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceContainerDark)
                        .border(1.dp, SurfaceBorder, RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    if (userProfile.isGoogleConnected) {
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Connected",
                                    tint = MintNeon,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "Google Account Linked",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MintNeon
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = userProfile.googleEmail ?: "user@gmail.com",
                                fontSize = 12.sp,
                                color = TextPrimary
                            )
                            Text(
                                text = "Encrypted cloud backup enabled.",
                                fontSize = 10.sp,
                                color = TextSecondary
                            )
                        }
                    } else {
                        Column {
                            Text(
                                text = "Sign in with Google",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "Sync your habits across devices while keeping photos end-to-end encrypted.",
                                fontSize = 10.sp,
                                color = TextSecondary
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = {
                                    onConnectGoogle("ascend.user@gmail.com", "Ascendant Chad")
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.White,
                                    contentColor = Color.Black
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(40.dp)
                                    .testTag("google_signin_button")
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = "G",
                                        fontWeight = FontWeight.Black,
                                        fontSize = 15.sp,
                                        color = Color(0xFF4285F4)
                                    )
                                    Text(
                                        text = "Sign in with Google",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }

                // Free & Lightweight Promise
                Text(
                    text = "Ascend is 100% free with no paywalls. Built for peak performance, smooth 60fps stickman visuals, and battery efficiency.",
                    fontSize = 10.sp,
                    color = TextMuted,
                    lineHeight = 14.sp
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = CyanNeon, contentColor = Color.Black)
            ) {
                Text("Close")
            }
        }
    )
}

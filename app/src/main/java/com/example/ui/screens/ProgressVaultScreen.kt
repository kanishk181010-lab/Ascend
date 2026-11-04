package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.model.PhotoAngle
import com.example.data.model.ProgressPhoto
import com.example.ui.components.ComparisonSlider
import com.example.ui.components.GlowCard
import com.example.ui.components.StickmanFaceGuideOverlay
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
fun ProgressVaultScreen(
    photos: List<ProgressPhoto>,
    onAddPhoto: (String, PhotoAngle, String) -> Unit,
    onDeletePhoto: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showAngleDialog by remember { mutableStateOf(false) }
    var pendingPhotoUri by remember { mutableStateOf<Uri?>(null) }
    var selectedPhotoAngle by remember { mutableStateOf(PhotoAngle.JAWLINE_45) }
    var photoNotes by remember { mutableStateOf("") }
    var showOverlayInPreview by remember { mutableStateOf(false) }

    // Android Photo Picker (zero-permission, fully Play Store compliant)
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            pendingPhotoUri = uri
            showAngleDialog = true
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Vault Header & Privacy Badge
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "ENCRYPTED VAULT",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 2.sp,
                        color = TextPrimary
                    )
                    Text(
                        text = "Progress photos & debloat angle scans.",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(MintNeon.copy(alpha = 0.12f))
                        .border(1.dp, MintNeon.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "AES-256",
                            tint = MintNeon,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = "AES-256 LOCAL",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = MintNeon
                        )
                    }
                }
            }
        }

        // Before & After Interactive Comparison Slider (if 2+ photos)
        item {
            if (photos.size >= 2) {
                val before = photos.last()
                val after = photos.first()
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "ASCENSION COMPARISON",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            color = CyanNeon
                        )
                        Text(
                            text = "Drag Divider ↔",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    ComparisonSlider(
                        beforeImageUri = before.uriString,
                        afterImageUri = after.uriString,
                        beforeLabel = before.dateFormatted.uppercase(),
                        afterLabel = after.dateFormatted.uppercase()
                    )
                }
            }
        }

        // Action Button: Add New Photo Scan
        item {
            Button(
                onClick = {
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("upload_progress_photo_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CyanNeon,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AddPhotoAlternate,
                    contentDescription = "Add Photo",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Import Progress Scan",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Privacy Reassurance Card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceDark)
                    .border(1.dp, SurfaceBorder, RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = "Zero Cloud Leakage",
                        tint = CyanNeon,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Zero Cloud Leakage: Face scans remain in encrypted device storage. No external AI server accesses your biometric images.",
                        fontSize = 11.sp,
                        color = TextSecondary,
                        lineHeight = 15.sp
                    )
                }
            }
        }

        // Section Title: Scans Timeline
        item {
            Text(
                text = "TIMELINE SCANS (${photos.size})",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                color = TextPrimary
            )
        }

        // List of photos
        items(photos) { photo ->
            PhotoItemCard(photo = photo, onDelete = { onDeletePhoto(photo.id) })
        }

        item {
            Spacer(modifier = Modifier.height(40.dp))
        }
    }

    // Modal Dialog to Select Angle and Add Note for newly picked photo
    if (showAngleDialog && pendingPhotoUri != null) {
        AlertDialog(
            onDismissRequest = {
                showAngleDialog = false
                pendingPhotoUri = null
            },
            containerColor = SurfaceDark,
            title = {
                Text(
                    text = "Configure Face Scan",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Preview box with optional stickman guide overlay
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.Black),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(pendingPhotoUri)
                                .build(),
                            contentDescription = "Preview",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        if (showOverlayInPreview) {
                            StickmanFaceGuideOverlay()
                        }
                    }

                    // Stickman guide toggle
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showOverlayInPreview = !showOverlayInPreview },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Stickman Alignment Guide",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                        Text(
                            text = if (showOverlayInPreview) "ACTIVE" else "SHOW",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyanNeon
                        )
                    }

                    Text("Select Camera Angle:", fontSize = 12.sp, color = TextSecondary)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(PhotoAngle.values()) { angle ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (selectedPhotoAngle == angle) CyanNeon else SurfaceContainerDark)
                                    .clickable { selectedPhotoAngle = angle }
                                    .padding(horizontal = 8.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = angle.label,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (selectedPhotoAngle == angle) Color.Black else TextSecondary
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = photoNotes,
                        onValueChange = { photoNotes = it },
                        label = { Text("Notes (e.g. Day 7 post-potassium flush)", color = TextSecondary) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedBorderColor = CyanNeon,
                            unfocusedBorderColor = SurfaceBorder
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("photo_notes_input")
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val uriStr = pendingPhotoUri.toString()
                        onAddPhoto(uriStr, selectedPhotoAngle, photoNotes)
                        showAngleDialog = false
                        pendingPhotoUri = null
                        photoNotes = ""
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CyanNeon, contentColor = Color.Black),
                    modifier = Modifier.testTag("save_photo_scan_button")
                ) {
                    Text("Save to Vault")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showAngleDialog = false
                    pendingPhotoUri = null
                }) {
                    Text("Cancel", color = TextSecondary)
                }
            }
        )
    }
}

@Composable
fun PhotoItemCard(
    photo: ProgressPhoto,
    onDelete: () -> Unit
) {
    val context = LocalContext.current
    var showGuide by remember { mutableStateOf(false) }

    GlowCard(
        borderColor = SurfaceBorder,
        modifier = Modifier.fillMaxWidth().testTag("photo_card_${photo.id}")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Photo Thumbnail with Stickman guide toggle
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Black)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(photo.uriString)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Face Scan",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                if (showGuide) {
                    StickmanFaceGuideOverlay()
                }

                // Overlay toggle button
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(4.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.7f))
                        .clickable { showGuide = !showGuide }
                        .padding(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = "Toggle Guide",
                        tint = if (showGuide) CyanNeon else TextSecondary,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(CyanNeon.copy(alpha = 0.15f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = photo.angle.label.uppercase(),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyanNeon
                        )
                    }

                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete scan",
                            tint = TextMuted,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = photo.dateFormatted,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                if (photo.notes.isNotBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = photo.notes,
                        fontSize = 11.sp,
                        color = TextSecondary,
                        maxLines = 2
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "Debloat Score: ${photo.debloatScore}%",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MintNeon
                    )
                    Text(
                        text = "• AES-256",
                        fontSize = 10.sp,
                        color = TextMuted
                    )
                }
            }
        }
    }
}

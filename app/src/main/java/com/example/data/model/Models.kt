package com.example.data.model

enum class HabitCategory(val displayName: String, val badgeColorHex: Long) {
    DEBLOAT("Debloat & Water", 0xFF00E5FF),
    JAWLINE("Jawline & Mewing", 0xFF00FFB2),
    POSTURE("Posture & Frame", 0xFFFFC837),
    SKINCARE("Skin & Glow", 0xFFB026FF),
    LIFESTYLE("Recovery & Sleep", 0xFF38BDF8)
}

enum class StickmanType {
    POSTURE_CHAD,
    DEBLOAT_WATER,
    MEWING_JAW,
    ICE_ROLLER,
    SLEEP_REST,
    WORKOUT_FRAME
}

data class Habit(
    val id: String,
    val title: String,
    val subtitle: String,
    val category: HabitCategory,
    val stickmanType: StickmanType,
    val target: Int = 1,
    val unit: String = "times",
    val completed: Boolean = false,
    val streak: Int = 0,
    val isCustom: Boolean = false
)

enum class PhotoAngle(val label: String) {
    FRONT("Front View"),
    JAWLINE_45("45° Jawline"),
    PROFILE_LEFT("Left Profile"),
    PROFILE_RIGHT("Right Profile")
}

data class ProgressPhoto(
    val id: String,
    val uriString: String,
    val timestamp: Long,
    val dateFormatted: String,
    val angle: PhotoAngle,
    val notes: String = "",
    val debloatScore: Int = 85,
    val isEncrypted: Boolean = true
)

data class AscensionTier(
    val name: String,
    val level: Int,
    val minScore: Int,
    val maxScore: Int,
    val title: String,
    val description: String,
    val stickmanPose: String
) {
    companion object {
        val ALL_TIERS = listOf(
            AscensionTier("Bloat Wanderer", 1, 0, 29, "Initiate", "High water retention, forward head posture. Time to begin the debloat ascension.", "Slouched"),
            AscensionTier("Ascending Neophyte", 2, 30, 54, "Rising", "Consistency taking root. Lymphatic fluid draining, posture straightening.", "Focused"),
            AscensionTier("Chiseled Sculptor", 3, 55, 79, "Sculptor", "Sharp jawline emerging, hollow cheek visibility, broad clavicle frame.", "Confident"),
            AscensionTier("Aesthetic Master", 4, 80, 100, "Ascended", "Peak facial leanness, zero unnecessary bloat, perfect tongue posture, Greek god clavicle alignment.", "Ascended")
        )

        fun fromScore(score: Int): AscensionTier {
            val clamped = score.coerceIn(0, 100)
            return ALL_TIERS.firstOrNull { clamped in it.minScore..it.maxScore } ?: ALL_TIERS.last()
        }
    }
}

data class ChatMessage(
    val id: String,
    val sender: String, // "USER" or "COACH"
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val quickActions: List<String> = emptyList()
)

data class UserProfile(
    val name: String = "Ascendant",
    val isGoogleConnected: Boolean = false,
    val googleEmail: String? = null,
    val isGhostMode: Boolean = true,
    val isVaultEncrypted: Boolean = true,
    val waterGoalMl: Int = 3500,
    val currentStreak: Int = 4,
    val totalPhotos: Int = 0
)

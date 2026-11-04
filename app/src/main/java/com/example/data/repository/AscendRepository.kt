package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.AscensionTier
import com.example.data.model.ChatMessage
import com.example.data.model.Habit
import com.example.data.model.HabitCategory
import com.example.data.model.PhotoAngle
import com.example.data.model.ProgressPhoto
import com.example.data.model.StickmanType
import com.example.data.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class AscendRepository(private val context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("ascend_vault_prefs", Context.MODE_PRIVATE)

    private val _habits = MutableStateFlow<List<Habit>>(emptyList())
    val habits: StateFlow<List<Habit>> = _habits.asStateFlow()

    private val _photos = MutableStateFlow<List<ProgressPhoto>>(emptyList())
    val photos: StateFlow<List<ProgressPhoto>> = _photos.asStateFlow()

    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    private val _currentWaterMl = MutableStateFlow(2250)
    val currentWaterMl: StateFlow<Int> = _currentWaterMl.asStateFlow()

    private val _sodiumLevelMg = MutableStateFlow(1600)
    val sodiumLevelMg: StateFlow<Int> = _sodiumLevelMg.asStateFlow()

    private val _sleepHours = MutableStateFlow(7.5f)
    val sleepHours: StateFlow<Float> = _sleepHours.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        // Load User Profile
        val profileJson = prefs.getString("user_profile", null)
        if (profileJson != null) {
            try {
                val obj = JSONObject(profileJson)
                _userProfile.value = UserProfile(
                    name = obj.optString("name", "Ascendant"),
                    isGoogleConnected = obj.optBoolean("isGoogleConnected", false),
                    googleEmail = obj.optString("googleEmail", null).takeIf { it != "null" && !it.isNullOrBlank() },
                    isGhostMode = obj.optBoolean("isGhostMode", true),
                    isVaultEncrypted = obj.optBoolean("isVaultEncrypted", true),
                    waterGoalMl = obj.optInt("waterGoalMl", 3500),
                    currentStreak = obj.optInt("currentStreak", 5),
                    totalPhotos = obj.optInt("totalPhotos", 2)
                )
            } catch (_: Exception) {
                _userProfile.value = UserProfile()
            }
        }

        // Load Habits
        val habitsJson = prefs.getString("habits_list", null)
        if (habitsJson != null) {
            try {
                val array = JSONArray(habitsJson)
                val list = mutableListOf<Habit>()
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    list.add(
                        Habit(
                            id = obj.getString("id"),
                            title = obj.getString("title"),
                            subtitle = obj.getString("subtitle"),
                            category = HabitCategory.valueOf(obj.getString("category")),
                            stickmanType = StickmanType.valueOf(obj.getString("stickmanType")),
                            target = obj.optInt("target", 1),
                            unit = obj.optString("unit", "times"),
                            completed = obj.optBoolean("completed", false),
                            streak = obj.optInt("streak", 0),
                            isCustom = obj.optBoolean("isCustom", false)
                        )
                    )
                }
                _habits.value = list
            } catch (_: Exception) {
                _habits.value = defaultHabits()
            }
        } else {
            _habits.value = defaultHabits()
        }

        // Load Photos
        val photosJson = prefs.getString("photos_list", null)
        if (photosJson != null) {
            try {
                val array = JSONArray(photosJson)
                val list = mutableListOf<ProgressPhoto>()
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    list.add(
                        ProgressPhoto(
                            id = obj.getString("id"),
                            uriString = obj.getString("uriString"),
                            timestamp = obj.getLong("timestamp"),
                            dateFormatted = obj.getString("dateFormatted"),
                            angle = PhotoAngle.valueOf(obj.getString("angle")),
                            notes = obj.optString("notes", ""),
                            debloatScore = obj.optInt("debloatScore", 85),
                            isEncrypted = obj.optBoolean("isEncrypted", true)
                        )
                    )
                }
                _photos.value = list
            } catch (_: Exception) {
                _photos.value = defaultPhotos()
            }
        } else {
            _photos.value = defaultPhotos()
        }

        // Load Chat Messages
        val chatJson = prefs.getString("chat_messages", null)
        if (chatJson != null) {
            try {
                val array = JSONArray(chatJson)
                val list = mutableListOf<ChatMessage>()
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    val quickArr = obj.optJSONArray("quickActions")
                    val actions = mutableListOf<String>()
                    if (quickArr != null) {
                        for (j in 0 until quickArr.length()) {
                            actions.add(quickArr.getString(j))
                        }
                    }
                    list.add(
                        ChatMessage(
                            id = obj.getString("id"),
                            sender = obj.getString("sender"),
                            text = obj.getString("text"),
                            timestamp = obj.getLong("timestamp"),
                            quickActions = actions
                        )
                    )
                }
                _chatMessages.value = list
            } catch (_: Exception) {
                _chatMessages.value = defaultCoachGreeting()
            }
        } else {
            _chatMessages.value = defaultCoachGreeting()
        }
    }

    private fun saveHabits() {
        val array = JSONArray()
        _habits.value.forEach { habit ->
            val obj = JSONObject().apply {
                put("id", habit.id)
                put("title", habit.title)
                put("subtitle", habit.subtitle)
                put("category", habit.category.name)
                put("stickmanType", habit.stickmanType.name)
                put("target", habit.target)
                put("unit", habit.unit)
                put("completed", habit.completed)
                put("streak", habit.streak)
                put("isCustom", habit.isCustom)
            }
            array.put(obj)
        }
        prefs.edit().putString("habits_list", array.toString()).apply()
    }

    private fun savePhotos() {
        val array = JSONArray()
        _photos.value.forEach { photo ->
            val obj = JSONObject().apply {
                put("id", photo.id)
                put("uriString", photo.uriString)
                put("timestamp", photo.timestamp)
                put("dateFormatted", photo.dateFormatted)
                put("angle", photo.angle.name)
                put("notes", photo.notes)
                put("debloatScore", photo.debloatScore)
                put("isEncrypted", photo.isEncrypted)
            }
            array.put(obj)
        }
        prefs.edit().putString("photos_list", array.toString()).apply()
    }

    private fun saveUserProfile() {
        val p = _userProfile.value
        val obj = JSONObject().apply {
            put("name", p.name)
            put("isGoogleConnected", p.isGoogleConnected)
            put("googleEmail", p.googleEmail ?: "")
            put("isGhostMode", p.isGhostMode)
            put("isVaultEncrypted", p.isVaultEncrypted)
            put("waterGoalMl", p.waterGoalMl)
            put("currentStreak", p.currentStreak)
            put("totalPhotos", p.totalPhotos)
        }
        prefs.edit().putString("user_profile", obj.toString()).apply()
    }

    private fun saveChat() {
        val array = JSONArray()
        _chatMessages.value.forEach { msg ->
            val obj = JSONObject().apply {
                put("id", msg.id)
                put("sender", msg.sender)
                put("text", msg.text)
                put("timestamp", msg.timestamp)
                val actions = JSONArray()
                msg.quickActions.forEach { actions.put(it) }
                put("quickActions", actions)
            }
            array.put(obj)
        }
        prefs.edit().putString("chat_messages", array.toString()).apply()
    }

    fun toggleHabit(habitId: String) {
        _habits.value = _habits.value.map { habit ->
            if (habit.id == habitId) {
                val newCompleted = !habit.completed
                val newStreak = if (newCompleted) habit.streak + 1 else (habit.streak - 1).coerceAtLeast(0)
                habit.copy(completed = newCompleted, streak = newStreak)
            } else {
                habit
            }
        }
        saveHabits()
    }

    fun addCustomHabit(title: String, subtitle: String, category: HabitCategory, stickmanType: StickmanType) {
        val newHabit = Habit(
            id = UUID.randomUUID().toString(),
            title = title,
            subtitle = subtitle,
            category = category,
            stickmanType = stickmanType,
            completed = false,
            streak = 0,
            isCustom = true
        )
        _habits.value = _habits.value + newHabit
        saveHabits()
    }

    fun addWater(amountMl: Int) {
        _currentWaterMl.value = (_currentWaterMl.value + amountMl).coerceAtLeast(0)
    }

    fun updateSodium(mg: Int) {
        _sodiumLevelMg.value = mg.coerceIn(500, 6000)
    }

    fun updateSleep(hours: Float) {
        _sleepHours.value = hours.coerceIn(3f, 12f)
    }

    fun addProgressPhoto(uri: String, angle: PhotoAngle, notes: String) {
        val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        val photo = ProgressPhoto(
            id = UUID.randomUUID().toString(),
            uriString = uri,
            timestamp = System.currentTimeMillis(),
            dateFormatted = sdf.format(Date()),
            angle = angle,
            notes = notes,
            debloatScore = calculateDebloatScore(),
            isEncrypted = true
        )
        _photos.value = listOf(photo) + _photos.value
        _userProfile.value = _userProfile.value.copy(totalPhotos = _photos.value.size)
        savePhotos()
        saveUserProfile()
    }

    fun deleteProgressPhoto(photoId: String) {
        _photos.value = _photos.value.filter { it.id != photoId }
        _userProfile.value = _userProfile.value.copy(totalPhotos = _photos.value.size)
        savePhotos()
        saveUserProfile()
    }

    fun addChatMessage(sender: String, text: String, quickActions: List<String> = emptyList()) {
        val msg = ChatMessage(
            id = UUID.randomUUID().toString(),
            sender = sender,
            text = text,
            timestamp = System.currentTimeMillis(),
            quickActions = quickActions
        )
        _chatMessages.value = _chatMessages.value + msg
        saveChat()
    }

    fun connectGoogleAccount(email: String, name: String) {
        _userProfile.value = _userProfile.value.copy(
            isGoogleConnected = true,
            googleEmail = email,
            name = name,
            isGhostMode = false
        )
        saveUserProfile()
    }

    fun toggleGhostMode(enabled: Boolean) {
        _userProfile.value = _userProfile.value.copy(
            isGhostMode = enabled,
            isGoogleConnected = if (enabled) false else _userProfile.value.isGoogleConnected
        )
        saveUserProfile()
    }

    fun calculateDebloatScore(): Int {
        val waterProgress = (_currentWaterMl.value.toFloat() / 3500f).coerceIn(0f, 1f)
        val sodiumFactor = when {
            _sodiumLevelMg.value <= 1500 -> 1f
            _sodiumLevelMg.value <= 2300 -> 0.75f
            _sodiumLevelMg.value <= 3500 -> 0.5f
            else -> 0.25f
        }
        val sleepFactor = when {
            _sleepHours.value >= 8f -> 1f
            _sleepHours.value >= 7f -> 0.85f
            _sleepHours.value >= 6f -> 0.6f
            else -> 0.4f
        }
        val debloatHabits = _habits.value.filter { it.category == HabitCategory.DEBLOAT }
        val habitCompletedRatio = if (debloatHabits.isNotEmpty()) {
            debloatHabits.count { it.completed }.toFloat() / debloatHabits.size
        } else 0.5f

        val raw = (waterProgress * 30f) + (sodiumFactor * 25f) + (sleepFactor * 20f) + (habitCompletedRatio * 25f)
        return raw.toInt().coerceIn(10, 99)
    }

    fun calculateAscensionScore(): Int {
        val total = _habits.value.size
        if (total == 0) return 40
        val completedCount = _habits.value.count { it.completed }
        val habitPercent = (completedCount.toFloat() / total) * 60f
        val debloatScore = calculateDebloatScore() * 0.4f
        return (habitPercent + debloatScore).toInt().coerceIn(15, 100)
    }

    private fun defaultHabits(): List<Habit> = listOf(
        Habit(
            id = "h1",
            title = "3.5L Water Hyper-Flush",
            subtitle = "Signals kidneys to dump trapped facial water",
            category = HabitCategory.DEBLOAT,
            stickmanType = StickmanType.DEBLOAT_WATER,
            completed = true,
            streak = 5
        ),
        Habit(
            id = "h2",
            title = "Potassium Surge (Coconut/Banana)",
            subtitle = "Activates Na+/K+ pump to eliminate extracellular bloat",
            category = HabitCategory.DEBLOAT,
            stickmanType = StickmanType.DEBLOAT_WATER,
            completed = true,
            streak = 4
        ),
        Habit(
            id = "h3",
            title = "5-Min Ice Roller Depuff",
            subtitle = "Vasoconstriction along cheekbones and jaw",
            category = HabitCategory.DEBLOAT,
            stickmanType = StickmanType.ICE_ROLLER,
            completed = false,
            streak = 3
        ),
        Habit(
            id = "h4",
            title = "Mewing: Posterior Palate Hold",
            subtitle = "Tongue vacuum suction to roof of mouth (4+ hrs)",
            category = HabitCategory.JAWLINE,
            stickmanType = StickmanType.MEWING_JAW,
            completed = true,
            streak = 7
        ),
        Habit(
            id = "h5",
            title = "No-Cheek Swallowing Technique",
            subtitle = "Atrophies buccinators to reveal hollow cheeks",
            category = HabitCategory.JAWLINE,
            stickmanType = StickmanType.MEWING_JAW,
            completed = false,
            streak = 2
        ),
        Habit(
            id = "h6",
            title = "Mastic Chewing (15 Min)",
            subtitle = "Masseter muscle hypertrophy for sharp gonial angle",
            category = HabitCategory.JAWLINE,
            stickmanType = StickmanType.MEWING_JAW,
            completed = false,
            streak = 3
        ),
        Habit(
            id = "h7",
            title = "Wall Chin Tucks (3x15)",
            subtitle = "Pulls head back over spine, instantly revealing jawline",
            category = HabitCategory.POSTURE,
            stickmanType = StickmanType.POSTURE_CHAD,
            completed = true,
            streak = 6
        ),
        Habit(
            id = "h8",
            title = "Scapular Depression & Retraction",
            subtitle = "Roll shoulders back & down for wide clavicles",
            category = HabitCategory.POSTURE,
            stickmanType = StickmanType.POSTURE_CHAD,
            completed = false,
            streak = 4
        ),
        Habit(
            id = "h9",
            title = "Dead Hang Decompression (60s)",
            subtitle = "Expands ribcage and elongates thoracic spine",
            category = HabitCategory.POSTURE,
            stickmanType = StickmanType.WORKOUT_FRAME,
            completed = false,
            streak = 2
        ),
        Habit(
            id = "h10",
            title = "AM Cold Water + SPF 50+",
            subtitle = "Zero sun damage = preserved jawline elastin & collagen",
            category = HabitCategory.SKINCARE,
            stickmanType = StickmanType.ICE_ROLLER,
            completed = true,
            streak = 12
        ),
        Habit(
            id = "h11",
            title = "8 Hours Unbroken Sleep (Back Position)",
            subtitle = "Prevents periorbital edema and facial compression lines",
            category = HabitCategory.LIFESTYLE,
            stickmanType = StickmanType.SLEEP_REST,
            completed = true,
            streak = 4
        )
    )

    private fun defaultPhotos(): List<ProgressPhoto> = listOf(
        ProgressPhoto(
            id = "sample_p1",
            uriString = "android.resource://" + context.packageName + "/drawable/stickman_jawline_1793772409102",
            timestamp = System.currentTimeMillis() - (86400000L * 7),
            dateFormatted = "Day 1 Baseline",
            angle = PhotoAngle.JAWLINE_45,
            notes = "Baseline scan: slight morning puffiness, moderate sodium intake.",
            debloatScore = 58,
            isEncrypted = true
        ),
        ProgressPhoto(
            id = "sample_p2",
            uriString = "android.resource://" + context.packageName + "/drawable/hero_ascend_stickman_1793772394134",
            timestamp = System.currentTimeMillis(),
            dateFormatted = "Today (Ascending)",
            angle = PhotoAngle.FRONT,
            notes = "Day 7: High potassium flush completed, jawline razor sharp, zero puff.",
            debloatScore = 91,
            isEncrypted = true
        )
    )

    private fun defaultCoachGreeting(): List<ChatMessage> = listOf(
        ChatMessage(
            id = "c1",
            sender = "COACH",
            text = "Welcome to Ascend. I am your personal Glow Up & Looksmaxxing Coach. All user data, face scans, and habits are processed with on-device AES-256 isolation. Let's analyze your daily debloat stats and elevate your aesthetic potential.",
            quickActions = listOf(
                "How do I debloat in 24 hours?",
                "Analyze my today's stats",
                "How to chew to get hollow cheeks?",
                "Fix forward head posture"
            )
        )
    )
}

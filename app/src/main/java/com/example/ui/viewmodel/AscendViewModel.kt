package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.AscensionTier
import com.example.data.model.ChatMessage
import com.example.data.model.Habit
import com.example.data.model.HabitCategory
import com.example.data.model.PhotoAngle
import com.example.data.model.ProgressPhoto
import com.example.data.model.StickmanType
import com.example.data.model.UserProfile
import com.example.data.repository.AscendRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AscendViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AscendRepository(application.applicationContext)

    val habits: StateFlow<List<Habit>> = repository.habits
    val photos: StateFlow<List<ProgressPhoto>> = repository.photos
    val chatMessages: StateFlow<List<ChatMessage>> = repository.chatMessages
    val userProfile: StateFlow<UserProfile> = repository.userProfile
    val waterMl: StateFlow<Int> = repository.currentWaterMl
    val sodiumMg: StateFlow<Int> = repository.sodiumLevelMg
    val sleepHours: StateFlow<Float> = repository.sleepHours

    private val _selectedHabitCategory = MutableStateFlow<HabitCategory?>(null)
    val selectedHabitCategory: StateFlow<HabitCategory?> = _selectedHabitCategory.asStateFlow()

    private val _isAiThinking = MutableStateFlow(false)
    val isAiThinking: StateFlow<Boolean> = _isAiThinking.asStateFlow()

    val debloatScore: StateFlow<Int> = combine(habits, waterMl, sodiumMg, sleepHours) { _, _, _, _ ->
        repository.calculateDebloatScore()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), repository.calculateDebloatScore())

    val ascensionScore: StateFlow<Int> = combine(habits, debloatScore) { _, _ ->
        repository.calculateAscensionScore()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), repository.calculateAscensionScore())

    val currentTier: StateFlow<AscensionTier> = ascensionScore.combine(ascensionScore) { score, _ ->
        AscensionTier.fromScore(score)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AscensionTier.fromScore(repository.calculateAscensionScore()))

    fun selectCategory(category: HabitCategory?) {
        _selectedHabitCategory.value = category
    }

    fun toggleHabit(id: String) {
        repository.toggleHabit(id)
    }

    fun addCustomHabit(title: String, subtitle: String, category: HabitCategory, stickmanType: StickmanType) {
        repository.addCustomHabit(title, subtitle, category, stickmanType)
    }

    fun logWater(ml: Int) {
        repository.addWater(ml)
    }

    fun updateSodium(mg: Int) {
        repository.updateSodium(mg)
    }

    fun updateSleep(hours: Float) {
        repository.updateSleep(hours)
    }

    fun addPhoto(uri: String, angle: PhotoAngle, notes: String) {
        repository.addProgressPhoto(uri, angle, notes)
    }

    fun deletePhoto(id: String) {
        repository.deleteProgressPhoto(id)
    }

    fun connectGoogle(email: String, name: String) {
        repository.connectGoogleAccount(email, name)
    }

    fun toggleGhostMode(enabled: Boolean) {
        repository.toggleGhostMode(enabled)
    }

    fun sendCoachMessage(query: String) {
        if (query.isBlank()) return
        repository.addChatMessage(sender = "USER", text = query)
        _isAiThinking.value = true

        viewModelScope.launch {
            delay(800) // Realistic offline NLP / AI response time
            val responseText = generateCoachResponse(query)
            val suggested = generateSuggestedFollowups(query)
            repository.addChatMessage(sender = "COACH", text = responseText, quickActions = suggested)
            _isAiThinking.value = false
        }
    }

    fun requestDailyCritique() {
        _isAiThinking.value = true
        viewModelScope.launch {
            delay(1000)
            val habitsList = habits.value
            val doneCount = habitsList.count { it.completed }
            val water = waterMl.value
            val sodium = sodiumMg.value
            val sleep = sleepHours.value
            val dScore = debloatScore.value

            val critique = buildString {
                append("ASCEND AESTHETIC AUDIT:\n\n")
                if (dScore >= 80) {
                    append("🔥 Bloat Status: Chiseled (${dScore}%). Subcutaneous water retention is minimal. High definition along gonial angle.\n\n")
                } else if (dScore >= 60) {
                    append("⚡ Bloat Status: Moderate (${dScore}%). Minor extracellular facial puffiness detected. Jawline soft around lower third.\n\n")
                } else {
                    append("⚠️ Bloat Status: Critical Bloat Alert (${dScore}%). Significant sodium-water retention obscuring bone structure.\n\n")
                }

                append("• Habits Executed: $doneCount of ${habitsList.size} protocols checked today.\n")
                append("• Hydration: ${water}ml / 3500ml target.\n")
                append("• Sodium Intake: ${sodium}mg (Recommended <2000mg).\n")
                append("• Sleep: ${sleep} hours recorded.\n\n")

                append("ACTIONABLE ASCENSION STEPS FOR TONIGHT:\n")
                if (water < 2500) {
                    append("1. Down 750ml cold water + 1 potassium source (coconut water or banana) to initiate osmotic flush.\n")
                } else {
                    append("1. Great hydration adherence. Maintain cellular balance.\n")
                }

                if (!habitsList.firstOrNull { it.id == "h7" }?.completed.let { it == true }) {
                    append("2. Execute 3 sets of 15 Wall Chin Tucks immediately to retract cervical spine and reveal mandible.\n")
                } else {
                    append("2. Posture work completed. Keep shoulders depressed.\n")
                }

                if (sleep < 7.5f) {
                    append("3. Sleep debt detected. Periorbital dark circles will worsen without 8h back-sleeping with elevated contour pillow.\n")
                } else {
                    append("3. Sleep window optimal. Continue sleeping on your back to prevent asymmetrical cheek compression.\n")
                }
            }

            repository.addChatMessage(
                sender = "COACH",
                text = critique,
                quickActions = listOf("How to fix neck posture fast?", "Explain Potassium-Sodium pump", "Start 5-min Ice Depuff")
            )
            _isAiThinking.value = false
        }
    }

    private fun generateCoachResponse(query: String): String {
        val q = query.lowercase()
        return when {
            q.contains("debloat") || q.contains("water") || q.contains("puff") ->
                "To debloat within 24 hours:\n1. Drink 3.5L of water—counterintuitively, hyper-hydrating stops vasopressin and signals your kidneys to release trapped facial fluid.\n2. Ingest 3500mg-4700mg of potassium (coconut water, spinach, avocado, bananas) to push extracellular sodium out.\n3. Ice plunge your face for 3x15s intervals to trigger rapid vasoconstriction.\n4. Avoid processed carbs and table salt for 24h."

            q.contains("mew") || q.contains("tongue") || q.contains("jaw") ->
                "Orthotropic Mewing Guide:\n• Place the entire tongue—especially the posterior third—flat against the roof of the mouth without touching front teeth.\n• Say the 'NG' sound in 'SING' and hold the back part up with light suction.\n• Keep lips closed and teeth lightly touching or slightly apart.\n• Breathe strictly through the nose 100% of the day and night."

            q.contains("chew") || q.contains("hollow") || q.contains("cheek") ->
                "Hollow Cheeks & Chewing Protocol:\n• Never contract your cheeks (buccinator muscles) while swallowing or drinking; swallow exclusively with the tongue.\n• Chewing mastic gum for 15 minutes 3x weekly strengthens the masseters for a wider gonial angle.\n• Buccinators will naturally atrophy within 6-8 weeks when cheek swallowing is eradicated."

            q.contains("posture") || q.contains("neck") || q.contains("clavicle") ->
                "Frame & Posture Ascension:\n1. Wall Chin Tucks: Stand flat against a wall, pull chin straight back making a deliberate double chin. Hold 5s. (15 reps x 3 sets). Fixes forward head posture and instantly sharpens jawline.\n2. Scapular Retractions: Pin shoulder blades down and together to widen the clavicles.\n3. 60-second daily dead hang to decompress thoracic spine."

            q.contains("skin") || q.contains("glow") || q.contains("spf") ->
                "Dermal Glow Rules:\n• Daily Broad Spectrum SPF 50+ is non-negotiable—UV breakdown destroys the collagen that keeps your jaw skin tight.\n• Morning ice wash + hyaluronic acid on damp skin.\n• Sleep on a silk pillowcase exclusively on your back to prevent sleep wrinkles and asymmetric cheek flattening."

            q.contains("analyze") || q.contains("stats") || q.contains("audit") ->
                "Your current Ascension Score is ${ascensionScore.value}/100 in Tier: ${currentTier.value.name}.\nDebloat index is at ${debloatScore.value}%. You've consumed ${waterMl.value}ml water with sodium at ${sodiumMg.value}mg. Stay consistent with your daily protocols to unlock maximum facial leanness."

            else ->
                "Understood. Consistency in the daily micro-habits—potassium-sodium balance, tongue posture, and cervical spinal alignment—compounds over 30 days into undeniable aesthetic ascension. Keep your daily checklist complete."
        }
    }

    private fun generateSuggestedFollowups(query: String): List<String> {
        val q = query.lowercase()
        return when {
            q.contains("debloat") -> listOf("What foods have highest potassium?", "Explain ice roll technique", "Analyze my today's stats")
            q.contains("mew") || q.contains("jaw") -> listOf("How long does mewing take to work?", "What is hard chewing?", "Fix forward head posture")
            else -> listOf("How do I debloat in 24 hours?", "Analyze my today's stats", "Fix forward head posture")
        }
    }
}

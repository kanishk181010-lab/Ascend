package com.example.data.protocols

import com.example.data.model.StickmanType

data class ProtocolStep(
    val stepNumber: Int,
    val title: String,
    val stickmanInstruction: String,
    val scientificReason: String,
    val durationMinutes: Int,
    val stickmanType: StickmanType
)

data class LooksmaxProtocol(
    val id: String,
    val title: String,
    val tag: String,
    val estimatedTime: String,
    val summary: String,
    val keyStat: String,
    val steps: List<ProtocolStep>
)

object LooksmaxxingProtocols {
    val ALL_PROTOCOLS = listOf(
        LooksmaxProtocol(
            id = "debloat_24h",
            title = "24-Hour Rapid Debloat",
            tag = "FACE LEANNESS",
            estimatedTime = "Morning & Evening",
            summary = "Scientifically flush out extracellular subcutaneous facial water by optimizing the potassium-sodium pump and activating lymphatic drainage.",
            keyStat = "-1.5cm jawline water retention in 24h",
            steps = listOf(
                ProtocolStep(
                    stepNumber = 1,
                    title = "The Potassium-to-Sodium Shock",
                    stickmanInstruction = "Stickman drinks 500ml pure coconut water or eats 2 bananas with 500ml water. Zero table salt for 24 hours.",
                    scientificReason = "Sodium retains water outside cells. High potassium activates the cellular sodium-potassium ATPase pump, dumping trapped facial water into urine.",
                    durationMinutes = 5,
                    stickmanType = StickmanType.DEBLOAT_WATER
                ),
                ProtocolStep(
                    stepNumber = 2,
                    title = "Lymphatic Drainage Sweep",
                    stickmanInstruction = "Stickman places fingertips at angle of jaw and sweeps gently down neck to collarbones (20 reps).",
                    scientificReason = "Facial puffiness is often pooled lymph fluid. Gravity and gentle mechanical strokes stimulate lymphatic drainage through cervical lymph nodes.",
                    durationMinutes = 6,
                    stickmanType = StickmanType.ICE_ROLLER
                ),
                ProtocolStep(
                    stepNumber = 3,
                    title = "Ice Plunge or Ice Roller",
                    stickmanInstruction = "Stickman submerges face in bowl of ice water for 3 intervals of 15 seconds, or rolls ice along cheekbones.",
                    scientificReason = "Cold temperature induces rapid vasoconstriction followed by reactive vasodilation, tightening epidermal tissue and eliminating morning puffiness.",
                    durationMinutes = 5,
                    stickmanType = StickmanType.ICE_ROLLER
                ),
                ProtocolStep(
                    stepNumber = 4,
                    title = "Hyperhydration Flush",
                    stickmanInstruction = "Stickman drinks 3.5 Liters of filtered water throughout the day.",
                    scientificReason = "Dehydration causes the kidneys to hoard water via vasopressin. Flooding the system stops water hoarding and flushes facial puffiness.",
                    durationMinutes = 1,
                    stickmanType = StickmanType.DEBLOAT_WATER
                )
            )
        ),
        LooksmaxProtocol(
            id = "jawline_mewing",
            title = "Jawline & Tongue Mechanics",
            tag = "BONE & SOFT TISSUE",
            estimatedTime = "Continuous Habit",
            summary = "Master orthotropics: tongue posture against the palate, masseter activation, and buccal fat reduction via proper adult swallowing.",
            keyStat = "Maximizes mandibular definition & forward maxilla",
            steps = listOf(
                ProtocolStep(
                    stepNumber = 1,
                    title = "Posterior Palate Suction Hold",
                    stickmanInstruction = "Stickman smiles, says 'NG' sound to anchor the back third of the tongue firmly against the roof of the mouth.",
                    scientificReason = "The tongue provides internal support for the maxilla and zygomatic bones, preventing downward facial lengthening and under-chin sagging.",
                    durationMinutes = 10,
                    stickmanType = StickmanType.MEWING_JAW
                ),
                ProtocolStep(
                    stepNumber = 2,
                    title = "No-Cheek Adult Swallow",
                    stickmanInstruction = "Stickman swallows water using only the tongue and throat muscles; lips stay completely still and cheeks do not contract.",
                    scientificReason = "Contracting buccinator cheek muscles during swallowing causes cheek hypertrophy. Isolating the tongue atrophies buccinators for hollow cheeks.",
                    durationMinutes = 5,
                    stickmanType = StickmanType.MEWING_JAW
                ),
                ProtocolStep(
                    stepNumber = 3,
                    title = "Mastic Chewing Sets",
                    stickmanInstruction = "Stickman chews firm mastic gum or fibrous raw foods equally on both sides (10 min each side).",
                    scientificReason = "Stimulates masseter muscle hypertrophy, creating a wider, more angular jaw angle (gonial angle) without asymmetric bite fatigue.",
                    durationMinutes = 20,
                    stickmanType = StickmanType.MEWING_JAW
                )
            )
        ),
        LooksmaxProtocol(
            id = "frame_posture",
            title = "Clavicle & Frame Ascension",
            tag = "V-TAPER & PRESENCE",
            estimatedTime = "15 min Daily",
            summary = "Eradicate forward head posture ('nerd neck') and rounded shoulders to expose clavicles and double apparent shoulder width.",
            keyStat = "+2 inches perceived shoulder width",
            steps = listOf(
                ProtocolStep(
                    stepNumber = 1,
                    title = "Wall Chin Tucks",
                    stickmanInstruction = "Stickman stands against wall, draws chin straight back making a double chin against resistance (15 reps x 3 sets).",
                    scientificReason = "Strengthens deep cervical flexors and decompresses suboccipital muscles, pulling the head back over the spine and instantly raising the jawline.",
                    durationMinutes = 5,
                    stickmanType = StickmanType.POSTURE_CHAD
                ),
                ProtocolStep(
                    stepNumber = 2,
                    title = "Scapular Depression & Retraction",
                    stickmanInstruction = "Stickman pins shoulder blades back and downwards as if tucking them into back pockets (hold for 10 seconds, 10 reps).",
                    scientificReason = "Releases tight pectoralis minor and activates lower trapezius, widening the clavicles and eliminating slouched round shoulders.",
                    durationMinutes = 5,
                    stickmanType = StickmanType.POSTURE_CHAD
                ),
                ProtocolStep(
                    stepNumber = 3,
                    title = "Dead Hang Decompression",
                    stickmanInstruction = "Stickman hangs freely from overhead pullup bar with relaxed spine for 60 seconds.",
                    scientificReason = "Decompresses thoracic spine, expands ribcage, resets shoulder mobility, and improves vertical posture elongation.",
                    durationMinutes = 3,
                    stickmanType = StickmanType.WORKOUT_FRAME
                )
            )
        ),
        LooksmaxProtocol(
            id = "skin_barrier",
            title = "Skin Radiance & Anti-Puff",
            tag = "DERMAL GLOW",
            estimatedTime = "AM & PM",
            summary = "Clinical-grade daily dermal protection to eliminate dullness, dark circles, and sleep-induced facial distortion.",
            keyStat = "Preserves collagen & maximizes skin clarity",
            steps = listOf(
                ProtocolStep(
                    stepNumber = 1,
                    title = "AM Cold Rinse & Broad Spectrum SPF 50+",
                    stickmanInstruction = "Stickman splashes cold water on face, applies lightweight hydrating gel and 2 finger lengths of SPF 50.",
                    scientificReason = "UV rays destroy 80% of facial collagen and elastin over time. Daily SPF protects skin density and prevents premature jaw laxity.",
                    durationMinutes = 4,
                    stickmanType = StickmanType.ICE_ROLLER
                ),
                ProtocolStep(
                    stepNumber = 2,
                    title = "Back Sleeping Elevation",
                    stickmanInstruction = "Stickman sleeps on back with head elevated by an extra ergonomic contour pillow.",
                    scientificReason = "Side and stomach sleeping causes asymmetrical mechanical facial wrinkling and pools venous fluid in the periorbital area (eye bags).",
                    durationMinutes = 1,
                    stickmanType = StickmanType.SLEEP_REST
                )
            )
        )
    )
}

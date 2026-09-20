package com.alx.fitness

import android.content.Context
import java.time.LocalDate
import java.time.temporal.WeekFields
import java.util.Locale

data class WorkoutRecord(
    val date: String,
    val title: String,
    val minutes: Int,
    val calories: Int
)

class ALXPrefs(context: Context) {
    private val prefs = context.getSharedPreferences("alx_fitness", Context.MODE_PRIVATE)

    var onboardingDone: Boolean
        get() = prefs.getBoolean("onboarding_done", false)
        set(value) = prefs.edit().putBoolean("onboarding_done", value).apply()

    var goal: String
        get() = prefs.getString("goal", "Tonificar") ?: "Tonificar"
        set(value) = prefs.edit().putString("goal", value).apply()

    var level: String
        get() = prefs.getString("level", "Intermedio") ?: "Intermedio"
        set(value) = prefs.edit().putString("level", value).apply()

    var equipment: String
        get() = prefs.getString("equipment", "Sin equipo") ?: "Sin equipo"
        set(value) = prefs.edit().putString("equipment", value).apply()

    var notifications: Boolean
        get() = prefs.getBoolean("notifications", true)
        set(value) = prefs.edit().putBoolean("notifications", value).apply()

    var coachVoice: Boolean
        get() = prefs.getBoolean("coach_voice", true)
        set(value) = prefs.edit().putBoolean("coach_voice", value).apply()

    var workoutSounds: Boolean
        get() = prefs.getBoolean("workout_sounds", true)
        set(value) = prefs.edit().putBoolean("workout_sounds", value).apply()

    var workoutVibration: Boolean
        get() = prefs.getBoolean("workout_vibration", true)
        set(value) = prefs.edit().putBoolean("workout_vibration", value).apply()

    var completed: Int
        get() = prefs.getInt("completed", 0)
        set(value) = prefs.edit().putInt("completed", value).apply()

    var totalMinutes: Int
        get() = prefs.getInt("total_minutes", 0)
        set(value) = prefs.edit().putInt("total_minutes", value).apply()

    var totalCalories: Int
        get() = prefs.getInt("total_calories", 0)
        set(value) = prefs.edit().putInt("total_calories", value).apply()

    var streak: Int
        get() = prefs.getInt("streak", 0)
        set(value) = prefs.edit().putInt("streak", value).apply()

    private var lastTrainingDate: String
        get() = prefs.getString("last_training_date", "") ?: ""
        set(value) = prefs.edit().putString("last_training_date", value).apply()

    val completedDates: Set<String>
        get() = prefs.getStringSet("completed_dates", emptySet())?.toSet() ?: emptySet()

    val history: List<WorkoutRecord>
        get() = (prefs.getStringSet("workout_history", emptySet()) ?: emptySet())
            .mapNotNull { raw ->
                val p = raw.split("|", limit = 4)
                if (p.size == 4) WorkoutRecord(p[0], p[1], p[2].toIntOrNull() ?: 0, p[3].toIntOrNull() ?: 0) else null
            }
            .sortedByDescending { it.date }

    fun recordWorkout(title: String, minutes: Int, calories: Int) {
        val today = LocalDate.now()
        val previous = lastTrainingDate.takeIf { it.isNotBlank() }?.let {
            runCatching { LocalDate.parse(it) }.getOrNull()
        }

        streak = when {
            previous == today -> streak.coerceAtLeast(1)
            previous == today.minusDays(1) -> streak + 1
            else -> 1
        }
        lastTrainingDate = today.toString()
        completed += 1
        totalMinutes += minutes
        totalCalories += calories

        val dates = completedDates.toMutableSet().apply { add(today.toString()) }
        val records = (prefs.getStringSet("workout_history", emptySet()) ?: emptySet()).toMutableSet()
        records.add("${today}|${title.replace("|", " ")}|$minutes|$calories")
        prefs.edit()
            .putStringSet("completed_dates", dates)
            .putStringSet("workout_history", records)
            .apply()
    }

    fun weeklyCompleted(): Int {
        val today = LocalDate.now()
        val fields = WeekFields.of(Locale.getDefault())
        return completedDates.count { raw ->
            runCatching { LocalDate.parse(raw) }.getOrNull()?.let { date ->
                date.get(fields.weekOfWeekBasedYear()) == today.get(fields.weekOfWeekBasedYear()) &&
                    date.get(fields.weekBasedYear()) == today.get(fields.weekBasedYear())
            } == true
        }
    }
}

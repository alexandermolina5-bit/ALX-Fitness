package com.alx.fitness

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun CalendarScreen(context: Context, level: String, onBack: () -> Unit) {
    val prefs = remember { ALXPrefs(context) }
    val recommended = when (level) { "Principiante" -> 3; "Avanzado" -> 5; else -> 4 }
    val plan = when (recommended) {
        3 -> listOf(true, false, true, false, true, false, false)
        5 -> listOf(true, true, false, true, true, true, false)
        else -> listOf(true, false, true, true, false, true, false)
    }
    val days = listOf("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom")
    val today = LocalDate.now()
    val monday = today.with(DayOfWeek.MONDAY)
    val completedDates = prefs.completedDates
    val weeklyCompleted = prefs.weeklyCompleted()
    val formatter = DateTimeFormatter.ofPattern("d MMM", Locale("es", "CR"))

    Column(Modifier.fillMaxSize().background(Color(0xFF08090D)).padding(20.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Volver") }
            Spacer(Modifier.width(6.dp))
            Column {
                Text("CALENDARIO ALX", fontSize = 27.sp, fontWeight = FontWeight.Black, color = Color(0xFF16C7F3))
                Text("Semana actual • $level", color = Color(0xFF9CA3AF))
            }
        }
        Spacer(Modifier.height(18.dp))
        Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF12141A)), shape = RoundedCornerShape(20.dp)) {
            Column(Modifier.padding(18.dp)) {
                Text("OBJETIVO SEMANAL", fontWeight = FontWeight.Black)
                Text("$weeklyCompleted de $recommended días completados", color = Color(0xFF16C7F3))
                Spacer(Modifier.height(10.dp))
                LinearProgressIndicator(
                    progress = { (weeklyCompleted.toFloat() / recommended).coerceIn(0f, 1f) },
                    modifier = Modifier.fillMaxWidth().height(9.dp),
                    color = Color(0xFF16C7F3),
                    trackColor = Color(0xFF1B1E27)
                )
            }
        }
        Spacer(Modifier.height(14.dp))
        days.forEachIndexed { index, day ->
            val date = monday.plusDays(index.toLong())
            val completed = date.toString() in completedDates
            val planned = plan[index]
            val isToday = date == today
            Card(
                Modifier.fillMaxWidth().padding(vertical = 4.dp),
                colors = CardDefaults.cardColors(containerColor = if (isToday) Color(0xFF1B1E27) else Color(0xFF12141A)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text("$day • ${date.format(formatter)}", fontWeight = FontWeight.Black)
                        Text(
                            when { completed -> "Entrenamiento completado"; planned -> "Entrenamiento programado"; else -> "Recuperación" },
                            color = if (completed || planned) Color(0xFF16C7F3) else Color(0xFF9CA3AF)
                        )
                    }
                    Icon(
                        if (completed) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                        null,
                        tint = if (completed) Color(0xFF16C7F3) else Color(0xFF4B5563)
                    )
                }
            }
        }
        Spacer(Modifier.height(10.dp))
        Text("El calendario se actualiza al terminar una rutina.", color = Color(0xFF9CA3AF), fontSize = 12.sp)
    }
}

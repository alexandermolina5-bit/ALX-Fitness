package com.alx.fitness

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NutritionScreen(goal:String, onBack:()->Unit) {
    var dayIndex by remember { mutableIntStateOf(0) }
    var selectedPlan by remember { mutableStateOf(if(goal=="Perder grasa") "Perder grasa" else "Aumentar masa muscular") }
    val days = ALXNutrition.week
    val day = days[dayIndex]

    Scaffold(
        containerColor = Color(0xFF08090D),
        topBar = {
            Column(Modifier.padding(20.dp)) {
                Text("ALX NUTRITION", color=Color(0xFF16C7F3), fontWeight=FontWeight.Black, fontSize=24.sp)
                Text("Plan de alimentación • $goal", color=Color(0xFF9CA3AF))
            }
        }
    ) { pad ->
        LazyColumn(Modifier.fillMaxSize().padding(pad).padding(horizontal=20.dp)) {
            item {
                Text("ELIGE TU PLAN",fontWeight=FontWeight.Bold)
                Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(8.dp)) {
                    listOf("Aumentar masa muscular","Perder grasa").forEach { plan ->
                        FilterChip(selected=selectedPlan==plan,onClick={selectedPlan=plan},label={Text(plan)},modifier=Modifier.weight(1f),colors=FilterChipDefaults.filterChipColors(selectedContainerColor=Color(0xFF16C7F3),selectedLabelColor=Color.Black))
                    }
                }
                Spacer(Modifier.height(12.dp))
                Card(colors=CardDefaults.cardColors(containerColor=Color(0xFF12141A)),shape=RoundedCornerShape(18.dp)) {
                    Column(Modifier.padding(16.dp)) {
                        Text(selectedPlan,fontWeight=FontWeight.Bold)
                        Text(ALXNutrition.calorieBand(selectedPlan),color=Color(0xFF16C7F3))
                        Spacer(Modifier.height(8.dp))
                        Text(if(selectedPlan=="Aumentar masa muscular") "Añade una porción extra de arroz, avena o papa y distribuye proteína en cada comida." else "Reduce ligeramente las porciones de almidones, mantén proteína y aumenta vegetales.",color=Color.White,fontSize=13.sp)
                        Spacer(Modifier.height(8.dp))
                        Text("Menú educativo de referencia. Ajusta cantidades a tus datos y consulta a un profesional si tienes una condición de salud.",color=Color(0xFF9CA3AF),fontSize=12.sp)
                    }
                }
                Spacer(Modifier.height(14.dp))
                Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween) {
                    Button(enabled=dayIndex>0,onClick={dayIndex--}) { Text("Anterior") }
                    Text(day.name,fontWeight=FontWeight.Black,fontSize=20.sp)
                    Button(enabled=dayIndex<days.lastIndex,onClick={dayIndex++}) { Text("Siguiente") }
                }
                Spacer(Modifier.height(14.dp))
                Text("${day.kcal} kcal  •  P ${day.protein}g  •  C ${day.carbs}g  •  G ${day.fats}g",color=Color(0xFF16C7F3),fontWeight=FontWeight.Bold)
                Spacer(Modifier.height(12.dp))
            }
            items(day.meals) { meal ->
                Card(Modifier.fillMaxWidth().padding(vertical=6.dp),colors=CardDefaults.cardColors(containerColor=Color(0xFF12141A)),shape=RoundedCornerShape(18.dp)) {
                    Column(Modifier.padding(16.dp)) {
                        Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween) {
                            Text(meal.title,fontWeight=FontWeight.Black)
                            Text("${meal.kcal} kcal",color=Color(0xFF16C7F3))
                        }
                        meal.foods.forEach {
                            Spacer(Modifier.height(8.dp))
                            Text("${it.name} — ${it.portion}",fontWeight=FontWeight.Bold)
                            Text("P ${it.protein}g • C ${it.carbs}g • G ${it.fats}g",color=Color(0xFF9CA3AF),fontSize=12.sp)
                        }
                    }
                }
            }
            item {
                Spacer(Modifier.height(16.dp))
                OutlinedButton(onClick=onBack,modifier=Modifier.fillMaxWidth()) { Text("VOLVER") }
                Spacer(Modifier.height(30.dp))
            }
        }
    }
}

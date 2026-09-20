package com.alx.fitness

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

private val Black = Color(0xFF08090D)
private val Panel = Color(0xFF12141A)
private val Panel2 = Color(0xFF1B1E27)
private val Lime = Color(0xFF16C7F3)
private val White = Color(0xFFF7F8FA)
private val Muted = Color(0xFF9CA3AF)
private val Red = Color(0xFFFF5964)

data class Exercise(val name:String,val muscle:String,val duration:Int,val equipment:String,val tip:String)
data class ExerciseTechnique(val start:String,val steps:List<String>,val breathing:String,val mistakes:List<String>,val easy:String,val advanced:String,val view:String)
data class Routine(val title:String,val subtitle:String,val minutes:Int,val level:String,val equipment:String,val calories:Int,val exercises:List<Exercise>)
data class Achievement(val title:String,val desc:String,val icon:String,val unlocked:Boolean)

private fun routineImage(r:Routine):Int = when {
 r.title.contains("CARDIO",true) -> R.drawable.routine_cardio
 r.title.contains("CORE",true)||r.title.contains("ABDOMEN",true)||r.title.contains("PLANCHA",true) -> R.drawable.routine_core
 r.title.contains("PIERNA",true)||r.title.contains("GLÚTE",true)||r.title.contains("PANTORR",true) -> R.drawable.routine_legs
 r.equipment!="Sin equipo"||r.title.contains("FUERZA",true) -> R.drawable.routine_strength
 else -> R.drawable.routine_full_body
}

private val routines = listOf(
 Routine("FULL BODY ALX","Fuerza + definición",28,"Intermedio","Sin equipo",240,
  listOf(
   Exercise("Sentadillas","Piernas",45,"Sin equipo","Rodillas alineadas y pecho arriba."),
   Exercise("Flexiones","Pecho",40,"Sin equipo","Aprieta abdomen y mantén el cuerpo recto."),
   Exercise("Zancadas","Piernas",40,"Sin equipo","Da un paso largo y controla la bajada."),
   Exercise("Plancha","Core",45,"Sin equipo","Mantén cadera estable y respira."),
   Exercise("Mountain climbers","Cardio",40,"Sin equipo","Ritmo constante sin perder postura.")
  )),
 Routine("CORE POWER","Abdomen y estabilidad",18,"Principiante","Sin equipo",150,
  listOf(
   Exercise("Plancha","Core",40,"Sin equipo","Aprieta glúteos y abdomen."),
   Exercise("Crunch","Abdomen",45,"Sin equipo","Sube con el abdomen, no con el cuello."),
   Exercise("Plancha lateral","Core",30,"Sin equipo","Mantén el cuerpo en línea."),
   Exercise("Dead bug","Core",40,"Sin equipo","Movimiento lento y controlado.")
  )),
 Routine("FUERZA CON EQUIPO","Hipertrofia funcional",35,"Avanzado","Mancuernas",320,
  listOf(
   Exercise("Press de pecho","Pecho",45,"Mancuernas","Controla la bajada."),
   Exercise("Peso muerto rumano","Piernas",45,"Mancuernas","Espalda neutra y cadera atrás."),
   Exercise("Curl de bíceps","Brazos",40,"Mancuernas","Evita balancear el cuerpo."),
   Exercise("Press militar","Hombros",40,"Mancuernas","Activa el core mientras presionas."),
   Exercise("Remo","Espalda",45,"Mancuernas","Lleva los codos hacia atrás.")
  )),
 Routine("CARDIO BURN","Quema calorías",20,"Intermedio","Sin equipo",210,
  listOf(
   Exercise("Jumping jacks","Cardio",45,"Sin equipo","Aterriza suave."),
   Exercise("Burpees","Cardio",30,"Sin equipo","Prioriza técnica sobre velocidad."),
   Exercise("Rodillas altas","Cardio",40,"Sin equipo","Mantén el torso erguido."),
   Exercise("Skater jumps","Cardio",40,"Sin equipo","Controla cada aterrizaje.")
  )),
 Routine("PIERNAS & GLÚTEOS","Fuerza inferior",30,"Intermedio","Bandas",270,
  listOf(
   Exercise("Sentadilla","Piernas",45,"Bandas","Empuja las rodillas hacia afuera."),
   Exercise("Puente de glúteos","Glúteos",45,"Bandas","Aprieta arriba durante un segundo."),
   Exercise("Zancada atrás","Piernas",40,"Sin equipo","Mantén el peso en el talón delantero."),
   Exercise("Abducción","Glúteos",40,"Bandas","Movimiento controlado.")
  )),
 Routine("CUERPO COMPLETO INICIAL","Base técnica y movilidad",22,"Principiante","Sin equipo",170,listOf(
  Exercise("Calentamiento dinámico","Cuerpo completo",180,"Sin equipo","Marcha, círculos de brazos y movilidad de cadera."),Exercise("Sentadilla a silla","Piernas",40,"Sin equipo","Controla la bajada."),Exercise("Flexión inclinada","Pecho",35,"Sin equipo","Apoya las manos en una superficie firme."),Exercise("Puente de glúteos","Glúteos",40,"Sin equipo","Aprieta arriba."),Exercise("Bird dog","Core",40,"Sin equipo","Mantén la cadera estable."),Exercise("Estiramiento final","Cuerpo completo",180,"Sin equipo","Respira sin rebotes."))),
 Routine("CUERPO COMPLETO AVANZADO","Potencia y resistencia",38,"Avanzado","Sin equipo",390,listOf(
  Exercise("Burpee con flexión","Cuerpo completo",40,"Sin equipo","Prioriza la postura."),Exercise("Sentadilla con salto","Piernas",40,"Sin equipo","Aterriza suave."),Exercise("Flexión diamante","Tríceps",35,"Sin equipo","Codos cerca del cuerpo."),Exercise("Mountain climber cruzado","Core",45,"Sin equipo","Cadera estable."),Exercise("Zancada con salto","Piernas",35,"Sin equipo","Alterna con control."),Exercise("Plancha arriba-abajo","Core",40,"Sin equipo","Evita balancear la cadera."))),
 Routine("PECHO: FLEXIONES","Aperturas y ángulos",26,"Intermedio","Sin equipo",220,listOf(
  Exercise("Flexión clásica","Pecho",40,"Sin equipo","Manos bajo los hombros."),Exercise("Flexión abierta","Pecho",40,"Sin equipo","Manos más abiertas que los hombros."),Exercise("Flexión cerrada","Tríceps",35,"Sin equipo","Codos pegados al torso."),Exercise("Flexión diamante","Pecho y tríceps",30,"Sin equipo","Forma un diamante con las manos."),Exercise("Flexión inclinada","Pecho inferior",40,"Sin equipo","Cuerpo recto."),Exercise("Flexión declinada","Pecho superior",35,"Sin equipo","Pies elevados y abdomen firme."))),
 Routine("DESAFÍO DE PLANCHAS","Core completo",20,"Principiante","Sin equipo",145,listOf(
  Exercise("Plancha frontal","Core",40,"Sin equipo","Cuerpo en línea."),Exercise("Plancha lateral derecha","Oblicuos",30,"Sin equipo","Eleva la cadera."),Exercise("Plancha lateral izquierda","Oblicuos",30,"Sin equipo","Eleva la cadera."),Exercise("Plancha con toque de hombros","Core",40,"Sin equipo","Separa los pies para estabilidad."),Exercise("Plancha arriba-abajo","Core",35,"Sin equipo","Alterna el brazo que inicia."),Exercise("Plancha invertida","Cadena posterior",35,"Sin equipo","Pecho abierto."))),
 Routine("HOMBROS CON MANCUERNAS","Deltoides completos",28,"Intermedio","Mancuernas",230,listOf(Exercise("Press militar","Hombros",45,"Mancuernas","No arquees la espalda."),Exercise("Elevación lateral","Hombros",40,"Mancuernas","Codos suaves."),Exercise("Elevación frontal","Hombros",40,"Mancuernas","Hasta la altura del hombro."),Exercise("Pájaros","Hombro posterior",40,"Mancuernas","Espalda neutra."))),
 Routine("BÍCEPS Y TRÍCEPS","Brazos definidos",30,"Intermedio","Mancuernas",240,listOf(Exercise("Curl alterno","Bíceps",45,"Mancuernas","Sin balanceo."),Exercise("Curl martillo","Bíceps",45,"Mancuernas","Muñeca neutra."),Exercise("Extensión sobre cabeza","Tríceps",45,"Mancuernas","Codos al frente."),Exercise("Patada de tríceps","Tríceps",40,"Mancuernas","Brazo superior inmóvil."))),
 Routine("ESPALDA CON BANDAS","Postura y tracción",27,"Principiante","Bandas",205,listOf(Exercise("Remo sentado","Espalda",45,"Bandas","Junta las escápulas."),Exercise("Jalón al pecho","Dorsales",45,"Bandas","Hombros abajo."),Exercise("Pull apart","Espalda alta",40,"Bandas","Brazos a la altura del pecho."),Exercise("Face pull","Hombro posterior",40,"Bandas","Lleva las manos hacia la cara."))),
 Routine("BANDAS: CUERPO COMPLETO","Fuerza total con resistencia",32,"Intermedio","Bandas",255,listOf(
  Exercise("Sentadilla con banda","Piernas",45,"Bandas","Coloca la banda sobre las rodillas y mantén tensión al bajar."),
  Exercise("Remo de pie con banda","Espalda",45,"Bandas","Pecho abierto y escápulas hacia atrás."),
  Exercise("Press de pecho con banda","Pecho",40,"Bandas","Ancla la banda de forma segura detrás del cuerpo."),
  Exercise("Press de hombros con banda","Hombros",40,"Bandas","Mantén abdomen firme y evita arquear la espalda."),
  Exercise("Curl de bíceps con banda","Bíceps",40,"Bandas","Pisa el centro de la banda y controla el regreso."),
  Exercise("Extensión de tríceps con banda","Tríceps",40,"Bandas","Mantén los codos estables."),
  Exercise("Caminata lateral con banda","Glúteos",45,"Bandas","Pasos cortos y tensión constante."))),
 Routine("BANDAS: PECHO Y HOMBROS","Empuje y estabilidad",27,"Intermedio","Bandas",215,listOf(
  Exercise("Press de pecho con banda","Pecho",45,"Bandas","Anclaje seguro y muñecas neutras."),
  Exercise("Apertura de pecho con banda","Pecho",40,"Bandas","Abre con control sin hiperextender hombros."),
  Exercise("Flexión con banda","Pecho",35,"Bandas","Banda sobre la espalda y cuerpo en línea."),
  Exercise("Press de hombros con banda","Hombros",40,"Bandas","Costillas controladas y core activo."),
  Exercise("Elevación lateral con banda","Hombros",40,"Bandas","Sube hasta la altura del hombro."),
  Exercise("Pull apart","Espalda alta",40,"Bandas","Separa la banda sin elevar los hombros."))),
 Routine("BANDAS: BRAZOS","Bíceps y tríceps",24,"Principiante","Bandas",175,listOf(
  Exercise("Curl de bíceps con banda","Bíceps",45,"Bandas","Codos cerca del torso."),
  Exercise("Curl martillo con banda","Bíceps",40,"Bandas","Muñecas neutras durante todo el movimiento."),
  Exercise("Curl unilateral con banda","Bíceps",40,"Bandas","Trabaja un brazo a la vez sin balancearte."),
  Exercise("Extensión de tríceps sobre cabeza","Tríceps",40,"Bandas","Mantén los codos apuntando al frente."),
  Exercise("Pressdown con banda","Tríceps",45,"Bandas","Anclaje alto y hombros relajados."),
  Exercise("Patada de tríceps con banda","Tríceps",40,"Bandas","Brazo superior estable."))),
 Routine("BANDAS: GLÚTEOS Y PIERNAS","Resistencia inferior",30,"Principiante","Bandas",225,listOf(
  Exercise("Sentadilla con banda","Piernas",45,"Bandas","Empuja suavemente las rodillas hacia afuera."),
  Exercise("Caminata lateral","Glúteos",45,"Bandas","Mantén tensión continua."),
  Exercise("Monster walk","Glúteos",45,"Bandas","Pasos diagonales cortos y controlados."),
  Exercise("Puente de glúteos con banda","Glúteos",45,"Bandas","Pausa un segundo arriba."),
  Exercise("Abducción de cadera","Glúteos",40,"Bandas","No inclines el torso."),
  Exercise("Patada posterior con banda","Glúteos",40,"Bandas","Evita arquear la zona lumbar."),
  Exercise("Peso muerto con banda","Piernas",45,"Bandas","Cadera atrás y espalda neutra."))),
 Routine("BANDAS: MOVILIDAD Y ACTIVACIÓN","Calentamiento y control",18,"Principiante","Bandas",105,listOf(
  Exercise("Pull apart suave","Espalda alta",40,"Bandas","Usa resistencia ligera."),
  Exercise("Rotación externa","Manguito rotador",40,"Bandas","Codo pegado al cuerpo."),
  Exercise("Pasos laterales","Glúteos",40,"Bandas","Rodillas ligeramente flexionadas."),
  Exercise("Puente de glúteos","Glúteos",40,"Bandas","Activa antes de aumentar resistencia."),
  Exercise("Bisagra de cadera","Cadena posterior",40,"Bandas","Practica el patrón con control."))),
 Routine("ABDOMEN TOTAL","Core de 360 grados",24,"Intermedio","Sin equipo",180,listOf(Exercise("Crunch controlado","Abdomen",40,"Sin equipo","No tires del cuello."),Exercise("Elevación de piernas","Abdomen inferior",35,"Sin equipo","Espalda baja apoyada."),Exercise("Bicicleta","Oblicuos",40,"Sin equipo","Gira desde el torso."),Exercise("Dead bug","Core",45,"Sin equipo","Movimiento lento."),Exercise("Hollow hold","Core",30,"Sin equipo","Costillas hacia la pelvis."))),
 Routine("CARDIO INICIAL","Bajo impacto",18,"Principiante","Sin equipo",150,listOf(Exercise("Marcha activa","Cardio",60,"Sin equipo","Mueve también los brazos."),Exercise("Step touch","Cardio",45,"Sin equipo","Paso lateral continuo."),Exercise("Rodillas alternas","Cardio",40,"Sin equipo","Torso erguido."),Exercise("Boxeo al aire","Cardio",45,"Sin equipo","Golpes controlados."))),
 Routine("CARDIO AVANZADO","Intervalos intensos",25,"Avanzado","Sin equipo",330,listOf(Exercise("Burpees","Cardio",35,"Sin equipo","Mantén tu técnica."),Exercise("High knees","Cardio",40,"Sin equipo","Apoyo ligero."),Exercise("Skater jumps","Cardio",40,"Sin equipo","Control lateral."),Exercise("Jumping lunges","Piernas",35,"Sin equipo","Aterriza suave."),Exercise("Mountain climbers","Cardio",45,"Sin equipo","Ritmo estable."))),
 Routine("BANDAS: ESPALDA COMPLETA","Tracción, postura y dorsales",30,"Intermedio","Bandas",230,listOf(
  Exercise("Remo sentado con banda","Espalda",45,"Bandas","Junta las escápulas sin encoger los hombros."),Exercise("Jalón al pecho con banda","Dorsales",45,"Bandas","Usa un anclaje alto firme."),Exercise("Remo unilateral con banda","Espalda",40,"Bandas","Mantén el torso estable."),Exercise("Face pull con banda","Espalda alta",40,"Bandas","Tira hacia la frente con codos abiertos."),Exercise("Pull apart","Espalda alta",40,"Bandas","Separa la banda a la altura del pecho."),Exercise("Buenos días con banda","Cadena posterior",45,"Bandas","Lleva la cadera atrás con espalda neutra."))),
 Routine("BANDAS: CORE ESTABLE","Abdomen contra resistencia",25,"Intermedio","Bandas",185,listOf(
  Exercise("Pallof press","Core",40,"Bandas","Resiste la rotación y mantén el torso al frente."),Exercise("Wood chop con banda","Oblicuos",40,"Bandas","Gira desde el torso con control."),Exercise("Crunch con banda","Abdomen",40,"Bandas","Acerca costillas y pelvis sin tirar del cuello."),Exercise("Dead bug con banda","Core",40,"Bandas","Mantén la espalda baja apoyada."),Exercise("Plancha con remo de banda","Core",35,"Bandas","Evita que la cadera rote."))),
 Routine("BANDAS: POTENCIA AVANZADA","Superseries de cuerpo completo",36,"Avanzado","Bandas",335,listOf(
  Exercise("Sentadilla con press de banda","Cuerpo completo",45,"Bandas","Une la subida con el press sin arquear la espalda."),Exercise("Peso muerto con remo de banda","Cadena posterior",45,"Bandas","Completa la bisagra antes de remar."),Exercise("Zancada con curl de banda","Piernas y bíceps",40,"Bandas","Estabiliza la rodilla delantera."),Exercise("Flexión con banda","Pecho",35,"Bandas","Mantén una línea recta de cabeza a talones."),Exercise("Thruster con banda","Cuerpo completo",40,"Bandas","Impulsa desde las piernas con control."),Exercise("Mountain climber con banda","Core",40,"Bandas","Usa tensión ligera y conserva la postura."))),
 Routine("PECHO COMPLETO MIXTO","Flexiones y mancuernas",34,"Intermedio","Mixto",285,listOf(
  Exercise("Flexión clásica","Pecho",40,"Sin equipo","Cuerpo recto y manos bajo los hombros."),Exercise("Press de pecho","Pecho",45,"Mancuernas","Baja con control y muñecas neutras."),Exercise("Aperturas con mancuernas","Pecho",40,"Mancuernas","Mantén una ligera flexión de codos."),Exercise("Flexión abierta","Pecho",40,"Sin equipo","No abras los codos completamente."),Exercise("Pullover con mancuerna","Pecho y dorsales",40,"Mancuernas","Costillas controladas."))),
 Routine("ESPALDA COMPLETA MIXTA","Fuerza y postura",34,"Intermedio","Mixto",280,listOf(
  Exercise("Remo con mancuernas","Espalda",45,"Mancuernas","Codos hacia la cadera."),Exercise("Jalón al pecho con banda","Dorsales",45,"Bandas","Anclaje alto y seguro."),Exercise("Pájaros con mancuernas","Espalda alta",40,"Mancuernas","Cuello largo y espalda neutra."),Exercise("Pull apart","Espalda alta",40,"Bandas","Hombros lejos de las orejas."),Exercise("Superman","Espalda baja",35,"Sin equipo","Eleva sin comprimir el cuello."))),
 Routine("HOMBROS 360 MIXTO","Deltoide frontal, lateral y posterior",32,"Intermedio","Mixto",245,listOf(
  Exercise("Press militar","Hombros",45,"Mancuernas","Activa el abdomen."),Exercise("Elevación lateral","Hombros",40,"Mancuernas","Sube hasta la línea del hombro."),Exercise("Elevación frontal con banda","Hombros",40,"Bandas","Evita balancearte."),Exercise("Pájaros","Hombro posterior",40,"Mancuernas","Inclina el torso con espalda neutra."),Exercise("Rotación externa","Manguito rotador",40,"Bandas","Usa resistencia ligera."))),
 Routine("PANTORRILLAS Y TOBILLOS","Fuerza y estabilidad inferior",22,"Principiante","Sin equipo",150,listOf(
  Exercise("Elevación de talones","Pantorrillas",45,"Sin equipo","Sube y baja en todo el rango."),Exercise("Elevación unilateral","Pantorrillas",35,"Sin equipo","Usa apoyo para equilibrarte."),Exercise("Puntas hacia adentro","Pantorrillas",40,"Sin equipo","Mantén las rodillas alineadas."),Exercise("Puntas hacia afuera","Pantorrillas",40,"Sin equipo","Movimiento lento y controlado."),Exercise("Movilidad de tobillo","Tobillos",45,"Sin equipo","Lleva la rodilla al frente sin levantar el talón.")))
)

private fun techniqueFor(e:Exercise):ExerciseTechnique{
 val lower=e.muscle.contains("Piernas",true)||e.muscle.contains("Glúte",true)||e.muscle.contains("Pantorr",true)||e.muscle.contains("Cadena",true)
 val core=e.muscle.contains("Core",true)||e.muscle.contains("Abdomen",true)||e.muscle.contains("Oblic",true)
 val cardio=e.muscle.contains("Cardio",true)
 val start=when{core->"Coloca la columna en posición neutra, activa abdomen y glúteos antes de moverte.";lower->"Pies firmes, rodillas alineadas y tronco estable; prepara el equipo sin tensión excesiva.";cardio->"Adopta una postura atlética, abdomen activo y espacio libre alrededor.";else->"Ajusta el equipo, estabiliza el tronco y coloca hombros lejos de las orejas."}
 val steps=when{core->listOf("Activa el abdomen antes de iniciar.","Realiza el recorrido lentamente sin perder la postura.","Regresa con control y repite sin contener la respiración.");lower->listOf("Inicia el movimiento desde cadera, rodillas o tobillos según el ejercicio.","Mantén rodillas alineadas con los pies.","Completa el rango cómodo y vuelve de forma controlada.");cardio->listOf("Comienza a ritmo moderado.","Mantén aterrizajes suaves y postura estable.","Aumenta velocidad solo si conservas la técnica.");else->listOf("Inicia con las articulaciones alineadas.","Mueve la carga o banda sin impulso.","Haz una pausa breve y regresa controlando la resistencia.")}
 val mistakes=when{core->listOf("Arquear la zona lumbar.","Moverse demasiado rápido.","Contener la respiración.");lower->listOf("Dejar que las rodillas colapsen hacia adentro.","Redondear la espalda.","Rebotar al final del recorrido.");cardio->listOf("Aterrizar con rigidez.","Perder la alineación por velocidad.","Ignorar dolor o mareo.");else->listOf("Elevar los hombros.","Usar impulso del tronco.","Elegir demasiada resistencia.")}
 val easy=when{cardio->"Reduce el impacto y realiza la versión caminando.";core->"Acorta el tiempo o apoya rodillas/pies para mayor estabilidad.";lower->"Reduce el rango y usa una superficie firme como apoyo.";else->"Usa menos peso o una banda más ligera y reduce el recorrido."}
 val advanced=when{cardio->"Aumenta el intervalo manteniendo aterrizajes suaves.";core->"Amplía la palanca o añade una pausa isométrica.";lower->"Añade resistencia, tempo lento o una pausa en el punto difícil.";else->"Añade resistencia, tempo 3-1-1 o trabajo unilateral."}
 val view=when{e.name.contains("remo",true)||e.name.contains("peso muerto",true)||e.name.contains("plancha",true)||e.name.contains("flexión",true)->"Lateral";e.muscle.contains("Espalda",true)->"Posterior y frontal";else->"Frontal y lateral"}
 return ExerciseTechnique(start,steps,"Inhala al preparar o regresar; exhala durante el esfuerzo principal.",mistakes,easy,advanced,view)
}

private val achievements = listOf(
 Achievement("Primer paso","Completa tu primer entrenamiento.","🔥",false),
 Achievement("Constante","Completa 5 entrenamientos.","⚡",false),
 Achievement("Racha de 7","Entrena siete días seguidos.","🏆",false),
 Achievement("Fuerza ALX","Quema 1.000 kcal estimadas.","💪",false),
 Achievement("Maestro del core","Completa 10 rutinas de abdomen.","🎯",false)
)

enum class Screen { HOME, ROUTINES, PROGRESS, ACHIEVEMENTS, PROFILE, DETAIL, WORKOUT, ONBOARDING, NUTRITION, CALENDAR }

class MainActivity:ComponentActivity(){
 override fun onCreate(savedInstanceState:Bundle?){super.onCreate(savedInstanceState);setContent{ALXApp(applicationContext)}}
}

@Composable
fun ALXApp(context: android.content.Context){
 val prefs = remember { ALXPrefs(context) }
 var screen by remember{mutableStateOf(if(prefs.onboardingDone) Screen.HOME else Screen.ONBOARDING)}
 var selected by remember{mutableStateOf(routines.first())}
 var completed by remember{mutableIntStateOf(prefs.completed)}
 var weeklyCompleted by remember{mutableIntStateOf(prefs.weeklyCompleted())}
 var totalMinutes by remember{mutableIntStateOf(prefs.totalMinutes)}
 var totalCalories by remember{mutableIntStateOf(prefs.totalCalories)}
 var streak by remember{mutableIntStateOf(prefs.streak)}
 var history by remember{mutableStateOf(prefs.history)}
 var goal by remember{mutableStateOf(prefs.goal)}
 var level by remember{mutableStateOf(prefs.level)}
 var equipment by remember{mutableStateOf(prefs.equipment)}
 var notifications by remember{mutableStateOf(prefs.notifications)}
 var coachVoice by remember{mutableStateOf(prefs.coachVoice)}
 var workoutSounds by remember{mutableStateOf(prefs.workoutSounds)}
 var workoutVibration by remember{mutableStateOf(prefs.workoutVibration)}

 MaterialTheme(colorScheme=darkColorScheme(
  background=Black,surface=Panel,primary=Lime,onPrimary=Black,onBackground=White,onSurface=White
 )){
  Surface(Modifier.fillMaxSize(),color=Black){
   when(screen){
    Screen.ONBOARDING->OnboardingScreen(onDone={prefs.onboardingDone=true;screen=Screen.HOME})
    Screen.HOME->HomeScreen(weeklyCompleted,goal,{selected=it;screen=Screen.DETAIL},{screen=Screen.ROUTINES},{screen=Screen.ACHIEVEMENTS},{screen=Screen.NUTRITION},{screen=Screen.CALENDAR},{screen=Screen.PROFILE})
    Screen.ROUTINES->RoutinesScreen(equipment,{equipment=it;prefs.equipment=it},{selected=it;screen=Screen.DETAIL}){screen=it}
    Screen.PROGRESS->ProgressScreen(completed,weeklyCompleted,totalMinutes,totalCalories,streak,history,goal){screen=it}
    Screen.ACHIEVEMENTS->AchievementsScreen(completed,totalCalories,streak,history){screen=it}
    Screen.NUTRITION->NutritionScreen(goal){screen=Screen.HOME}
    Screen.CALENDAR->CalendarScreen(context,level){screen=Screen.HOME}
    Screen.PROFILE->ProfileScreen(goal,level,equipment,notifications,coachVoice,workoutSounds,workoutVibration,
      {goal=it;prefs.goal=it},{level=it;prefs.level=it},{equipment=it;prefs.equipment=it},{notifications=it;prefs.notifications=it},
      {coachVoice=it;prefs.coachVoice=it},{workoutSounds=it;prefs.workoutSounds=it},{workoutVibration=it;prefs.workoutVibration=it}){screen=it}
    Screen.DETAIL->DetailScreen(selected,{screen=Screen.ROUTINES}){screen=Screen.WORKOUT}
    Screen.WORKOUT->ProfessionalWorkoutScreen(selected,coachVoice,workoutSounds,workoutVibration,{
      prefs.recordWorkout(selected.title,selected.minutes,selected.calories)
      completed=prefs.completed;weeklyCompleted=prefs.weeklyCompleted();totalMinutes=prefs.totalMinutes
      totalCalories=prefs.totalCalories;streak=prefs.streak;history=prefs.history;screen=Screen.PROGRESS
    },{screen=Screen.DETAIL})
   }
  }
 }
}

@Composable fun Header(){
 Row(Modifier.fillMaxWidth().padding(20.dp),horizontalArrangement=Arrangement.SpaceBetween,verticalAlignment=Alignment.CenterVertically){
  Column{Text("ALX",fontSize=28.sp,fontWeight=FontWeight.Black,color=Lime);Text("FITNESS",fontSize=10.sp,fontWeight=FontWeight.Bold,letterSpacing=3.sp)}
  Image(painterResource(R.drawable.alx_avatar),"Avatar ALX",Modifier.size(46.dp).clip(CircleShape),contentScale=ContentScale.Crop)
 }
}
@Composable fun BottomNav(current:Screen,onTab:(Screen)->Unit){
 NavigationBar(containerColor=Panel){
  listOf(Triple(Screen.HOME,Icons.Default.Home,"Inicio"),Triple(Screen.ROUTINES,Icons.Default.FitnessCenter,"Rutinas"),
   Triple(Screen.PROGRESS,Icons.Default.BarChart,"Progreso"),Triple(Screen.PROFILE,Icons.Default.Person,"Perfil")).forEach{(s,i,l)->
   NavigationBarItem(selected=current==s,onClick={onTab(s)},icon={Icon(i,l)},label={Text(l)},
    colors=NavigationBarItemDefaults.colors(selectedIconColor=Lime,selectedTextColor=Lime,indicatorColor=Color.Transparent,unselectedIconColor=Muted,unselectedTextColor=Muted))
  }
 }
}

@Composable fun OnboardingScreen(onDone:()->Unit){
 Box(Modifier.fillMaxSize().background(Black)){
  Image(painterResource(R.drawable.alx_onboarding_bg),"Entrenador ALX en gimnasio",Modifier.fillMaxSize(),contentScale=ContentScale.Crop,alignment=Alignment.Center)
  Box(Modifier.fillMaxSize().background(Brush.horizontalGradient(listOf(Color(0xE608090D),Color(0x5A08090D),Color.Transparent))))
  Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color(0x4208090D),Color.Transparent,Color(0xE608090D)))))
  Column(Modifier.fillMaxSize().padding(horizontal=26.dp,vertical=30.dp)){
   Text("ALX",fontSize=54.sp,fontWeight=FontWeight.Black,color=White)
   Text("FITNESS",fontSize=15.sp,fontWeight=FontWeight.Bold,letterSpacing=5.sp,color=Lime)
   Spacer(Modifier.height(38.dp))
   Text("TRANSFORMA\nTU CUERPO.",fontSize=38.sp,lineHeight=40.sp,fontWeight=FontWeight.Black,color=White)
   Spacer(Modifier.height(12.dp))
   Text("Entrenamientos con equipo o sin equipo.\nTu disciplina, tu ritmo, tu progreso.",color=White,fontSize=16.sp,lineHeight=22.sp)
   Spacer(Modifier.weight(1f))
   Button(onClick=onDone,modifier=Modifier.fillMaxWidth().height(62.dp),colors=ButtonDefaults.buttonColors(containerColor=Lime,contentColor=Black),shape=RoundedCornerShape(20.dp)){
    Text("COMENZAR",fontWeight=FontWeight.Black,fontSize=17.sp)
   }
  }
 }
}

@Composable fun HomeScreen(completed:Int,goal:String,onSelect:(Routine)->Unit,onRoutines:()->Unit,onAchievements:()->Unit,onNutrition:()->Unit,onCalendar:()->Unit,onProfile:()->Unit){
 Scaffold(containerColor=Black,bottomBar={
  BottomNav(Screen.HOME){tab->
   when(tab){Screen.ROUTINES->onRoutines();Screen.ACHIEVEMENTS->onAchievements();Screen.PROFILE->onProfile();else->{}}
  }
 }){pad->
  LazyColumn(Modifier.fillMaxSize().padding(pad)){
   item{Header()}
   item{Column(Modifier.padding(horizontal=20.dp)){
    Text("HOLA, ATLETA",color=Muted,fontSize=12.sp,fontWeight=FontWeight.Bold)
    Text("LISTO PARA ROMPER\nTUS LÍMITES.",fontSize=29.sp,fontWeight=FontWeight.Black)
    Spacer(Modifier.height(16.dp))
    Card(Modifier.fillMaxWidth(),shape=RoundedCornerShape(24.dp),colors=CardDefaults.cardColors(containerColor=Panel2)){
     Box(Modifier.fillMaxWidth().height(265.dp)){
      Image(painterResource(R.drawable.alx_home_hero_bg),"Rutina recomendada Full Body ALX",Modifier.fillMaxSize(),contentScale=ContentScale.Crop,alignment=Alignment.Center)
      Box(Modifier.fillMaxSize().background(Brush.horizontalGradient(listOf(Color(0xF208090D),Color(0xB008090D),Color.Transparent))))
      Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Transparent,Color(0x9A08090D)))))
      Column(Modifier.align(Alignment.BottomStart).padding(20.dp)){
       Text("RECOMENDADO PARA TI",color=Lime,fontSize=11.sp,fontWeight=FontWeight.Bold)
       Text("FULL BODY ALX",fontSize=23.sp,fontWeight=FontWeight.Black)
       Text("28 min  •  240 kcal  •  Intermedio")
       Spacer(Modifier.height(9.dp))
       Button(onClick={onSelect(routines.first())},colors=ButtonDefaults.buttonColors(containerColor=Lime,contentColor=Black),shape=RoundedCornerShape(14.dp)){Text("EMPEZAR",fontWeight=FontWeight.Black)}
      }
     }
    }
    Spacer(Modifier.height(22.dp))
    Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween){
     Text("TU SEMANA",fontSize=18.sp,fontWeight=FontWeight.Bold);Text("$completed sesiones",color=Lime,fontWeight=FontWeight.Bold)
    }
    Spacer(Modifier.height(10.dp))
    Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(7.dp)){repeat(7){d->
     Box(Modifier.weight(1f).height(48.dp).clip(RoundedCornerShape(11.dp)).background(if(d<completed.coerceAtMost(7))Lime else Panel2),contentAlignment=Alignment.Center){
      Text(if(d<completed.coerceAtMost(7))"✓" else "•",color=if(d<completed.coerceAtMost(7))Black else Muted,fontWeight=FontWeight.Bold)
     }}}
    Spacer(Modifier.height(20.dp))
    Text("OBJETIVO ACTUAL",color=Muted,fontSize=11.sp,fontWeight=FontWeight.Bold)
    Text(goal,fontSize=22.sp,fontWeight=FontWeight.Bold,color=Lime)
    Spacer(Modifier.height(20.dp))
    Text("ACCESOS RÁPIDOS",fontSize=18.sp,fontWeight=FontWeight.Bold)
    Spacer(Modifier.height(10.dp))
    Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(10.dp)){
     QuickCard("Rutinas",Icons.Default.FitnessCenter,Modifier.weight(1f)){onRoutines()}
     QuickCard("Nutrición",Icons.Default.Restaurant,Modifier.weight(1f)){onNutrition()}
    }
    Spacer(Modifier.height(10.dp))
    Row(Modifier.fillMaxWidth()){
     QuickCard("Calendario",Icons.Default.CalendarMonth,Modifier.weight(1f)){onCalendar()}
     Spacer(Modifier.width(10.dp))
     QuickCard("Logros",Icons.Default.EmojiEvents,Modifier.weight(1f)){onAchievements()}
    }
    Spacer(Modifier.height(22.dp))
    Text("PARA TI",fontSize=18.sp,fontWeight=FontWeight.Bold)
    Spacer(Modifier.height(8.dp))
   }}
   items(routines.take(3)){RoutineCard(it){onSelect(it)}}
   item{Spacer(Modifier.height(16.dp))}
  }
 }
}

@Composable fun QuickCard(title:String,icon:androidx.compose.ui.graphics.vector.ImageVector,modifier:Modifier=Modifier,onClick:()->Unit){
 Card(modifier.height(100.dp).clickable{onClick()},colors=CardDefaults.cardColors(containerColor=Panel),shape=RoundedCornerShape(18.dp)){
  Column(Modifier.fillMaxSize().padding(15.dp),verticalArrangement=Arrangement.SpaceBetween){Icon(icon,null,tint=Lime);Text(title,fontWeight=FontWeight.Bold)}
 }
}
@Composable fun RoutineCard(r:Routine,onClick:()->Unit){
 Card(Modifier.fillMaxWidth().padding(horizontal=20.dp,vertical=5.dp).clickable{onClick()},colors=CardDefaults.cardColors(containerColor=Panel),shape=RoundedCornerShape(19.dp)){
  Box(Modifier.fillMaxWidth().height(142.dp)){
   Image(painterResource(routineImage(r)),r.title,Modifier.fillMaxSize(),contentScale=ContentScale.Crop)
   Box(Modifier.fillMaxSize().background(Brush.horizontalGradient(listOf(Color(0xF208090D),Color(0xBC08090D),Color(0x3A08090D)))))
   Row(Modifier.fillMaxSize().padding(16.dp),verticalAlignment=Alignment.CenterVertically){
    Column(Modifier.weight(1f)){Text(r.title,fontWeight=FontWeight.Black,fontSize=18.sp);Text(r.subtitle,color=White);Text("${r.minutes} min • ${r.calories} kcal • ${r.equipment}",color=Lime,fontSize=11.sp,fontWeight=FontWeight.Bold)}
    Icon(Icons.Default.ChevronRight,null,tint=White)
   }
  }
 }
}

@Composable fun RoutinesScreen(filter:String,setFilter:(String)->Unit,onSelect:(Routine)->Unit,onTab:(Screen)->Unit){
 Scaffold(containerColor=Black,bottomBar={BottomNav(Screen.ROUTINES,onTab)}){pad->
  LazyColumn(Modifier.fillMaxSize().padding(pad)){
   item{Header();Column(Modifier.padding(horizontal=20.dp)){Text("RUTINAS",fontSize=30.sp,fontWeight=FontWeight.Black);Text("Elige cómo quieres entrenar.",color=Muted);Spacer(Modifier.height(15.dp))
    Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){listOf("Sin equipo","Mancuernas").forEach{f->FilterChip(selected=filter==f,onClick={setFilter(f)},label={Text(f)},colors=FilterChipDefaults.filterChipColors(selectedContainerColor=Lime,selectedLabelColor=Black))}}
    Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){listOf("Bandas","Mixto").forEach{f->FilterChip(selected=filter==f,onClick={setFilter(f)},label={Text(f)},colors=FilterChipDefaults.filterChipColors(selectedContainerColor=Lime,selectedLabelColor=Black))}}
    Spacer(Modifier.height(15.dp))
   }}
   items(routines.filter{it.equipment==filter}){RoutineCard(it){onSelect(it)}}
   item{Column(Modifier.padding(20.dp)){Text("TODAS",color=Muted,fontSize=11.sp,fontWeight=FontWeight.Bold);Spacer(Modifier.height(8.dp))}}
  }
 }
}

@Composable fun DetailScreen(r:Routine,onBack:()->Unit,onStart:()->Unit){
 var techniqueExercise by remember{mutableStateOf<Exercise?>(null)}
 techniqueExercise?.let{ExerciseTechniqueDialog(it){techniqueExercise=null}}
 Column(Modifier.fillMaxSize().background(Black)){
  Box(Modifier.fillMaxWidth().height(285.dp)){Image(painterResource(routineImage(r)),r.title,Modifier.fillMaxSize(),contentScale=ContentScale.Crop)
   Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Transparent,Black))))
   IconButton({onBack()},Modifier.padding(10.dp)){Icon(Icons.Default.ArrowBack,"Atrás",tint=White)}
   Column(Modifier.align(Alignment.BottomStart).padding(20.dp)){Text(r.title,color=Lime,fontSize=27.sp,fontWeight=FontWeight.Black);Text("${r.minutes} min • ${r.calories} kcal • ${r.level}")}}
  LazyColumn(Modifier.weight(1f).padding(horizontal=20.dp)){item{Spacer(Modifier.height(16.dp));Text(r.subtitle,fontSize=18.sp,fontWeight=FontWeight.Bold);Text("${r.equipment} • ${r.exercises.size} ejercicios",color=Muted);Spacer(Modifier.height(12.dp))}
   items(r.exercises){e->Row(Modifier.fillMaxWidth().padding(vertical=9.dp).clickable{techniqueExercise=e},verticalAlignment=Alignment.CenterVertically){
    Box(Modifier.size(42.dp).clip(CircleShape).background(Panel2),contentAlignment=Alignment.Center){Text("${r.exercises.indexOf(e)+1}",color=Lime,fontWeight=FontWeight.Bold)}
    Spacer(Modifier.width(12.dp));Column(Modifier.weight(1f)){Text(e.name,fontWeight=FontWeight.Bold);Text("${e.muscle} • ${e.equipment}",color=Muted,fontSize=12.sp);Text("Ver técnica",color=Lime,fontSize=11.sp)};Text("${e.duration}s",color=Lime,fontWeight=FontWeight.Bold)
   }}
  }
  Button(onClick=onStart,modifier=Modifier.fillMaxWidth().padding(20.dp).height(57.dp),colors=ButtonDefaults.buttonColors(containerColor=Lime,contentColor=Black),shape=RoundedCornerShape(16.dp)){Text("COMENZAR ENTRENAMIENTO",fontWeight=FontWeight.Black)}
 }
}

@Composable fun ExerciseTechniqueDialog(e:Exercise,onDismiss:()->Unit){
 val t=remember(e){techniqueFor(e)}
 AlertDialog(onDismissRequest=onDismiss,title={Column{Text(e.name,fontWeight=FontWeight.Black);Text("FICHA TÉCNICA • ${e.muscle}",color=Lime,fontSize=12.sp,fontWeight=FontWeight.Bold)}},text={Column(Modifier.heightIn(max=490.dp).verticalScroll(rememberScrollState())){
  TechniqueSection("EQUIPO",e.equipment)
  TechniqueSection("VISTA RECOMENDADA",t.view)
  TechniqueSection("POSICIÓN INICIAL",t.start)
  Text("EJECUCIÓN PASO A PASO",color=Lime,fontSize=11.sp,fontWeight=FontWeight.Bold);t.steps.forEachIndexed{i,s->Text("${i+1}. $s",Modifier.padding(top=5.dp))}
  TechniqueSection("RESPIRACIÓN",t.breathing)
  Text("ERRORES FRECUENTES",color=Lime,fontSize=11.sp,fontWeight=FontWeight.Bold);t.mistakes.forEach{Text("• $it",Modifier.padding(top=4.dp))}
  TechniqueSection("VARIANTE FÁCIL",t.easy)
  TechniqueSection("VARIANTE AVANZADA",t.advanced)
  Text("Consejo ALX: ${e.tip}",Modifier.padding(top=8.dp),color=Lime,fontWeight=FontWeight.Bold)
 }},confirmButton={Button(onClick=onDismiss,colors=ButtonDefaults.buttonColors(containerColor=Lime,contentColor=Black)){Text("ENTENDIDO",fontWeight=FontWeight.Black)}})
}
@Composable private fun TechniqueSection(title:String,body:String){Text(title,Modifier.padding(top=10.dp),color=Lime,fontSize=11.sp,fontWeight=FontWeight.Bold);Text(body,Modifier.padding(top=3.dp))}

@Composable fun WorkoutScreen(r:Routine,onFinish:()->Unit){
 var idx by remember{mutableIntStateOf(0)};var sec by remember{mutableIntStateOf(r.exercises.first().duration)}
 val e=r.exercises[idx]
 LaunchedEffect(idx){sec=e.duration;while(sec>0){delay(1000);sec--};if(idx<r.exercises.lastIndex)idx++ else onFinish()}
 Column(Modifier.fillMaxSize().background(Black).padding(20.dp),horizontalAlignment=Alignment.CenterHorizontally){
  Spacer(Modifier.height(18.dp));Text("ALX TRAINING",color=Lime,fontWeight=FontWeight.Black,letterSpacing=2.sp)
  Spacer(Modifier.height(18.dp));Image(painterResource(R.drawable.alx_avatar),"Avatar demostrando ejercicio",Modifier.fillMaxWidth().height(330.dp).clip(RoundedCornerShape(28.dp)),contentScale=ContentScale.Crop)
  Spacer(Modifier.height(20.dp));Text(e.name,fontSize=30.sp,fontWeight=FontWeight.Black);Text(e.tip,color=Muted,textAlign=TextAlign.Center)
  Spacer(Modifier.height(18.dp));Text(String.format("00:%02d",sec),fontSize=65.sp,fontWeight=FontWeight.Black,color=Lime)
  Text("Ejercicio ${idx+1} de ${r.exercises.size}",color=Muted)
  LinearProgressIndicator(progress={(idx+1f)/r.exercises.size},Modifier.fillMaxWidth().padding(vertical=18.dp),color=Lime,trackColor=Panel2)
  Spacer(Modifier.weight(1f))
  Button(onClick={if(idx<r.exercises.lastIndex)idx++ else onFinish()},modifier=Modifier.fillMaxWidth().height(57.dp),colors=ButtonDefaults.buttonColors(containerColor=Lime,contentColor=Black),shape=RoundedCornerShape(16.dp)){Text(if(idx<r.exercises.lastIndex)"SIGUIENTE" else "TERMINAR",fontWeight=FontWeight.Black)}
 }
}

@Composable fun ProgressScreen(completed:Int,weeklyCompleted:Int,totalMinutes:Int,totalCalories:Int,streak:Int,history:List<WorkoutRecord>,goal:String,onTab:(Screen)->Unit){
 Scaffold(containerColor=Black,bottomBar={BottomNav(Screen.PROGRESS,onTab)}){pad->LazyColumn(Modifier.fillMaxSize().padding(pad)){
  item{Column(Modifier.padding(horizontal=20.dp)){Header();Text("TU PROGRESO",fontSize=30.sp,fontWeight=FontWeight.Black);Text("Datos reales guardados en tu dispositivo.",color=Muted);Spacer(Modifier.height(20.dp))
   Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(10.dp)){ProgressMini("$completed","Sesiones",Modifier.weight(1f));ProgressMini("$totalMinutes","Minutos",Modifier.weight(1f));ProgressMini("$streak","Racha",Modifier.weight(1f))}
   Spacer(Modifier.height(12.dp));Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(10.dp)){ProgressMini("$totalCalories","kcal",Modifier.weight(1f));ProgressMini("$weeklyCompleted","Esta semana",Modifier.weight(1f))}
   Spacer(Modifier.height(20.dp));Text("META",color=Muted,fontSize=11.sp,fontWeight=FontWeight.Bold);Text(goal,fontSize=24.sp,color=Lime,fontWeight=FontWeight.Black);Spacer(Modifier.height(15.dp))
   Card(Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=Panel),shape=RoundedCornerShape(20.dp)){Column(Modifier.padding(20.dp)){Text("OBJETIVO SEMANAL",fontWeight=FontWeight.Bold);Text("$weeklyCompleted de 5 días",color=Muted);Spacer(Modifier.height(12.dp));LinearProgressIndicator(progress={(weeklyCompleted.coerceAtMost(5)/5f)},Modifier.fillMaxWidth().height(10.dp),color=Lime,trackColor=Panel2);Spacer(Modifier.height(14.dp));Text(if(weeklyCompleted>=5)"¡Meta cumplida! 🔥" else "Sigue así. Te faltan ${5-weeklyCompleted.coerceAtMost(5)} días.",color=Lime,fontWeight=FontWeight.Bold)}}
   Spacer(Modifier.height(22.dp));Text("HISTORIAL RECIENTE",fontSize=18.sp,fontWeight=FontWeight.Bold);Spacer(Modifier.height(6.dp))
  }}
  if(history.isEmpty()) item{Text("Completa una rutina para comenzar tu historial.",Modifier.padding(horizontal=20.dp,vertical=16.dp),color=Muted)}
  items(history.take(10)){record->
   Card(Modifier.fillMaxWidth().padding(horizontal=20.dp,vertical=5.dp),colors=CardDefaults.cardColors(containerColor=Panel),shape=RoundedCornerShape(16.dp)){
    Row(Modifier.padding(15.dp),verticalAlignment=Alignment.CenterVertically){Icon(Icons.Default.CheckCircle,null,tint=Lime);Spacer(Modifier.width(12.dp));Column(Modifier.weight(1f)){Text(record.title,fontWeight=FontWeight.Bold);Text(record.date,color=Muted,fontSize=12.sp)};Text("${record.minutes} min\n${record.calories} kcal",color=Lime,fontSize=12.sp,textAlign=TextAlign.End)}
   }
  }
  item{Spacer(Modifier.height(18.dp))}
 }}
}
@Composable fun ProgressMini(value:String,label:String,modifier:Modifier=Modifier){Card(modifier.height(105.dp),colors=CardDefaults.cardColors(containerColor=Panel),shape=RoundedCornerShape(18.dp)){Column(Modifier.fillMaxSize().padding(13.dp),verticalArrangement=Arrangement.SpaceBetween){Text(value,fontSize=27.sp,fontWeight=FontWeight.Black,color=Lime);Text(label,color=Muted,fontSize=12.sp)}}}

@Composable fun AchievementsScreen(completed:Int,totalCalories:Int,streak:Int,history:List<WorkoutRecord>,onTab:(Screen)->Unit){
 val coreCount=history.count{it.title.contains("CORE",true)||it.title.contains("ABDOMEN",true)||it.title.contains("PLANCHA",true)}
 val status=listOf(completed>=1,completed>=5,streak>=7,totalCalories>=1000,coreCount>=10)
 Scaffold(containerColor=Black,bottomBar={BottomNav(Screen.HOME,onTab)}){pad->LazyColumn(Modifier.fillMaxSize().padding(pad)){item{Header();Column(Modifier.padding(horizontal=20.dp)){Text("LOGROS",fontSize=30.sp,fontWeight=FontWeight.Black);Text("${status.count{it}} de ${status.size} desbloqueados",color=Muted);Spacer(Modifier.height(8.dp));LinearProgressIndicator(progress={status.count{it}/status.size.toFloat()},Modifier.fillMaxWidth().height(9.dp),color=Lime,trackColor=Panel2);Spacer(Modifier.height(16.dp))}}
  items(achievements.mapIndexed{index,a->a.copy(unlocked=status[index])}){a->
   Card(Modifier.fillMaxWidth().padding(horizontal=20.dp,vertical=6.dp),colors=CardDefaults.cardColors(containerColor=if(a.unlocked)Panel else Color(0xFF0E1015)),shape=RoundedCornerShape(18.dp)){
    Row(Modifier.padding(17.dp),verticalAlignment=Alignment.CenterVertically){Text(a.icon,fontSize=30.sp);Spacer(Modifier.width(15.dp));Column(Modifier.weight(1f)){Text(a.title,fontWeight=FontWeight.Black,color=if(a.unlocked)Lime else Muted);Text(a.desc,color=Muted,fontSize=13.sp)};if(a.unlocked)Icon(Icons.Default.CheckCircle,null,tint=Lime)else Icon(Icons.Default.Lock,null,tint=Muted)}}
  }
 }}
}

@Composable fun ProfileScreen(goal:String,level:String,equipment:String,notifications:Boolean,coachVoice:Boolean,workoutSounds:Boolean,workoutVibration:Boolean,setGoal:(String)->Unit,setLevel:(String)->Unit,setEquipment:(String)->Unit,setNotifications:(Boolean)->Unit,setCoachVoice:(Boolean)->Unit,setWorkoutSounds:(Boolean)->Unit,setWorkoutVibration:(Boolean)->Unit,onTab:(Screen)->Unit){
 Scaffold(containerColor=Black,bottomBar={BottomNav(Screen.PROFILE,onTab)}){pad->Column(Modifier.fillMaxSize().padding(pad).verticalScroll(rememberScrollState()).padding(20.dp)){
  Header();Row(verticalAlignment=Alignment.CenterVertically){Image(painterResource(R.drawable.alx_avatar),null,Modifier.size(85.dp).clip(CircleShape),contentScale=ContentScale.Crop);Spacer(Modifier.width(16.dp));Column{Text("ATLETA ALX",fontSize=22.sp,fontWeight=FontWeight.Black);Text("Perfil de entrenamiento",color=Muted)}}
  Spacer(Modifier.height(25.dp));SettingTitle("OBJETIVO")
  listOf("Tonificar","Aumentar masa muscular","Perder grasa","Mejorar condición").forEach{OptionChip(it,goal==it){setGoal(it)}}
  SettingTitle("NIVEL")
  listOf("Principiante","Intermedio","Avanzado").forEach{OptionChip(it,level==it){setLevel(it)}}
  SettingTitle("EQUIPO DISPONIBLE")
  listOf("Sin equipo","Mancuernas","Bandas","Mixto").forEach{OptionChip(it,equipment==it){setEquipment(it)}}
  SettingTitle("RECORDATORIOS")
  Row(Modifier.fillMaxWidth().padding(vertical=5.dp),verticalAlignment=Alignment.CenterVertically){Column(Modifier.weight(1f)){Text("Recordatorios diarios",fontWeight=FontWeight.Bold);Text("Mantén tu racha activa",color=Muted,fontSize=13.sp)};Switch(checked=notifications,onCheckedChange=setNotifications)}
  SettingTitle("ENTRENADOR")
  SettingSwitch("Voz del entrenador","Preparación, ejercicio, descanso y finalización",coachVoice,setCoachVoice)
  SettingSwitch("Señales de sonido","Avisos breves durante la rutina",workoutSounds,setWorkoutSounds)
  SettingSwitch("Vibración","Confirmación háptica en los cambios",workoutVibration,setWorkoutVibration)
  Spacer(Modifier.height(25.dp));Text("ALX FITNESS v5.3-dev",color=Muted,fontSize=12.sp)
 }}
}
@Composable fun SettingTitle(t:String){Spacer(Modifier.height(20.dp));Text(t,color=Muted,fontSize=11.sp,fontWeight=FontWeight.Bold);Spacer(Modifier.height(7.dp))}
@Composable fun OptionChip(t:String,selected:Boolean,onClick:()->Unit){FilterChip(selected=selected,onClick=onClick,label={Text(t)},modifier=Modifier.padding(end=7.dp,bottom=6.dp),colors=FilterChipDefaults.filterChipColors(selectedContainerColor=Lime,selectedLabelColor=Black))}
@Composable fun SettingSwitch(title:String,subtitle:String,checked:Boolean,onChecked:(Boolean)->Unit){Row(Modifier.fillMaxWidth().padding(vertical=5.dp),verticalAlignment=Alignment.CenterVertically){Column(Modifier.weight(1f)){Text(title,fontWeight=FontWeight.Bold);Text(subtitle,color=Muted,fontSize=13.sp)};Switch(checked=checked,onCheckedChange=onChecked)}}

package com.alx.fitness

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.speech.tts.TextToSpeech
import android.view.ViewGroup
import android.widget.VideoView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.delay
import java.text.Normalizer
import java.util.Locale

private val TrainingBlack = Color(0xFF08090D)
private val TrainingPanel = Color(0xFF171A22)
private val TrainingLime = Color(0xFF16C7F3)
private val TrainingMuted = Color(0xFF9CA3AF)
private const val REST_SECONDS = 20

private enum class TrainingPhase { EXERCISE, REST }

private fun mediaKey(name:String):String = Normalizer.normalize(name.lowercase(Locale.ROOT),Normalizer.Form.NFD)
    .replace("\\p{Mn}+".toRegex(),"").replace("[^a-z0-9]+".toRegex(),"_").trim('_')

private fun animationPrefix(exercise:Exercise):Pair<String,Int> = when {
 exercise.muscle.contains("Cardio",true)||exercise.name.contains("burpee",true)||exercise.name.contains("jump",true)||exercise.name.contains("rodillas",true)||exercise.name.contains("marcha",true) -> "alx_cardio" to 16
 exercise.muscle.contains("Core",true)||exercise.muscle.contains("Abdomen",true)||exercise.muscle.contains("Oblic",true)||exercise.name.contains("plancha",true)||exercise.name.contains("crunch",true) -> "alx_core" to 16
 exercise.equipment.contains("Mancuerna",true)||exercise.equipment.contains("Banda",true)||exercise.equipment.contains("Mixto",true) -> "alx_weights" to 16
 else -> "alx_push" to 15
}

@Composable
private fun ExerciseFrameAnimation(exercise:Exercise,paused:Boolean){
 val context=LocalContext.current
 val (prefix,count)=remember(exercise.name,exercise.muscle,exercise.equipment){animationPrefix(exercise)}
 val frames=remember(prefix){(0 until count).mapNotNull{index->
  context.resources.getIdentifier("${prefix}_${index.toString().padStart(2,'0')}","drawable",context.packageName).takeIf{it!=0}
 }}
 var frame by remember(prefix){mutableIntStateOf(0)}
 LaunchedEffect(prefix,paused){
  if(!paused&&frames.isNotEmpty())while(true){delay(90);frame=(frame+1)%frames.size}
 }
 Box(Modifier.fillMaxWidth().height(300.dp).clip(RoundedCornerShape(26.dp)).background(TrainingPanel)){
  if(frames.isNotEmpty()) Image(painterResource(frames[frame]),"Demostración animada de ${exercise.name}",Modifier.fillMaxSize(),contentScale=ContentScale.Fit)
  else Image(painterResource(R.drawable.alx_avatar),"Demostración de ${exercise.name}",Modifier.fillMaxSize(),contentScale=ContentScale.Crop)
  Surface(Modifier.align(Alignment.BottomCenter).padding(10.dp),color=Color(0xB808090D),shape=RoundedCornerShape(12.dp)){
   Text(if(frames.isEmpty())"DEMOSTRACIÓN NO DISPONIBLE" else "DEMOSTRACIÓN EN MOVIMIENTO",Modifier.padding(horizontal=12.dp,vertical=6.dp),fontSize=10.sp,color=TrainingLime,fontWeight=FontWeight.Bold)
  }
 }
}

@Composable
private fun ExerciseMedia(exercise:Exercise,paused:Boolean){
 val context=LocalContext.current
 val rawId=remember(exercise.name){context.resources.getIdentifier("exercise_${mediaKey(exercise.name)}","raw",context.packageName)}
 if(rawId==0){
  ExerciseFrameAnimation(exercise,paused)
 }else{
  AndroidView(factory={ctx->VideoView(ctx).apply{layoutParams=ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);setVideoURI(Uri.parse("android.resource://${ctx.packageName}/$rawId"));setOnPreparedListener{it.isLooping=true;start()}}},update={if(paused)it.pause() else it.start()},modifier=Modifier.fillMaxWidth().height(300.dp).clip(RoundedCornerShape(26.dp)))
 }
}

@Composable
fun ProfessionalWorkoutScreen(r:Routine,voiceEnabled:Boolean,soundEnabled:Boolean,vibrationEnabled:Boolean,onFinish:()->Unit,onExit:()->Unit){
 val context=LocalContext.current
 var idx by remember{mutableIntStateOf(0)}
 var phase by remember{mutableStateOf(TrainingPhase.EXERCISE)}
 var seconds by remember{mutableIntStateOf(r.exercises.first().duration)}
 var paused by remember{mutableStateOf(false)}
 var showExit by remember{mutableStateOf(false)}
 val exercise=r.exercises[idx]
 val tts=remember{TextToSpeech(context){}}
 val tone=remember{ToneGenerator(AudioManager.STREAM_MUSIC,70)}

 DisposableEffect(Unit){tts.language=Locale("es","ES");onDispose{tts.stop();tts.shutdown();tone.release()}}
 fun signal(text:String){if(voiceEnabled)tts.speak(text,TextToSpeech.QUEUE_FLUSH,null,"alx-${System.nanoTime()}");if(soundEnabled)tone.startTone(ToneGenerator.TONE_PROP_BEEP,130);if(vibrationEnabled)vibrate(context)}
 fun startExercise(newIndex:Int){idx=newIndex;phase=TrainingPhase.EXERCISE;seconds=r.exercises[newIndex].duration;paused=false;signal("Comienza ${r.exercises[newIndex].name}")}
 fun advance(){if(idx==r.exercises.lastIndex){signal("Entrenamiento finalizado");onFinish()}else{phase=TrainingPhase.REST;seconds=REST_SECONDS;paused=false;signal("Descanso. Siguiente ejercicio ${r.exercises[idx+1].name}")}}

 LaunchedEffect(Unit){delay(500);signal("Prepárate. 3, 2, 1. Comienza ${r.exercises.first().name}")}

 LaunchedEffect(idx,phase,paused){
  if(!paused){while(seconds>0){delay(1000);seconds--};if(phase==TrainingPhase.EXERCISE)advance() else startExercise(idx+1)}
 }

 if(showExit){AlertDialog(onDismissRequest={showExit=false},icon={Icon(Icons.Default.StopCircle,null)},title={Text("¿Detener la rutina?")},text={Text("El entrenamiento actual no se guardará como completado.")},confirmButton={Button(onClick=onExit,colors=ButtonDefaults.buttonColors(containerColor=Color(0xFFFF5964))){Text("DETENER")}},dismissButton={TextButton(onClick={showExit=false}){Text("CONTINUAR")}})}

 Column(Modifier.fillMaxSize().background(TrainingBlack).padding(18.dp),horizontalAlignment=Alignment.CenterHorizontally){
  Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween,verticalAlignment=Alignment.CenterVertically){IconButton(onClick={showExit=true}){Icon(Icons.Default.Close,"Salir")};Text(if(phase==TrainingPhase.REST)"DESCANSO" else "ALX TRAINING",color=TrainingLime,fontWeight=FontWeight.Black,letterSpacing=2.sp);Text("${idx+1}/${r.exercises.size}",color=TrainingMuted)}
  if(phase==TrainingPhase.EXERCISE){
   ExerciseMedia(exercise,paused)
   Spacer(Modifier.height(14.dp));Text(exercise.name,fontSize=27.sp,fontWeight=FontWeight.Black,textAlign=TextAlign.Center);Text(exercise.tip,color=TrainingMuted,textAlign=TextAlign.Center)
  }else{
   Box(Modifier.fillMaxWidth().height(300.dp).clip(RoundedCornerShape(26.dp)).background(TrainingPanel),contentAlignment=Alignment.Center){Column(horizontalAlignment=Alignment.CenterHorizontally){Icon(Icons.Default.HourglassBottom,null,tint=TrainingLime,modifier=Modifier.size(70.dp));Text("RECUPÉRATE",fontSize=28.sp,fontWeight=FontWeight.Black);Text("Siguiente: ${r.exercises[idx+1].name}",color=TrainingMuted,textAlign=TextAlign.Center)}}
   Spacer(Modifier.height(14.dp));Text("Respira y prepárate",fontSize=22.sp,fontWeight=FontWeight.Bold)
  }
  Text(String.format("%02d:%02d",seconds/60,seconds%60),fontSize=58.sp,fontWeight=FontWeight.Black,color=TrainingLime)
  LinearProgressIndicator(progress={(idx+(if(phase==TrainingPhase.REST)1f else .35f))/r.exercises.size},Modifier.fillMaxWidth().padding(vertical=10.dp),color=TrainingLime,trackColor=TrainingPanel)
  Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceEvenly){
   TrainingControl(Icons.Default.SkipPrevious,"Anterior",idx>0){startExercise(idx-1)}
   TrainingControl(Icons.Default.Replay,"Repetir",phase==TrainingPhase.EXERCISE){startExercise(idx)}
   FilledIconButton(onClick={paused=!paused},modifier=Modifier.size(64.dp),colors=IconButtonDefaults.filledIconButtonColors(containerColor=TrainingLime,contentColor=TrainingBlack)){Icon(if(paused)Icons.Default.PlayArrow else Icons.Default.Pause,if(paused)"Reanudar" else "Pausar",Modifier.size(34.dp))}
   TrainingControl(Icons.Default.SkipNext,"Siguiente",true){if(phase==TrainingPhase.REST)startExercise(idx+1) else advance()}
  }
  Spacer(Modifier.weight(1f));Text(if(paused)"RUTINA EN PAUSA" else if(phase==TrainingPhase.REST)"DESCANSO AUTOMÁTICO DE ${REST_SECONDS}s" else "Temporizador independiente del video",color=if(paused)TrainingLime else TrainingMuted,fontSize=12.sp,fontWeight=FontWeight.Bold)
 }
}

@Composable private fun TrainingControl(icon:androidx.compose.ui.graphics.vector.ImageVector,label:String,enabled:Boolean,onClick:()->Unit){Column(horizontalAlignment=Alignment.CenterHorizontally){IconButton(onClick=onClick,enabled=enabled,modifier=Modifier.clip(CircleShape).background(TrainingPanel)){Icon(icon,label,tint=if(enabled)Color.White else TrainingMuted)};Text(label,fontSize=10.sp,color=TrainingMuted)}}

private fun vibrate(context:Context){val vibrator=context.getSystemService(Vibrator::class.java)?:return;if(Build.VERSION.SDK_INT>=26)vibrator.vibrate(VibrationEffect.createOneShot(90,VibrationEffect.DEFAULT_AMPLITUDE))else @Suppress("DEPRECATION") vibrator.vibrate(90)}

# Guía de videos locales ALX Fitness

Los MP4 se colocan en `app/src/main/res/raw/`. El motor busca automáticamente un archivo llamado `exercise_<nombre_normalizado>.mp4`.

Ejemplos:

- `Sentadillas` → `exercise_sentadillas.mp4`
- `Flexión con banda` → `exercise_flexion_con_banda.mp4`
- `Curl de bíceps` → `exercise_curl_de_biceps.mp4`

Mientras no exista un MP4 específico, la pantalla reproduce una secuencia animada local según el tipo de ejercicio (cardio, core, fuerza o empuje). El avatar fijo solo se utiliza si tampoco se encuentra esa secuencia.

Recomendaciones: clip corto, sin audio, encuadre vertical, repetición visual continua y técnica completa visible. La duración de la serie, la pausa y el descanso los controla la aplicación.

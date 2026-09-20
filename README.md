# ALX Fitness 5.4 — corrección de recursos visuales

App fitness Android local-first, con estética futurista/deportiva/premium.

## Corrección visual 5.4
- Las tarjetas y los detalles de las rutinas muestran imágenes deportivas por categoría.
- El entrenamiento reproduce animaciones locales para cardio, core, fuerza y empuje.
- Un video MP4 específico en `res/raw` sigue teniendo prioridad cuando esté disponible.
- Los recursos funcionan sin conexión y no dependen de ChatGPT.

## Funciones
- Onboarding / portada ALX.
- Avatar del entrenador basado en la imagen de referencia.
- Inicio personalizado.
- Objetivo configurable: tonificar, fuerza, pérdida de grasa o condición.
- Nivel: principiante, intermedio y avanzado.
- Rutinas con y sin equipo.
- Filtros por equipo.
- 27 programas: cuerpo completo por nivel, pecho, espalda, hombros, brazos, abdomen, pantorrillas, cardio y una biblioteca ampliada con bandas.
- Rutinas con equipo mixto y filtros por equipo disponible.
- Fichas técnicas por ejercicio con ejecución, respiración, errores y variantes.
- Plan nutricional semanal con opciones para aumentar masa muscular o perder grasa.
- Calendario semanal con recomendación de 3, 4 o 5 días según el nivel.
- Área del avatar ampliada a pantalla completa durante cada ejercicio.
- Ficha detallada con ejercicios, músculos, equipo y consejos.
- Modo entrenamiento con temporizador y barra de progreso.
- Progreso semanal, sesiones, minutos, racha e historial.
- Sistema de logros.
- Perfil y preferencias.
- Recordatorios (preferencia local visual).
- Arquitectura preparada para añadir backend, Room, Firebase, autenticación, videos y pagos.

## Abrir en Android Studio
1. Descomprime `ALX_Fitness_Android.zip`.
2. Abre la carpeta `ALX_Fitness`.
3. Sincroniza Gradle.
4. Ejecuta en un emulador o teléfono Android.

## Para producción
La siguiente fase debería añadir:
- Room/DataStore para persistencia real.
- Firebase/Auth para cuentas y sincronización.
- Notificaciones programadas.
- Videos/animaciones de cada ejercicio.
- Planificador inteligente según objetivos.
- Medidas corporales y gráficas.
- Suscripción Premium / Google Play Billing.
- Backend y panel administrativo.
- Pruebas y publicación en Google Play.

# ALX Fitness 5.0 - Integración iniciada

Base: proyecto Android ALX Fitness GitHub.

## Integrado en este bloque
- 5 rutinas nuevas específicas con bandas elásticas.
- 31 entradas iniciales en el catálogo audiovisual, con vista preferida y vestuario del avatar.
- Convención de video: MP4 corto en bucle; temporizador y navegación permanecen controlados por la app.
- Avatar objetivo: 3D realista basado en la fotografía de referencia del usuario; físico delgado, atlético y natural.
- En pecho, espalda, hombros, brazos y core se permite versión deportiva sin camisa cuando ayude a visualizar la técnica.

## Pendiente del siguiente bloque
- Producir y aprobar el avatar maestro frontal/lateral/posterior.
- Producir los MP4; el catálogo marca todos como pendientes para no confundir recursos planificados con recursos existentes.
- Integrar reproductor local de video por ejercicio y fallback a imagen.
- Añadir pausa/reanudar/repetir/retroceder y descansos.
- Voz de entrenador y cuenta regresiva.
- Ampliar rutinas por músculo, nivel, objetivo y equipo.

## Integrado en 5.1-dev
- Motor local de video por identificador estable: `exercise_<nombre_normalizado>` dentro de `res/raw`.
- Reproducción MP4 en bucle y avatar como respaldo automático cuando el archivo todavía no existe.
- Temporizador independiente del video.
- Pausa y reanudación de rutina y video.
- Repetir ejercicio, volver al anterior y avanzar al siguiente.
- Descanso automático de 20 segundos con vista del siguiente ejercicio.
- Detener rutina con confirmación y regreso seguro al detalle.
- Voz del entrenador, señal sonora y vibración configurables desde Perfil.
- Avisos de inicio, descanso, siguiente ejercicio y finalización.

Los videos continúan marcados como pendientes hasta que cada MP4 real sea producido, asociado y probado.

## Integrado en 5.2-dev
- Espalda completa con bandas.
- Core con bandas.
- Rutina avanzada de bandas con superseries de cuerpo completo.
- Pecho completo mixto con flexiones y mancuernas.
- Espalda completa mixta.
- Hombros 360 mixto.
- Rutina específica de pantorrillas y movilidad de tobillos.
- Filtro visible para rutinas de equipo mixto.
- Ficha técnica accesible desde cada ejercicio: equipo, posición inicial, ejecución paso a paso, respiración, errores frecuentes, variante fácil, variante avanzada y vista recomendada.

## Integrado en 5.3-dev
- Nueva portada inicial fotorealista con entrenador completo y composición azul neón.
- Nueva portada interna para la rutina recomendada Full Body ALX.
- Textos y botones implementados como controles Android reales, no incrustados en las fotografías.
- Sustitución del color verde lima por azul cian en navegación, controles, progreso y entrenador.
- Recursos gráficos independientes optimizados para reutilización dentro del proyecto.
- Flujo de compilación automática para generar APK de depuración y AAB de publicación.

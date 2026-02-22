# Checklist de despliegue seguro y rollback

## Antes del despliegue
- [ ] Definir `PLANGASTOS_AUTH_JWT_SECRET` nuevo (mínimo 32 bytes) en el gestor de secretos del entorno.
- [ ] Mantener la llave anterior en `PLANGASTOS_AUTH_JWT_PREVIOUS_SECRET` para permitir validación durante rotación.
- [ ] Verificar que `PLANGASTOS_AUTH_JWT_EXPIRATION_SECONDS` esté dentro de la política (`PLANGASTOS_AUTH_JWT_MIN_EXPIRATION_SECONDS` y `PLANGASTOS_AUTH_JWT_MAX_EXPIRATION_SECONDS`).
- [ ] Configurar límites de login (`PLANGASTOS_AUTH_LOGIN_RATE_LIMIT_*`) según perfil de riesgo del entorno.
- [ ] Confirmar que logs de `AuthEventLogger` sean recolectados por SIEM (eventos `LOGIN_SUCCESS` y `LOGIN_FAILURE`).

## Durante el despliegue
- [ ] Desplegar primero en staging con secretos equivalentes al entorno productivo.
- [ ] Ejecutar smoke test de autenticación: login exitoso, login inválido, acceso con token.
- [ ] Validar que no se registren datos sensibles (sin password ni token en logs).
- [ ] Monitorear métricas de 401/429-equivalente funcional (bloqueos por rate limit) durante la ventana inicial.

## Después del despliegue
- [ ] Confirmar que tokens emitidos con la nueva llave sean aceptados.
- [ ] Mantener llave previa por una ventana de transición (ej. TTL máximo del token + margen operativo).
- [ ] Retirar `PLANGASTOS_AUTH_JWT_PREVIOUS_SECRET` una vez finalizada la transición.
- [ ] Registrar evidencia del cambio (ticket, versión desplegada, timestamp, operador).

## Plan de rollback
1. Restaurar versión anterior de la aplicación.
2. Reestablecer variables de entorno previas (llave activa y configuración de rate limit conocida).
3. Reiniciar instancias y validar `/auth/login` + endpoint protegido.
4. Revisar logs de auth para confirmar recuperación estable.
5. Comunicar incidente y estado de rollback al equipo.

## Criterios para activar rollback
- Aumento sostenido de errores de autenticación no esperados.
- Fallo de validación de tokens recién emitidos.
- Comportamiento anómalo del rate limiting que afecte usuarios legítimos.

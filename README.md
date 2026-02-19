# plangastos-api
Plan Gastos (Spring Boot Backend API).

## Migraciones

El proyecto utiliza scripts SQL versionados en `src/main/resources/db/migration`.
La migración `V5__create_usuarios_and_foreign_keys.sql` agrega:

- tabla `usuarios` con unicidad en `email`.
- claves foráneas nombradas de forma consistente para entidades relacionales (`rubros`, `cuentas_financieras`, `presupuestos`, `partidas_planificadas`, `transacciones`).

## Seed opcional para desarrollo

Existe un seed de usuarios de prueba exclusivo para **entorno local**:

- Clase: `DevUsuarioSeedConfig`
- Perfil requerido: `dev`
- Flag requerido: `plangastos.seed.dev-users.enabled=true`

Ejemplo de ejecución:

```bash
SPRING_PROFILES_ACTIVE=dev ./mvnw spring-boot:run
```

## Credenciales de prueba (solo local/dev)

> ⚠️ Estas credenciales son para desarrollo local. No usar ni desplegar en producción.

- `dev.admin@plangastos.local` / `Admin123!`
- `dev.user@plangastos.local` / `User123!`

En base de datos se guardan como hashes BCrypt en el seed.

La migración `V6__enforce_usuario_not_null_on_core_entities.sql` agrega `usuario_id` obligatorio en `cuentas_financieras`, `rubros`, `presupuestos` y `partidas_planificadas`, rellena registros históricos nulos o inválidos con un usuario de migración y aplica claves foráneas con `ON DELETE RESTRICT` para impedir eliminar usuarios con datos asociados.

## Contrato API de autenticación

### Endpoints

#### `POST /auth/login`

Request (`application/json`):

```json
{
  "email": "dev.user@plangastos.local",
  "password": "User123!"
}
```

Response exitosa (`200 OK`):

```json
{
  "success": true,
  "message": "Login exitoso",
  "data": {
    "accessToken": "<jwt>",
    "tokenType": "Bearer",
    "expiresAt": "2026-01-01T12:00:00Z"
  },
  "timestamp": "2026-01-01T10:00:00Z"
}
```

#### `POST /auth/register`

Request (`application/json`):

```json
{
  "email": "nuevo.usuario@plangastos.local",
  "password": "Password123!"
}
```

Response exitosa (`200 OK`):

```json
{
  "success": true,
  "message": "Usuario registrado",
  "data": {
    "accessToken": "<jwt>",
    "tokenType": "Bearer",
    "expiresAt": "2026-01-01T12:00:00Z"
  },
  "timestamp": "2026-01-01T10:00:00Z"
}
```

### Códigos de error (auth)

Formato de error estándar:

```json
{
  "timestamp": "2026-01-01T10:00:00",
  "status": 401,
  "error": "Unauthorized",
  "messages": [
    {
      "field": "auth",
      "message": "Credenciales inválidas"
    }
  ],
  "errorCode": "UNAUTHORIZED",
  "path": "/auth/login"
}
```

Errores relevantes:

- `400 Bad Request`: request inválido (JSON mal formado, validaciones de `email/password`).
- `401 Unauthorized`: credenciales inválidas, usuario inactivo, token ausente/inválido en rutas protegidas.
- `403 Forbidden`: usuario autenticado sin permisos para acceder a un recurso.
- `409 Conflict`: violación de integridad (ej. constraints en base de datos).
- `422 Unprocessable Entity`: reglas de negocio (ej. email ya registrado en `/auth/register`).

## Autenticación en endpoints protegidos

El front debe enviar el header `Authorization` con esquema Bearer en **cada request protegida**:

```http
Authorization: Bearer <accessToken>
```

Si el header no existe, no comienza con `Bearer `, o el JWT es inválido/expirado, la API responde `401 Unauthorized`.

## Contexto de usuario en endpoints de negocio

Los endpoints de negocio protegidos filtran por el usuario autenticado (`@AuthenticationPrincipal CurrentUser`) y **ya no requieren `usuarioId` como query/path param enviado por cliente** para listar/consultar datos del usuario.

Ejemplos:

- `GET /api/v1/presupuesto`
- `GET /api/v1/rubro`
- `GET /api/v1/cuenta-financiera`
- `GET /api/v1/partida-planificada`

## Ejemplos de consumo (login + listado por usuario)

### 1) Login

```bash
curl -X POST 'http://localhost:8080/auth/login' \
  -H 'Content-Type: application/json' \
  -d '{
    "email": "dev.user@plangastos.local",
    "password": "User123!"
  }'
```

### 2) Listado de presupuestos del usuario autenticado

```bash
curl -X GET 'http://localhost:8080/api/v1/presupuesto' \
  -H 'Authorization: Bearer <accessToken>'
```

### 3) Listado de rubros del usuario autenticado

```bash
curl -X GET 'http://localhost:8080/api/v1/rubro' \
  -H 'Authorization: Bearer <accessToken>'
```


## Endurecimiento de autenticación

### Rotación de secretos JWT por entorno

La API soporta llave activa + llave previa para rotación sin corte:

- `PLANGASTOS_AUTH_JWT_SECRET`: llave activa para firmar.
- `PLANGASTOS_AUTH_JWT_PREVIOUS_SECRET`: llave anterior usada solo para validar tokens durante transición.
- `plangastos.auth.jwt.validation-secrets[]`: lista interna de llaves de validación (activa + previa).

Recomendación: rotar por entorno (dev/staging/prod) con llaves distintas y ciclo periódico (ej. cada 60-90 días).

### Política de expiración de tokens

- `PLANGASTOS_AUTH_JWT_EXPIRATION_SECONDS` define TTL del access token.
- `PLANGASTOS_AUTH_JWT_MIN_EXPIRATION_SECONDS` / `PLANGASTOS_AUTH_JWT_MAX_EXPIRATION_SECONDS` definen límites permitidos.
- Si el TTL configurado queda fuera del rango, la app falla al iniciar para evitar configuración insegura.

### Logging de eventos de auth

Se registran eventos estructurados para login exitoso/fallido mediante `AuthEventLogger`:

- `auth_event=LOGIN_SUCCESS`
- `auth_event=LOGIN_FAILURE`

Los logs no exponen credenciales ni tokens: solo hash truncado del principal, IP y user-agent saneado.

### Rate limiting de login

Configuración disponible:

- `PLANGASTOS_AUTH_LOGIN_RATE_LIMIT_ENABLED`
- `PLANGASTOS_AUTH_LOGIN_RATE_LIMIT_MAX_ATTEMPTS`
- `PLANGASTOS_AUTH_LOGIN_RATE_LIMIT_WINDOW_SECONDS`
- `PLANGASTOS_AUTH_LOGIN_RATE_LIMIT_BLOCK_SECONDS`

Cuando se supera el umbral, el login se bloquea temporalmente para mitigar fuerza bruta.

## Checklist de despliegue seguro y rollback

Ver `docs/security-deployment-checklist.md`.

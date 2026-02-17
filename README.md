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

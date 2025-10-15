# Ticarum - Servicio de competiciones (UMU)

## Ejecución
- Requisitos: Java 17, Maven
- Ejecutar: `mvn spring-boot:run`
- H2 console: `/h2-console` (JDBC: `jdbc:h2:mem:ticarumdb`)
- Swagger UI: `/swagger-ui.html`

## Endpoints principales
- POST `/api/competitions` crea competición
- POST `/api/competitions/{id}/teams` registra equipo
- GET `/api/competitions/{id}/teams` lista equipos
- POST `/api/competitions/{id}/firstday/generate` genera jornada 1
- GET `/api/competitions/{id}/firstday/matches` partidos
- GET `/api/competitions/{id}/firstday/unassigned` equipos no asignados

## Datos iniciales
- `src/main/resources/data.sql` crea una competición "Liga UMU" con días y 6 equipos.

## Notas
- Por día y pista se programan como máximo 2 partidos.
- Si no cabe toda la primera jornada, se devuelven partidos asignados y equipos no asignados.
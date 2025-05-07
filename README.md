# Servicio Académico

## Descripción

El Servicio Académico es una aplicación diseñada para la gestión educativa que permite administrar estudiantes, cursos y matrículas. Esta API REST facilita la operación eficiente de instituciones educativas mediante un sistema modular y extensible.

## Características principales

- Gestión completa de estudiantes
- Administración de cursos académicos
- Sistema de matrículas y seguimiento
- API REST para integración con otros sistemas
- Arquitectura basada en microservicios

## Requisitos previos

- Java 21 o superior
- Maven 3.6 o superior
- Base de datos compatible con JPA (MySQL, PostgreSQL, H2, etc.)

## Instalación

1. Clone el repositorio:
   ```bash
   git clone https://github.com/tu-usuario/academic-service.git
   cd academic-service
   ```

2. Compile el proyecto:
   ```bash
   ./mvnw clean install
   ```
# Configuración de DevTools para reinicio automático
spring.devtools.restart.enabled=true
spring.devtools.restart.poll-interval=2s
spring.devtools.restart.quiet-period=1s
spring.devtools.livereload.enabled=true
## Configuración

Las configuraciones principales se encuentran en el archivo `application.properties` (o `application.yml`) ubicado en `src/main/resources/`.

### Ejemplo de configuración básica:

# Validación de circulación vehicular Pico y Placa - Backend

Este repositorio contiene la API REST para el sistema de consulta de restricciones vehiculares "Pico y Placa" del Distrito Metropolitano de Quito (DMQ). La aplicación está construida siguiendo principios de Arquitectura Hexagonal y utilizando el framework SpringBoot.

## 🚀 Tecnologías Principales

- **Java 17**: Lenguaje de programación principal.
- **Spring Boot 3.4.2**: Framework base para la creación de la API.
- **Spring Data JPA**: Para la persistencia de datos.
- **Flyway**: Gestión de migraciones de base de datos.
- **PostgreSQL / H2**: Soporte para bases de datos relacionales (H2 para pruebas/desarrollo rápido).
- **Spring Security**: Configuración básica de seguridad.
- **JUnit 5, Cucumber & ArchUnit**: Suite completa de pruebas unitarias, BDD y de arquitectura.
- **Jacoco**: Reportes de cobertura de código (mínimo 90% requerido para el dominio).
- **Caffeine**: Caché de alto rendimiento para optimizar consultas.

---

## 🛠️ Requisitos Previos

Asegúrate de tener instalados:

- **JDK 17**
- **Maven 3.8+**
- **PostgreSQL** (opcional, configurado por defecto para usar H2 en memoria si no se especifica otro perfil).

---

## ⚙️ Configuración e Instalación

1. **Clonar el repositorio:**

   ```bash
   git clone https://github.com/edwynnJSK/pico-y-placa-backend.git
   cd pico-y-placa-backend
   ```

2. **Configuración de base de datos:**
   Por defecto, el proyecto usa H2. Si deseas usar PostgreSQL, configura las variables en `src/main/resources/application.properties` o mediante variables de entorno:

   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/picoyplaca
   spring.datasource.username=tu_usuario
   spring.datasource.password=tu_contrasena
   ```

3. **Compilar el proyecto:**

   ```bash
   mvn clean install
   ```

---

## 🧑‍💻 Ejecución en Desarrollo

Para iniciar el servidor de desarrollo local:

```bash
mvn spring-boot:run
```

La API estará disponible en `http://localhost:8080/api/v1`.

### Documentación de la API (Swagger UI)

Puedes consultar y probar los endpoints en:
`http://localhost:8080/swagger-ui.html`

---

## 🧪 Pruebas y Calidad

El proyecto pone un fuerte énfasis en la calidad y las pruebas:

- **Ejecutar todos los tests:**
  ```bash
  mvn test
  ```
- **Verificar cobertura con JaCoCo:**

  ```bash
  mvn verify
  ```

  El reporte se generará en `target/site/jacoco/index.html`.

- **Pruebas BDD (Cucumber):**
  Las historias de usuario se encuentran en `src/test/resources/features`.

---

## 📦 Construcción para Producción

Para generar el archivo JAR ejecutable:

```bash
mvn clean package -DskipTests
```

El artefacto se encontrará en la carpeta `target/picoyplaca-1.0.0-SNAPSHOT.jar`.

---

## 📄 Arquitectura del Proyecto

El proyecto sigue una **Arquitectura Hexagonal (Ports & Adapters)**:

- `com.dmq.picoyplaca.domain`: Núcleo de la aplicación (Reglas de negocio, entidades del dominio).
- `com.dmq.picoyplaca.application`: Casos de uso y DTOs.
- `com.dmq.picoyplaca.infrastructure`: Adaptadores de entrada (REST Controllers) y salida (Persistencia, Reloj, etc.).

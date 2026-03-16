# 🎭 Microservicio de Clientes (ms-clientes)

Este microservicio es responsable de la gestión de información de personas y clientes en el sistema financiero. Sigue los principios de **Clean Architecture** (Arquitectura Hexagonal) para separar la lógica de negocio de los detalles técnicos.

---

## 🏗️ Arquitectura y Diseño

### Entidades de Dominio
- **Persona:** Clase base con atributos comunes (nombre, identificación, dirección, etc.).
- **Cliente:** Extiende de Persona con atributos específicos (contraseña, estado, clienteId).

### Persistencia (Infraestructura)
- Usamos **JPA** con la estrategia de herencia `@Inheritance(strategy = InheritanceType.JOINED)`.
- Base de Datos: `db_clientes` (PostgreSQL).

### Comunicación Asincrónica
- Publica eventos hacia el exchange `cliente.events` en RabbitMQ.
- Eventos soportados: `cliente.creado`, `cliente.actualizado`, `cliente.contrasenia.actualizada`.

---

## 🚀 Endpoints REST (Base URL: `/api/clientes`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `POST` | `/` | Crea un nuevo cliente |
| `GET` | `/` | Lista todos los clientes |
| `GET` | `/{id}` | Obtiene un cliente por su ID único |
| `PUT` | `/{id}` | Actualiza datos básicos de un cliente |
| `DELETE` | `/{id}` | Elimina (lógicamente) un cliente |
| `PATCH` | `/{id}/contrasena` | Actualiza la contraseña del cliente |

---

## 🛠️ Guía de Run & Debug (Desarrollo)

### 1. Requisitos Previos
- Docker y Docker Compose (para BD y RabbitMQ).
- Java 17 (JDK).
- (Opcional) Maven instalado globalmente.

### 2. Levantar Infraestructura
Desde la raíz del proyecto:
```bash
docker-compose up -d
```

### 3. Compilación (Build)
Si tienes Maven instalado:
```bash
mvn clean package -DskipTests
```
Si **NO** tienes Maven instalado, puedes usar Docker:
```bash
docker run --rm -v "${PWD}:/app" -w /app maven:3.9-eclipse-temurin-17 mvn clean package -DskipTests
```

### 4. Ejecución (Run)
Desde la carpeta `target` de `ms-clientes`:
```bash
java -jar ms-clientes-0.0.1-SNAPSHOT.jar
```

### 5. Depuración (Debug)

#### En VS Code:
1. Abre el archivo `MsClientesApplication.java`.
2. Presiona `F5` o haz clic en "Run and Debug" en la barra lateral.
3. Si pides un `launch.json`, usa esta configuración:
```json
{
    "type": "java",
    "name": "Debug ms-clientes",
    "request": "launch",
    "mainClass": "com.devsu.clientes.MsClientesApplication",
    "projectName": "ms-clientes"
}
```

#### En IntelliJ IDEA:
1. Localiza `MsClientesApplication.java`.
2. Haz clic derecho y selecciona **"Debug 'MsClientesApplication'"**.
3. El servicio escuchará en el puerto `8001`.

---

## 📝 Notas de Implementación
- **MapStruct:** Se utiliza para mapear entre DTOs y Entidades de Dominio (Configurado para inyección de Spring).
- **Global Exception Handler:** Centraliza el manejo de errores devolviendo un JSON estructurado con `timestamp`, `status`, `error` y `message`.
- **Validaciones:** Se utiliza `jakarta.validation` para asegurar la integridad de los datos de entrada.

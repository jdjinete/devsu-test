# 🎭 Microservicio de Clientes (ms-clientes)

Este microservicio es responsable de la gestión de información de personas y clientes en el sistema financiero. Sigue los principios de **Clean Architecture** (Arquitectura Hexagonal) para separar la lógica de negocio de los detalles técnicos.

---

## 🏗️ Arquitectura y Diseño

### Entidades de Dominio
- **Persona:** Clase base con atributos comunes (nombre, identificación, dirección, etc.).
- **Cliente:** Extiende de Persona con atributos específicos (contraseña, estado, clienteId).
- **Lógica de Validación:** El dominio exige que la contraseña tenga **al menos 8 caracteres**.

### Persistencia (Infraestructura)
- Usamos **JPA** con la estrategia de herencia `@Inheritance(strategy = InheritanceType.JOINED)`.
- Base de Datos: `db_clientes` (PostgreSQL).

### Comunicación Asincrónica
- Publica eventos hacia el exchange `cliente.events` en RabbitMQ.
- Eventos soportados: `cliente.creado`, `cliente.actualizado`, `cliente.contrasenia.actualizada`.

---

## 🚀 Endpoints REST (Base URL: `/api/clientes`)

| Método | Endpoint | Puerto Externo | Descripción |
|--------|----------|----------------|-------------|
| `POST` | `/`      | `8001`         | Crea un nuevo cliente (Min 8 chars pass) |
| `GET`  | `/`      | `8001`         | Lista todos los clientes |
| `GET`  | `/{id}`  | `8001`         | Obtiene un cliente por su ID único |
| `PUT`  | `/{id}`  | `8001`         | Actualiza datos básicos de un cliente |
| `DELETE`| `/{id}` | `8001`         | Elimina (lógicamente) un cliente |

---

## 🛠️ Guía de Run & Debug (Producción/Contenedor)

### En Docker
El servicio corre internamente en el puerto **8080** y se expone en el **8001**.
La base de datos se configura mediante variables de entorno en el `docker-compose.yml`:
- `SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/db_clientes`

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
- **Port Standardization:** Se usa el puerto 8080 internamente para consistencia entre microservicios.
- **Validaciones:** Se utiliza `jakarta.validation` para los requests y lógica de dominio para reglas críticas.

# 📚 Índice del Proyecto - Sistema Financiero Distribuido

---

## 🚀 Guía de Inicio Rápido

### Primeros Pasos (5 minutos)

```bash
# 1. Clonar el repositorio
git clone <url>
cd devsu-test

# 2. Levantar Infrastructure con Docker
docker-compose up -d

# 3. Verificar que todo esté corriendo
docker-compose ps

# 4. Acceder a RabbitMQ Management
# http://localhost:15672  (guest / guest)
```

> **Nota:** La documentación técnica detallada (.engram) está disponible solo en desarrollo local (no se sube a git)

---

## 🏗️ Arquitectura del Sistema

### Microservicios

| Microservicio | Puerto | BD | Documentación | Responsabilidad |
|---------------|--------|-----|---------------|-----------------|
| **ms-clientes** | `8001` | `db_clientes` | [README.md](./ms-clientes/README.md) | Gestión de Personas y Clientes |
| **ms-cuentas** | `8002` | `db_cuentas` | - | Cuentas, Movimientos y Reportes |

### Stack Tecnológico
- **Java 17+** con Spring Boot
- **PostgreSQL** para persistencia
- **RabbitMQ** para eventos asincronos
- **Docker & Docker Compose** para orquestación
- **JUnit 5, Mockito, Testcontainers** para pruebas
- **MapStruct, Lombok** para utilidades

---

## 🌐 Topología de Red

```
┌─ Tu Máquina (localhost) ────────────────────┐
│  8001 → ms-clientes                         │
│  8002 → ms-cuentas                          │
│  5432 → PostgreSQL                          │
│  5672 → RabbitMQ AMQP                       │
│ 15672 → RabbitMQ Management                 │
└─────────────────────────────────────────────┘
         ↓
┌─ Docker Network (backend-network) ─────────┐
│  postgres:5432 (BD)                         │
│  rabbitmq:5672 (Message Broker)             │
│  ms-clientes:8080 (interno)                 │
│  ms-cuentas:8080 (interno)                  │
└─────────────────────────────────────────────┘
```

---

## 📋 Scripts de Inicialización

### `scripts/init-db.sql`
Crea las bases de datos y usuarios en PostgreSQL:
- `db_clientes` (para ms-clientes)
- `db_cuentas` (para ms-cuentas)
- Usuarios específicos con permisos limitados

### `scripts/rabbitmq-definitions.json`
Pre-configura RabbitMQ con:
- Exchanges: `cliente.events`, `cuenta.events`
- Queues: `cliente.eventos.queue`, `cuenta.eventos.queue`
- Bindings y usuarios

**Más detalles:** Ver [scripts/README.md](scripts/README.md)

---

## 📁 Estructura del Proyecto

```
devsu-test/
│
├── scripts/                      # 🔧 Scripts de Inicialización
│   ├── init-db.sql              # Inicialización PostgreSQL
│   ├── rabbitmq-definitions.json # Configuración RabbitMQ
│   └── README.md                 # Documentación de scripts
│
├── ms-clientes/                  # 🎭 Microservicio 1 (por crear)
│   ├── src/main/java/com/devsu/clientes/
│   │   ├── domain/               # Lógica de negocio pura
│   │   ├── application/          # Casos de uso
│   │   └── infrastructure/       # Adaptadores técnicos
│   ├── src/test/java/...         # Tests
│   └── pom.xml
│
├── ms-cuentas/                   # 💰 Microservicio 2 (por crear)
│   ├── src/main/java/com/devsu/cuentas/
│   │   ├── domain/               # Lógica de negocio pura
│   │   ├── application/          # Casos de uso
│   │   └── infrastructure/       # Adaptadores técnicos
│   ├── src/test/java/...         # Tests
│   └── pom.xml
│
├── docker-compose.yml            # 🐋 Orquestación Docker
├── .env.example                  # 🔐 Variables de entorno (template)
├── .gitignore                    # Git ignore (archivos sensibles)
└── README.md                     # Este archivo
```

---

## 🏛️ Clean Architecture (Hexagonal)

Cada microservicio debe seguir esta estructura:

```
ms-xxxxx/
├── src/main/java/com/devsu/xxxxx/
│   ├── domain/                          # Core business logic
│   │   ├── entity/                      # Domain entities (NO Spring)
│   │   ├── exception/                   # Business exceptions
│   │   ├── port/                        # Interfaces (outbound)
│   │   │   ├── RepositoryPort.java
│   │   │   └── EventPublisherPort.java
│   │   └── vo/                          # Value objects
│   │
│   ├── application/                     # Use cases (Orchestration)
│   │   ├── port/                        # Inbound ports (interfaces)
│   │   ├── service/                     # Implementations
│   │   └── dto/                         # DTOs internos
│   │
│   └── infrastructure/                  # Technical details
│       ├── adapters/
│       │   ├── in/
│       │   │   ├── web/                 # REST Controllers
│       │   │   │   ├── controller/
│       │   │   │   ├── dto/
│       │   │   │   └── exception/
│       │   │   └── event/               # RabbitMQ Consumers
│       │   │
│       │   └── out/
│       │       ├── persistence/         # JPA Entities, Repos
│       │       │   └── mapper/          # MapStruct
│       │       └── event/               # RabbitMQ Producers
│       │
│       └── config/                      # Spring Configuration
│           ├── RabbitMQConfig.java
│           └── JpaConfig.java
└── src/test/java/...                    # Tests
```

---

## 🌉 Ejemplo: Comunicación Asincrónica

### Cliente es Eliminado en ms-clientes

```
1. DELETE /api/clientes/{id}
   ↓
2. ms-clientes elimina cliente de BD
   ↓
3. ms-clientes PUBLICA "ClienteInhabilitadoEvent"
   ↓
4. Evento va a RabbitMQ (exchange: cliente.events)
   ↓
5. ms-cuentas CONSUME evento
   ↓
6. ms-cuentas busca cuentas del cliente
   ↓
7. ms-cuentas marca cuentas como INACTIVAS
```

> **🚀 Flow Recomendado:** Este es el patrón implementado en los microservicios

---

## 📊 Conexiones de Servicios

### PostgreSQL - Docker (Puerto 5433)
```
jdbc:postgresql://localhost:5433/db_clientes
jdbc:postgresql://localhost:5433/db_cuentas
```

### PostgreSQL - Existente (Puerto 5432)
```
jdbc:postgresql://localhost:5432/...
```

### PostgreSQL - Desde dentro de Docker
```
jdbc:postgresql://postgres:5432/db_clientes
jdbc:postgresql://postgres:5432/db_cuentas
```

### RabbitMQ
```
amqp://guest:guest@localhost:5672/     (local)
amqp://guest:guest@rabbitmq:5672/      (desde Docker)
```

---

## 🧪 Estrategia de Pruebas

### Pruebas Unitarias
- ✅ Entidades de dominio
- ✅ Casos de uso (services)
- ✅ Value objects
- **Tool:** JUnit 5 + Mockito

### Pruebas de Integración
- ✅ Controllers REST
- ✅ Repositories
- ✅ Consumers de eventos
- **Tool:** @SpringBootTest + Testcontainers

### Cobertura Requerida
- Lógica de negocio: **80%+**
- Validaciones: **90%+**
- Controllers: **70%+**

---

## 🐛 Debugging y Monitoreo

### Logs en Tiempo Real
```bash
docker-compose logs -f            # Todos
docker-compose logs -f ms-postgres  # Solo PostgreSQL
docker-compose logs -f ms-rabbitmq  # Solo RabbitMQ
```

### Ejecutar y Depurar (Run & Debug)
Para desarrollar el microservicio `ms-clientes`:

1.  **Levantar Infra:** `docker-compose up -d`
2.  **IDE (Recomendado):**
    *   Abrir la carpeta raíz en VS Code o IntelliJ.
    *   Localizar la clase `MsClientesApplication.java`.
    *   Ejecutar en modo **Debug** (F5 en VS Code).
3.  **Terminal:**
    *   Build: `mvn clean package`
    *   Run: `java -jar target/ms-clientes-0.0.1-SNAPSHOT.jar`

Para más detalles, consulta el [README de ms-clientes](./ms-clientes/README.md).

### Acceder a Bases de Datos
```bash
# PostgreSQL CLI
docker-compose exec ms-postgres psql -U postgres -d db_clientes

# Ver tablas
\dt
\l   # listar bases de datos
```

### RabbitMQ Management
```
URL: http://localhost:15672
Usuario: guest
Contraseña: guest
```

---

## ✅ Checklist Antes de Empezar

- [ ] Docker y Docker Compose instalados
- [ ] Java 17+ instalado
- [ ] Maven o Gradle instalado
- [ ] `docker-compose up -d` ejecutado exitosamente
- [ ] `docker-compose ps` muestra todos los servicios UP
- [ ] PostgreSQL accesible en puerto 5432
- [ ] RabbitMQ Management accesible en http://localhost:15672
- [ ] `.env.example` copiado a `.env` (opcional para desarrollo)

---

## 📚 Documentación del Proyecto

- **Scripts:** [scripts/README.md](scripts/README.md)
- **Docker:** `docker-compose.yml`
- **Configuración:** `.env.example`

> **📖 Nota:** Documentación detallada de arquitectura y decisiones de diseño está disponible en la carpeta `.engram/` (solo en desarrollo local)

---

## 🤝 Contribuir

Al añadir nuevas características:

1. ✅ Sigue Clean Architecture (Hexagonal)
2. ✅ Escribe tests (80%+ cobertura)
3. ✅ Mantén el código limpio y documentado
4. ✅ Usa commit messages descriptivos
5. ✅ Respeta las reglas de negocio (BigDecimal para dinero, validaciones, etc.)

---

## 🚨 Notas de Seguridad

**NUNCA** commitear a git:
- `❌ .env` (usa `.env.example`)
- `❌ Credenciales` en código
- `❌ Claves privadas`
- `❌ localhost/127.0.0.1` hardcodeado

Ver [.gitignore](.gitignore) para archivos ignorados.

---

## 📞 Troubleshooting Rápido

| Problema | Solución |
|----------|----------|
| Port 5432 en uso | `lsof -i :5432` → `kill -9 <PID>` |
| Port 5672 en uso | `lsof -i :5672` → `kill -9 <PID>` |
| Docker no inicia | Reiniciar Docker Desktop |
| Datos corruptos | `docker-compose down -v` |
| No ve cambios | Reconstruir: `docker-compose up -d --build` |

Ver `.engram/SETUP.md` en tu copia local para más detalles

---

## 🎓 Referencias Externas

- 📖 [Spring Boot Docs](https://spring.io/projects/spring-boot)
- 🐘 [PostgreSQL Docs](https://www.postgresql.org/docs/)
- 🐰 [RabbitMQ Docs](https://www.rabbitmq.com/documentation.html)
- 🏗️ [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- 🧪 [JUnit 5 Guide](https://junit.org/junit5/docs/current/user-guide/)

---

**Última actualización:** 2026-03-15  
**Versión:** 1.0.0

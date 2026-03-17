# 💰 Microservicio de Cuentas y Movimientos (ms-cuentas)

Este microservicio es responsable de la gestión de cuentas bancarias, el registro de movimientos transaccionales y la generación de reportes financieros. Sigue los principios de **Clean Architecture** (Arquitectura Hexagonal) y asegura la integridad de los saldos mediante lógica de dominio rica.

---

## 🏗️ Arquitectura y Diseño

### Entidades de Dominio
- **Cuenta:** Entidad pura que encapsula la lógica de depósitos y retiros. Valida el saldo disponible en tiempo real.
- **Movimiento:** Registra cada transacción (débito/crédito) vinculada a una cuenta, manteniendo la trazabilidad del saldo resultante.

### Lógica de Negocio Crítica (F2/F3)
- **Validación de Saldo (F3):** Si un retiro supera el saldo actual, el dominio lanza una `SaldoNoDisponibleException`.
- **Actualización de Saldo (F2):** Los movimientos actualizan el saldo de la cuenta de forma atómica y transaccional.
- **Tipos de Datos:** Se utiliza estrictamente `BigDecimal` para garantizar la precisión decimal en todas las operaciones monetarias.

### Comunicación Asincrónica
- **Consumidor (RabbitMQ):** Escucha eventos del exchange `cliente.events`. 
- **Acción:** Cuando un cliente es inhabilitado en `ms-clientes`, este microservicio marca automáticamente todas las cuentas asociadas a dicho `clienteId` como `estado = false`.

---

## 🚀 Endpoints REST (Base URL: `/`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `POST` | `/cuentas` | Crea una nueva cuenta bancaria |
| `GET` | `/cuentas` | Lista todas las cuentas registradas |
| `PUT` | `/cuentas/{id}` | Actualiza datos de una cuenta |
| `POST` | `/movimientos` | Registra un nuevo movimiento (Depósito/Retiro) |
| `GET` | `/movimientos` | Historial completo de movimientos |
| `GET` | `/reportes` | Genera estado de cuenta por cliente y rango de fechas |

### Generación de Reportes
- **Endpoint**: `/reportes?fecha=YYYY-MM-DD,YYYY-MM-DD&cliente={id}`
- **Formato de Fecha**: Debe ser `YYYY-MM-DD` (ej: `2022-01-01,2022-12-31`). El sistema maneja automáticamente el rango desde el inicio del primer día hasta el final del último.

---

## 🛠️ Guía de Run & Debug (Desarrollo)

### 1. Requisitos Previos
- Docker y Docker Compose (para BD y RabbitMQ).
- Java 17 (JDK).
- Maven.

### 2. Levantar Infraestructura

### En Contenedor
El servicio corre internamente en el puerto **8080** y se expone en el **8002**.
Configuración de persistencia:
- `SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/db_cuentas`

Desde la raíz del proyecto:
```bash
docker-compose up -d
```

### 3. Compilación (Build)
```bash
mvn clean package -DskipTests -pl ms-cuentas -am
```

### 4. Ejecución (Run)
Desde la carpeta `target` de `ms-cuentas`:
```bash
java -jar ms-cuentas-0.0.1-SNAPSHOT.jar
```

### 5. Depuración (Debug)
#### En VS Code:
1. Localiza `MsCuentasApplication.java`.
2. Presiona `F5` o usa la configuración de launch.
3. El servicio escuchará en el puerto `8002`.

---

## 🧪 Estrategia de Pruebas 
- **Pruebas Unitarias:** Localizadas en `src/test/java/.../domain/CuentaBalanceTest.java`.
- **Foco:** Validación exhaustiva de la lógica de saldos y manejo de excepciones de negocio (`SaldoNoDisponibleException`) sin dependencias de base de datos.

---

## 📝 Notas de Implementación
- **Arquitectura Hexagonal:** Separación total entre el `domain` (reglas puras) y la `infrastructure` (JPA/Spring).
- **Port Standardization:** Puerto 8080 interno para simplificar el despliegue Docker.
- **Jackson Mapping:** El `ReporteDTO` usa anotaciones `@JsonProperty` para cumplir exactamente con el formato de salida requerido (Nombres con espacios y PascalCase).
- **Consistencia:** Uso de `BigDecimal` para todas las operaciones monetarias.

# Scripts de Inicialización - Sistema Financiero Distribuido

## Descripción General
Esta carpeta contiene scripts de configuración e inicialización para los servicios de infrastructura del proyecto.

## Archivos

### 1. `init-db.sql`
**Propósito:** Script de inicialización de PostgreSQL ejecutado automáticamente al levantar el contenedor.

**Qué hace:**
- Crea la base de datos `db_clientes` para el microservicio ms-clientes
- Crea la base de datos `db_cuentas` para el microservicio ms-cuentas
- Crea usuarios específicos con permisos limitados para cada microservicio
- Configura permisos por defecto para las nuevas tablas

**Credenciales:**
- Usuario administrador: `admin` / `admin123`
- Usuario ms-clientes: `ms_clientes_user` / `clientes_pass_123`
- Usuario ms-cuentas: `ms_cuentas_user` / `cuentas_pass_123`

⚠️ **IMPORTANTE:** En producción, cambiar todas las contraseñas y usar gestión de secretos.

### 2. `rabbitmq-definitions.json`
**Propósito:** Archivo de definiciones de RabbitMQ que pre-configura exchanges, queues y bindings.

**Qué define:**
- **Exchanges:**
  - `cliente.events`: Topic exchange para eventos de clientes
  - `cuenta.events`: Topic exchange para eventos de cuentas
- **Queues:**
  - `cliente.eventos.queue`: Cola para eventos de clientes
  - `cuenta.eventos.queue`: Cola para eventos de cuentas
- **Bindings:**
  - Vinculación de exchanges a queues con routing keys

## Cómo Usar

### Levantar el Entorno Completo
```bash
cd /ruta/del/proyecto
docker-compose up -d
```

### Verificar la Salud de los Servicios
```bash
docker-compose ps
```

### Ver Logs de Inicialización
```bash
docker-compose logs postgres
docker-compose logs rabbitmq
```

### Acceder a las Bases de Datos
```bash
# Desde tu máquina local (necesitas tener psql instalado)
psql -h localhost -U admin -d db_clientes
psql -h localhost -U admin -d db_cuentas

# Desde un contenedor
docker-compose exec postgres psql -U admin -d db_clientes
```

### Acceder a RabbitMQ Management
- URL: http://localhost:15672
- Usuario: `guest`
- Contraseña: `guest`

## Conexiones desde los Microservicios

### PostgreSQL
```
URL: jdbc:postgresql://postgres:5432/db_clientes
URL: jdbc:postgresql://postgres:5432/db_cuentas
Usuario: admin / ms_clientes_user / ms_cuentas_user
```

### RabbitMQ
```
Host: rabbitmq
Puerto: 5672
URL: amqp://guest:guest@rabbitmq:5672/
```

## Limpieza

### Detener y eliminar contenedores
```bash
docker-compose down
```

### Detener, eliminar y limpiar volúmenes (cuidado: elimina datos)
```bash
docker-compose down -v
```

## Troubleshooting

### PostgreSQL no inicia
- Verificar logs: `docker-compose logs postgres`
- Asegurar que el puerto 5432 no está en uso: `lsof -i :5432`

### RabbitMQ no inicia
- Verificar logs: `docker-compose logs rabbitmq`
- Asegurar que el puerto 5672 no está en uso: `lsof -i :5672`

### Problemas de permisos en volúmenes
- Ejecutar: `docker system prune -a`
- Borrar el volumen: `docker volume rm nombre_volumen`
- Reiniciar: `docker-compose up -d`

-- ============================================================================
-- SCRIPT DE BASE DE DATOS - PRUEBA TÉCNICA (BaseDatos.sql)
-- ============================================================================
-- Este script contiene el DDL completo y los datos de prueba iniciales
-- para los microservicios ms-clientes y ms-cuentas.
-- ============================================================================

-------------------------------------------------------------------------------
-- 1. ESTRUCTURA PARA MS-CLIENTES (Base de datos: db_clientes)
-------------------------------------------------------------------------------

-- Tabla Personas (Base para Herencia JPA JOINED)
CREATE TABLE personas (
    persona_id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    genero VARCHAR(1) NOT NULL,
    edad INTEGER NOT NULL,
    identificacion VARCHAR(20) NOT NULL UNIQUE,
    direccion VARCHAR(200) NOT NULL,
    telefono VARCHAR(20) NOT NULL,
    estado VARCHAR(10) NOT NULL,
    fecha_registro BIGINT NOT NULL
);

-- Tabla Clientes (Extiende Personas)
CREATE TABLE clientes (
    persona_id INTEGER PRIMARY KEY REFERENCES personas(persona_id),
    cliente_id BIGINT NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL,
    estado_cliente VARCHAR(20) NOT NULL,
    ultimo_acceso BIGINT
);

-------------------------------------------------------------------------------
-- 2. ESTRUCTURA PARA MS-CUENTAS (Base de datos: db_cuentas)
-------------------------------------------------------------------------------

-- Tabla Cuentas
CREATE TABLE cuentas (
    id SERIAL PRIMARY KEY,
    numero_cuenta VARCHAR(20) NOT NULL UNIQUE,
    tipo_cuenta VARCHAR(20) NOT NULL,
    saldo_inicial DECIMAL(19,2) NOT NULL,
    saldo_actual DECIMAL(19,2) NOT NULL,
    estado BOOLEAN NOT NULL,
    cliente_id BIGINT NOT NULL -- Referencia lógica al ms-clientes
);

-- Tabla Movimientos
CREATE TABLE movimientos (
    id SERIAL PRIMARY KEY,
    fecha TIMESTAMP NOT NULL,
    tipo_movimiento VARCHAR(20) NOT NULL,
    valor DECIMAL(19,2) NOT NULL,
    saldo DECIMAL(19,2) NOT NULL,
    cuenta_id INTEGER NOT NULL REFERENCES cuentas(id)
);

-------------------------------------------------------------------------------
-- 3. DATOS DE PRUEBA (Casos de Uso del Requerimiento)
-------------------------------------------------------------------------------

-- Inserts para ms-clientes
-- Jose Lema
INSERT INTO personas (nombre, genero, edad, identificacion, direccion, telefono, estado, fecha_registro)
VALUES ('Jose Lema', 'M', 35, '1712345678', 'Otavalo sn y principal', '098254785', 'ACTIVO', 1710633600000);
INSERT INTO clientes (persona_id, cliente_id, contrasena, estado_cliente)
VALUES (1, 1001, '1234', 'ACTIVO');

-- Marianela Montalvo
INSERT INTO personas (nombre, genero, edad, identificacion, direccion, telefono, estado, fecha_registro)
VALUES ('Marianela Montalvo', 'F', 28, '1722345678', 'Amazonas y NNUU', '097548965', 'ACTIVO', 1710633600000);
INSERT INTO clientes (persona_id, cliente_id, contrasena, estado_cliente)
VALUES (2, 1002, '5678', 'ACTIVO');

-- Juan Osorio
INSERT INTO personas (nombre, genero, edad, identificacion, direccion, telefono, estado, fecha_registro)
VALUES ('Juan Osorio', 'M', 40, '1732345678', '13 junio y Equinoccial', '098874587', 'ACTIVO', 1710633600000);
INSERT INTO clientes (persona_id, cliente_id, contrasena, estado_cliente)
VALUES (3, 1003, '1245', 'ACTIVO');

-- Inserts para ms-cuentas
-- Cuenta Jose Lema (Ahorros)
INSERT INTO cuentas (numero_cuenta, tipo_cuenta, saldo_inicial, saldo_actual, estado, cliente_id)
VALUES ('478758', 'Ahorros', 2000.00, 2000.00, true, 1001);

-- Cuenta Marianela Montalvo (Corriente)
INSERT INTO cuentas (numero_cuenta, tipo_cuenta, saldo_inicial, saldo_actual, estado, cliente_id)
VALUES ('225487', 'Corriente', 100.00, 100.00, true, 1002);

-- Cuenta Juan Osorio (Ahorros)
INSERT INTO cuentas (numero_cuenta, tipo_cuenta, saldo_inicial, saldo_actual, estado, cliente_id)
VALUES ('495878', 'Ahorros', 0.00, 0.00, true, 1003);

-- Cuenta Marianela Montalvo (Ahorros)
INSERT INTO cuentas (numero_cuenta, tipo_cuenta, saldo_inicial, saldo_actual, estado, cliente_id)
VALUES ('496825', 'Ahorros', 540.00, 540.00, true, 1002);

-- Cuenta Jose Lema (Corriente)
INSERT INTO cuentas (numero_cuenta, tipo_cuenta, saldo_inicial, saldo_actual, estado, cliente_id)
VALUES ('585545', 'Corriente', 1000.00, 1000.00, true, 1001);

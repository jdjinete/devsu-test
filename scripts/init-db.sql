-- ============================================================================
-- Script de Inicialización de PostgreSQL
-- Crear bases de datos y esquemas para los microservicios
-- ============================================================================
-- Este script se ejecuta automáticamente cuando se inicia el contenedor
-- PostgreSQL por primera vez (definido en docker-compose.yml)
-- ============================================================================

-- Crear base de datos para ms-clientes
CREATE DATABASE db_clientes
  WITH ENCODING 'UTF8';

-- Crear base de datos para ms-cuentas
CREATE DATABASE db_cuentas
  WITH ENCODING 'UTF8';

# ============================================================================
# Dockerfile Multistage Optimizado - Microservicio Cuentas
# ============================================================================

# STAGE 1: Construcción (Maven)
FROM maven:3.8.4-openjdk-17-slim AS builder
WORKDIR /app

# Copiar el POM raíz
COPY pom.xml .

# Copiar los directorios de los módulos
COPY ms-clientes ms-clientes/
COPY ms-cuentas ms-cuentas/

# Ejecutar el empaquetado del microservicio específico
RUN mvn clean package -DskipTests -pl ms-cuentas -am

# STAGE 2: Ejecución (JRE Ligero)
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Crear un usuario sin privilegios
RUN addgroup -S devsu && adduser -S devsu -G devsu
USER devsu

# Copiar el artefacto generado
COPY --from=builder /app/ms-cuentas/target/ms-cuentas-*.jar app.jar

# Exponer el puerto por defecto
EXPOSE 8080

ENTRYPOINT ["java", "-Xmx512m", "-jar", "app.jar"]

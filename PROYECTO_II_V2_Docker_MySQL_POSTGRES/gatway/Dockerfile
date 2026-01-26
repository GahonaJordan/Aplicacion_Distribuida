# Build stage
FROM maven:3.8.7-eclipse-temurin-17 AS builder
WORKDIR /app

# Copiar los archivos de configuración de Maven
COPY pom.xml .
COPY src ./src

# Compilar el proyecto
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copiar el JAR generado desde el build stage
COPY --from=builder /app/target/*.jar app.jar

# Exponer el puerto del gateway
EXPOSE 8080

# Ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]

# Stage 1: build
FROM maven:3.9.4-eclipse-temurin-17 AS builder
WORKDIR /app

# Copiar pom y dependencias para cache
COPY pom.xml ./
COPY .mvn .mvn/
COPY mvnw mvnw
RUN chmod +x mvnw || true
RUN mvn dependency:go-offline -B

# Copiar código y compilar
COPY src ./src
RUN mvn clean package -DskipTests -B

# Stage 2: runtime
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copiar el jar compilado (usa wildcard para no hardcodear nombre)
COPY --from=builder /app/target/Gahona_Product-0.0.1-SNAPSHOT.jar app.jar

# Puerto por defecto de Spring Boot (ajusta si tu application.properties usa otro)
EXPOSE 9091

# Opcional: permitir pasar opciones JVM vía variable de entorno
ENV JAVA_OPTS=""

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]

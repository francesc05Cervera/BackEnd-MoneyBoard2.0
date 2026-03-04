# Fase 1: Build del progetto usando Maven ed Eclipse Temurin (Java 17)
FROM maven:3.8.7-eclipse-temurin-17 AS build
COPY . .
RUN mvn clean package -DskipTests

# Fase 2: Esecuzione dell'app con Java 17 (Eclipse Temurin)
FROM eclipse-temurin:17-jre-jammy
COPY --from=build /target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
# 1. fázis: Építés (Build)
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY checkstyle.xml .
COPY src ./src
# Lefordítjuk az alkalmazást (kihagyva a teszteket a gyorsabb build érdekében)
RUN mvn clean package -DskipTests

# 2. fázis: Futtatás (Run)
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/target/recipe-app-0.0.1-SNAPSHOT.jar app.jar

# Port kitettsége
EXPOSE 8080

# Alkalmazás indítása
ENTRYPOINT ["java", "-jar", "app.jar"]
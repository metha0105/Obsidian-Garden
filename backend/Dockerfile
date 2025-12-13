# Build stage
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /app/target/garden-0.0.1-SNAPSHOT.jar app.jar
ENV SERVER_PORT=8080
EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
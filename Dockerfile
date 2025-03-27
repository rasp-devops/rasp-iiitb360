FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
COPY lib ./lib

RUN mvn install:install-file \
    -Dfile=lib/rasp-platform-1.0.0-SNAPSHOT.jar \
    -DgroupId=rasp-platform \
    -DartifactId=rasp-platform \
    -Dversion=1.0.0-SNAPSHOT \
    -Dpackaging=jar

COPY src ./src

RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]

# Don't change this file and this is universal for Rasp-Platform usage.

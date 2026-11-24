FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml ./pom.xml
COPY restaurant-service ./restaurant-service

RUN mvn -f restaurant-service/pom.xml clean package spring-boot:repackage -DskipTests

FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build /app/restaurant-service/target/*.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]
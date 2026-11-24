FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml ./pom.xml
COPY order-service ./order-service

RUN mvn -f order-service/pom.xml clean package spring-boot:repackage -DskipTests

FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build /app/order-service/target/*.jar app.jar

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "app.jar"]
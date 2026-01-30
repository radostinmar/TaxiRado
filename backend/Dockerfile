FROM gradle:8.7-jdk17 AS build
WORKDIR /app
COPY --chown=gradle:gradle . /app
RUN gradle :backend:bootJar --no-daemon

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/backend/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

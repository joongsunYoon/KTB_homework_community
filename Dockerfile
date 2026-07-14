FROM eclipse-temurin:21-jdk AS builder

WORKDIR /workspace

COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./

RUN chmod +x ./gradlew

COPY src src

RUN ./gradlew clean bootJar --no-daemon



FROM eclipse-temurin:21-jre

WORKDIR /app

RUN groupadd --system spring \
    && useradd --system \
       --gid spring \
       --no-create-home \
       spring

COPY --from=builder \
     --chown=spring:spring \
     /workspace/build/libs/*.jar \
     /app/app.jar

USER spring

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
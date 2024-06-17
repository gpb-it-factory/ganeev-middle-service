FROM openjdk:24-slim-bullseye
WORKDIR /app
COPY build/libs/* build/lib/

COPY build/libs/ganeev-middle-service-0.0.1-SNAPSHOT.jar build/middle-service.jar
WORKDIR /app/build
ENTRYPOINT ["java", "-jar", "middle-service.jar"]
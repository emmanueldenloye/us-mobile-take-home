FROM openjdk:17-jdk-alpine3.13

WORKDIR /app

COPY build/libs/us-mobile-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
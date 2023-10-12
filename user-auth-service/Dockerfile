FROM openjdk:17

WORKDIR /app

COPY target/user-auth-service-0.0.1-SNAPSHOT.jar /app/user-auth-service.jar

EXPOSE 8080

CMD ["java", "-jar", "user-auth-service.jar"]
FROM openjdk:17

WORKDIR /app

COPY target/movie-showtime-service-0.0.1-SNAPSHOT.jar /app/cinema-movie-service.jar

EXPOSE 8081

CMD ["java", "-jar", "cinema-movie-service.jar"]
# Cinema-managment-app

The Cinema Management web app, as its name suggests, is an application developed to efficiently manage multiple cinemas within a single country.
I developed this app to showcase my academic-level knowledge in Java programming and to help me secure my first job as a Java backend developer.

## Technologies Used

- Java 17
- Spring Boot 3.1.4
- MySQL 8.0
- RabbitMQ 3.7

## Installation Guide

### Requirements
- Docker installed

To start the containers, all you need to do is run the following command:

```shell
docker-compose up -d
```

Initially, you may encounter an issue where "user-auth-service" and "cinema-movie-service" containers can't find their respective databases, causing them to stop. No need to worry, you can resolve this issue by following these steps:

1.Open your command line terminal.

2.Type the following command to access the MySQL shell:

```shell
docker exec -it mysql mysql -u root -p
```

3.You will be prompted to enter the MySQL root password, which is Password1234.

4.After successfully logging in, execute the following SQL commands:

```shell
CREATE DATABASE IF NOT EXISTS user_auth_db;
CREATE DATABASE IF NOT EXISTS cinema_movie_db;
```

5.Now, all you need to do is restart the two containers (user-auth-service and cinema-movie-service), and they should run properly.

Since an external volume is set, you won't encounter this error on subsequent runs of docker-compose. These databases will be saved. However, please note that all other data will be lost because the CREATE-DROP strategy is set for both of them. This application is intended for demonstrating programming skills purposes.

With these steps, you should be able to get your Docker containers up and running smoothly.

## Table of Contents

- [User Service](#user-service)
- [Cinema Service](#cinema-service)

## User Service

The User Service is responsible for handling user and employee data, including performing CRUD (Create, Read, Update, Delete) operations. Additionally, it plays a crucial role in user authentication and security.

## Cinema Service

The Cinema Service is a comprehensive solution for managing various aspects of cinema operations, including:
- CRUD operations on cinemas, movie theaters, seats, movies, and showtimes.
- Movie ratings and reviews.
- Ticket and reservation management.

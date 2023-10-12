# Cinema-managment-app

The Cinema Management web app, as its name suggests, is an application developed to efficiently manage multiple cinemas within a single country.
I developed this app to showcase my academic-level knowledge in Java programming and to help me secure my first job as a Java backend developer.

## Technologies Used

- Java 17
- Spring Boot 3.1.4
- MySQL 8.0
- RabbitMQ 3.7

## Entity-Relationship Diagrams (EER)

I've provided visual representations in form of .png of the database structures through Entity-Relationship Diagrams (EER). These diagrams offer a comprehensive overview of how the data is organized and the relationships between different entities in the system.

Feel free to explore these diagrams to gain insights into the underlying data models of the User and Cinema databases.


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

- [Overview](#overview)
- [User Service](#user-service)
- [Cinema Service](#cinema-service)
- [Notification Service](#notification-service)

## Overview

The Cinema Management System comprises four distinct user types, each serving a specific purpose within the system:

### Ordinary User (User)

The **Ordinary User**, also known as a **User**, can perform various actions within the system. To become a user:

1. Register: Start by registering and providing the required information to create your account.

2. Valid Gmail Account: Registration is limited to users with valid Gmail accounts. Other email domains, such as Hotmail and Yahoo, are not supported.

3. Verification: After registration, a verification email will be sent to your Gmail account for identity confirmation.

4. Login: Once verified, you can log in to your account and access specific functionalities. Details on the actions available to ordinary users will be specified in upcoming chapters.

### Admin (Super User)

The **Admin**, a super user, plays a critical role in maintaining and managing the system. The admin account is hardcoded into the system and cannot be created explicitly. Admins have extensive privileges, ensuring the system functions smoothly. A detailed list of actions available to the admin role will be outlined in upcoming chapters.

### Cashier (Employee)

**Cashiers** are employees responsible for various tasks within a cinema. Their roles and actions within the system will be elaborated upon in subsequent chapters.

### Manager (Employee)

**Managers** are employees who oversee and supervise cashiers working in cinemas. Their specific roles and actions will be discussed in detail in the following chapters.

These user types are integral to the Cinema Management System, each contributing to its functionality and purpose. In the upcoming sections, we will explore the actions and responsibilities associated with each role.

## User Service

The **User Service** plays a pivotal role in the Cinema Management System, primarily responsible for a range of key tasks including user management, authentication, and privilege management. Let's delve into the various aspects of this service:

### Authentication

One of the core responsibilities of the User Service is to handle authentication for all users within the system. It ensures that users are properly authenticated and informed in case of any issues during the authentication process.

### Security with Spring Security

The User Service leverages **Spring Security** to fortify the authentication process and overall security within the Cinema Management System. Spring Security provides a robust framework for implementing authentication and authorization, helping to safeguard sensitive data and ensure that only authorized users can access specific features.

### Admin Privileges

Admin user accounts are paramount within the User Service, as they are endowed with exceptional privileges. These privileges are required for several critical functions, including:

#### Deleting Ordinary Users

In situations where an ordinary user loses their password, there is currently no means to recover their account. The recommended course of action is for the user to contact the management team, who will then notify the admin to delete the user's account. Afterward, the user can recreate their account using the same email address.

#### Filtering Users

The admin has the ability to filter all users based on various parameters, facilitating efficient user management and organization.

#### Employee Management

The User Service empowers the admin to create and manage employees within the system, specifically Cashiers and Managers, and assign them to specific cinemas.

#### Filtering Employees

Just as with users, the admin can also filter employees based on various parameters, streamlining the management of cinema staff.

#### Deleting Employees

Admins can initiate the removal of employees when necessary, maintaining control over the workforce within the system.

#### Updating Manager Employees

In cases where a manager employee vacates their position, the admin has the authority to update the manager's information. This flexibility is crucial as it avoids the deletion of all subordinates (cashiers), which may not always be necessary.

The User Service stands as a central component in the Cinema Management System, ensuring smooth authentication, user management, and the maintenance of an efficient cinema workforce.

The User Service stands as a central component in the Cinema Management System, ensuring smooth authentication, user management, and the maintenance of an efficient cinema workforce while upholding the highest standards of security.

For detailed API documentation and interaction with the system, you can access the [Swagger API](http://localhost:8080/api/swagger-ui/index.html#/) interface on this link when service is running.

Admin credentials:
- Email: admin@gmail.com
- Password: password1

### Note
Some endpoint routes require parameter "Authorization" and here you will provide yout jwt in the context "Bearer [jwt token goes here]"

## Cinema Service

The **Cinema Service** is a robust and integral component of the Cinema Management System, responsible for storing data related to various entities, including:

- Cinemas
- Movie Theaters
- Movies
- Movie Ratings
- Showtimes
- Tickets
- Reservations

The service is divided into three main parts, each catering to distinct functionalities:

### Part 1: Cinemas and Movie Theaters

The first part focuses on managing data about cinemas and their associated theaters. Admins play a pivotal role in this section by creating, updating, and, if necessary, deleting cinemas. They are also responsible for creating movie theaters, filtering them based on their cinemas, and managing deletions. Every user, regardless of their role, has the ability to search for cinemas based on their name, address, and city.

### Part 2: Movies and Movie Ratings

The second part of the service is dedicated to managing movies and their associated ratings. Admins in this section create, update, and delete movies. Every user can search for movies based on criteria such as title, genre, description, and release year. Only Ordinary users can submit movie ratings, delete their own ratings, and filter all ratings for a particular movie.

### Part 3: Showtimes, Tickets, and Reservations

The third and final part is responsible for creating showtimes. Cinema managers and admins have the authority to create showtimes, while only admins can manage their deletion and confirmation. Showtimes must be created at least two days before projection and utilize the strength of Java scheduling to automate state changes effortlessly. Showtimes progress through stages including CREATION (not yet scheduled for projection), SCHEDULED, IN_PROGRESS, and FINISHED.

This part of the Cinema Service provides endpoints for creating and deleting tickets for showtimes, a process exclusively handled by cashier employees. Transaction locks ensure the uniqueness of tickets. Additionally, it includes endpoints for creating and deleting showtime reservations, which can only be done by ordinary users and are thread-safe. To maintain the integrity of showtime projections, these reservations are automatically deleted five hours before their scheduled showtime by a task scheduler.

The Cinema Service encompasses a wide range of functionalities to ensure smooth cinema operations, covering everything from cinema and movie management to showtime scheduling, ticket handling, and reservation management.

For detailed API documentation and interaction with the system, you can access the [Swagger API](http://localhost:8081/api/swagger-ui/index.html#/) when the services is running.

**Predefined Cinema:**
- Name: Cinema 1
- Email: cinema1@gmail.com
- Address: Address1
- City: City1
- Phone: +38166111122

### Note
Some endpoint routes require parameter "Authorization" and here you will provide yout jwt in the context "Bearer [jwt token goes here]"

## Notification Service

The **Notification Service** is responsible for managing notifications from both the User and Cinema Services. Its primary function is to send email notifications to the provided Gmail accounts based on incoming notifications.

This service operates primarily behind the scenes and doesn't expose any endpoints for direct access. Instead, it plays a crucial role in ensuring that notifications are delivered promptly to the relevant users.

The Notification Service leverages **RabbitMQ** for communication, facilitating efficient message handling and ensuring that notifications are reliably delivered.

The service helps to streamline communication within the Cinema Management System, ensuring that users and cinema personnel receive timely notifications and updates via email.

## Bugs

While every effort has been made to ensure the stability and reliability of this project, there may be cases that have not been covered, not intentionally, and could potentially lead to unexpected issues or "bugs." Please keep in mind that this project is primarily an endeavor to showcase my knowledge in Java and Spring, as well as problem-solving abilities.

If you come across any issues, bugs, or unexpected behavior, I kindly request that you notify me. Your feedback is highly appreciated and valuable in enhancing the project. You can report bugs by creating an issue in the project's [issue tracker](https://github.com/your-repo/issues) or by reaching out via email at [randoideveloper@gmail.com].

Thank you for your understanding and for contributing to the improvement of this project.

## Future Enhancements

My project is set to evolve with exciting developments in the pipeline. I am committed to enhancing the user experience and the functionality of our system. Here are some of the future improvements you can expect:

1. Expanded User Roles: I plan to introduce more user roles to provide a more tailored experience for different user types. This could include roles like "Moderator," "Contributor," or "VIP" users with unique privileges.

2. Diversified Cinema Components: In addition to theaters, I intend to incorporate various other components within the cinema ecosystem. This may include adding features like a food shop, a café, or comfortable waiting areas, enhancing the overall cinema experience.

3. User Account Management: I will introduce features for ordinary users to recover their passwords independently and allow them to delete their accounts if they choose to do so, enhancing user control over their profiles.

4. Enhanced Filtration Options: To provide users with a more refined search experience, I will be expanding the filtration options. Users will have more ways to sort and filter data to find precisely what they are looking for.

5. Additional Entities and Information: I'll be introducing new entities and enriching existing ones with more information. This will provide users with a richer and more informative experience on my platform.

6. WebSocket Integration: To enhance real-time communication and interactions, I'll implement WebSocket technology for specific purposes. This will improve the responsiveness of my platform and open up exciting new possibilities for features like real-time chat, notifications, and updates.

These future improvements reflect my commitment to continuous innovation and my dedication to providing an exceptional user experience. I look forward to delivering these enhancements and more to make my platform even more valuable and engaging for my users. Stay tuned for updates and watch my platform evolve.

## License

This project is open source and is released under the [Creative Commons Zero License](https://creativecommons.org/publicdomain/zero/1.0/).

You are free to use, modify, and distribute this software for any purpose without any restrictions. Please refer to the [LICENSE](LICENSE) file for more details.



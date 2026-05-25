# Real-Time Chat Application

A beginner-to-intermediate Java and Spring Boot project for a Software Engineering Intern resume. The app lets users register, login, create chat rooms, join rooms, send messages, and view previous messages stored in PostgreSQL.

The first version uses REST APIs and database storage. WebSockets, JWT authentication, typing indicators, and online/offline status are suggested as Phase 2 improvements.

## Tech Stack

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- PostgreSQL
- Maven
- HTML, CSS, JavaScript
- Postman for API testing

## Project Architecture

```text
Browser / Postman
      |
      v
Controller layer  -> receives HTTP requests and returns responses
      |
      v
Service layer     -> contains business logic
      |
      v
Repository layer  -> talks to the database using Spring Data JPA
      |
      v
PostgreSQL tables -> users, chat_rooms, room_members, messages
```

## Folder Structure

```text
src/main/java/com/example/realtimechat
 ┣ config
 ┃ ┗ WebConfig.java
 ┣ controller
 ┃ ┣ AuthController.java
 ┃ ┣ ChatRoomController.java
 ┃ ┣ GlobalExceptionHandler.java
 ┃ ┗ MessageController.java
 ┣ dto
 ┃ ┣ ApiResponse.java
 ┃ ┣ AuthResponse.java
 ┃ ┣ CreateRoomRequest.java
 ┃ ┣ JoinRoomRequest.java
 ┃ ┣ LoginRequest.java
 ┃ ┣ MessageRequest.java
 ┃ ┣ MessageResponse.java
 ┃ ┣ RegisterRequest.java
 ┃ ┗ RoomResponse.java
 ┣ entity
 ┃ ┣ ChatRoom.java
 ┃ ┣ Message.java
 ┃ ┗ User.java
 ┣ repository
 ┃ ┣ ChatRoomRepository.java
 ┃ ┣ MessageRepository.java
 ┃ ┗ UserRepository.java
 ┣ service
 ┃ ┣ AuthService.java
 ┃ ┣ ChatRoomService.java
 ┃ ┗ MessageService.java
 ┗ RealTimeChatApplication.java

src/main/resources
 ┣ static
 ┃ ┣ index.html
 ┃ ┣ script.js
 ┃ ┗ styles.css
 ┗ application.properties
```

## Database Schema

### users

| Column | Type | Meaning |
| --- | --- | --- |
| id | BIGSERIAL primary key | Unique user id |
| full_name | VARCHAR | User's display name |
| username | VARCHAR unique | Login username |
| email | VARCHAR unique | User email |
| password | VARCHAR | Demo password storage |
| created_at | TIMESTAMP | Account creation time |

### chat_rooms

| Column | Type | Meaning |
| --- | --- | --- |
| id | BIGSERIAL primary key | Unique room id |
| name | VARCHAR unique | Room name |
| description | VARCHAR | Room details |
| created_by_user_id | BIGINT foreign key | References `users.id` |
| created_at | TIMESTAMP | Room creation time |

### room_members

This join table is created by the `@ManyToMany` mapping between users and rooms.

| Column | Type | Meaning |
| --- | --- | --- |
| room_id | BIGINT foreign key | References `chat_rooms.id` |
| user_id | BIGINT foreign key | References `users.id` |

### messages

| Column | Type | Meaning |
| --- | --- | --- |
| id | BIGSERIAL primary key | Unique message id |
| content | VARCHAR | Message text |
| sender_id | BIGINT foreign key | References `users.id` |
| room_id | BIGINT foreign key | References `chat_rooms.id` |
| sent_at | TIMESTAMP | Message send time |

Relationships:

- One user can create many rooms.
- One room can contain many messages.
- One user can send many messages.
- Users and rooms have a many-to-many relationship through `room_members`.

## How To Run

1. Create a PostgreSQL database:

```sql
CREATE DATABASE chat_app;
```

2. Update database username and password in `src/main/resources/application.properties`.

3. Start the app:

```bash
mvn spring-boot:run
```

4. Open the UI:

```text
http://localhost:8081
```

Spring Boot will create/update the tables because `spring.jpa.hibernate.ddl-auto=update` is enabled.

## API Documentation

### Register User

`POST http://localhost:8081/api/auth/register`

Request:

```json
{
  "fullName": "Asha Sharma",
  "username": "asha",
  "email": "asha@example.com",
  "password": "secret123"
}
```

Success response: `201 Created`

```json
{
  "userId": 1,
  "fullName": "Asha Sharma",
  "username": "asha",
  "message": "Registration successful."
}
```

### Login User

`POST http://localhost:8081/api/auth/login`

Request:

```json
{
  "username": "asha",
  "password": "secret123"
}
```

Success response: `200 OK`

```json
{
  "userId": 1,
  "fullName": "Asha Sharma",
  "username": "asha",
  "message": "Login successful."
}
```

### Create Room

`POST http://localhost:8081/api/rooms`

Request:

```json
{
  "name": "Java Learners",
  "description": "Room for Java discussions",
  "createdByUserId": 1
}
```

Success response: `201 Created`

```json
{
  "id": 1,
  "name": "Java Learners",
  "description": "Room for Java discussions",
  "createdByUsername": "asha",
  "memberCount": 1
}
```

### Fetch Rooms

`GET http://localhost:8081/api/rooms`

Success response: `200 OK`

```json
[
  {
    "id": 1,
    "name": "Java Learners",
    "description": "Room for Java discussions",
    "createdByUsername": "asha",
    "memberCount": 1
  }
]
```

### Join Room

`POST http://localhost:8081/api/rooms/{roomId}/join`

Request:

```json
{
  "userId": 1
}
```

Success response: `200 OK`

### Send Message

`POST http://localhost:8081/api/messages`

Request:

```json
{
  "senderId": 1,
  "roomId": 1,
  "content": "Hello everyone!"
}
```

Success response: `201 Created`

```json
{
  "id": 1,
  "content": "Hello everyone!",
  "senderId": 1,
  "senderUsername": "asha",
  "roomId": 1,
  "sentAt": "2026-05-25T10:15:30"
}
```

### Fetch Messages

`GET http://localhost:8081/api/rooms/{roomId}/messages`

Success response: `200 OK`

```json
[
  {
    "id": 1,
    "content": "Hello everyone!",
    "senderId": 1,
    "senderUsername": "asha",
    "roomId": 1,
    "sentAt": "2026-05-25T10:15:30"
  }
]
```

Common error response: `400 Bad Request`

```json
{
  "message": "Room not found."
}
```

## Step-By-Step Implementation Guide

1. Create a Spring Boot project with Spring Web, Spring Data JPA, Validation, and PostgreSQL Driver.
2. Configure PostgreSQL in `application.properties`.
3. Create entity classes for `User`, `ChatRoom`, and `Message`.
4. Create repository interfaces extending `JpaRepository`.
5. Create DTO classes so API requests and responses stay separate from database entities.
6. Create service classes for business logic like registration, login, room creation, and message sending.
7. Create REST controllers to expose endpoints.
8. Add exception handling with `@RestControllerAdvice`.
9. Add the static frontend files in `src/main/resources/static`.
10. Test each API in Postman before testing through the browser UI.

## Important Concepts Explained

### Spring Boot

Spring Boot helps you create Java backend applications quickly. It starts an embedded server, configures common libraries, and lets you focus on writing controllers, services, repositories, and business logic.

### REST API

A REST API lets the frontend communicate with the backend using HTTP. For example, the browser sends `POST /api/messages` to save a message, and the backend returns JSON.

### JPA

JPA maps Java objects to database tables. In this project, `User` becomes the `users` table, `ChatRoom` becomes `chat_rooms`, and `Message` becomes `messages`.

### Database Mapping

Annotations like `@Entity`, `@Id`, `@ManyToOne`, and `@ManyToMany` describe how Java classes connect to database tables and relationships.

### Dependency Injection

Spring creates objects like repositories and services for you. A service receives its repository through the constructor. This keeps code clean and easier to test.

### MVC Architecture

MVC separates responsibilities:

- Model: entities and DTOs.
- View: HTML, CSS, and JavaScript frontend.
- Controller: REST controllers that handle user requests.

### Request-Response Cycle

1. User clicks a button in the browser.
2. JavaScript sends an HTTP request.
3. Controller receives the request.
4. Service runs business logic.
5. Repository reads or writes database data.
6. Controller returns a JSON response.
7. JavaScript updates the UI.

### How Database Communication Happens

The controller does not directly talk to PostgreSQL. The service calls a repository, and Spring Data JPA generates common SQL queries from repository method names like `findByUsername` and `findByRoomIdOrderBySentAtAsc`.

## Phase 2 Improvements

### WebSockets for Real-Time Messaging

Current version uses REST, so messages appear when the frontend fetches history. To make messages instantly appear for all users:

- Add `spring-boot-starter-websocket`.
- Configure a WebSocket endpoint like `/ws`.
- Use STOMP topics such as `/topic/rooms/{roomId}`.
- When a message is saved, broadcast it to all connected clients in that room.

### JWT Authentication

Current login returns user details only. With JWT:

- Add Spring Security.
- Hash passwords with BCrypt.
- Return a signed token after login.
- Send `Authorization: Bearer <token>` with each request.
- Validate the token before allowing protected APIs.

### Online/Offline Status

Store a user's status in memory or database:

- Set status to online after login or WebSocket connection.
- Set status to offline after logout or WebSocket disconnect.
- Show status beside usernames in the room member list.

### Typing Indicator

With WebSockets:

- Send a small event when a user starts typing.
- Broadcast `"asha is typing..."` to other users in the same room.
- Clear the message after a few seconds or after the user sends the message.

## Resume-Ready Project Description

Built a full-stack chat application using Java, Spring Boot, Spring Data JPA, PostgreSQL, and JavaScript. Implemented user registration/login, chat room creation, room joining, message persistence, chat history APIs, layered backend architecture, DTO-based request/response handling, and a responsive web UI inspired by modern chat platforms.

Resume bullet points:

- Developed REST APIs for user authentication, chat room management, and message history using Spring Boot.
- Designed JPA entities and PostgreSQL relationships for users, rooms, room members, and messages.
- Implemented layered architecture with controllers, services, repositories, DTOs, and global exception handling.
- Built a responsive frontend using HTML, CSS, and JavaScript to consume backend APIs.
- Prepared the project for future WebSocket-based real-time messaging and JWT authentication.

## Interview Questions And Answers

### 1. Why did you use Spring Boot?

Spring Boot reduces setup work and gives an embedded server, auto-configuration, and easy dependency management. It lets me focus on building APIs and business logic.

### 2. What is the role of a controller?

A controller receives HTTP requests, calls the service layer, and returns HTTP responses, usually as JSON.

### 3. Why did you create DTOs?

DTOs separate API data from database entities. This keeps the API clean and prevents exposing unnecessary database fields.

### 4. What is JPA used for?

JPA maps Java objects to database tables and lets the application perform database operations through repositories.

### 5. What is the difference between `@OneToMany` and `@ManyToOne`?

`@ManyToOne` means many records point to one record. Many messages belong to one room, so `Message` has a `@ManyToOne` relationship with `ChatRoom`.

### 6. How does sending a message work?

The frontend sends message data to `POST /api/messages`. The controller calls `MessageService`, the service finds the sender and room, creates a `Message` entity, and the repository saves it in PostgreSQL.

### 7. Is this fully real-time?

Phase 1 is real-time-ready but uses REST. Phase 2 adds WebSockets so messages are pushed instantly to all connected users.

### 8. What would you improve for production?

I would hash passwords, add JWT authentication, validate room membership more strictly, add WebSockets, write tests, add pagination for messages, and improve error logging.

## Security Note

This beginner version stores passwords as plain text to keep login logic easy to understand. For any real project, use Spring Security with BCrypt password hashing before storing passwords.

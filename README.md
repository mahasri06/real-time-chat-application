# Real-Time Chat Application

A beginner-to-intermediate full-stack chat application built with **Java, Spring Boot, Spring Data JPA, PostgreSQL, REST APIs, and HTML/CSS/JavaScript**.

This project demonstrates clean backend engineering fundamentals through a realistic chat platform where users can register, log in, create chat rooms, join rooms, send messages, and view previous chat history.

> Current version: REST-based chat with persistent message history.  
> Planned upgrade: WebSocket + STOMP real-time messaging, JWT authentication, and Spring Security.

## Project Status

| Area | Status |
| --- | --- |
| User registration | Completed |
| User login | Completed |
| Chat room creation | Completed |
| Join chat room | Completed |
| Send messages | Completed |
| View chat history | Completed |
| Responsive frontend UI | Completed |
| PostgreSQL persistence | Completed |
| WebSocket live messaging | Planned |
| JWT authentication | Planned |
| Spring Security | Planned |
| File/image sharing | Planned |

## Why This Project Is Useful

This project is designed for students who know Java, OOP, Spring Boot basics, and SQL basics. It is intentionally not over-engineered. The focus is on writing understandable backend code with a clean structure that can be explained confidently in college reviews, viva, and internship interviews.

It demonstrates:

- Java backend development
- REST API design
- Layered architecture
- Spring Boot dependency injection
- JPA/Hibernate database mapping
- PostgreSQL integration
- DTO-based request/response handling
- Basic frontend-to-backend communication
- GitHub-ready project documentation

## Tech Stack

| Technology | Usage |
| --- | --- |
| Java 17 | Main backend programming language |
| Spring Boot | Backend application framework |
| Spring Web | REST API development |
| Spring Data JPA | Database access layer |
| Hibernate | ORM implementation used by JPA |
| PostgreSQL | Relational database for users, rooms, and messages |
| Maven | Build and dependency management |
| HTML | Frontend structure |
| CSS | Responsive UI styling |
| JavaScript | API calls and UI behavior |
| Git/GitHub | Version control and portfolio hosting |

## Features

### Authentication

- Register a new user
- Login with username and password
- Basic validation
- Clean response DTOs

### Chat Rooms

- Create a new chat room
- View available rooms
- Join a room
- Track room member count

### Messaging

- Send text messages in a room
- Store messages in PostgreSQL
- Fetch previous messages by room
- Display sender name and timestamp

### Frontend

- Modern chat layout inspired by WhatsApp Web and Discord
- Sidebar for rooms and authentication
- Main chat window
- Message input box
- Responsive design for smaller screens

## Architecture

```text
Browser UI
   |
   | REST API calls
   v
Spring Boot Controllers
   |
   v
Service Layer
   |
   v
Repository Layer
   |
   v
PostgreSQL Database
```

## Backend Folder Structure

```text
src/main/java/com/example/realtimechat
 ┣ config
 ┃ ┗ WebConfig.java
 ┣ controller
 ┃ ┣ AuthController.java
 ┃ ┣ ChatRoomController.java
 ┃ ┣ MessageController.java
 ┃ ┗ GlobalExceptionHandler.java
 ┣ dto
 ┃ ┣ RegisterRequest.java
 ┃ ┣ LoginRequest.java
 ┃ ┣ AuthResponse.java
 ┃ ┣ CreateRoomRequest.java
 ┃ ┣ JoinRoomRequest.java
 ┃ ┣ RoomResponse.java
 ┃ ┣ MessageRequest.java
 ┃ ┣ MessageResponse.java
 ┃ ┗ ApiResponse.java
 ┣ entity
 ┃ ┣ User.java
 ┃ ┣ ChatRoom.java
 ┃ ┗ Message.java
 ┣ repository
 ┃ ┣ UserRepository.java
 ┃ ┣ ChatRoomRepository.java
 ┃ ┗ MessageRepository.java
 ┣ service
 ┃ ┣ AuthService.java
 ┃ ┣ ChatRoomService.java
 ┃ ┗ MessageService.java
 ┗ RealTimeChatApplication.java
```

## Database Design

### users

| Column | Description |
| --- | --- |
| id | Primary key |
| full_name | User's full name |
| username | Unique username |
| email | Unique email |
| password | Demo password field |
| created_at | Account creation timestamp |

### chat_rooms

| Column | Description |
| --- | --- |
| id | Primary key |
| name | Unique room name |
| description | Room description |
| created_by_user_id | Foreign key referencing users |
| created_at | Room creation timestamp |

### room_members

| Column | Description |
| --- | --- |
| room_id | Foreign key referencing chat_rooms |
| user_id | Foreign key referencing users |

### messages

| Column | Description |
| --- | --- |
| id | Primary key |
| content | Message text |
| sender_id | Foreign key referencing users |
| room_id | Foreign key referencing chat_rooms |
| sent_at | Message timestamp |

## API Documentation

Base URL:

```text
http://localhost:8081
```

### Register User

```http
POST /api/auth/register
Content-Type: application/json
```

Request:

```json
{
  "fullName": "Asha Sharma",
  "username": "asha",
  "email": "asha@example.com",
  "password": "secret123"
}
```

Response:

```json
{
  "userId": 1,
  "fullName": "Asha Sharma",
  "username": "asha",
  "message": "Registration successful."
}
```

### Login User

```http
POST /api/auth/login
Content-Type: application/json
```

Request:

```json
{
  "username": "asha",
  "password": "secret123"
}
```

Response:

```json
{
  "userId": 1,
  "fullName": "Asha Sharma",
  "username": "asha",
  "message": "Login successful."
}
```

### Create Room

```http
POST /api/rooms
Content-Type: application/json
```

Request:

```json
{
  "name": "Java Learners",
  "description": "Room for Java discussions",
  "createdByUserId": 1
}
```

### Get All Rooms

```http
GET /api/rooms
```

### Join Room

```http
POST /api/rooms/{roomId}/join
Content-Type: application/json
```

Request:

```json
{
  "userId": 1
}
```

### Send Message

```http
POST /api/messages
Content-Type: application/json
```

Request:

```json
{
  "senderId": 1,
  "roomId": 1,
  "content": "Hello everyone!"
}
```

### Get Chat History

```http
GET /api/rooms/{roomId}/messages
```

## Setup Instructions

### Prerequisites

- Java 17 or higher
- Maven
- PostgreSQL
- Git
- VS Code, IntelliJ IDEA, or Eclipse

### 1. Clone The Repository

```bash
git clone https://github.com/YOUR_USERNAME/real-time-chat-application.git
cd real-time-chat-application
```

### 2. Create PostgreSQL Database

```sql
CREATE DATABASE chat_app;
```

### 3. Configure Database Credentials

The project uses environment variables so real passwords are not committed to GitHub.

PowerShell:

```powershell
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="your_database_password"
```

Optional:

```powershell
$env:DB_URL="jdbc:postgresql://localhost:5432/chat_app"
```

The default application config is:

```properties
spring.datasource.url=${DB_URL:jdbc:postgresql://localhost:5432/chat_app}
spring.datasource.username=${DB_USERNAME:postgres}
spring.datasource.password=${DB_PASSWORD:}
server.port=8081
```

### 4. Run The Application

Using Maven:

```bash
mvn spring-boot:run
```

On this Windows setup, you can also use:

```powershell
.\run-app.ps1
```

### 5. Open The Application

```text
http://localhost:8081
```

## Important Security Note

The current version uses simple password validation to keep the project beginner-friendly. For a production system, passwords must be hashed using BCrypt and APIs should be protected using Spring Security and JWT.

Recommended production improvements:

- BCrypt password hashing
- JWT authentication
- Spring Security filters
- HTTPS
- Role-based authorization
- Environment variables for all secrets
- Input validation
- Rate limiting for login APIs

## Future Enhancements

### Phase 2

- Add Spring WebSocket
- Add STOMP protocol
- Broadcast messages instantly to room subscribers
- Add typing indicators
- Add online/offline status
- Add read receipts

### Phase 3

- Add Spring Security
- Add JWT authentication
- Add BCrypt password hashing
- Protect REST APIs
- Authenticate WebSocket connections

### Phase 4

- Add one-to-one chat
- Add user profiles
- Add file/image sharing
- Add notifications
- Add pagination for message history

### Phase 5

- Add Docker
- Add Redis for online status and caching
- Add Kafka/RabbitMQ for notification processing
- Deploy on AWS

## What I Learned

- How to build REST APIs with Spring Boot
- How to structure backend code professionally
- How controller, service, and repository layers work
- How JPA maps Java objects to database tables
- How relational database relationships work
- How frontend JavaScript communicates with backend APIs
- How to prepare a project for GitHub and interviews

## Resume Description

Built a full-stack chat application using Java, Spring Boot, Spring Data JPA, PostgreSQL, and JavaScript. Implemented user registration, login, chat room creation, room joining, message persistence, chat history APIs, layered backend architecture, DTO-based request/response models, global exception handling, and a responsive chat UI. Designed the project for future WebSocket-based real-time messaging and JWT authentication.

## Resume Bullet Points

- Developed REST APIs for user authentication, chat room management, and message history using Spring Boot.
- Designed JPA entities and PostgreSQL relationships for users, chat rooms, room members, and messages.
- Implemented a layered backend architecture using controllers, services, repositories, entities, and DTOs.
- Built a responsive frontend with HTML, CSS, and JavaScript to consume backend APIs.
- Prepared the system architecture for WebSocket real-time messaging, JWT authentication, and scalable deployment.

## Interview Talking Points

- Explain why REST is used for registration, login, and chat history.
- Explain why WebSockets are better for live message delivery.
- Explain how JPA/Hibernate maps entities to database tables.
- Explain the purpose of DTOs.
- Explain controller-service-repository architecture.
- Explain how database foreign keys connect users, rooms, and messages.
- Explain how JWT and Spring Security would improve the current version.

## Project Evaluation

For a UG student or Software Engineering Intern candidate, this is a strong project if you can explain it clearly.

Current level:

```text
Beginner to Intermediate
```

Estimated resume value:

```text
8/10 for UG internship applications
```

It becomes a stronger 9/10 project after adding:

- Spring Security
- JWT authentication
- WebSocket + STOMP real-time messaging
- Password hashing
- Basic tests
- Screenshots in README

## License

This project is created for learning, portfolio, and academic submission purposes.

# Golf Club Management System

This project implements a RESTful API for managing golf club members and tournaments. It is designed to run in a containerized environment using Docker and connects to a MySQL database.

---

## Features

- **Manage Members**: Add, retrieve, and search members.
- **Manage Tournaments**: Add, retrieve, and search tournaments.
- **Link Members to Tournaments**: Assign members to tournaments and retrieve participating members.
- **Containerized Deployment**: Simplified setup with Docker for developers.

---

## Prerequisites

Ensure you have the following installed on your machine:

- **Docker**
- **Docker Compose**
- **Java 17**
- **Maven**

---

## Getting Started with Docker

### 1. Clone the Repository

Clone the project repository:

```bash
git clone https://github.com/brendaleearmstrong/S4-SDAT-QAP2
cd S4-SDAT-QAP2
```

### 2. Build the Application

Run the Maven build process to package the application:

```bash
./mvnw clean install
```

### 3. Run the Application with Docker Compose

Start the application using Docker Compose:

```bash
docker-compose up --build
```

This will:
1. Set up the MySQL database in a Docker container.
2. Deploy the Spring Boot application in another Docker container.

### 4. Verify the Containers

Check if the containers are running:

```bash
docker ps
```

You should see two containers:
- **`golfclub-mysql`** for the MySQL database.
- **`golfclub-app`** for the Spring Boot application.

### 5. Access the Application

- **API Base URL**: [http://localhost:8080](http://localhost:8080)
- **Health Check**: [http://localhost:8080/api/v1/health](http://localhost:8080/api/v1/health)

### 6. Stop the Application

Stop the containers and clean up resources:

```bash
docker-compose down --volumes
```

---

## API Endpoints

### Member Endpoints

| Method | Endpoint                         | Description                                |
|--------|----------------------------------|--------------------------------------------|
| GET    | `/api/v1/members`                | Retrieve all members.                     |
| POST   | `/api/v1/members`                | Add a new member.                         |
| GET    | `/api/v1/members/{id}`           | Retrieve a member by ID.                  |
| GET    | `/api/v1/members/search`         | Search members with filters.              |

**Member Search Parameters**:

- `/api/v1/members/search`
  - Query Parameters:
    - **`name`**: Filter members by name.
    - **`phone`**: Filter members by phone number.
    - **`membershipType`**: Filter by membership type.
    - **`startDate`**: Filter by membership start date.

### Tournament Endpoints

| Method | Endpoint                          | Description                                |
|--------|-----------------------------------|--------------------------------------------|
| GET    | `/api/v1/tournaments`             | Retrieve all tournaments.                 |
| POST   | `/api/v1/tournaments`             | Add a new tournament.                     |
| GET    | `/api/v1/tournaments/{id}`        | Retrieve a tournament by ID.              |
| GET    | `/api/v1/tournaments/search`      | Search tournaments with filters.          |

**Tournament Search Parameters**:

- `/api/v1/tournaments/search`
  - Query Parameters:
    - **`startDate`**: Filter by tournament start date.
    - **`location`**: Filter by location.
    - **`entryFee`**: Filter by entry fee.

### Linking Members and Tournaments

| Method | Endpoint                                  | Description                              |
|--------|------------------------------------------|------------------------------------------|
| POST   | `/api/v1/tournaments/{id}/members`       | Add members to a tournament.             |
| GET    | `/api/v1/tournaments/{id}/members`       | Get all members in a tournament.         |

---

## Docker Commands

### Build and Start Docker Containers

Use the following command to build and start the containers:

```bash
docker-compose up -d
```

### Check Running Containers

To ensure the containers are running:

```bash
docker ps
```

### Restart the Environment

To refresh or restart the volumes:

```bash
docker-compose down --volumes
docker-compose up -d
```

### Logs

View the logs for troubleshooting:

```bash
docker logs golfclub-app
docker logs golfclub-mysql
```

---

## Testing with Postman

- **Test the API Endpoints**: Use Postman to interact with the API.
- **Collection**: You can create a Postman collection to streamline testing the endpoints.

---

## Project Details

### Docker Compose Services

1. **MySQL Database**
   - Service: `mysql`
   - Port: `3306`
   - Data Volume: `/var/lib/mysql`
   - User: `golfuser`
   - Password: `golfpass`

2. **Spring Boot Application**
   - Service: `app`
   - Port: `8080`

---

## Example Database Entities

### Member Entity

- **ID** (auto-generated)
- **Name**
- **Address**
- **Email**
- **Phone Number**
- **Start Date of Membership**
- **Duration of Membership**

### Tournament Entity

- **ID** (auto-generated)
- **Start Date**
- **End Date**
- **Location**
- **Entry Fee**
- **Cash Prize Amount**
- **Participating Members**


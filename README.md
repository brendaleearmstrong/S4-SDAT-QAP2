# Golf Club Tournament Management System - QAP2

A Spring Boot REST API for managing golf club members and tournaments, built with MySQL and Docker. This project demonstrates Object-Relational Mapping (ORM) patterns and containerization techniques.

## Getting Started with Docker

### Prerequisites
- Docker Desktop
- Git

### Running the Application

1. **Clone the Repository**
```bash
git clone https://github.com/brendaleearmstrong/S4-SDAT-QAP2.git
cd S4-SDAT-QAP2
```

2. **Start the Application with Docker**
```bash
docker-compose up --build
```

This command:
- Builds the Spring Boot application
- Creates a MySQL database container
- Links the application with the database
- Exposes the API on port 8080

3. **Verify Setup**
```bash
docker ps
```
You should see two containers:
- `golfclub-mysql`: Database container
- `golfclub-app`: Spring Boot application container

4. **Stop the Application**
```bash
docker-compose down
```

## API Documentation

### Member Endpoints

#### Basic CRUD Operations
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/members` | Create new member |
| GET | `/api/v1/members` | Get all members |
| GET | `/api/v1/members/{id}` | Get member by ID |
| PUT | `/api/v1/members/{id}` | Update member |
| DELETE | `/api/v1/members/{id}` | Delete member |

#### Search and Filter Operations
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/members/search/name/{name}` | Search members by name |
| GET | `/api/v1/members/search/phone/{phone}` | Search by phone number |
| GET | `/api/v1/members/search/status/{status}` | Filter by status |
| GET | `/api/v1/members/search/active` | Get only active members |
| GET | `/api/v1/members/search/tournaments` | Search by minimum tournament count |
| GET | `/api/v1/members/search/tournament-date` | Find members by tournament date |
| GET | `/api/v1/members/top-participants` | Get most active participants |

#### Member Management Operations
| Method | Endpoint | Description |
|--------|----------|-------------|
| PATCH | `/api/v1/members/{id}/status` | Update membership status |
| PATCH | `/api/v1/members/{id}/duration` | Extend membership duration |
| POST | `/api/v1/members/{id}/check-status` | Check membership status |

### Tournament Endpoints

#### Basic Operations
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/tournaments` | Create new tournament |
| GET | `/api/v1/tournaments` | Get all tournaments |
| GET | `/api/v1/tournaments/{id}` | Get tournament by ID |
| PUT | `/api/v1/tournaments/{id}` | Update tournament |
| DELETE | `/api/v1/tournaments/{id}` | Delete tournament |

### API Request Examples

#### Create Member
```http
POST /api/v1/members
Content-Type: application/json

{
    "memberName": "John Doe",
    "memberAddress": "123 Golf Street",
    "memberEmail": "john@example.com",
    "memberPhone": "123-456-7890",
    "startDate": "2024-01-01",
    "duration": 12
}
```

#### Create Tournament
```http
POST /api/v1/tournaments
Content-Type: application/json

{
    "startDate": "2024-12-01",
    "endDate": "2024-12-03",
    "location": "Main Course",
    "entryFee": 100.00,
    "cashPrizeAmount": 1000.00,
    "minimumParticipants": 2,
    "maximumParticipants": 100
}
```

## Data Models

### Member Entity
- ID (auto-generated)
- Member Name (letters and spaces, 2-50 chars)
- Member Address
- Member Email (unique, validated)
- Member Phone (format: ###-###-####)
- Start Date
- Duration (1-60 months)
- Status (ACTIVE, EXPIRED, SUSPENDED, PENDING)
- Total Tournaments Played
- Total Winnings

### Tournament Entity
- ID (auto-generated)
- Start Date (future or present)
- End Date (future or present)
- Location
- Entry Fee (positive number)
- Cash Prize Amount (zero or positive)
- Participant Limits (min: 2, max: 100)
- Status (SCHEDULED, IN_PROGRESS, COMPLETED, CANCELLED)
- Participating Members

## Docker Configuration

### Container Details
1. **MySQL Database**
   - Image: MySQL 8.0
   - Port: 3306
   - Environment:
     - Database: golfclub
     - Username: golfuser
     - Password: golfpass

2. **Spring Boot Application**
   - Built from Dockerfile
   - Port: 8080
   - Dependencies: MySQL container

## Troubleshooting

### Common Issues and Solutions

1. **Container Start-up Issues**
```bash
# Remove existing containers and volumes
docker-compose down -v

# Rebuild and start
docker-compose up --build
```

2. **Database Connection Issues**
```bash
# Check MySQL container logs
docker logs golfclub-mysql

# Check application logs
docker logs golfclub-app
```

3. **Port Conflicts**
- Ensure ports 8080 and 3306 are available
- If needed, modify port mappings in docker-compose.yml

## Testing

The API has been tested using Postman. Test screenshots are available showing successful:
- Member creation and retrieval
- Tournament creation and retrieval
- Member searches
- Tournament searches

The API is accessible at `http://localhost:8080` once Docker containers are running.

## Course Information
- Course: Software Design, Architecture, Testing
- Assessment: QAP2
- Date: November 2024

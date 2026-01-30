# TaxiRado

A modern ridesharing application built with Compose Multiplatform for Android, iOS, and Web, with a Spring Boot backend.

## Features

- **Multi-platform Support**: Single codebase for Android, iOS, and Web using Compose Multiplatform
- **User Roles**: Users can register as Driver, Passenger, or Both
- **Driver Features**: 
  - Announce future rides with origin, destination, departure time, and price
  - Manage available seats
- **Passenger Features**:
  - Browse available rides
  - Book rides with one click
  - View ride details including driver information

## Tech Stack

### Frontend
- **Compose Multiplatform**: Shared UI across Android, iOS, and Web
- **Kotlin**: Primary programming language
- **Navigation 3**: Jetpack Compose Navigation for type-safe routing
- **Ktor Client**: HTTP client for API communication
- **Kotlinx Serialization**: JSON serialization
- **Latest Standards**: Android Target SDK 35, iOS App Store compliance

### Backend
- **Spring Boot 3.2.5**: Backend framework
- **Kotlin**: Backend programming language
- **PostgreSQL**: Database
- **JPA/Hibernate**: ORM for database operations
- **RESTful API**: Communication protocol

## Prerequisites

- JDK 17 or higher
- Docker and Docker Compose
- Gradle 8.7+ (or use included wrapper)

## Running the Application

### Backend with Docker Compose

The backend can be run using Docker Compose. First, build the backend JAR locally:

```bash
# Build the backend JAR
./gradlew :backend:bootJar --no-daemon
```

Then start the services:

```bash
# Start the backend and PostgreSQL database
docker-compose up --build -d

# Check the status
docker-compose ps

# View logs
docker-compose logs -f backend

# The backend will be available at http://localhost:8080
```

**Note**: If the backend fails to start with "UnknownHostException: postgres", restart it:

```bash
docker-compose restart backend
```

To stop the services:

```bash
docker-compose down

# To stop and remove volumes
docker-compose down -v
```

### Running Locally (Development)

#### Backend

```bash
# Make sure PostgreSQL is running locally or update application.properties
cd backend
./gradlew bootRun
```

#### Android App

```bash
./gradlew :composeApp:assembleDebug
# Or open the project in Android Studio
```

#### iOS App

```bash
# Open the project in Xcode
cd iosApp
open iosApp.xcworkspace
```

#### Web App

```bash
./gradlew :composeApp:wasmJsBrowserDevelopmentRun
# The web app will open in your default browser
```

## API Endpoints

### Swagger/OpenAPI Documentation

The backend includes interactive API documentation using Swagger/OpenAPI:

```
Swagger UI: http://localhost:8080/swagger-ui.html
OpenAPI JSON: http://localhost:8080/v3/api-docs
```

The Swagger UI provides:
- Interactive API testing
- Request/response examples
- Data model schemas
- Authentication details (when applicable)

### Users
- `POST /api/users/register` - Register a new user
- `GET /api/users` - Get all users
- `GET /api/users/{id}` - Get user by ID

### Rides
- `POST /api/rides` - Create a new ride (driver)
- `GET /api/rides` - Get all rides
- `GET /api/rides/available` - Get available rides
- `GET /api/rides/{id}` - Get ride by ID
- `GET /api/rides/driver/{driverId}` - Get rides by driver

### Bookings
- `POST /api/bookings` - Create a booking (passenger)
- `GET /api/bookings/{id}` - Get booking by ID
- `GET /api/bookings/ride/{rideId}` - Get bookings for a ride
- `GET /api/bookings/passenger/{passengerId}` - Get bookings for a passenger

## Project Structure

```
TaxiRado/
├── backend/                    # Spring Boot backend
│   ├── src/
│   │   └── main/
│   │       ├── kotlin/
│   │       │   └── com/taxirado/backend/
│   │       │       ├── controller/     # REST controllers
│   │       │       ├── model/          # JPA entities
│   │       │       ├── repository/     # Data repositories
│   │       │       └── dto/            # Data transfer objects
│   │       └── resources/
│   │           └── application.properties
│   ├── Dockerfile
│   └── build.gradle.kts
├── composeApp/                 # Multiplatform frontend
│   ├── src/
│   │   ├── commonMain/         # Shared code
│   │   ├── androidMain/        # Android-specific
│   │   ├── iosMain/            # iOS-specific
│   │   └── wasmJsMain/         # Web-specific
│   └── build.gradle.kts
├── docker-compose.yml
└── README.md
```

## Configuration

### Backend Configuration

Edit `backend/src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/taxirado
spring.datasource.username=taxirado
spring.datasource.password=taxirado123
```

### Frontend API URL

The API client defaults to `http://localhost:8080`. To change this, edit the `ApiClient` class in `composeApp/src/commonMain/kotlin/com/taxirado/ApiClient.kt`.

## Development

### Building the Project

```bash
# Build all modules
./gradlew build

# Build only backend
./gradlew :backend:build

# Build only frontend
./gradlew :composeApp:build
```

## License

MIT

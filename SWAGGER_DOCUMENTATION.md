# Swagger API Documentation - TaxiRado

## Access Points

When the backend is running, Swagger documentation is available at:

- **Swagger UI (Interactive)**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON Spec**: http://localhost:8080/v3/api-docs
- **OpenAPI YAML Spec**: http://localhost:8080/v3/api-docs.yaml

## What You'll See

### API Overview
- **Title**: TaxiRado API
- **Version**: 1.0.0
- **Description**: REST API for TaxiRado ridesharing platform. Users can register as drivers or passengers, drivers can announce rides, and passengers can book available rides.
- **Contact**: TaxiRado - https://github.com/radostinmar/TaxiRado
- **Server**: http://localhost:8080

### API Groups

#### 1. Users (User Management API)
Registration and user information endpoints

**Endpoints:**
- `POST /api/users/register` - Register a new user with a role (DRIVER, PASSENGER, or BOTH)
  - Response 201: User successfully registered
  - Response 409: Username already exists
  
- `GET /api/users/{id}` - Retrieve user information by user ID
  - Response 200: User found
  - Response 404: User not found
  
- `GET /api/users` - Retrieve a list of all registered users
  - Response 200: List of users retrieved successfully

#### 2. Rides (Ride Management API)
Create and browse rides

**Endpoints:**
- `POST /api/rides` - Drivers can announce a new ride with origin, destination, time, and price
  - Response 201: Ride created successfully
  - Response 404: Driver not found
  
- `GET /api/rides` - Retrieve all rides in the system
  - Response 200: List of all rides
  
- `GET /api/rides/available` - Retrieve all available future rides that passengers can book
  - Response 200: List of available rides

#### 3. Bookings (Booking Management API)
Passengers can book rides

**Endpoints:**
- `POST /api/bookings` - Passengers can book available seats on a ride
  - Response 201: Booking created successfully
  - Response 404: Ride or passenger not found
  - Response 400: Not enough available seats

## Interactive Features

The Swagger UI provides:
- **Try it out**: Test each endpoint directly from the browser
- **Request/Response Examples**: See sample data formats
- **Schema Documentation**: View data models (User, Ride, Booking)
- **Parameter Documentation**: Understand what each field means
- **Response Status Codes**: Know what each HTTP status means

## Data Models

### UserRegistrationRequest
```json
{
  "username": "string",
  "password": "string",
  "email": "string",
  "role": "DRIVER" | "PASSENGER" | "BOTH"
}
```

### UserResponse
```json
{
  "id": 0,
  "username": "string",
  "email": "string",
  "role": "DRIVER" | "PASSENGER" | "BOTH"
}
```

### RideCreationRequest
```json
{
  "driverId": 0,
  "origin": "string",
  "destination": "string",
  "departureTime": "2026-01-30T19:00:00",
  "price": 0.0,
  "availableSeats": 0
}
```

### RideResponse
```json
{
  "id": 0,
  "driverId": 0,
  "driverName": "string",
  "origin": "string",
  "destination": "string",
  "departureTime": "2026-01-30T19:00:00",
  "price": 0.0,
  "availableSeats": 0,
  "status": "AVAILABLE" | "FULL" | "COMPLETED" | "CANCELLED"
}
```

### BookingRequest
```json
{
  "rideId": 0,
  "passengerId": 0,
  "seatsBooked": 1
}
```

### BookingResponse
```json
{
  "id": 0,
  "rideId": 0,
  "passengerId": 0,
  "seatsBooked": 1,
  "status": "CONFIRMED" | "CANCELLED"
}
```

## How to Use

1. Start the backend:
   ```bash
   # Build the backend JAR
   ./gradlew :backend:bootJar --no-daemon
   
   # Start with Docker Compose
   docker-compose up --build -d
   
   # Restart backend if needed
   docker-compose restart backend
   ```

2. Open your browser to: http://localhost:8080/swagger-ui.html

3. Explore the API:
   - Click on any endpoint to expand it
   - Click "Try it out" to test the endpoint
   - Fill in the request body/parameters
   - Click "Execute" to send the request
   - View the response

## Example Workflow in Swagger UI

1. **Register a Driver**:
   - Navigate to Users → POST /api/users/register
   - Click "Try it out"
   - Enter request body:
     ```json
     {
       "username": "john_driver",
       "password": "password123",
       "email": "john@example.com",
       "role": "DRIVER"
     }
     ```
   - Click "Execute"
   - Note the returned user ID

2. **Create a Ride**:
   - Navigate to Rides → POST /api/rides
   - Click "Try it out"
   - Enter request body with driver ID from step 1:
     ```json
     {
       "driverId": 1,
       "origin": "San Francisco",
       "destination": "Los Angeles",
       "departureTime": "2026-02-15T10:00:00",
       "price": 50.00,
       "availableSeats": 3
     }
     ```
   - Click "Execute"

3. **Register a Passenger**:
   - Navigate to Users → POST /api/users/register
   - Register as PASSENGER

4. **View Available Rides**:
   - Navigate to Rides → GET /api/rides/available
   - Click "Try it out" → "Execute"
   - See the ride created in step 2

5. **Book a Ride**:
   - Navigate to Bookings → POST /api/bookings
   - Use passenger ID and ride ID
   - Click "Execute" to complete the booking

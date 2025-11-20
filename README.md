# User Management API

A Spring Boot application providing user management, authentication, and event tracking capabilities with JWT-based security.

## Features

- **User Authentication**: Registration, login, and email verification
- **JWT Security**: Token-based authentication and authorization
- **Role-Based Access Control**: Admin and User roles
- **Event Tracking**: Comprehensive audit logging for user actions
- **Password Management**: Secure password change with attempt limits
- **Email Notifications**: Verification code delivery via Gmail SMTP
- **Admin Dashboard**: User and event management endpoints
- **API Documentation**: Interactive Swagger UI

## Tech Stack

- **Java 17**
- **Spring Boot 3.5.7**
- **Spring Security** with JWT authentication
- **Spring Data JPA** with MySQL
- **Docker & Docker Compose**
- **Swagger/OpenAPI** for API documentation
- **JavaMailSender** for email verification
- **Lombok** for reducing boilerplate
- **MapStruct** for object mapping

## Prerequisites

- Java 17 or higher
- Docker and Docker Compose
- Gradle 8.14.3 (wrapper included)

## Quick Start

### 1. Clone and Navigate to Project

```bash
cd InterviewProject
```

### 2. Build the Application

```bash
./gradlew clean build
```

For Windows:
```bash
gradlew.bat clean build
```

### 3. Run with Docker Compose

```bash
docker-compose up --build
```

This will:
- Start MySQL 8.0 database on port 3306
- Build and start the Spring Boot application on port 8080
- Create default admin user automatically

### 4. Access the Application

- **API Base URL**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs**: http://localhost:8080/api-docs

## Default Admin Credentials

```
Email: admin@gmail.com
Username: admin
Password: admin1234
Phone: +994501234567
```

**⚠️ Important**: Change the admin password after first login!

## Environment Variables

Configure these in `docker-compose.yml` or your environment:

```yaml
JWT_SECRET_KEY: your-base64-encoded-secret-key
ADMIN_EMAIL: admin@gmail.com
ADMIN_PASSWORD: admin1234
ADMIN_USERNAME: admin
ADMIN_PHONE: +994501234567
```

## Database Configuration

### MySQL (Default)

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/interview_user_management
spring.datasource.username=root
spring.datasource.password=root1234
```

### Docker MySQL Access

```bash
docker exec -it interview-mysql mysql -u root -proot1234
```

## API Endpoints

### Authentication (`/api/v1/auth`)

#### Register User
```http
POST /api/v1/auth/signup
Content-Type: application/json

{
  "name": "John",
  "surname": "Doe",
  "userName": "johndoe",
  "email": "john.doe@example.com",
  "password": "password123",
  "phone": "+994501234567"
}
```

#### Verify Account
```http
POST /api/v1/auth/verify
Content-Type: application/json

{
  "email": "john.doe@example.com",
  "verificationCode": "123456"
}
```

#### Login
```http
POST /api/v1/auth/login
Content-Type: application/json

{
  "email": "john.doe@example.com",
  "password": "password123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expiresIn": 3600000
}
```

#### Resend Verification Code
```http
POST /api/v1/auth/resend?email=john.doe@example.com
```

### User Management (`/api/v1/users`)

#### Get Current User Profile
```http
GET /api/v1/users/my-profile
Authorization: Bearer {token}
```

#### Change Password
```http
PATCH /api/v1/users/my-profile/password
Authorization: Bearer {token}
Content-Type: application/json

{
  "currentPassword": "oldPassword123",
  "newPassword": "newPassword456"
}
```

#### Delete Current User Account
```http
DELETE /api/v1/users/my-profile
Authorization: Bearer {token}
```

### Admin Endpoints

All admin endpoints require `ROLE_ADMIN` and the `Authorization: Bearer {token}` header.

#### Get All Users (Paginated)
```http
GET /api/v1/users?page=0&size=10&sort=id,asc
Authorization: Bearer {admin-token}
```

#### Search Users
```http
GET /api/v1/users/search?query=john&page=0&size=10
Authorization: Bearer {admin-token}
```

#### Get Users by Status
```http
GET /api/v1/users/status/ACTIVE?page=0&size=10
Authorization: Bearer {admin-token}
```

#### Get User Statistics
```http
GET /api/v1/users/stats/count
Authorization: Bearer {admin-token}
```

**Response:**
```json
{
  "total": 25,
  "active": 20,
  "deleted": 5
}
```

#### Get User by ID
```http
GET /api/v1/users/1
Authorization: Bearer {admin-token}
```

#### Reset User Password (Admin)
```http
PATCH /api/v1/users/1/password
Authorization: Bearer {admin-token}
Content-Type: application/json

{
  "newPassword": "newPassword123"
}
```

#### Delete User (Admin)
```http
DELETE /api/v1/users/1
Authorization: Bearer {admin-token}
```

### Event Management (`/api/v1/events`)

#### Get Current User Events
```http
GET /api/v1/events/my-events?page=0&size=10
Authorization: Bearer {token}
```

#### Get Latest Events for Current User
```http
GET /api/v1/events/my-events/latest
Authorization: Bearer {token}
```

#### Get All Events (Admin)
```http
GET /api/v1/events?page=0&size=10&sort=eventTime,desc
Authorization: Bearer {admin-token}
```

#### Filter Events (Admin)
```http
GET /api/v1/events?userId=1&eventType=PASSWORD_CHANGED&startDate=2024-01-01T00:00:00&endDate=2024-12-31T23:59:59&page=0&size=10
Authorization: Bearer {admin-token}
```

#### Get Events by User ID (Admin)
```http
GET /api/v1/events/user/1?page=0&size=10
Authorization: Bearer {admin-token}
```

#### Get Events by Type (Admin)
```http
GET /api/v1/events/type/USER_REGISTERED?page=0&size=10
Authorization: Bearer {admin-token}
```

#### Get Event Count by User (Admin)
```http
GET /api/v1/events/count/user/1
Authorization: Bearer {admin-token}
```

## Event Types

The system tracks the following events:

- `USER_REGISTERED`: User account created
- `USER_VERIFIED`: Email verification completed
- `PASSWORD_CHANGED`: Password successfully changed
- `USER_DELETED`: User account deleted

## User Statuses

- `ACTIVE`: Normal active user
- `DELETED`: Soft-deleted user (cannot login)

## User Roles

- `USER`: Standard user with basic permissions
- `ADMIN`: Administrator with full system access

## Email Configuration

The application uses Gmail SMTP for sending verification emails. Configure in `application.properties`:

```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
```

**Note**: Use [Google App Passwords](https://support.google.com/accounts/answer/185833) instead of your regular password.

## Security Features

- **JWT Token**: 1-hour expiration (configurable)
- **Password Encoding**: BCrypt hashing
- **Verification Codes**: 15-minute expiration
- **Password Change Limit**: 1 change per user (configurable)
- **Stateless Sessions**: No server-side session storage
- **Role-Based Authorization**: Method-level security with `@PreAuthorize`

## Testing with cURL

### Register and Login Flow

```bash
# 1. Register new user
curl -X POST http://localhost:8080/api/v1/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Jane",
    "surname": "Smith",
    "userName": "janesmith",
    "email": "jane.smith@example.com",
    "password": "secure123",
    "phone": "+994501234568"
  }'

# 2. Check email for verification code and verify
curl -X POST http://localhost:8080/api/v1/auth/verify \
  -H "Content-Type: application/json" \
  -d '{
    "email": "jane.smith@example.com",
    "verificationCode": "123456"
  }'

# 3. Login to get JWT token
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "jane.smith@example.com",
    "password": "secure123"
  }'

# 4. Use token to access protected endpoints
curl -X GET http://localhost:8080/api/v1/users/my-profile \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## Database Schema & Entity Relationships

### Entity Relationship Overview

The application has a simple but effective database design with two main entities:

**User (Parent Entity)**
- Stores user account information, credentials, and verification details
- Contains fields like name, email, password, role (ADMIN/USER), status (ACTIVE/DELETED)
- Tracks `passwordChangeAttempts` to limit how many times a user can change their password

**UserEvent (Child Entity - Audit Log)**
- Records all important user actions as audit trail
- Has a **Many-to-One relationship** with User (many events belong to one user)
- Cannot exist without a User (foreign key constraint)
- Tracks event types: USER_REGISTERED, USER_VERIFIED, PASSWORD_CHANGED, USER_DELETED

### Key Relationship Characteristics

- **One-to-Many**: Each User can have multiple Events, but each Event belongs to exactly one User
- **Eager Loading**: When you fetch an Event, it automatically loads the associated User information
- **No Cascading**: Deleting a User does NOT delete their Events - this preserves audit history
- **Soft Delete**: Users are marked as DELETED (status change) rather than removed from database
- **Privacy Protection**: When Events are returned in API responses, sensitive User fields (password, verification codes) are automatically hidden

### Why This Design?

This separation allows the system to maintain a complete audit trail of user actions while keeping user management independent. Even if a user deletes their account, the system retains evidence of their past actions for compliance and security purposes.

## Admin Auto-Initialization on Startup

### How the Admin Account is Created

The application uses Spring Boot's **CommandLineRunner** interface to automatically create a default admin user when the application first starts. This is implemented in the `AdminUserInitializer` class.

### The Process Explained

**1. Application Startup Trigger**
When Spring Boot starts, it looks for any beans that implement `CommandLineRunner` and executes their `run()` method automatically. This happens after all Spring components are initialized but before the application starts accepting requests.

**2. Existence Check**
The initializer first checks if an admin user already exists by searching for the configured admin email in the database. This check ensures the admin is only created once - no duplicates are created on subsequent restarts.

**3. Admin Creation (If Needed)**
If no admin exists, the system creates one with:
- Predefined credentials (configurable via environment variables)
- ADMIN role for full system access
- ACTIVE status 
- Pre-verified status (no email verification needed)
- Password change attempts set to 0

**4. Password Security**
The admin password is encrypted using BCrypt before being stored in the database - never stored in plain text.

**5. Logging**
The system logs whether it created a new admin or found an existing one, making it easy to track in application logs.

### Configuring Admin Credentials

The admin credentials can be customized through environment variables without changing code:

- `ADMIN_EMAIL` - Email address for admin login (default: admin@gmail.com)
- `ADMIN_PASSWORD` - Admin password (default: admin1234)
- `ADMIN_USERNAME` - Username for admin (default: admin)
- `ADMIN_PHONE` - Phone number (default: +994501234567)

These can be set in Docker Compose, system environment variables, or hosting platform configurations.

### Why This Approach?

- **Convenience**: No manual database setup needed - admin ready on first launch
- **Security**: Credentials not hardcoded, can be changed per environment
- **Reliability**: Idempotent operation - safe to run multiple times
- **Automation**: Perfect for containerized deployments and CI/CD pipelines

## Password Change Limit System

### What It Does

The application restricts how many times a regular user can change their password to prevent abuse and maintain security. This is controlled through the `LimitProperties` configuration class.

### How It Works

**1. Configuration**
The `LimitProperties` class stores the maximum allowed password changes. Currently set to 1 by default, meaning users can change their password only once after registration.

**2. Tracking Attempts**
Each User entity has a `passwordChangeAttempts` field that starts at 0. Every time a user successfully changes their password, this counter increments by 1.

**3. Enforcement**
Before allowing a password change, the system checks:
- Has the user reached their limit? → Reject with error
- Is the current password correct? → Validate
- Is the new password different from current? → Validate
- If all checks pass → Allow change and increment counter

**4. Audit Trail**
Every successful password change creates a `PASSWORD_CHANGED` event in the audit log for security tracking.

### Making the Limit Configurable

While currently hardcoded at 1, the limit can be made configurable in several ways:

**Option A: Application Properties**
Add a property in `application.properties` that can be changed without recompiling code:
```properties
app.limits.max-password-change-attempts=3
```

**Option B: Environment Variable**
Set via Docker or system environment, allowing different limits per deployment environment (development vs production).

**Option C: Database Configuration**
Store the limit in a database table for runtime changes without application restart - ideal for SaaS platforms.

### Different Use Cases

- **Limit = 1**: Strict security - force password change after setup, then it's fixed
- **Limit = 3-5**: Moderate - allows users to fix mistakes or respond to security concerns
- **Unlimited**: For testing environments or special admin accounts

### Admin Override

Admins can reset any user's password through the admin API endpoint (`PATCH /api/v1/users/{userId}/password`) which bypasses the limit check entirely. This ensures admins can always help users who forgot passwords or need account recovery, while maintaining restrictions on regular users.

## Project Structure

```
src/main/java/com/example/interviewproject/
├── configuration/
│   ├── AdminUserInitializer.java      # Auto-creates admin on startup
│   ├── ApplicationConfiguration.java   # Core Spring beans
│   ├── EmailConfiguration.java         # Gmail SMTP setup
│   ├── JwtAuthenticationFilter.java    # JWT token validation filter
│   ├── JwtProperties.java              # JWT configuration properties
│   ├── SecurityConfiguration.java      # Spring Security setup
│   └── SwaggerConfig.java              # API documentation config
├── controller/
│   ├── AuthenticationController.java   # Registration, login, verify
│   ├── UserController.java             # User CRUD operations
│   ├── EventController.java            # Event/audit log endpoints
│   └── demoController.java             # Demo/test endpoints
├── dto/
│   ├── request/
│   │   ├── UserLoginRequest.java       # Login payload
│   │   ├── UserRegisterRequest.java    # Registration payload
│   │   ├── VerifyUserRequest.java      # Email verification payload
│   │   └── PasswordRequest.java        # Password change payload
│   └── response/
│       ├── LoginResponse.java          # JWT token response
│       └── CurrentUserInfo.java        # User profile response
├── model/
│   ├── User.java                       # User entity (implements UserDetails)
│   ├── UserEvent.java                  # Audit log entity
│   ├── UserRole.java                   # Enum: ADMIN, USER
│   ├── UserStatus.java                 # Enum: ACTIVE, DELETED
│   └── EventType.java                  # Enum: event types
├── repository/
│   ├── UserRepository.java             # User data access + custom queries
│   └── EventRepository.java            # Event data access + filtering
├── service/
│   ├── AuthenticationService.java      # Registration, login, verification
│   ├── UserService.java                # User management business logic
│   ├── EventService.java               # Event tracking and retrieval
│   ├── JwtService.java                 # JWT generation and validation
│   └── EmailService.java               # Email sending functionality
└── Utils/
    └── LimitProperties.java            # Configurable limits (password attempts)
```

## Development

### Run without Docker

1. Start MySQL locally:
```bash
mysql -u root -p
CREATE DATABASE interview_user_management;
```

2. Update `application.properties` with your MySQL credentials

3. Run the application:
```bash
./gradlew bootRun
```

### Build JAR

```bash
./gradlew bootJar
```

The JAR will be in `build/libs/InterviewProject-0.0.1-SNAPSHOT.jar`

### Run JAR

```bash
java -jar build/libs/InterviewProject-0.0.1-SNAPSHOT.jar
```

## Troubleshooting

### Port Already in Use

```bash
# Kill process on port 8080
lsof -ti:8080 | xargs kill -9

# Or change port in application.properties
server.port=8081
```

### Docker Issues

```bash
# Stop and remove containers
docker-compose down

# Remove volumes
docker-compose down -v

# Rebuild from scratch
docker-compose up --build --force-recreate
```

### Database Connection Failed

Check that MySQL is running:
```bash
docker ps | grep mysql
```

Check logs:
```bash
docker logs interview-mysql
docker logs interview-spring-app
```

## License

This project is created for interview/demonstration purposes.

## Contact

For questions or issues, please create an issue in the repository.

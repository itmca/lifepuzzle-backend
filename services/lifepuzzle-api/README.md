# lifepuzzle-api

Spring Boot REST API service for the LifePuzzle application.

## Project Structure

```bash
lifepuzzle-api/
├── src/main/java/io/itmca/lifepuzzle/
│   ├── LifePuzzleApplication.java
│   ├── domain/
│   │   ├── auth/                     # Authentication & Authorization
│   │   │   ├── endpoint/             # REST endpoints
│   │   │   ├── jwt/                  # JWT token handling
│   │   │   ├── service/              # Auth business logic
│   │   │   └── type/                 # Auth types & enums
│   │   ├── hero/                     # Hero management
│   │   │   ├── endpoint/             # Hero REST endpoints
│   │   │   ├── entity/               # Hero JPA entities
│   │   │   ├── file/                 # Hero file handling
│   │   │   ├── repository/           # Data access layer
│   │   │   ├── service/              # Hero business logic
│   │   │   └── type/                 # Hero types & enums
│   │   ├── story/                    # Story management
│   │   │   ├── endpoint/             # Story REST endpoints
│   │   │   ├── entity/               # Story JPA entities
│   │   │   ├── file/                 # Story file handling
│   │   │   ├── repository/           # Data access layer
│   │   │   ├── service/              # Story business logic
│   │   │   └── type/                 # Story types & enums
│   │   ├── user/                     # User management
│   │   │   ├── endpoint/             # User REST endpoints
│   │   │   ├── entity/               # User JPA entities
│   │   │   ├── file/                 # User file handling
│   │   │   ├── model/                # Domain models
│   │   │   ├── repository/           # Data access layer
│   │   │   ├── service/              # User business logic
│   │   │   └── type/                 # User types & enums
│   │   ├── gallery/                  # Gallery management
│   │   │   ├── endpoint/             # Gallery REST endpoints
│   │   │   ├── repository/           # Data access layer
│   │   │   └── service/              # Gallery business logic
│   │   └── sites/                    # App linking & sharing
│   │       └── *.java                # Universal links & app links
│   └── global/
│       ├── ai/                       # AI integrations
│       │   ├── chat/                 # OpenAI chat service
│       │   └── stt/                  # Speech-to-text service
│       ├── aop/                      # Aspect-oriented programming
│       ├── config/                   # Application configuration
│       ├── exception/                # Exception handling
│       │   └── handler/              # Global exception handlers
│       ├── file/                     # File management
│       │   ├── repository/           # File storage (S3)
│       │   └── service/              # File upload services
│       ├── jpa/                      # JPA configurations
│       ├── resolver/                 # Argument resolvers
│       ├── slack/                    # Slack integrations
│       └── util/                     # Utility classes
├── src/main/resources/
│   ├── application*.yml              # Environment configurations
│   ├── db/migration/                 # Flyway database migrations
│   ├── logback-spring.xml            # Logging configuration
│   └── templates/                    # HTML templates
├── src/test/                         # Test code
│   └── java/io/itmca/lifepuzzle/
│       ├── domain/                   # Domain-specific tests
│       └── testsupport/              # Test utilities
└── http/                             # HTTP test files
    ├── login.http
    ├── register.http
    └── withdraw.http
```

## Domain Architecture

Each domain follows a consistent structure:

```bash
domain/{domain-name}/
├── endpoint/                         # REST API endpoints
│   ├── request/                      # Request DTOs
│   ├── response/                     # Response DTOs
│   │   └── dto/                      # Data transfer objects
│   └── {Domain}Endpoint.java
├── entity/                           # JPA entities
├── repository/                       # Data access repositories
├── service/                          # Business logic services
├── file/                            # File-related classes
├── model/                           # Domain models
└── type/                            # Enums and type definitions
```

## Key Features

- **Authentication**: JWT-based auth with Apple/Kakao social login
- **Hero Management**: Create and manage hero profiles
- **Story System**: Story creation with multimedia support
- **Gallery**: Photo and content organization
- **File Handling**: S3-based file storage with image resizing
- **AI Integration**: OpenAI chat and Deepgram speech-to-text
- **Monitoring**: Sentry integration with Slack notifications

## Technology Stack

- **Framework**: Spring Boot
- **Database**: MySQL with Flyway migrations
- **Authentication**: JWT with social login (Apple, Kakao)
- **File Storage**: AWS S3
- **AI Services**: OpenAI, Deepgram
- **Monitoring**: Sentry
- **Testing**: JUnit with integration test support

## Getting Started

### Prerequisites

- Java 11+
- MySQL 8.0+
- AWS S3 bucket
- Required API keys (OpenAI, Deepgram, social login providers)

### Configuration

Configure environment-specific settings in:
- `application-local.yml` - Local development
- `application-test.yml` - Testing
- `application-prod.yml` - Production

### Running the Application

```bash
# Development
./gradlew bootRun --args='--spring.profiles.active=local'

# Tests
./gradlew test
```

### HTTP Testing

Use the provided HTTP files in the `http/` directory to test endpoints:
- `login.http` - Authentication testing
- `register.http` - User registration testing
- `withdraw.http` - User withdrawal testing
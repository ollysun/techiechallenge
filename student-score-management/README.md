# Student Scores Management System

A comprehensive Spring Boot web application for managing student scores across 5 subjects with statistical reporting capabilities.

## Features

- **Student Management**: Create and manage student records
- **Score Management**: Store and validate scores for 5 subjects (0-100 range)
- **Statistical Reports**: Generate reports with mean, median, and mode calculations
- **RESTful API**: Fully documented OpenAPI/Swagger endpoints
- **Pagination & Filtering**: Efficient data retrieval with search capabilities
- **Data Validation**: Comprehensive input validation and error handling
- **Docker Support**: Containerized deployment with Docker Compose

## Technology Stack

- **Backend**: Spring Boot 3.1.0, Java 17
- **Database**: PostgreSQL 15
- **API Documentation**: OpenAPI 3 (Swagger)
- **Testing**: JUnit 5, Mockito, Spring Boot Test
- **Containerization**: Docker, Docker Compose
- **Build Tool**: Maven

## Design Patterns & Architecture

### Domain-Driven Design (DDD)
- Clear separation of concerns with Entity, Repository, Service, and Controller layers
- Rich domain models with business logic validation

### Factory Pattern
- `StatisticsCalculator` acts as a factory for statistical computations
- Centralized logic for mean, median, and mode calculations

### Repository Pattern
- Abstract data access layer
- Consistent data operations across different entities

### Builder Pattern (implicit)
- Fluent API creation through constructors and setters
- Clean object creation in tests

## Database Schema

### Students Table
- `id` (Primary Key)
- `name` (Required)
- `student_id` (Unique)
- `created_at`, `updated_at` (Timestamps)

### Subject_Scores Table
- `id` (Primary Key)
- `student_id` (Foreign Key)
- `subject_name` (Required)
- `score` (0-100, Required)
- Unique constraint on (student_id, subject_name)

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/students` | Create student with scores |
| GET | `/api/students` | Get paginated students with filtering |
| GET | `/api/students/{id}` | Get student by ID |
| GET | `/api/students/reports` | Generate comprehensive reports |
| DELETE | `/api/students/{id}` | Delete student |

## Statistical Calculations

- **Mean**: Average of all subject scores
- **Median**: Middle value of sorted scores
- **Mode**: Most frequently occurring score (returns smallest in case of ties)

## Running the Application

### Prerequisites
- Docker and Docker Compose
- Java 17 (for local development)

### Using Docker Compose (Recommended)

1. **Clone and build the project**:
   ```bash
   git clone <repository-url>
   cd student-scores-app
   
2, **Build and run with Docker Compose**:
   ```bash
   docker-compose up --build
   ```
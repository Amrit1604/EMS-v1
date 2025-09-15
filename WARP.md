# WARP.md

This file provides guidance to WARP (warp.dev) when working with code in this repository.

## Quick Start

The easiest way to start the entire system is to use the automated startup script:
```bash
# Windows
./start-payroll-system.bat

# Manual startup (if needed)
# 1. Start MongoDB on localhost:27017
# 2. Backend services in order:
cd backend/discovery-server && mvn spring-boot:run
cd backend/employee-service && mvn spring-boot:run  
cd backend/payroll-service && mvn spring-boot:run
cd backend/api-gateway && mvn spring-boot:run

# 3. Frontend options:
cd frontend && npm start                    # React app (port 3000)
cd employee-payroll-frontend && npm start  # Alternative React frontend
```

## Common Development Commands

### Backend (Spring Boot Microservices)
```bash
# Build all services
mvn clean compile -f backend/discovery-server/pom.xml
mvn clean compile -f backend/employee-service/pom.xml
mvn clean compile -f backend/payroll-service/pom.xml
mvn clean compile -f backend/api-gateway/pom.xml

# Run tests
mvn test -f backend/employee-service/pom.xml
mvn test -f backend/payroll-service/pom.xml

# Build JARs
mvn clean package -f backend/employee-service/pom.xml
mvn clean package -f backend/payroll-service/pom.xml

# Run integration tests
mvn verify -f backend/employee-service/pom.xml
mvn verify -f backend/payroll-service/pom.xml
```

### Frontend Development
```bash
# Main frontend (HTML/CSS/JS with Python server)
cd frontend && python -m http.server 3000

# React frontend alternative 
cd employee-payroll-frontend && npm install && npm start
cd employee-payroll-frontend && npm test
cd employee-payroll-frontend && npm run build
```

### Database Operations
```bash
# Start MongoDB (Docker)
docker run -d -p 27017:27017 --name mongodb mongo:latest

# Connect to MongoDB shell
mongosh mongodb://localhost:27017/employeedb

# Check collections
db.employees.find()
db.departments.find()
db.payrolls.find()
```

## Architecture Overview

This is a **microservices-based Employee Payroll Management System** with the following components:

### Service Architecture
```
Frontend (Port 3000) → API Gateway (8080) → [Eureka Discovery (8761)]
                                  ↓
                        Employee Service (8081) ← → MongoDB (employeedb)
                        Payroll Service (8082)  ← → MongoDB (employeedb)
```

### Service Responsibilities

**Discovery Server (Port 8761)**
- Eureka service registry for microservice discovery
- All backend services register here
- Web UI available at http://localhost:8761

**API Gateway (Port 8080)**
- Single entry point for all client requests
- Routes requests to appropriate microservices
- CORS enabled for frontend communication

**Employee Service (Port 8081)**
- Employee CRUD operations with full lifecycle management
- Department and Designation master data management
- Entity relationships: Employee → Department, Designation, Manager
- JWT security configuration (currently disabled for development)
- MongoDB collections: `employees`, `departments`, `designations`

**Payroll Service (Port 8082)**
- Payroll processing with status workflow (PENDING → APPROVED → PAID)
- Tax calculations and salary computations
- PDF/Excel report generation utilities
- Feign client integration with Employee Service
- MongoDB collection: `payrolls`

### Frontend Architecture
Two frontend implementations exist:

**Main Frontend (`/frontend`)**
- HTML/CSS/Bootstrap with Vanilla JavaScript
- Direct API integration with backend services
- Responsive design with real-time updates
- Python HTTP server for development

**React Frontend (`/employee-payroll-frontend`)**
- React 19 with modern hooks and functional components
- React Router for SPA navigation
- Zustand for state management (auth store pattern)
- React Query for server state management
- Proxy configuration to API Gateway

## Key Design Patterns

### Backend Patterns
- **Repository Pattern**: `@Repository` interfaces extending MongoRepository
- **Service Layer**: Business logic in `@Service` classes with implementation separation
- **DTO Pattern**: Separate DTOs for API contracts vs internal entities
- **Controller Pattern**: REST controllers with OpenAPI/Swagger documentation
- **Exception Handling**: Global exception handler with custom exceptions
- **Security**: JWT-based authentication (configurable, currently disabled)

### Entity Relationships
```java
Employee {
  @DBRef Department department
  @DBRef Designation designation  
  @DBRef Employee manager
  @DBRef Set<Employee> subordinates
  Address address (embedded)
  BankDetails bankDetails (embedded)
}

Payroll {
  String employeeId (reference to Employee Service)
  PayrollStatus status (PENDING/APPROVED/PAID)
  BigDecimal calculations...
}
```

### Frontend Patterns (React)
- **Component Structure**: Layout → Pages → Components hierarchy
- **State Management**: Zustand stores with persistence
- **Routing**: Protected routes with role-based access
- **API Integration**: Centralized HTTP client with React Query
- **Form Handling**: Controlled components with validation

## Development Guidelines

### Backend Development
- Java 17+ required, Spring Boot 3.1.5 framework
- MongoDB as shared database across services (`employeedb`)
- Service-to-service communication via Feign clients
- All services register with Eureka for discovery
- OpenAPI documentation available at `/swagger-ui.html` for each service
- Security annotations present but disabled for development ease

### Database Schema
- Single MongoDB database `employeedb` shared across services
- Employee service owns: `employees`, `departments`, `designations` collections
- Payroll service owns: `payrolls` collection
- Cross-service data access via Feign clients, not direct DB access

### Frontend Development
- Two parallel frontend implementations maintained
- Main frontend: Traditional web app with Bootstrap 5 + JavaScript
- React frontend: Modern SPA with component-based architecture
- Both proxy through API Gateway (port 8080)

### Testing Strategy
- Unit tests: Service layer business logic testing
- Integration tests: Repository layer with embedded MongoDB
- Manual testing: Swagger UI endpoints for API testing
- End-to-end testing: Frontend automation through web interface

### Security Configuration
- JWT infrastructure implemented but disabled for development
- CSRF disabled, CORS enabled for localhost development
- Role-based authorization annotations present (`@PreAuthorize`)
- Production security can be enabled via configuration

## Service Dependencies

**Startup Order (Critical)**:
1. MongoDB (external dependency)
2. Discovery Server (Eureka registry)
3. Employee Service (master data provider)
4. Payroll Service (depends on Employee Service)
5. API Gateway (routes to all services)
6. Frontend application

**Inter-service Communication**:
- Payroll Service → Employee Service (via Feign client)
- All services → Discovery Server (service registration)
- Frontend → API Gateway → Individual Services

This architecture enables independent development and deployment of each service while maintaining data consistency and proper service boundaries.
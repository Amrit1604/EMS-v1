# Employee Payroll System

A comprehensive, modern Employee Payroll Management System built with microservices architecture, featuring Spring Boot backend services, MongoDB database, and a professional React frontend with animations and responsive design.

## 🚀 Features

### Backend Features
- **Microservices Architecture** with Spring Boot
- **Employee Management** - Complete CRUD operations for employee data
- **Payroll Processing** - Automated salary calculations with tax deductions
- **Department & Designation Management** - Organizational structure management
- **JWT Authentication** - Secure role-based access control (HR/Admin/Employee)
- **Service Discovery** - Eureka server for microservice registration
- **API Gateway** - Centralized routing and load balancing
- **MongoDB Integration** - Flexible document-based data storage
- **PDF & Excel Reports** - Automated payslip and report generation
- **Tax Calculations** - Indian tax slabs and professional tax integration

### Frontend Features
- **Modern React SPA** - Single Page Application with routing
- **Professional UI/UX** - Clean, responsive design with Tailwind CSS
- **Smooth Animations** - Framer Motion powered transitions
- **Interactive Dashboard** - Real-time analytics and charts
- **Role-based Navigation** - Dynamic menu based on user permissions
- **Form Validation** - Comprehensive input validation and error handling
- **State Management** - Zustand for efficient state handling
- **Material Design** - Material-UI components integration

## 🏗️ Architecture

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Frontend      │    │   API Gateway   │    │ Discovery Server│
│   (React SPA)   │◄──►│   (Port 8080)   │◄──►│   (Port 8761)   │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                                │
                    ┌───────────┼───────────┐
                    │                       │
            ┌───────▼────────┐    ┌────────▼────────┐
            │ Employee Service│    │ Payroll Service │
            │   (Port 8081)   │    │   (Port 8082)   │
            └────────────────┘    └─────────────────┘
                    │                       │
            ┌───────▼────────┐    ┌────────▼────────┐
            │   MongoDB      │    │   MongoDB       │
            │   (Employees)  │    │   (Payroll)     │
            └────────────────┘    └─────────────────┘
```

## 🛠️ Technology Stack

### Backend
- **Framework**: Spring Boot 3.1.5
- **Security**: Spring Security with JWT
- **Database**: MongoDB
- **Service Discovery**: Netflix Eureka
- **API Gateway**: Spring Cloud Gateway
- **Documentation**: OpenAPI/Swagger
- **Build Tool**: Maven
- **Java Version**: 17+

### Frontend
- **Framework**: React 18
- **Routing**: React Router DOM
- **State Management**: Zustand
- **Styling**: Tailwind CSS + Material-UI
- **Animations**: Framer Motion
- **Charts**: Recharts
- **HTTP Client**: Axios
- **Form Handling**: React Hook Form + Yup
- **Icons**: Lucide React

## 📦 Project Structure

```
employee-payroll-system/
├── backend/
│   ├── discovery-server/          # Eureka Server
│   ├── api-gateway/              # Spring Cloud Gateway
│   ├── config-server/            # Configuration Server (Pending)
│   ├── employee-service/         # Employee Management Service
│   │   ├── src/main/java/com/payroll/employee/
│   │   │   ├── controller/       # REST Controllers
│   │   │   ├── service/          # Business Logic
│   │   │   ├── repository/       # Data Access Layer
│   │   │   ├── entity/           # JPA Entities
│   │   │   ├── dto/              # Data Transfer Objects
│   │   │   ├── config/           # Configuration Classes
│   │   │   ├── security/         # Security Components
│   │   │   └── exception/        # Exception Handling
│   │   └── pom.xml
│   └── payroll-service/          # Payroll Management Service
│       ├── src/main/java/com/payroll/salary/
│       │   ├── controller/       # REST Controllers
│       │   ├── service/          # Business Logic
│       │   ├── repository/       # Data Access Layer
│       │   ├── entity/           # JPA Entities
│       │   ├── dto/              # Data Transfer Objects
│       │   ├── client/           # Feign Clients
│       │   ├── utils/            # Utility Classes
│       │   └── exception/        # Exception Handling
│       └── pom.xml
└── frontend/
    ├── src/
    │   ├── components/           # Reusable Components
    │   │   ├── Layout/           # Layout Components
    │   │   ├── Auth/             # Authentication Components
    │   │   └── Dashboard/        # Dashboard Components
    │   ├── pages/                # Page Components
    │   │   ├── Auth/             # Login/Register Pages
    │   │   ├── Dashboard/        # Dashboard Page
    │   │   ├── Employees/        # Employee Management
    │   │   ├── Payroll/          # Payroll Management
    │   │   ├── Departments/      # Department Management
    │   │   ├── Designations/     # Designation Management
    │   │   ├── Reports/          # Reports & Analytics
    │   │   └── Settings/         # System Settings
    │   ├── stores/               # State Management
    │   ├── utils/                # Utility Functions
    │   └── styles/               # Global Styles
    ├── package.json
    └── tailwind.config.js
```

## 🚀 Getting Started

### Prerequisites
- Java 17 or higher
- Node.js 16 or higher
- MongoDB 4.4 or higher
- Maven 3.6 or higher

### Backend Setup

1. **Start MongoDB**
   ```bash
   # Using Docker
   docker run -d -p 27017:27017 --name mongodb mongo:latest
   
   # Or start your local MongoDB instance
   mongod
   ```

2. **Start Discovery Server**
   ```bash
   cd backend/discovery-server
   mvn spring-boot:run
   ```
   Server will start on `http://localhost:8761`

3. **Start API Gateway**
   ```bash
   cd backend/api-gateway
   mvn spring-boot:run
   ```
   Gateway will start on `http://localhost:8080`

4. **Start Employee Service**
   ```bash
   cd backend/employee-service
   mvn spring-boot:run
   ```
   Service will start on `http://localhost:8081`

5. **Start Payroll Service**
   ```bash
   cd backend/payroll-service
   mvn spring-boot:run
   ```
   Service will start on `http://localhost:8082`

### Frontend Setup

1. **Install Dependencies**
   ```bash
   cd frontend
   npm install
   ```

2. **Start Development Server**
   ```bash
   npm start
   ```
   Application will start on `http://localhost:3000`

## 🔐 Authentication

The system uses JWT-based authentication with role-based access control:

### Demo Credentials
- **Admin**: `admin@company.com` / `admin123`
- **HR**: `hr@company.com` / `hr123`
- **Employee**: `employee@company.com` / `emp123`

### Roles & Permissions
- **Admin**: Full system access
- **HR**: Employee and payroll management
- **Employee**: View own profile and payslips

## 📊 API Documentation

Once the services are running, access the API documentation:

- **Employee Service**: `http://localhost:8081/swagger-ui.html`
- **Payroll Service**: `http://localhost:8082/swagger-ui.html`
- **API Gateway**: `http://localhost:8080/swagger-ui.html`

## 🎨 UI Features

### Dashboard
- Real-time statistics and KPIs
- Interactive charts and graphs
- Recent activity feed
- Quick action buttons

### Employee Management
- Complete employee lifecycle management
- Advanced search and filtering
- Bulk operations support
- Profile management with photo upload

### Payroll Processing
- Automated salary calculations
- Tax deduction management
- Payslip generation (PDF/Excel)
- Approval workflow

### Reports & Analytics
- Comprehensive reporting system
- Export capabilities (PDF/Excel)
- Custom date ranges
- Department-wise analytics

## 🔧 Configuration

### Environment Variables

#### Backend Services
```properties
# Database Configuration
spring.data.mongodb.uri=mongodb://localhost:27017/payroll_db

# JWT Configuration
jwt.secret=mySecretKey
jwt.expiration=86400000

# Eureka Configuration
eureka.client.service-url.defaultZone=http://localhost:8761/eureka
```

#### Frontend
```env
REACT_APP_API_BASE_URL=http://localhost:8080
REACT_APP_ENVIRONMENT=development
```

## 🧪 Testing

### Backend Testing
```bash
# Run unit tests
mvn test

# Run integration tests
mvn verify
```

### Frontend Testing
```bash
# Run tests
npm test

# Run tests with coverage
npm run test:coverage
```

## 📈 Performance Optimization

### Backend
- Connection pooling for MongoDB
- Caching with Redis (configurable)
- Async processing for heavy operations
- Database indexing for optimal queries

### Frontend
- Code splitting with React.lazy()
- Image optimization and lazy loading
- Bundle size optimization
- Service worker for caching

## 📊 System Requirements

### Minimum Requirements
- **CPU**: 2 cores
- **RAM**: 4GB
- **Storage**: 10GB
- **Network**: Broadband internet connection

### Recommended Requirements
- **CPU**: 4+ cores
- **RAM**: 8GB+
- **Storage**: 20GB+ SSD
- **Network**: High-speed internet connection

---

**Built with ❤️ by the Development Team**

# Employee Payroll System

A comprehensive microservices-based employee payroll management system with automated startup and complete web interface.

## 🚀 Quick Start

**Just run the batch file to start everything automatically:**

```bash
start-payroll-system.bat
```

This will:
1. Start all backend services (Discovery, Employee, Payroll, API Gateway)
2. Launch the frontend web server
3. Automatically open http://localhost:3000 in your browser

## ✨ Features

- **Complete Employee Management** - Add, edit, delete employees with departments/designations
- **Payroll Processing** - Create, approve, and process payments
- **Department & Designation Management** - Master data management
- **Dashboard Analytics** - Real-time statistics and counts
- **Responsive Web Interface** - Works on desktop and mobile
- **Automated Startup** - One-click system launch

## 🏗️ Architecture

**Microservices Backend:**
- **Discovery Server** (Port 8761) - Eureka service registry
- **Employee Service** (Port 8081) - Employee and master data management
- **Payroll Service** (Port 8082) - Payroll processing and calculations
- **API Gateway** (Port 8080) - Single entry point for all APIs

**Frontend:**
- **Web Interface** (Port 3000) - HTML/CSS/JavaScript admin panel

## 🛠️ Technologies

- **Backend**: Spring Boot 3.1.5, Spring Cloud Gateway, Netflix Eureka
- **Database**: MongoDB (employeedb)
- **Frontend**: HTML5, Bootstrap 5, Vanilla JavaScript
- **Security**: CSRF disabled for testing, JWT ready for production

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.6+
- MongoDB running on localhost:27017
- Python 3.x (for frontend server)

## 🎯 Usage

### Automated Startup
```bash
# Windows - Double click or run:
start-payroll-system.bat
```

### Manual Startup (if needed)
```bash
# 1. Start Discovery Server
cd backend/discovery-server && mvn spring-boot:run

# 2. Start Employee Service  
cd backend/employee-service && mvn spring-boot:run

# 3. Start Payroll Service
cd backend/payroll-service && mvn spring-boot:run

# 4. Start API Gateway
cd backend/api-gateway && mvn spring-boot:run

# 5. Start Frontend
cd frontend && python -m http.server 3000
```

## 🌐 API Endpoints

All APIs accessible through API Gateway at `http://localhost:8080/api`

### Employee Management
- `GET /employees` - List all employees (paginated)
- `POST /employees` - Create new employee
- `GET /employees/{id}` - Get employee details
- `PUT /employees/{id}` - Update employee
- `DELETE /employees/{id}` - Delete employee

### Payroll Management
- `GET /payrolls` - List all payrolls (paginated)
- `POST /payrolls` - Create new payroll
- `GET /payrolls/{id}` - Get payroll details
- `PUT /payrolls/{id}` - Update payroll
- `PATCH /payrolls/{id}/approve` - Approve payroll
- `PATCH /payrolls/{id}/process-payment` - Process payment
- `GET /payrolls/employee/{employeeId}` - Get payrolls by employee
- `GET /payrolls/period?month=1&year=2024` - Get payrolls by period

### Master Data
- `GET /departments` - List departments
- `POST /departments` - Create department
- `DELETE /departments/{id}` - Delete department
- `GET /designations` - List designations  
- `POST /designations` - Create designation
- `DELETE /designations/{id}` - Delete designation

## 📊 Sample Data

The system includes sample data:
- **Department**: IT (Code: IT001)
- **Designation**: Developer (Code: DEV001)
- **Employee**: John Doe (EMP001)
- **Payroll**: January 2024 (₹80,000 salary)

## 🔧 Configuration

### MongoDB Database
- Database: `employeedb`
- Collections: employees, departments, designations, payrolls

### Service Ports
- Discovery Server: 8761
- Employee Service: 8081  
- Payroll Service: 8082
- API Gateway: 8080
- Frontend: 3000

### Security Settings
- CSRF: Disabled for testing
- JWT: Configured but disabled for development
- CORS: Enabled for localhost

## 🎨 Frontend Features

- **Dashboard**: Statistics cards with real-time data
- **Employee Management**: Full CRUD with form validation
- **Payroll Workflow**: Create → Approve → Process Payment
- **Master Data**: Department and designation management
- **Responsive Design**: Bootstrap-based mobile-friendly UI
- **Error Handling**: User-friendly error messages and loading states

## 🚦 Status Indicators

**Payroll Status Badges:**
- 🟡 PENDING - Awaiting approval
- 🟢 APPROVED - Ready for payment  
- 🔵 PAID - Payment completed

## 📝 Testing

The system has been thoroughly tested:
- ✅ All CRUD operations working
- ✅ Payroll workflow (Create → Approve → Pay)
- ✅ Form validation and error handling
- ✅ API integration and data loading
- ✅ Responsive design and navigation
- ✅ Empty states and null safety

## 🔄 Development Notes

- Security disabled for easy testing
- All services auto-register with Eureka
- Frontend uses direct API calls to backend
- MongoDB shared across all services
- Batch file handles service startup timing

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🆘 Support

For support and questions:
- Create an issue in the repository
- Contact the development team
- Check the documentation wiki

## 🔮 Future Enhancements

- [ ] Config Server implementation
- [ ] Advanced reporting with custom dashboards
- [ ] Real-time notifications system
- [ ] Audit logging and compliance features
- [ ] Mobile application (React Native)
- [ ] Advanced analytics and ML insights
- [ ] Multi-tenant architecture support
- [ ] Integration with external HR systems

## 📊 System Requirements

### Minimum Requirements
- **CPU**: 2 cores
- **RAM**: 4GB
- **Storage**: 10GB
- **Network**: Broadband internet connection

### Recommended Requirements
- **CPU**: 4+ cores
- **RAM**: 8GB+
- **Storage**: 20GB+ SSD
- **Network**: High-speed internet connection

---

**Built with ❤️ by the Development Team**

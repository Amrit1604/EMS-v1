# EMS-v1 — Employee & Payroll Management System

A compact Spring Boot microservices demo for employee, attendance, leave and payroll management. This repository contains a small microservices ecosystem wired with Eureka service discovery, an API Gateway, and a minimal static frontend (HTML) to exercise key features.

---

## Overview

Services included:

- `service-registry` — Eureka server (discovery).
- `api-gateway` — Spring Cloud Gateway fronting all services.
- `employee-service` — User / employee CRUD and auth-related endpoints.
- `payroll-service` — Payroll, attendance, leave and dashboard logic.

A tiny set of front-end HTML pages lives at the repo root (dashboard, employees, payroll, attendance, leaves) for manual testing and quick demos.

---

## Recent fixes & improvements (highlights)

- Resolved compatibility and actuator exposure issues so services register cleanly with Eureka.
- Reworked the frontend (static HTML) to call actual API endpoints used by the backend.
- Attendance UX & server improvements:
  - Server now computes and persists `hoursWorked`, `overtimeHours`, and `breakHours` on checkout, rounding to 2 decimals.
  - Frontend parses backend ISO-local datetimes robustly and displays working hours as `HH:MM:SS` (with a tooltip showing decimal hours like `0.15h`).
  - Very short durations (seconds/minutes) are rendered clearly instead of showing `0.0h`.

This README documents how to run and test those flows.

---

## Quick start

Prerequisites:

- Java 17+ and Maven
- MongoDB running on `localhost:27017`
- (Optional) curl, or a browser for static HTML pages

Start each service (from the corresponding folder) using Maven wrapper or your IDE. Example (Windows cmd):

```cmd
cd api-gateway
mvnw spring-boot:run

cd ..\employee-service
mvnw spring-boot:run

cd ..\payroll-service
mvnw spring-boot:run

cd ..\service-registry
mvnw spring-boot:run
```

Note: ports used in the workspace when tested were typically:
- service-registry: 8761
- api-gateway: 8080
- employee-service: 8081
- payroll-service: 8082

Adjust ports via `application.properties` if required.

---

## Frontend pages

Open the static HTML files from the repo root in a browser (or serve them from a static host):

- `dashboard.html` — summary & links
- `employees.html` — employee CRUD UI
- `attendance.html` — attendance check-in/checkout UI and table view
- `payroll.html` — payroll UI
- `leaves.html` — leave management UI

The UI pages hit the `payroll-service` and `employee-service` endpoints directly (by default configured to `http://localhost:8082/api` for payroll).

---

## Important API snippets

Employee creation (use JSON body):

```json
POST /api/employees
{
  "employeeId": "EMP001",
  "fullName": "Jane Smith",
  "email": "jane@example.com",
  "phone": "1234567890",
  "department": "HR",
  "position": "Manager",
  "joinDate": "2024-07-01",
  "baseSalary": 50000
}
```

Attendance quick operations (query params):

- Check-in:

```cmd
POST /api/attendance/checkin?employeeId=EMP001
```

- Check-out (server computes and persists hours):

```cmd
POST /api/attendance/checkout?employeeId=EMP001
```

Get attendance for an employee:

```cmd
GET /api/attendance/employee/{employeeId}
```

Dashboard summary (payroll service):

```cmd
GET /api/dashboard/summary
```

---

## How attendance hours are computed (summary)

- On checkout the server computes `Duration` between `checkInTime` and `checkOutTime`.
- Break hours (if filled) are subtracted.
- `hoursWorked` and `overtimeHours` are rounded to 2 decimals and persisted.
- Frontend prefers server-provided `hoursWorked` and formats the cell as `HH:MM:SS` with a hover showing decimal hours (e.g. `0.15h`).

This change prevents tiny intervals from rendering as `0.0h` and provides a human-friendly display.

---

## Testing the attendance flow (manual)

1. Ensure `payroll-service` and `employee-service` are running and registered in Eureka.
2. Create or confirm an employee exists (see employee creation snippet above).
3. Trigger check-in:

```cmd
curl -v -X POST "http://localhost:8082/api/attendance/checkin?employeeId=EMP001"
```

4. Trigger check-out (server calculates and saves hours):

```cmd
curl -v -X POST "http://localhost:8082/api/attendance/checkout?employeeId=EMP001"
```

5. Inspect attendance JSON:

```cmd
curl -s "http://localhost:8082/api/attendance/employee/EMP001" | jq .
```

6. Reload `attendance.html` and confirm the `Working Hours` column shows a readable `HH:MM:SS` value and that hovering the cell shows the decimal hours tooltip.

---

## Troubleshooting

- 400 on checkout:
  - Usually means: no check-in exists for today, or the employee already checked out. Inspect the attendance records for that employee and date.
  - Use `GET /api/attendance/employee/{employeeId}` to confirm.

- Actuator/health 404 from the gateway:
  - Ensure `management.endpoints.web.exposure.include` includes `health` and `info` where needed and that actuator dependency is present.

- Field mismatches when creating employees:
  - The employee model expects `fullName`, `baseSalary`, and `joinDate` (not `firstName`/`lastName` or `salary`/`hireDate`). Use the JSON example above.

---

## Development notes & next steps

- Consider adding unit tests for `AttendanceService.calculateHours()` to cover edge cases: DST changes, midnight crossings, fractional seconds and breaks.
- Optionally implement live elapsed-time in the attendance UI for currently checked-in employees.
- Add better error responses (return exception message in the JSON body) to make API debugging easier.

---

## Contact & credits

Repository owner: Amrit1604

If you want, I can: add tests, improve the UI, or implement an export feature for payroll reports.

---

Enjoy — use the front-end pages to try the flow quickly and paste any API responses here if you want me to diagnose an edge case.

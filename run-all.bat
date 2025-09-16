@echo off
echo Starting Eureka Server...
start "Eureka Server" cmd /c "cd /d c:\Users\amrit\OneDrive\Desktop\EMS-v3\eureka-server & mvnw.cmd clean spring-boot:run -DskipTests"

timeout /t 20 /nobreak > nul

echo Starting Employee Service...
start "Employee Service" cmd /c "cd /d c:\Users\amrit\OneDrive\Desktop\EMS-v3\employee & mvnw.cmd clean spring-boot:run -DskipTests"

timeout /t 20 /nobreak > nul

echo Starting Payroll Service...
start "Payroll Service" cmd /c "cd /d c:\Users\amrit\OneDrive\Desktop\EMS-v3\Payroll & mvnw.cmd clean spring-boot:run -DskipTests"

timeout /t 20 /nobreak > nul

echo Starting API Gateway...
start "API Gateway" cmd /c "cd /d c:\Users\amrit\OneDrive\Desktop\EMS-v3\api-gateway & mvnw.cmd clean spring-boot:run -DskipTests"

echo All services started!
echo Access the application at http://localhost:8080
echo Eureka Dashboard at http://localhost:8761
pause
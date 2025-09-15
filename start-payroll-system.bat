@echo off
echo ========================================
echo  Employee Payroll System Startup
echo ========================================
echo.

:: Set window title
title Employee Payroll System - Starting Services

:: Check if Java is installed
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Java is not installed or not in PATH
    echo Please install Java 17 or higher
    pause
    exit /b 1
)

:: Check if Python is installed
python --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Python is not installed or not in PATH
    echo Please install Python 3.x
    pause
    exit /b 1
)

echo Starting services in order...
echo.

:: Start Discovery Server (Eureka)
echo [1/4] Starting Discovery Server (Eureka)...
start "Discovery Server" cmd /k "cd /d %~dp0backend\discovery-server && mvn spring-boot:run"
echo Waiting for Discovery Server to start...
timeout /t 15 /nobreak >nul

:: Start Employee Service
echo [2/4] Starting Employee Service...
start "Employee Service" cmd /k "cd /d %~dp0backend\employee-service && mvn spring-boot:run"
echo Waiting for Employee Service to start...
timeout /t 15 /nobreak >nul

:: Start Payroll Service
echo [3/4] Starting Payroll Service...
start "Payroll Service" cmd /k "cd /d %~dp0backend\payroll-service && mvn spring-boot:run"
echo Waiting for Payroll Service to start...
timeout /t 15 /nobreak >nul

:: Start API Gateway
echo [4/4] Starting API Gateway...
start "API Gateway" cmd /k "cd /d %~dp0backend\api-gateway && mvn spring-boot:run"
echo Waiting for API Gateway to start...
timeout /t 20 /nobreak >nul

:: Start Frontend Server
echo Starting Frontend Server...
start "Frontend Server" cmd /k "cd /d %~dp0frontend && python -m http.server 3000"
echo Waiting for Frontend Server to start...
timeout /t 5 /nobreak >nul

echo.
echo ========================================
echo  All Services Started Successfully!
echo ========================================
echo.
echo Services running on:
echo - Discovery Server: http://localhost:8761
echo - API Gateway:      http://localhost:8080
echo - Employee Service: http://localhost:8081
echo - Payroll Service:  http://localhost:8082
echo - Frontend:         http://localhost:3000
echo.
echo Opening Employee Payroll System...

:: Open the frontend in default browser
start http://localhost:3000

echo.
echo System is ready! Close this window to stop all services.
echo Press any key to exit...
pause >nul

:: Kill all Java processes when script ends
echo Stopping all services...
taskkill /f /im java.exe >nul 2>&1
taskkill /f /im python.exe >nul 2>&1
echo Services stopped.

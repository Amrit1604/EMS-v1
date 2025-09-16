# SpringBootEmployeeSalary-v2

http://localhost:8081/api/auth/login

POST request
body - 
{
"username": "admin",
"password": "admin123"
}

it will generate a jwt token

http://localhost:8081/api/departments
GET request - to see all departments
/departments/{id} - to see departments id wise

POST request - to add departments
body -
{
"name": "Human Resources"
}

DELETE request - to delete department id wise
http://localhost:8081/api/employees
GET request - to see all employees
POST request - add employee
Ex - 
body-
{
"name": "Robert Wilson",
"email": "robert.wilson@example.com",
"joiningDate": "2025-10-01"
}

Add employee department wise -
{
"name": "Robert Wilson",
"email": "robert.wilson@chitkara.edu.in",
"joiningDate": "2025-09-20",
"department": {
"id": 1
}

DELETE request - delete employees

/employees/{id} - to see employees id wise
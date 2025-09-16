// Redirect to login if not authenticated
if (!localStorage.getItem('token')) {
    window.location.href = '/index.html';
}

// Navigation
document.querySelectorAll('.sidebar a').forEach(link => {
    link.addEventListener('click', (e) => {
        e.preventDefault();
        const section = e.target.dataset.section;
        showSection(section);
    });
});

function showSection(sectionId) {
    document.querySelectorAll('.section').forEach(sec => sec.classList.add('hidden'));
    document.getElementById(sectionId).classList.remove('hidden');
}

// ----- Departments -----
async function loadDepartments() {
    try {
        const res = await fetch('http://localhost:8081/api/departments', {
            headers: { 'Authorization': `Bearer ${localStorage.getItem('token')}` }
        });
        const departments = await res.json();
        const tbody = document.querySelector('#departmentsTable tbody');
        tbody.innerHTML = departments.map(d => `
            <tr>
                <td>${d.id}</td>
                <td>${d.name}</td>
                <td>
                    <button onclick="editDepartment(${d.id},'${d.name}')">Edit</button>
                    <button onclick="deleteDepartment(${d.id})">Delete</button>
                </td>
            </tr>
        `).join('');
    } catch (err) { console.error(err); }
}

function showDepartmentForm() {
    document.getElementById("deptModalTitle").textContent = "Add Department";
    document.getElementById("deptName").value = "";
    document.getElementById("departmentModal").classList.remove("hidden");
}

function closeDepartmentModal() { document.getElementById("departmentModal").classList.add("hidden"); }

async function submitDepartment() {
    const name = document.getElementById("deptName").value;
    if (!name) return alert("Enter department name!");

    await fetch('http://localhost:8081/api/departments', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem('token')}`
        },
        body: JSON.stringify({ name })
    });
    closeDepartmentModal();
    loadDepartments();
}

async function deleteDepartment(id) {
    if (!confirm("Delete this department?")) return;
    await fetch(`http://localhost:8081/api/departments/${id}`, {
        method: 'DELETE',
        headers: { 'Authorization': `Bearer ${localStorage.getItem('token')}` }
    });
    loadDepartments();
}

function editDepartment(id, name) {
    document.getElementById("deptModalTitle").textContent = "Edit Department";
    document.getElementById("deptName").value = name;
    document.getElementById("departmentModal").classList.remove("hidden");
    document.getElementById("deptName").dataset.editId = id; // store id
}

// ----- Employees -----
async function loadDepartmentsDropdown() {
    const res = await fetch('http://localhost:8081/api/departments', {
        headers: { 'Authorization': `Bearer ${localStorage.getItem('token')}` }
    });
    const departments = await res.json();
    const select = document.getElementById("employeeDept");
    select.innerHTML = "";
    departments.forEach(d => { select.innerHTML += `<option value="${d.id}">${d.name}</option>`; });
}

async function loadEmployees() {
    const res = await fetch('http://localhost:8081/api/employees', {
        headers: { 'Authorization': `Bearer ${localStorage.getItem('token')}` }
    });
    const employees = await res.json();
    const tbody = document.querySelector('#employeesTable tbody');
    tbody.innerHTML = employees.map(emp => `
        <tr>
            <td>${emp.id}</td>
            <td>${emp.name}</td>
            <td>${emp.email}</td>
            <td>${emp.department?.name || 'N/A'}</td>
            <td>
                <button onclick="editEmployee(${emp.id})">Edit</button>
                <button onclick="deleteEmployee(${emp.id})">Delete</button>
            </td>
        </tr>
    `).join('');
}

function showEmployeeForm() {
    document.getElementById("empModalTitle").textContent = "Add Employee";
    document.getElementById("empName").value = "";
    document.getElementById("empEmail").value = "";
    document.getElementById("empDate").value = "";
    loadDepartmentsDropdown();
    document.getElementById("employeeModal").classList.remove("hidden");
}

function closeEmployeeModal() { document.getElementById("employeeModal").classList.add("hidden"); }

async function submitEmployee() {
    const id = document.getElementById("empName").dataset.editId;
    const name = document.getElementById("empName").value;
    const email = document.getElementById("empEmail").value;
    const joiningDate = document.getElementById("empDate").value;
    const departmentId = document.getElementById("employeeDept").value;

    if (!name || !email || !joiningDate) return alert("Fill all fields!");

    const payload = { name, email, joiningDate, department: { id: departmentId } };
    const url = id ? `http://localhost:8081/api/employees/${id}` : 'http://localhost:8081/api/employees';
    const method = id ? 'PUT' : 'POST';

    await fetch(url, {
        method,
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem('token')}`
        },
        body: JSON.stringify(payload)
    });

    closeEmployeeModal();
    loadEmployees();
}

async function deleteEmployee(id) {
    if (!confirm("Delete this employee?")) return;
    await fetch(`http://localhost:8081/api/employees/${id}`, {
        method: 'DELETE',
        headers: { 'Authorization': `Bearer ${localStorage.getItem('token')}` }
    });
    loadEmployees();
}

async function editEmployee(id) {
    const res = await fetch(`http://localhost:8081/api/employees/${id}`, {
        headers: { 'Authorization': `Bearer ${localStorage.getItem('token')}` }
    });
    const emp = await res.json();
    document.getElementById("empModalTitle").textContent = "Edit Employee";
    document.getElementById("empName").value = emp.name;
    document.getElementById("empEmail").value = emp.email;
    document.getElementById("empDate").value = emp.joiningDate;
    document.getElementById("empName").dataset.editId = emp.id;
    loadDepartmentsDropdown();
    document.getElementById("employeeDept").value = emp.department.id;
    document.getElementById("employeeModal").classList.remove("hidden");
}

// Initial Load
loadDepartments();
loadEmployees();

// Employee Payroll System - JavaScript

// API Base URL
const API_BASE_URL = 'http://localhost:8080/api';

// Global variables
let employees = [];
let departments = [];
let designations = [];
let payrolls = [];
let currentEditingEmployee = null;

// Initialize the application
document.addEventListener('DOMContentLoaded', function() {
    // Show dashboard by default
    showPage('dashboard');

    // Load initial data
    loadDashboardData();
    loadDepartments();
    loadDesignations();
    loadEmployees();
    loadPayrolls();
});

// Navigation functions
function showPage(pageId) {
    // Hide all pages
    document.querySelectorAll('.page').forEach(page => {
        page.classList.remove('active');
    });

    // Show selected page
    document.getElementById(pageId).classList.add('active');

    // Update navigation
    document.querySelectorAll('.nav-link').forEach(link => {
        link.classList.remove('active');
    });

    // Refresh data when switching pages
    switch(pageId) {
        case 'dashboard':
            loadDashboardData();
            break;
        case 'employees':
            loadEmployees();
            break;
        case 'payroll':
            loadPayrolls();
            break;
        case 'departments':
            loadDepartments();
            break;
        case 'designations':
            loadDesignations();
            break;
    }
}

// API Helper functions
async function apiCall(endpoint, method = 'GET', data = null) {
    try {
        const options = {
            method: method,
            headers: {
                'Content-Type': 'application/json',
            }
        };

        if (data) {
            options.body = JSON.stringify(data);
        }

        const response = await fetch(`${API_BASE_URL}${endpoint}`, options);

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        return await response.json();
    } catch (error) {
        console.error('API call failed:', error);
        showAlert('Error: ' + error.message, 'danger');
        throw error;
    }
}

// Dashboard functions
async function loadDashboardData() {
    try {
        // Load counts for dashboard cards
        const employeesData = await apiCall('/employees?page=0&size=1');
        const payrollsData = await apiCall('/payrolls?page=0&size=1');
        const departmentsData = await apiCall('/departments');
        const designationsData = await apiCall('/designations');

        document.getElementById('totalEmployees').textContent = employeesData.totalElements || 0;
        document.getElementById('totalPayrolls').textContent = payrollsData.totalElements || 0;
        document.getElementById('totalDepartments').textContent = departmentsData.length || 0;
        document.getElementById('totalDesignations').textContent = designationsData.length || 0;
    } catch (error) {
        console.error('Failed to load dashboard data:', error);
    }
}

// Employee functions
async function loadEmployees() {
    try {
        const data = await apiCall('/employees?page=0&size=100');
        employees = data.content || data || [];
        // normalize backend baseSalary -> salary for UI convenience
        employees = employees.map(e => ({
            ...e,
            salary: e.baseSalary ? (typeof e.baseSalary === 'number' ? e.baseSalary : parseFloat(e.baseSalary)) : 0
        }));
        renderEmployeesTable();
    } catch (error) {
        console.error('Failed to load employees:', error);
        employees = [];
        renderEmployeesTable();
    }
}

function renderEmployeesTable() {
    const tbody = document.getElementById('employeesTable');
    tbody.innerHTML = '';

    if (employees.length === 0) {
        tbody.innerHTML = '<tr><td colspan="7" class="text-center">No employees found</td></tr>';
        return;
    }

    employees.forEach(employee => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${employee.employeeCode || 'N/A'}</td>
            <td>${(employee.firstName || '') + ' ' + (employee.lastName || '')}</td>
            <td>${employee.email || 'N/A'}</td>
            <td>${employee.department?.name || 'N/A'}</td>
            <td>${employee.designation?.title || 'N/A'}</td>
            <td>₹${employee.salary?.toLocaleString() || 0}</td>
            <td>
                <button class="btn btn-sm btn-primary me-1" onclick="editEmployee('${employee.id}')">
                    <i class="fas fa-edit"></i>
                </button>
                <button class="btn btn-sm btn-danger" onclick="deleteEmployee('${employee.id}')">
                    <i class="fas fa-trash"></i>
                </button>
            </td>
        `;
        tbody.appendChild(row);
    });
}

function showEmployeeForm(employeeId = null) {
    currentEditingEmployee = employeeId;

    // Reset form
    document.getElementById('employeeForm').reset();

    // Load departments and designations for dropdowns
    loadDepartmentOptions();
    loadDesignationOptions();

    // If editing, populate form
    if (employeeId) {
        const employee = employees.find(emp => emp.id === employeeId);
        if (employee) {
            document.getElementById('employeeCode').value = employee.employeeCode;
            document.getElementById('firstName').value = employee.firstName;
            document.getElementById('lastName').value = employee.lastName;
            document.getElementById('email').value = employee.email;
            document.getElementById('phone').value = employee.phone;
            document.getElementById('dateOfBirth').value = employee.dateOfBirth;
            document.getElementById('departmentId').value = employee.department?.id || '';
            document.getElementById('designationId').value = employee.designation?.id || '';
            document.getElementById('hireDate').value = employee.hireDate;
            document.getElementById('salary').value = employee.salary;
            document.getElementById('address').value = employee.address;
        }
    }

    // Show modal
    new bootstrap.Modal(document.getElementById('employeeModal')).show();
}

function editEmployee(employeeId) {
    showEmployeeForm(employeeId);
}

async function saveEmployee() {
    const form = document.getElementById('employeeForm');
    if (!form.checkValidity()) {
        form.reportValidity();
        return;
    }

    const employeeData = {
        // Backend expects EmployeeDTO shape
        employeeCode: document.getElementById('employeeCode').value,
        firstName: document.getElementById('firstName').value,
        lastName: document.getElementById('lastName').value,
        email: document.getElementById('email').value,
        phoneNumber: document.getElementById('phone').value,
        dateOfBirth: document.getElementById('dateOfBirth').value,
        // backend field is joiningDate
        joiningDate: document.getElementById('hireDate').value,
        departmentId: document.getElementById('departmentId').value,
        designationId: document.getElementById('designationId').value,
        // backend expects baseSalary (BigDecimal)
        baseSalary: parseFloat(document.getElementById('salary').value),
        // provide sensible defaults required by backend DTO
        status: 'ACTIVE',
        employmentType: 'FULL_TIME',
        // backend expects an object for address; use streetAddress to store freeform textarea
        address: {
            streetAddress: document.getElementById('address').value || ''
        }
    };

    try {
        if (currentEditingEmployee) {
            await apiCall(`/employees/${currentEditingEmployee}`, 'PUT', employeeData);
            showAlert('Employee updated successfully!', 'success');
        } else {
            await apiCall('/employees', 'POST', employeeData);
            showAlert('Employee created successfully!', 'success');
        }

        // Close modal and refresh data
        bootstrap.Modal.getInstance(document.getElementById('employeeModal')).hide();
        loadEmployees();
        loadDashboardData();
    } catch (error) {
        console.error('Failed to save employee:', error);
    }
}

async function deleteEmployee(employeeId) {
    if (confirm('Are you sure you want to delete this employee?')) {
        try {
            await apiCall(`/employees/${employeeId}`, 'DELETE');
            showAlert('Employee deleted successfully!', 'success');
            loadEmployees();
            loadDashboardData();
        } catch (error) {
            console.error('Failed to delete employee:', error);
        }
    }
}

// Department functions
async function loadDepartments() {
    try {
        departments = await apiCall('/departments');
        renderDepartmentsTable();
    } catch (error) {
        console.error('Failed to load departments:', error);
        departments = [];
        renderDepartmentsTable();
    }
}

function renderDepartmentsTable() {
    const tbody = document.getElementById('departmentsTable');
    tbody.innerHTML = '';

    if (departments.length === 0) {
        tbody.innerHTML = '<tr><td colspan="4" class="text-center">No departments found</td></tr>';
        return;
    }

    departments.forEach(department => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${department.code || 'N/A'}</td>
            <td>${department.name || 'N/A'}</td>
            <td>${department.description || 'N/A'}</td>
            <td>
                <button class="btn btn-sm btn-danger" onclick="deleteDepartment('${department.id}')">
                    <i class="fas fa-trash"></i>
                </button>
            </td>
        `;
        tbody.appendChild(row);
    });
}

function showDepartmentForm() {
    document.getElementById('departmentForm').reset();
    new bootstrap.Modal(document.getElementById('departmentModal')).show();
}

async function saveDepartment() {
    const form = document.getElementById('departmentForm');
    if (!form.checkValidity()) {
        form.reportValidity();
        return;
    }

    const departmentData = {
        code: document.getElementById('departmentCode').value,
        name: document.getElementById('departmentName').value,
        description: document.getElementById('departmentDescription').value
    };

    try {
        await apiCall('/departments', 'POST', departmentData);
        showAlert('Department created successfully!', 'success');
        bootstrap.Modal.getInstance(document.getElementById('departmentModal')).hide();
        loadDepartments();
        loadDashboardData();
    } catch (error) {
        console.error('Failed to save department:', error);
    }
}

async function deleteDepartment(departmentId) {
    if (confirm('Are you sure you want to delete this department?')) {
        try {
            await apiCall(`/departments/${departmentId}`, 'DELETE');
            showAlert('Department deleted successfully!', 'success');
            loadDepartments();
            loadDashboardData();
        } catch (error) {
            console.error('Failed to delete department:', error);
        }
    }
}

function loadDepartmentOptions() {
    const select = document.getElementById('departmentId');
    select.innerHTML = '<option value="">Select Department</option>';

    departments.forEach(department => {
        const option = document.createElement('option');
        option.value = department.id;
        option.textContent = department.name;
        select.appendChild(option);
    });
}

// Designation functions
async function loadDesignations() {
    try {
        designations = await apiCall('/designations');
        renderDesignationsTable();
    } catch (error) {
        console.error('Failed to load designations:', error);
        designations = [];
        renderDesignationsTable();
    }
}

function renderDesignationsTable() {
    const tbody = document.getElementById('designationsTable');
    tbody.innerHTML = '';

    if (designations.length === 0) {
        tbody.innerHTML = '<tr><td colspan="4" class="text-center">No designations found</td></tr>';
        return;
    }

    designations.forEach(designation => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${designation.code || 'N/A'}</td>
            <td>${designation.title || 'N/A'}</td>
            <td>${designation.description || 'N/A'}</td>
            <td>
                <button class="btn btn-sm btn-danger" onclick="deleteDesignation('${designation.id}')">
                    <i class="fas fa-trash"></i>
                </button>
            </td>
        `;
        tbody.appendChild(row);
    });
}

function showDesignationForm() {
    document.getElementById('designationForm').reset();
    new bootstrap.Modal(document.getElementById('designationModal')).show();
}

async function saveDesignation() {
    const form = document.getElementById('designationForm');
    if (!form.checkValidity()) {
        form.reportValidity();
        return;
    }

    const designationData = {
        code: document.getElementById('designationCode').value,
        title: document.getElementById('designationTitle').value,
        description: document.getElementById('designationDescription').value
    };

    try {
        await apiCall('/designations', 'POST', designationData);
        showAlert('Designation created successfully!', 'success');
        bootstrap.Modal.getInstance(document.getElementById('designationModal')).hide();
        loadDesignations();
        loadDashboardData();
    } catch (error) {
        console.error('Failed to save designation:', error);
    }
}

async function deleteDesignation(designationId) {
    if (confirm('Are you sure you want to delete this designation?')) {
        try {
            await apiCall(`/designations/${designationId}`, 'DELETE');
            showAlert('Designation deleted successfully!', 'success');
            loadDesignations();
            loadDashboardData();
        } catch (error) {
            console.error('Failed to delete designation:', error);
        }
    }
}

function loadDesignationOptions() {
    const select = document.getElementById('designationId');
    select.innerHTML = '<option value="">Select Designation</option>';

    designations.forEach(designation => {
        const option = document.createElement('option');
        option.value = designation.id;
        option.textContent = designation.title;
        select.appendChild(option);
    });
}

// Payroll functions
async function loadPayrolls() {
    try {
        const data = await apiCall('/payrolls?page=0&size=100');
        payrolls = data.content || data || [];
        renderPayrollTable();
    } catch (error) {
        console.error('Failed to load payrolls:', error);
        payrolls = [];
        renderPayrollTable();
    }
}

function renderPayrollTable() {
    const tbody = document.getElementById('payrollTable');
    tbody.innerHTML = '';

    if (payrolls.length === 0) {
        tbody.innerHTML = '<tr><td colspan="7" class="text-center">No payrolls found</td></tr>';
        return;
    }

    payrolls.forEach(payroll => {
        const row = document.createElement('tr');
        const statusClass = `status-${(payroll.paymentStatus || 'pending').toLowerCase()}`;

        row.innerHTML = `
            <td>${(payroll.employeeName || 'N/A')} (${payroll.employeeCode || 'N/A'})</td>
            <td>${getMonthName(payroll.month)} ${payroll.year || 'N/A'}</td>
            <td>₹${payroll.basicSalary?.toLocaleString() || 0}</td>
            <td>₹${payroll.netSalary?.toLocaleString() || payroll.basicSalary?.toLocaleString() || 0}</td>
            <td><span class="badge ${statusClass}">${payroll.paymentStatus || 'PENDING'}</span></td>
            <td>${payroll.paymentMethod || 'N/A'}</td>
            <td>
                <button class="btn btn-sm btn-success me-1" onclick="approvePayroll('${payroll.id}')"
                        ${payroll.paymentStatus === 'APPROVED' || payroll.paymentStatus === 'PAID' ? 'disabled' : ''}>
                    <i class="fas fa-check"></i>
                </button>
                <button class="btn btn-sm btn-primary me-1" onclick="processPayment('${payroll.id}')"
                        ${payroll.paymentStatus === 'PAID' ? 'disabled' : ''}>
                    <i class="fas fa-credit-card"></i>
                </button>
                <button class="btn btn-sm btn-danger" onclick="deletePayroll('${payroll.id}')">
                    <i class="fas fa-trash"></i>
                </button>
            </td>
        `;
        tbody.appendChild(row);
    });
}

function showPayrollForm() {
    document.getElementById('payrollForm').reset();
    loadEmployeeOptions();

    // Set default values
    const today = new Date();
    document.getElementById('paymentDate').value = today.toISOString().split('T')[0];
    document.getElementById('year').value = today.getFullYear();

    new bootstrap.Modal(document.getElementById('payrollModal')).show();
}

function loadEmployeeOptions() {
    const select = document.getElementById('payrollEmployeeId');
    select.innerHTML = '<option value="">Select Employee</option>';

    employees.forEach(employee => {
        const option = document.createElement('option');
        option.value = employee.id;
        option.textContent = `${employee.firstName || ''} ${employee.lastName || ''} (${employee.employeeCode || ''})`;
        option.dataset.salary = employee.salary || 0;
        option.dataset.employeeCode = employee.employeeCode || '';
        option.dataset.firstName = employee.firstName || '';
        option.dataset.lastName = employee.lastName || '';
        select.appendChild(option);
    });

    // Remove existing event listeners to prevent duplicates
    const newSelect = select.cloneNode(true);
    select.parentNode.replaceChild(newSelect, select);

    // Auto-fill salary when employee is selected
    newSelect.addEventListener('change', function() {
        const selectedOption = this.options[this.selectedIndex];
        if (selectedOption.dataset.salary) {
            document.getElementById('basicSalary').value = selectedOption.dataset.salary;
        }
    });
}

async function savePayroll() {
    const form = document.getElementById('payrollForm');
    if (!form.checkValidity()) {
        form.reportValidity();
        return;
    }

    const selectedEmployee = employees.find(emp => emp.id === document.getElementById('payrollEmployeeId').value);

    const payrollData = {
        employeeId: document.getElementById('payrollEmployeeId').value,
        employeeCode: selectedEmployee.employeeCode,
        employeeName: `${selectedEmployee.firstName} ${selectedEmployee.lastName}`,
        month: parseInt(document.getElementById('month').value),
        year: parseInt(document.getElementById('year').value),
        paymentDate: document.getElementById('paymentDate').value,
        basicSalary: parseFloat(document.getElementById('basicSalary').value),
        paymentStatus: document.getElementById('paymentStatus').value,
        paymentMethod: document.getElementById('paymentMethod').value
    };

    try {
        await apiCall('/payrolls', 'POST', payrollData);
        showAlert('Payroll created successfully!', 'success');
        bootstrap.Modal.getInstance(document.getElementById('payrollModal')).hide();
        loadPayrolls();
        loadDashboardData();
    } catch (error) {
        console.error('Failed to save payroll:', error);
    }
}

async function approvePayroll(payrollId) {
    try {
        await apiCall(`/payrolls/${payrollId}/approve?approvedBy=Admin`, 'PATCH');
        showAlert('Payroll approved successfully!', 'success');
        loadPayrolls();
    } catch (error) {
        console.error('Failed to approve payroll:', error);
    }
}

async function processPayment(payrollId) {
    try {
        await apiCall(`/payrolls/${payrollId}/process-payment`, 'PATCH');
        showAlert('Payment processed successfully!', 'success');
        loadPayrolls();
    } catch (error) {
        console.error('Failed to process payment:', error);
    }
}

async function deletePayroll(payrollId) {
    if (confirm('Are you sure you want to delete this payroll?')) {
        try {
            await apiCall(`/payrolls/${payrollId}`, 'DELETE');
            showAlert('Payroll deleted successfully!', 'success');
            loadPayrolls();
            loadDashboardData();
        } catch (error) {
            console.error('Failed to delete payroll:', error);
        }
    }
}

// Utility functions
function getMonthName(monthNumber) {
    const months = [
        'January', 'February', 'March', 'April', 'May', 'June',
        'July', 'August', 'September', 'October', 'November', 'December'
    ];
    return months[monthNumber - 1] || '';
}

function showAlert(message, type = 'info') {
    // Create alert element
    const alertDiv = document.createElement('div');
    alertDiv.className = `alert alert-${type} alert-dismissible fade show position-fixed`;
    alertDiv.style.cssText = 'top: 20px; right: 20px; z-index: 9999; min-width: 300px;';
    alertDiv.innerHTML = `
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;

    // Add to body
    document.body.appendChild(alertDiv);

    // Auto remove after 5 seconds
    setTimeout(() => {
        if (alertDiv.parentNode) {
            alertDiv.parentNode.removeChild(alertDiv);
        }
    }, 5000);
}

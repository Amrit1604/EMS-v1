import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from 'react-query';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

// Components
import Layout from './components/Layout/Layout';
import Dashboard from './pages/Dashboard/Dashboard';
import Employees from './pages/Employees/Employees';
import EmployeeForm from './pages/Employees/EmployeeForm';
import EmployeeDetails from './pages/Employees/EmployeeDetails';
import Payroll from './pages/Payroll/Payroll';
import PayrollForm from './pages/Payroll/PayrollForm';
import PayrollDetails from './pages/Payroll/PayrollDetails';
import Departments from './pages/Departments/Departments';
import Designations from './pages/Designations/Designations';
import Reports from './pages/Reports/Reports';
import Settings from './pages/Settings/Settings';
import Login from './pages/Auth/Login';
import ProtectedRoute from './components/Auth/ProtectedRoute';

// Stores
import { useAuthStore } from './stores/authStore';

// Create a client
const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      refetchOnWindowFocus: false,
      retry: 1,
      staleTime: 5 * 60 * 1000, // 5 minutes
    },
  },
});

function App() {
  const { isAuthenticated } = useAuthStore();

  return (
    <QueryClientProvider client={queryClient}>
      <Router>
        <div className="App min-h-screen bg-gradient-to-br from-slate-50 to-blue-50">
          <Routes>
            {/* Public Routes */}
            <Route 
              path="/login" 
              element={!isAuthenticated ? <Login /> : <Navigate to="/dashboard" />} 
            />
            
            {/* Protected Routes */}
            <Route path="/" element={<ProtectedRoute><Layout /></ProtectedRoute>}>
              <Route index element={<Navigate to="/dashboard" />} />
              <Route path="dashboard" element={<Dashboard />} />
              
              {/* Employee Routes */}
              <Route path="employees" element={<Employees />} />
              <Route path="employees/new" element={<EmployeeForm />} />
              <Route path="employees/:id" element={<EmployeeDetails />} />
              <Route path="employees/:id/edit" element={<EmployeeForm />} />
              
              {/* Payroll Routes */}
              <Route path="payroll" element={<Payroll />} />
              <Route path="payroll/new" element={<PayrollForm />} />
              <Route path="payroll/:id" element={<PayrollDetails />} />
              <Route path="payroll/:id/edit" element={<PayrollForm />} />
              
              {/* Master Data Routes */}
              <Route path="departments" element={<Departments />} />
              <Route path="designations" element={<Designations />} />
              
              {/* Other Routes */}
              <Route path="reports" element={<Reports />} />
              <Route path="settings" element={<Settings />} />
            </Route>
            
            {/* Fallback */}
            <Route path="*" element={<Navigate to="/dashboard" />} />
          </Routes>
          
          {/* Toast Notifications */}
          <ToastContainer
            position="top-right"
            autoClose={5000}
            hideProgressBar={false}
            newestOnTop={false}
            closeOnClick
            rtl={false}
            pauseOnFocusLoss
            draggable
            pauseOnHover
            theme="light"
            className="mt-16"
          />
        </div>
      </Router>
    </QueryClientProvider>
  );
}

export default App;

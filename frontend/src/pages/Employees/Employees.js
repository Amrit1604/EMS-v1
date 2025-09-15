import React, { useState } from 'react';
import { motion } from 'framer-motion';
import { useNavigate } from 'react-router-dom';
import {
  Plus,
  Search,
  Filter,
  Download,
  Eye,
  Edit,
  Trash2,
  MoreVertical,
  Users
} from 'lucide-react';

const Employees = () => {
  const navigate = useNavigate();
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedDepartment, setSelectedDepartment] = useState('all');
  const [selectedStatus, setSelectedStatus] = useState('all');

  // Mock data - replace with actual API calls
  const employees = [
    {
      id: '1',
      employeeCode: 'EMP001',
      firstName: 'John',
      lastName: 'Doe',
      email: 'john.doe@company.com',
      department: 'Engineering',
      designation: 'Senior Developer',
      status: 'ACTIVE',
      joiningDate: '2023-01-15',
      baseSalary: 75000
    },
    {
      id: '2',
      employeeCode: 'EMP002',
      firstName: 'Jane',
      lastName: 'Smith',
      email: 'jane.smith@company.com',
      department: 'HR',
      designation: 'HR Manager',
      status: 'ACTIVE',
      joiningDate: '2022-08-20',
      baseSalary: 65000
    },
    {
      id: '3',
      employeeCode: 'EMP003',
      firstName: 'Mike',
      lastName: 'Johnson',
      email: 'mike.johnson@company.com',
      department: 'Sales',
      designation: 'Sales Executive',
      status: 'ON_LEAVE',
      joiningDate: '2023-03-10',
      baseSalary: 55000
    }
  ];

  const departments = ['Engineering', 'HR', 'Sales', 'Marketing', 'Finance'];
  const statuses = ['ACTIVE', 'INACTIVE', 'ON_LEAVE', 'TERMINATED'];

  const filteredEmployees = employees.filter(employee => {
    const matchesSearch = 
      employee.firstName.toLowerCase().includes(searchTerm.toLowerCase()) ||
      employee.lastName.toLowerCase().includes(searchTerm.toLowerCase()) ||
      employee.email.toLowerCase().includes(searchTerm.toLowerCase()) ||
      employee.employeeCode.toLowerCase().includes(searchTerm.toLowerCase());
    
    const matchesDepartment = selectedDepartment === 'all' || employee.department === selectedDepartment;
    const matchesStatus = selectedStatus === 'all' || employee.status === selectedStatus;
    
    return matchesSearch && matchesDepartment && matchesStatus;
  });

  const getStatusColor = (status) => {
    const colors = {
      ACTIVE: 'bg-green-100 text-green-800',
      INACTIVE: 'bg-gray-100 text-gray-800',
      ON_LEAVE: 'bg-yellow-100 text-yellow-800',
      TERMINATED: 'bg-red-100 text-red-800'
    };
    return colors[status] || colors.ACTIVE;
  };

  return (
    <div className="space-y-6">
      {/* Header */}
      <motion.div
        initial={{ opacity: 0, y: -20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.5 }}
        className="flex flex-col sm:flex-row sm:items-center sm:justify-between"
      >
        <div className="flex items-center space-x-3">
          <div className="w-10 h-10 bg-blue-100 rounded-lg flex items-center justify-center">
            <Users className="w-6 h-6 text-blue-600" />
          </div>
          <div>
            <h1 className="text-3xl font-bold text-gray-900">Employees</h1>
            <p className="text-gray-600">Manage your workforce</p>
          </div>
        </div>
        <div className="mt-4 sm:mt-0 flex items-center space-x-3">
          <motion.button
            whileHover={{ scale: 1.05 }}
            whileTap={{ scale: 0.95 }}
            className="btn-secondary flex items-center space-x-2"
          >
            <Download className="w-4 h-4" />
            <span>Export</span>
          </motion.button>
          <motion.button
            whileHover={{ scale: 1.05 }}
            whileTap={{ scale: 0.95 }}
            onClick={() => navigate('/employees/new')}
            className="btn-primary flex items-center space-x-2"
          >
            <Plus className="w-4 h-4" />
            <span>Add Employee</span>
          </motion.button>
        </div>
      </motion.div>

      {/* Filters */}
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.5, delay: 0.1 }}
        className="bg-white rounded-xl shadow-soft p-6 border border-gray-100"
      >
        <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
          {/* Search */}
          <div className="relative">
            <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-5 h-5 text-gray-400" />
            <input
              type="text"
              placeholder="Search employees..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="input-field pl-10"
            />
          </div>

          {/* Department Filter */}
          <div>
            <select
              value={selectedDepartment}
              onChange={(e) => setSelectedDepartment(e.target.value)}
              className="input-field"
            >
              <option value="all">All Departments</option>
              {departments.map(dept => (
                <option key={dept} value={dept}>{dept}</option>
              ))}
            </select>
          </div>

          {/* Status Filter */}
          <div>
            <select
              value={selectedStatus}
              onChange={(e) => setSelectedStatus(e.target.value)}
              className="input-field"
            >
              <option value="all">All Statuses</option>
              {statuses.map(status => (
                <option key={status} value={status}>{status.replace('_', ' ')}</option>
              ))}
            </select>
          </div>

          {/* Clear Filters */}
          <div>
            <button
              onClick={() => {
                setSearchTerm('');
                setSelectedDepartment('all');
                setSelectedStatus('all');
              }}
              className="w-full btn-secondary flex items-center justify-center space-x-2"
            >
              <Filter className="w-4 h-4" />
              <span>Clear Filters</span>
            </button>
          </div>
        </div>
      </motion.div>

      {/* Employee List */}
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.5, delay: 0.2 }}
        className="bg-white rounded-xl shadow-soft border border-gray-100 overflow-hidden"
      >
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead className="bg-gray-50 border-b border-gray-200">
              <tr>
                <th className="px-6 py-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Employee
                </th>
                <th className="px-6 py-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Department
                </th>
                <th className="px-6 py-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Status
                </th>
                <th className="px-6 py-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Joining Date
                </th>
                <th className="px-6 py-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Salary
                </th>
                <th className="px-6 py-4 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Actions
                </th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {filteredEmployees.map((employee, index) => (
                <motion.tr
                  key={employee.id}
                  initial={{ opacity: 0, y: 20 }}
                  animate={{ opacity: 1, y: 0 }}
                  transition={{ delay: index * 0.1 }}
                  className="hover:bg-gray-50 transition-colors duration-200"
                >
                  <td className="px-6 py-4 whitespace-nowrap">
                    <div className="flex items-center">
                      <div className="w-10 h-10 bg-gradient-to-r from-blue-500 to-purple-600 rounded-full flex items-center justify-center">
                        <span className="text-white font-semibold text-sm">
                          {employee.firstName.charAt(0)}{employee.lastName.charAt(0)}
                        </span>
                      </div>
                      <div className="ml-4">
                        <div className="text-sm font-medium text-gray-900">
                          {employee.firstName} {employee.lastName}
                        </div>
                        <div className="text-sm text-gray-500">{employee.employeeCode}</div>
                        <div className="text-sm text-gray-500">{employee.email}</div>
                      </div>
                    </div>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <div className="text-sm text-gray-900">{employee.department}</div>
                    <div className="text-sm text-gray-500">{employee.designation}</div>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <span className={`inline-flex px-2 py-1 text-xs font-semibold rounded-full ${getStatusColor(employee.status)}`}>
                      {employee.status.replace('_', ' ')}
                    </span>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                    {new Date(employee.joiningDate).toLocaleDateString()}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                    ₹{employee.baseSalary.toLocaleString()}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                    <div className="flex items-center justify-end space-x-2">
                      <motion.button
                        whileHover={{ scale: 1.1 }}
                        whileTap={{ scale: 0.9 }}
                        onClick={() => navigate(`/employees/${employee.id}`)}
                        className="p-2 text-gray-400 hover:text-blue-600 transition-colors duration-200"
                      >
                        <Eye className="w-4 h-4" />
                      </motion.button>
                      <motion.button
                        whileHover={{ scale: 1.1 }}
                        whileTap={{ scale: 0.9 }}
                        onClick={() => navigate(`/employees/${employee.id}/edit`)}
                        className="p-2 text-gray-400 hover:text-green-600 transition-colors duration-200"
                      >
                        <Edit className="w-4 h-4" />
                      </motion.button>
                      <motion.button
                        whileHover={{ scale: 1.1 }}
                        whileTap={{ scale: 0.9 }}
                        className="p-2 text-gray-400 hover:text-red-600 transition-colors duration-200"
                      >
                        <Trash2 className="w-4 h-4" />
                      </motion.button>
                    </div>
                  </td>
                </motion.tr>
              ))}
            </tbody>
          </table>
        </div>

        {filteredEmployees.length === 0 && (
          <div className="text-center py-12">
            <Users className="mx-auto h-12 w-12 text-gray-400" />
            <h3 className="mt-2 text-sm font-medium text-gray-900">No employees found</h3>
            <p className="mt-1 text-sm text-gray-500">
              Try adjusting your search or filter criteria.
            </p>
          </div>
        )}
      </motion.div>
    </div>
  );
};

export default Employees;

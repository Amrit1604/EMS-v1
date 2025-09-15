import React, { useState } from 'react';
import { motion } from 'framer-motion';
import { useNavigate } from 'react-router-dom';
import {
  Plus,
  Search,
  Building,
  Users,
  Edit,
  Trash2,
  Eye
} from 'lucide-react';

const Departments = () => {
  const navigate = useNavigate();
  const [searchTerm, setSearchTerm] = useState('');

  // Mock data - replace with actual API calls
  const departments = [
    {
      id: '1',
      name: 'Engineering',
      description: 'Software development and technical operations',
      headOfDepartment: 'John Smith',
      employeeCount: 45,
      budget: 2500000,
      location: 'Building A, Floor 3',
      createdDate: '2023-01-15'
    },
    {
      id: '2',
      name: 'Human Resources',
      description: 'Employee relations and organizational development',
      headOfDepartment: 'Sarah Johnson',
      employeeCount: 12,
      budget: 800000,
      location: 'Building B, Floor 1',
      createdDate: '2023-01-15'
    },
    {
      id: '3',
      name: 'Sales',
      description: 'Revenue generation and client relationships',
      headOfDepartment: 'Mike Wilson',
      employeeCount: 28,
      budget: 1800000,
      location: 'Building A, Floor 2',
      createdDate: '2023-02-01'
    },
    {
      id: '4',
      name: 'Marketing',
      description: 'Brand promotion and market research',
      headOfDepartment: 'Emily Davis',
      employeeCount: 18,
      budget: 1200000,
      location: 'Building B, Floor 2',
      createdDate: '2023-02-15'
    }
  ];

  const filteredDepartments = departments.filter(dept =>
    dept.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
    dept.description.toLowerCase().includes(searchTerm.toLowerCase()) ||
    dept.headOfDepartment.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const totalEmployees = departments.reduce((sum, dept) => sum + dept.employeeCount, 0);
  const totalBudget = departments.reduce((sum, dept) => sum + dept.budget, 0);

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
          <div className="w-10 h-10 bg-purple-100 rounded-lg flex items-center justify-center">
            <Building className="w-6 h-6 text-purple-600" />
          </div>
          <div>
            <h1 className="text-3xl font-bold text-gray-900">Departments</h1>
            <p className="text-gray-600">Manage organizational departments</p>
          </div>
        </div>
        <div className="mt-4 sm:mt-0">
          <motion.button
            whileHover={{ scale: 1.05 }}
            whileTap={{ scale: 0.95 }}
            onClick={() => navigate('/departments/new')}
            className="btn-primary flex items-center space-x-2"
          >
            <Plus className="w-4 h-4" />
            <span>Add Department</span>
          </motion.button>
        </div>
      </motion.div>

      {/* Stats Cards */}
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.5, delay: 0.1 }}
        className="grid grid-cols-1 md:grid-cols-3 gap-6"
      >
        <div className="bg-white rounded-xl shadow-soft p-6 border border-gray-100">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm font-medium text-gray-600">Total Departments</p>
              <p className="text-2xl font-bold text-gray-900">{departments.length}</p>
            </div>
            <div className="w-12 h-12 bg-purple-100 rounded-lg flex items-center justify-center">
              <Building className="w-6 h-6 text-purple-600" />
            </div>
          </div>
        </div>

        <div className="bg-white rounded-xl shadow-soft p-6 border border-gray-100">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm font-medium text-gray-600">Total Employees</p>
              <p className="text-2xl font-bold text-gray-900">{totalEmployees}</p>
            </div>
            <div className="w-12 h-12 bg-blue-100 rounded-lg flex items-center justify-center">
              <Users className="w-6 h-6 text-blue-600" />
            </div>
          </div>
        </div>

        <div className="bg-white rounded-xl shadow-soft p-6 border border-gray-100">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm font-medium text-gray-600">Total Budget</p>
              <p className="text-2xl font-bold text-gray-900">₹{(totalBudget / 100000).toFixed(1)}L</p>
            </div>
            <div className="w-12 h-12 bg-green-100 rounded-lg flex items-center justify-center">
              <Building className="w-6 h-6 text-green-600" />
            </div>
          </div>
        </div>
      </motion.div>

      {/* Search */}
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.5, delay: 0.2 }}
        className="bg-white rounded-xl shadow-soft p-6 border border-gray-100"
      >
        <div className="relative max-w-md">
          <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-5 h-5 text-gray-400" />
          <input
            type="text"
            placeholder="Search departments..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="input-field pl-10"
          />
        </div>
      </motion.div>

      {/* Departments Grid */}
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.5, delay: 0.3 }}
        className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6"
      >
        {filteredDepartments.map((department, index) => (
          <motion.div
            key={department.id}
            initial={{ opacity: 0, scale: 0.95 }}
            animate={{ opacity: 1, scale: 1 }}
            transition={{ delay: index * 0.1 }}
            whileHover={{ y: -5, scale: 1.02 }}
            className="bg-white rounded-xl shadow-soft hover:shadow-medium p-6 border border-gray-100 transition-all duration-300"
          >
            <div className="flex items-start justify-between mb-4">
              <div className="w-12 h-12 bg-gradient-to-r from-purple-500 to-blue-600 rounded-lg flex items-center justify-center">
                <Building className="w-6 h-6 text-white" />
              </div>
              <div className="flex items-center space-x-2">
                <motion.button
                  whileHover={{ scale: 1.1 }}
                  whileTap={{ scale: 0.9 }}
                  onClick={() => navigate(`/departments/${department.id}`)}
                  className="p-2 text-gray-400 hover:text-blue-600 transition-colors duration-200"
                >
                  <Eye className="w-4 h-4" />
                </motion.button>
                <motion.button
                  whileHover={{ scale: 1.1 }}
                  whileTap={{ scale: 0.9 }}
                  onClick={() => navigate(`/departments/${department.id}/edit`)}
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
            </div>

            <div className="mb-4">
              <h3 className="text-xl font-semibold text-gray-900 mb-2">{department.name}</h3>
              <p className="text-sm text-gray-600 mb-3">{department.description}</p>
              
              <div className="space-y-2">
                <div className="flex items-center justify-between">
                  <span className="text-sm text-gray-500">Head of Department</span>
                  <span className="text-sm font-medium text-gray-900">{department.headOfDepartment}</span>
                </div>
                
                <div className="flex items-center justify-between">
                  <span className="text-sm text-gray-500">Employees</span>
                  <div className="flex items-center space-x-1">
                    <Users className="w-4 h-4 text-gray-400" />
                    <span className="text-sm font-medium text-gray-900">{department.employeeCount}</span>
                  </div>
                </div>
                
                <div className="flex items-center justify-between">
                  <span className="text-sm text-gray-500">Budget</span>
                  <span className="text-sm font-medium text-gray-900">
                    ₹{(department.budget / 100000).toFixed(1)}L
                  </span>
                </div>
                
                <div className="flex items-center justify-between">
                  <span className="text-sm text-gray-500">Location</span>
                  <span className="text-sm font-medium text-gray-900">{department.location}</span>
                </div>
              </div>
            </div>

            <div className="pt-4 border-t border-gray-200">
              <div className="flex items-center justify-between">
                <span className="text-xs text-gray-500">
                  Created: {new Date(department.createdDate).toLocaleDateString()}
                </span>
                <motion.button
                  whileHover={{ scale: 1.05 }}
                  whileTap={{ scale: 0.95 }}
                  onClick={() => navigate(`/departments/${department.id}`)}
                  className="text-sm text-blue-600 hover:text-blue-700 font-medium"
                >
                  View Details
                </motion.button>
              </div>
            </div>
          </motion.div>
        ))}
      </motion.div>

      {filteredDepartments.length === 0 && (
        <motion.div
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          className="text-center py-12"
        >
          <Building className="mx-auto h-12 w-12 text-gray-400" />
          <h3 className="mt-2 text-sm font-medium text-gray-900">No departments found</h3>
          <p className="mt-1 text-sm text-gray-500">
            Try adjusting your search criteria or create a new department.
          </p>
        </motion.div>
      )}
    </div>
  );
};

export default Departments;
